package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage{

    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory =()-> DriverManager.getConnection(dbUrl,dbUser,dbPassword);
    }

    @Override
    public void clear() {
        try(Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM resume")) {

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void save(Resume resume) {
        try(Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
            preparedStatement.setString(1, resume.getUuid());
            preparedStatement.setString(2, resume.getFullName());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public Resume get(String uuid) {
        try(Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *FROM resume r WHERE r.uuid = ?")) {
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()) {
                throw  new NotExistStorageException(uuid);
            }
            return new Resume(uuid, resultSet.getString("full_name"));
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void delete(String uuid) {
        try(Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM resume r WHERE r.uuid = ?")) {
            preparedStatement.setString(1, uuid);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void update(Resume resume) {
        try(Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE resume SET full_name=? WHERE uuid = ?")) {
            preparedStatement.setString(1, resume.getFullName());
            preparedStatement.setString(2,resume.getUuid());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list= new ArrayList<>();
        try(Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *FROM resume")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (!resultSet.next()) {
               list.add(new Resume(resultSet.getString("uuid"), resultSet.getString("full_name")));
            }
            return list;
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public int size() {
        try(Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM resume")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()) {
                return 0;
            }
            return resultSet.getInt("count");
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
