package io.github.evancolewright.saphira;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.evancolewright.saphira.exception.DatabaseClientInitializationException;
import io.github.evancolewright.saphira.exception.UncheckedSQLException;
import io.github.evancolewright.saphira.query.QueryResults;
import org.jetbrains.annotations.NotNull;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class DatabaseClient
{
    private final HikariDataSource hikariDataSource;
    private final ExecutorService threadPool;

    /**
     * Creates a new DatabaseClient instance.
     *
     * @param databaseType        The type of database you want to connect to
     * @param databaseCredentials The credentials to use to authenticate the client
     */
    public DatabaseClient(@NotNull DatabaseDriver databaseType, @NotNull DatabaseCredentials databaseCredentials)
    {
        this.threadPool = Executors.newCachedThreadPool();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(databaseType.getJdbcUrlPrefix() + "://" + databaseCredentials.host + ":" + databaseCredentials.port + "/" + databaseCredentials.database);
        hikariConfig.setUsername(databaseCredentials.username);
        hikariConfig.setPassword(databaseCredentials.password);

        hikariConfig.addDataSourceProperty("useUnicode", "true");
        hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");

        try
        {
            Class.forName(databaseType.getDriverClassName());
        } catch (ClassNotFoundException exception)
        {
            // If it is MySQL, they may be using an older driver...
            if (databaseType == DatabaseDriver.MYSQL)
            {
                try
                {
                    // Try to get the old driver
                    Class.forName("com.mysql.jdbc.Driver");
                    // We also seem to have to set it here
                    hikariConfig.setDataSourceClassName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException exception1)
                {
                    throw new DatabaseClientInitializationException("Failed to find any suitable driver class for MySQL!", exception);
                    // SOL
                }
            }
            throw new DatabaseClientInitializationException(String.format("Failed to find driver class: %s", databaseType.getDriverClassName()), exception);
            // SOL
        }

        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    /**
     * Executes a SQL DML statement and returns the number of rows that were altered.
     *
     * @param sqlStatement The SQL statement to execute
     * @param psPreparer   The preparer that prepares the SQL statement
     * @return The number of rows altered
     * @throws UncheckedSQLException If a {@link SQLException} occurs
     */
    public int update(@NotNull String sqlStatement, @NotNull SQLConsumer<PreparedStatement> psPreparer) throws UncheckedSQLException
    {
        try (Connection connection = this.getHikariDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement(sqlStatement))
        {
            psPreparer.accept(statement);
            return statement.executeUpdate();
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * Does the same thing as {@link DatabaseClient#update(String, SQLConsumer)} except
     * does everything asynchronously and returns a {@link CompletableFuture}.
     *
     * @see DatabaseClient#update(String, SQLConsumer)
     */
    public CompletableFuture<Integer> updateAsync(@NotNull String sqlStatement, @NotNull SQLConsumer<PreparedStatement> psPreparer) throws UncheckedSQLException
    {
        return CompletableFuture.supplyAsync(() -> this.update(sqlStatement, psPreparer), this.threadPool);
    }

    /**
     * Queries the database for results and returns a {@link QueryResults} instance.
     *
     * @param sqlStatement The SQL statement to execute
     * @return The results of the query
     * @throws UncheckedSQLException If a {@link SQLException} occurs
     * @see QueryResults
     */
    public QueryResults query(@NotNull String sqlStatement) throws UncheckedSQLException
    {
        try (Connection connection = this.getHikariDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement(sqlStatement))
        {
            try (ResultSet resultSet = statement.executeQuery())
            {
                CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
                cachedRowSet.populate(resultSet);
                return new QueryResults(cachedRowSet);
            }
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * Does the same thing as {@link DatabaseClient#query(String)} except
     * does everything asynchronously and returns a {@link CompletableFuture}.
     *
     * @see DatabaseClient#query(String)
     */
    public CompletableFuture<QueryResults> queryAsync(@NotNull String sqlStatement)
    {
        return CompletableFuture.supplyAsync(() -> this.query(sqlStatement), this.threadPool);
    }

    /**
     * Submits a batch of commands to the database.  You must include all
     * {@link PreparedStatement#addBatch()} calls inside the preparer.
     *
     * @param sqlStatement The SQL statement to execute
     * @return The results of the query
     * @throws UncheckedSQLException If a {@link SQLException} occurs
     */
    public int[] executeBatch(@NotNull String sqlStatement, @NotNull SQLConsumer<PreparedStatement> psPreparer)
    {
        try (Connection connection = this.getHikariDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement(sqlStatement))
        {
            connection.setAutoCommit(false);

            psPreparer.accept(statement);

            int[] count = statement.executeBatch();
            connection.commit();

            return count;
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * Does the same thing as {@link DatabaseClient#executeBatch(String, SQLConsumer)} except
     * does everything asynchronously and converts the int[] returned from {@link PreparedStatement#executeBatch()}
     * to a {@link List<Integer>} and returns as a {@link CompletableFuture}.
     *
     * @see DatabaseClient#executeBatch(String, SQLConsumer)
     */
    public CompletableFuture<List<Integer>> executeBatchAsync(String sqlStatement, SQLConsumer<PreparedStatement> psPreparer)
    {
        return CompletableFuture.supplyAsync(() -> Arrays.stream(this.executeBatch(sqlStatement, psPreparer))
                .boxed().collect(Collectors.toList()), this.threadPool);
    }

    /**
     * Shuts down the internal {@link com.zaxxer.hikari.pool.HikariPool}.
     * <p>
     * You should remember to call this whenever you no longer need the instance anymore.
     */
    public void shutdown()
    {
        if (isConnected())
            this.hikariDataSource.close();
    }

    /**
     * Checks whether the internal {@link com.zaxxer.hikari.pool.HikariPool} is connected.
     *
     * @return Whether the pool is connected
     */
    public boolean isConnected()
    {
        return this.hikariDataSource != null && !this.hikariDataSource.isClosed();
    }

    /**
     * Returns the internal {@link HikariDataSource}.  You can use this instance to
     * get pooled {@link Connection} by using {@link HikariDataSource#getConnection()}.
     *
     * @return The hikari datasource instance
     */
    public HikariDataSource getHikariDataSource()
    {
        return this.hikariDataSource;
    }
}
