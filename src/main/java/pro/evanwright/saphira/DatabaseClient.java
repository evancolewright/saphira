package pro.evanwright.saphira;

import pro.evanwright.saphira.exception.UncheckedSQLException;
import pro.evanwright.saphira.query.QueryResults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class DatabaseClient {
    private final ExecutorService threadPool;

    protected DatabaseClient() {
        this.threadPool = Executors.newCachedThreadPool();
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
        try (Connection connection = this.getConnection(); PreparedStatement statement = connection.prepareStatement(sqlStatement)) {
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
     * @see DatabaseClient#update(String, SQLConsumer)
     */
    public CompletableFuture<Integer> updateAsync(@NotNull String sqlStatement, @NotNull SQLConsumer<PreparedStatement> psPreparer) throws UncheckedSQLException {
        return CompletableFuture.supplyAsync(() -> this.update(sqlStatement, psPreparer), this.threadPool);
    }

    /**
     * Queries the database for results and returns a {@link QueryResults} instance.
     *
     * @param sqlStatement The SQL statement to execute
     * @param psPreparer   The preparer that prepares the SQL statement
     * @return The results of the query
     * @throws UncheckedSQLException If a {@link SQLException} occurs
     * @see QueryResults
     */
    public QueryResults query(@NotNull String sqlStatement, @Nullable SQLConsumer<PreparedStatement> psPreparer) throws UncheckedSQLException {
        try (Connection connection = this.getConnection(); PreparedStatement statement = connection.prepareStatement(sqlStatement)) {
            if (psPreparer != null)
                psPreparer.accept(statement);
            try (ResultSet resultSet = statement.executeQuery()) {
                CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
                cachedRowSet.populate(resultSet);
                return new QueryResults(cachedRowSet);
            }
        } catch (SQLException exception) {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * Does the same thing as {@link DatabaseClient#query(String, SQLConsumer)} except
     * does everything asynchronously and returns a {@link CompletableFuture}.
     *
     * @see DatabaseClient#query(String, SQLConsumer)
     */
    public CompletableFuture<QueryResults> queryAsync(@NotNull String sqlStatement, @Nullable SQLConsumer<PreparedStatement> psPreparer) {
        return CompletableFuture.supplyAsync(() -> this.query(sqlStatement, psPreparer), this.threadPool);
    }

    public abstract void shutdown();

    public abstract Connection getConnection() throws SQLException;
}