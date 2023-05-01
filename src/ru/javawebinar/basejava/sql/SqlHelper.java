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
        try (Connection connection = connectionFactory.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            return statementExecutor.execute(preparedStatement);
        } catch (SQLException e) {
           throw convertException(e);
        }
    }

    private static RuntimeException convertException(SQLException e) {
        if (Objects.equals(e.getSQLState(), DUPLICATE_ERROR_STATE)) {
            return new ExistStorageException(null);
        } else {
            return new StorageException(e);
        }
    }

    public void execute(String sql) {
        execute(sql, PreparedStatement::execute);
    }
    public <T> T transactionalExecute(SqlTransaction<T> executor) {
        try (Connection connection = connectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);
                T result = executor.execute(connection);
                connection.commit();
                return result;
            }catch (SQLException e) {
                connection.rollback();
                throw convertException(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }


    public interface StatementExecutor<T> {
        T execute(PreparedStatement statement) throws SQLException;
    }
}




