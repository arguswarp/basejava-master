package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class SqlHelper {
    private static final String DUPLICATE_ERROR_STATE = "23505";
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T> T execute(String sql, Statement<T> statement) {
        try (Connection connection = connectionFactory.getConnection()) {
            return statement.execute(connection.prepareStatement(sql));
        } catch (SQLException e) {
            if (Objects.equals(e.getSQLState(), DUPLICATE_ERROR_STATE)) {
                throw new ExistStorageException(null);
            } else {
                throw new StorageException(e);
            }
        }
    }

    public interface Statement<T> {
        T execute(PreparedStatement statement) throws SQLException;
    }
}




