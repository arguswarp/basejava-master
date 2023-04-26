package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.basejava.storage.AbstractStorage.COMPARATOR_RESUME;

public class SqlStorage implements Storage {

    public final ConnectionFactory connectionFactory;

    public final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        sqlHelper = new SqlHelper(connectionFactory);
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.execute("INSERT INTO resume (uuid, full_name) VALUES (?,?)",
                preparedStatement -> doSaveStatement(resume, preparedStatement));
    }

    private static Object doSaveStatement(Resume resume, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, resume.getUuid());
        preparedStatement.setString(2, resume.getFullName());
        preparedStatement.execute();
        return null;
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("SELECT *FROM resume r WHERE r.uuid = ?",
                preparedStatement -> {
                    preparedStatement.setString(1, uuid);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (!resultSet.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    return new Resume(uuid, resultSet.getString("full_name"));
                });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume r WHERE r.uuid = ?",
                preparedStatement -> {
                    preparedStatement.setString(1, uuid);
                    preparedStatement.execute();
                    return null;
                });
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.execute("UPDATE resume SET full_name=? WHERE uuid = ?",
                preparedStatement -> doSaveStatement(resume, preparedStatement));
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute("SELECT * FROM resume",
                preparedStatement -> {
                    List<Resume> list = new ArrayList<>();
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        list.add(new Resume(resultSet.getString("uuid").trim(), resultSet.getString("full_name")));
                    }
                    list.sort(COMPARATOR_RESUME);
                    return list;
                });
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT COUNT(*) FROM resume",
                preparedStatement -> {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (!resultSet.next()) {
                        return 0;
                    }
                    return resultSet.getInt("count");
                });
    }
}
