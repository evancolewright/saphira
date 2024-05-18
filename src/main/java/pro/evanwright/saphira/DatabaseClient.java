package pro.evanwright.saphira;

import pro.evanwright.saphira.exception.UncheckedSQLException;
import pro.evanwright.saphira.query.QueryResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class DatabaseClient {

    private final ExecutorService threadPool;
    private final ThreadLocal<Connection> transactionConnection;

    protected DatabaseClient() {
        this.threadPool = Executors.newCachedThreadPool();
        this.transactionConnection = new ThreadLocal<>();
    }

    /**
     * Queries the database for results and returns a {@link QueryResult} instance.
     *
     * @param sqlStatement The SQL statement to execute
     * @return The results of the query
     * @throws UncheckedSQLException If a {@link SQLException} occurs
     *
     * @see QueryResult
     */
    public QueryResult query(@NotNull String sqlStatement) throws UncheckedSQLException {
        return query(sqlStatement, new Object[0]);
    }

    /**
     * Queries the database for results and returns a {@link QueryResult} instance.
     *
     * @param sqlStatement The SQL statement to execute
     * @param params The parameters for the statement
     * @return The results of the query
     * @throws UncheckedSQLException If a {@link SQLException} occurs
     *
     * @see QueryResult
     */
    public QueryResult query(@NotNull String sqlStatement, @NotNull Object... params) throws UncheckedSQLException {
        return query(sqlStatement, preparedStatement -> {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
        });
    }

    /**
     * Queries the database for results and returns a {@link QueryResult} instance.
     *
     * @param sqlStatement The SQL statement to execute
     * @param psPreparer   The preparer that prepares the SQL statement
     * @return The results of the query
     * @throws UncheckedSQLException If a {@link SQLException} occurs
     *
     * @see QueryResult
     */
    public QueryResult query(@NotNull String sqlStatement, @Nullable SQLConsumer<PreparedStatement> psPreparer) throws UncheckedSQLException {
        try (Connection connection = this.getConnectionInternal(); PreparedStatement statement = connection.prepareStatement(sqlStatement)) {
            if (psPreparer != null)
                psPreparer.accept(statement);
            try (ResultSet resultSet = statement.executeQuery()) {
                CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
                cachedRowSet.populate(resultSet);
                return new QueryResult(cachedRowSet);
            }
        } catch (SQLException exception) {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * Does the same thing as {@link DatabaseClient#query(String)} except
     * does everything asynchronously and returns a {@link CompletableFuture}.
     *
     * @see DatabaseClient#query(String)
     */
    public CompletableFuture<QueryResult> queryAsync(@NotNull String sqlStatement) {
        return queryAsync(sqlStatement, null);
    }

    /**
     * Does the same thing as {@link DatabaseClient#query(String, SQLConsumer)} except
     * does everything asynchronously and returns a {@link CompletableFuture}.
     *
     * @see DatabaseClient#query(String, SQLConsumer)
     */
    public CompletableFuture<QueryResult> queryAsync(@NotNull String sqlStatement, @Nullable SQLConsumer<PreparedStatement> psPreparer) {
        return CompletableFuture.supplyAsync(() -> this.query(sqlStatement, psPreparer), this.threadPool);
    }

    /**
     * Executes a SQL DML statement and returns the number of rows that were altered.
     *
     * @param sqlStatement The SQL statement to execute
     * @return The number of rows altered
     * @throws UncheckedSQLException If a {@link SQLException} occurs
     */
    public int update(@NotNull String sqlStatement) throws UncheckedSQLException {
        return update(sqlStatement, new Object[0]);
    }

    /**
     * Executes a SQL DML statement and returns the number of rows that were altered.
     *
     * @param sqlStatement The SQL statement to execute
     * @param params The parameters for the statement
     * @return The number of rows altered
     * @throws UncheckedSQLException If a {@link SQLException} occurs
     */
    public int update(@NotNull String sqlStatement, Object... params) throws UncheckedSQLException {
        return update(sqlStatement, preparedStatement -> {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
        });
    }

    /**
     * Executes a SQL DML statement and returns the number of rows that were altered.
     *
     * @param sqlStatement The SQL statement to execute
     * @param psPreparer   The preparer that prepares the SQL statement
     * @return The number of rows altered
     * @throws UncheckedSQLException If a {@link SQLException} occurs
     */
    public int update(@NotNull String sqlStatement, @Nullable SQLConsumer<PreparedStatement> psPreparer) throws UncheckedSQLException {
        try (Connection connection = this.getConnectionInternal(); PreparedStatement statement = connection.prepareStatement(sqlStatement)) {
            if (psPreparer != null)
                psPreparer.accept(statement);
            return statement.executeUpdate();
        } catch (SQLException exception) {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * Does the same thing as {@link DatabaseClient#update(String, SQLConsumer)} except
     * does everything asynchronously and returns a {@link CompletableFuture}.
     *
     * @see DatabaseClient#update(String)
     */
    public CompletableFuture<Integer> updateAsync(@NotNull String sqlStatement) throws UncheckedSQLException {
        return updateAsync(sqlStatement, null);
    }

    /**
     * Does the same thing as {@link DatabaseClient#update(String, SQLConsumer)} except
     * does everything asynchronously and returns a {@link CompletableFuture}.
     *
     * @see DatabaseClient#update(String, SQLConsumer)
     */
    public CompletableFuture<Integer> updateAsync(@NotNull String sqlStatement, @Nullable SQLConsumer<PreparedStatement> psPreparer) throws UncheckedSQLException {
        return CompletableFuture.supplyAsync(() -> this.update(sqlStatement, psPreparer), this.threadPool);
    }

    /**
     * Submits a batch of commands to the database. You must include all
     * {@link PreparedStatement#addBatch} calls inside the preparer.
     *
     * @param sqlStatement The SQL statement to execute
     * @return The results of the query
     * @throws UncheckedSQLException If a {@link SQLException} occurs
     */
    public int executeBatch(@NotNull String sqlStatement, @NotNull SQLConsumer<PreparedStatement> psPreparer) throws UncheckedSQLException {
        Connection connection = null;
        try {
            connection = this.getConnectionInternal();
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sqlStatement)) {
                psPreparer.accept(statement);
                int[] affectedRecords = statement.executeBatch();
                connection.commit();
                return Arrays.stream(affectedRecords).sum();
            }
        } catch (SQLException exception) {
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException rollbackEx) {
                exception.addSuppressed(rollbackEx);
            }
            throw new UncheckedSQLException(exception);
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException ignored) { /* ignore this since there isn't much we can do */ }
        }
    }

    /**
     * Submits a batch of commands to the database. You must include all
     * {@link PreparedStatement#addBatch} calls inside the preparer.
     *
     * @param sqlStatement The SQL statement to execute
     * @return The results of the query
     * @throws UncheckedSQLException If a {@link SQLException} occurs
     */
    public CompletableFuture<Integer> executeBatchAsync(@NotNull String sqlStatement, @NotNull SQLConsumer<PreparedStatement> psPreparer)  {
        return CompletableFuture.supplyAsync(() -> this.executeBatch(sqlStatement, psPreparer), this.threadPool);
    }

    /**
     * Starts a transaction and executes the specified operation.
     * This method manages the entire transaction lifecycle by committing the transaction if the operation
     * succeeds or rolling back if an exception occurs.
     *
     * @param <T> The type of the result returned by the operation
     * @param operation The transactional operation to be executed
     * @throws UncheckedSQLException If a {@link SQLException} occurs during the transaction
     */
    public <T> T executeTransaction(TransactionOperation<T> operation) throws UncheckedSQLException {
        try (Connection connection = this.getConnectionInternal()) {
            try {
                connection.setAutoCommit(false);
                transactionConnection.set(connection);

                T result = operation.execute();
                connection.commit();
                return result;
            } catch (SQLException exception) {
                connection.rollback();
                System.err.println("Transaction rolled back due to: " + exception.getMessage());
                throw new UncheckedSQLException(exception);
            }
        } catch (SQLException exception) {
            throw new UncheckedSQLException(exception);
        } finally {
            transactionConnection.remove();
        }
    }

    /**
     * Does the same thing as {@link DatabaseClient#executeTransaction(TransactionOperation)} except
     * does everything asynchronously and returns a {@link CompletableFuture}.
     *
     * @see DatabaseClient#executeTransactionAsync(TransactionOperation)
     */
    public <T> CompletableFuture<T> executeTransactionAsync(TransactionOperation<T> operation) throws UncheckedSQLException {
        return CompletableFuture.supplyAsync(() -> executeTransaction(operation), this.threadPool);
    }

    public abstract void shutdown();

    private Connection getConnectionInternal() throws SQLException {
        Connection connection = transactionConnection.get();  // If we are in a transaction, use the cached connection
        if (connection == null) {
            return this.getConnection();
        }

        return connection;
    }

    public abstract Connection getConnection() throws SQLException;
}