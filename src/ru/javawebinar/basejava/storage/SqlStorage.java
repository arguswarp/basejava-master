package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            executeSave(resume, connection
                    , "INSERT INTO resume (uuid, full_name) VALUES (?,?)"
                    , preparedStatement1 -> {
                        preparedStatement1.setString(1, resume.getUuid());
                        preparedStatement1.setString(2, resume.getFullName());
                        preparedStatement1.execute();
                        return null;
                    }
                    , "INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)"
                    , (preparedStatement2, entry) -> {
                        preparedStatement2.setString(1, resume.getUuid());
                        preparedStatement2.setString(2, entry.getKey().name());
                        preparedStatement2.setString(3, entry.getValue());
                    });
            return null;
        });

//        sqlHelper.transactionalExecute(connection -> {
//            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
//                preparedStatement.setString(1, resume.getUuid());
//                preparedStatement.setString(2, resume.getFullName());
//                preparedStatement.execute();
//            }
//            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
//                for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
//                    preparedStatement.setString(1, resume.getUuid());
//                    preparedStatement.setString(2, entry.getKey().name());
//                    preparedStatement.setString(3, entry.getValue());
//                    preparedStatement.addBatch();
//                }
//                preparedStatement.executeBatch();
//            }
//            return null;
//        });
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
            executeSave(resume, connection
                    , "UPDATE resume SET full_name=? WHERE uuid = ?"
                    , preparedStatement1 -> {
                        preparedStatement1.setString(1, resume.getFullName());
                        preparedStatement1.setString(2, resume.getUuid());
                        if (preparedStatement1.executeUpdate() == 0) {
                            throw new NotExistStorageException(resume.getUuid());
                        }
                        return null;
                    }
                    , "UPDATE contact c SET value=? WHERE type=? AND resume_uuid=?"
                    , (preparedStatement2, entry) -> {
                        preparedStatement2.setString(1, entry.getValue());
                        preparedStatement2.setString(2, entry.getKey().name());
                        preparedStatement2.setString(3, resume.getUuid());
                    });
            return null;
        });

//        sqlHelper.transactionalExecute(connection -> {
//            try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE resume SET full_name=? WHERE uuid = ?")) {
//                preparedStatement.setString(1, resume.getFullName());
//                preparedStatement.setString(2, resume.getUuid());
//                if (preparedStatement.executeUpdate() == 0) {
//                    throw new NotExistStorageException(resume.getUuid());
//                }
//            }
//            try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE contact c SET value=? WHERE type=? AND resume_uuid=?")) {
//                for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
//                    preparedStatement.setString(1, entry.getValue());
//                    preparedStatement.setString(2, entry.getKey().name());
//                    preparedStatement.setString(3, resume.getUuid());
//                    preparedStatement.addBatch();
//                }
//                preparedStatement.executeBatch();
//            }
//            return null;
//        });
    }

    private <T> void executeSave(Resume resume, Connection connection
            , String sql1, SqlHelper.StatementExecutor<T> executor1
            , String sql2, SqlEntryExecutor<ContactType, String> executor2) throws SQLException {
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
        return sqlHelper.transactionalExecute(connection -> {
            List<Resume> resumes = new ArrayList<>();
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM resume  ORDER BY full_name, uuid")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Resume resume = new Resume(resultSet.getString("uuid"), resultSet.getString("full_name"));
                    resumes.add(resume);
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM contact where resume_uuid=?")) {
                for (Resume resume : resumes) {
                    preparedStatement.setString(1, resume.getUuid());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        String value = resultSet.getString("value");
                        ContactType type = ContactType.valueOf(resultSet.getString("type"));
                        resume.addContact(type, value);
                    }
                }
            }
            return resumes;
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
