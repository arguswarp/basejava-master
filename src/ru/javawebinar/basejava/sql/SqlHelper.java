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

    public <T> T execute(String sql, StatementExecutor<T> statementExecutor) {
        try (Connection connection = connectionFactory.getConnection()) {
            return statementExecutor.execute(connection.prepareStatement(sql));
        } catch (SQLException e) {
            if (Objects.equals(e.getSQLState(), DUPLICATE_ERROR_STATE)) {
                throw new ExistStorageException(null);
            } else {
                throw new StorageException(e);
            }
        }
    }

    public void execute(String sql) {
        execute(sql, PreparedStatement::execute);
    }

    public interface StatementExecutor<T> {
        T execute(PreparedStatement statement) throws SQLException;
    }
}




