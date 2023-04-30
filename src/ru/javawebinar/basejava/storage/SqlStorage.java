package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
        sqlHelper.<Void>execute("INSERT INTO resume (uuid, full_name) VALUES (?,?)",
                preparedStatement -> {
                    preparedStatement.setString(1, resume.getUuid());
                    preparedStatement.setString(2, resume.getFullName());
                    preparedStatement.execute();
                    return null;
                });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("SELECT * FROM resume r WHERE r.uuid = ?",
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
        sqlHelper.<Void>execute("UPDATE resume SET full_name=? WHERE uuid = ?",
                preparedStatement -> {
                    preparedStatement.setString(1, resume.getFullName());
                    preparedStatement.setString(2, resume.getUuid());
                    if (preparedStatement.executeUpdate() == 0) {
                        throw new NotExistStorageException(resume.getUuid());
                    }
                    return null;
                });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute("SELECT * FROM resume ORDER BY full_name, uuid",
                preparedStatement -> {
                    List<Resume> resumes = new ArrayList<>();
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        resumes.add(new Resume(resultSet.getString("uuid"), resultSet.getString("full_name")));
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
