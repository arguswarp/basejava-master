package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    public final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume");
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.transactionalExecute(connection -> {
            executeSave(resume, connection,
                    "INSERT INTO resume (uuid, full_name) VALUES (?,?)",
                    preparedStatement1 -> {
                        preparedStatement1.setString(1, resume.getUuid());
                        preparedStatement1.setString(2, resume.getFullName());
                        preparedStatement1.execute();
                        return null;
                    },
                    "INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)",
                    (preparedStatement2, entry) -> {
                        preparedStatement2.setString(1, resume.getUuid());
                        preparedStatement2.setString(2, entry.getKey().name());
                        preparedStatement2.setString(3, entry.getValue());
                    });
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("" +
                        "SELECT * FROM resume r " +
                        "LEFT JOIN contact c " +
                        "ON  r.uuid = c.resume_uuid " +
                        "WHERE r.uuid=?",
                preparedStatement -> {
                    preparedStatement.setString(1, uuid);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (!resultSet.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume resume = new Resume(uuid, resultSet.getString("full_name"));
                    do {
                        String value = resultSet.getString("value");
                        ContactType type = ContactType.valueOf(resultSet.getString("type"));
                        resume.addContact(type, value);
                    } while (resultSet.next());

                    return resume;
                });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.<Void>execute("DELETE FROM resume r WHERE r.uuid = ?",
                preparedStatement -> {
                    preparedStatement.setString(1, uuid);
                    if (preparedStatement.executeUpdate() == 0) {
                        throw new NotExistStorageException(uuid);
                    }
                    return null;
                });
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionalExecute(connection -> {
            executeSave(resume, connection,
                    "UPDATE resume SET full_name=? WHERE uuid = ?",
                    preparedStatement1 -> {
                        preparedStatement1.setString(1, resume.getFullName());
                        preparedStatement1.setString(2, resume.getUuid());
                        if (preparedStatement1.executeUpdate() == 0) {
                            throw new NotExistStorageException(resume.getUuid());
                        }
                        return null;
                    },
                    "UPDATE contact SET value=? WHERE type=? AND resume_uuid=?",
                    (preparedStatement2, entry) -> {
                        preparedStatement2.setString(1, entry.getValue());
                        preparedStatement2.setString(2, entry.getKey().name());
                        preparedStatement2.setString(3, resume.getUuid());
                    });
            return null;
        });
    }

    private <T> void executeSave(Resume resume, Connection connection,
                                 String sql1, SqlHelper.StatementExecutor<T> executor1,
                                 String sql2, SqlEntryExecutor<ContactType, String> executor2) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql1)) {
            executor1.execute(preparedStatement);
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql2)) {
            for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
                executor2.execute(preparedStatement, entry);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    private interface SqlEntryExecutor<K, V> {
        void execute(PreparedStatement preparedStatement, Map.Entry<K, V> entry) throws SQLException;
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute("" +
                "SELECT * FROM resume " +
                "Left JOIN contact c " +
                "ON resume.uuid = c.resume_uuid " +
                "ORDER BY full_name, uuid", preparedStatement -> {
            Map<String, Resume> map = new LinkedHashMap<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String key = resultSet.getString("uuid");
                map.putIfAbsent(key, new Resume(key, resultSet.getString("full_name")));
                map.get(key).addContact(ContactType.valueOf(resultSet.getString("type")), resultSet.getString("value"));
            }
            return map.values().stream().toList();
        });
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT COUNT(*) FROM resume",
                preparedStatement -> {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    return resultSet.next() ? resultSet.getInt("count") : 0;
                });
    }
}
