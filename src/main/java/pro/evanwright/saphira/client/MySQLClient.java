package pro.evanwright.saphira.client;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import pro.evanwright.saphira.DatabaseClient;
import pro.evanwright.saphira.DatabaseCredentials;
import pro.evanwright.saphira.exception.DatabaseClientInitializationException;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLClient extends DatabaseClient
{
    private final HikariDataSource hikariDataSource;
    public MySQLClient(@NotNull DatabaseCredentials databaseCredentials)
    {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + databaseCredentials.host + ":" + databaseCredentials.port + "/" + databaseCredentials.database);
        hikariConfig.setUsername(databaseCredentials.username);
        hikariConfig.setPassword(databaseCredentials.password);

        hikariConfig.addDataSourceProperty("useUnicode", "true");
        hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");

        try
        {
            // Try to use the new driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException exception)
        {
            try
            {
                // Fallback to the old driver
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException exception1)
            {
                // You are SOL
                throw new DatabaseClientInitializationException("Failed to load a suitable MySQL driver!", exception1);
            }
        }

        try
        {
            this.hikariDataSource = new HikariDataSource(hikariConfig);
        } catch (Exception exception)
        {
            throw new DatabaseClientInitializationException("Failed to start the HikariPool.  Are you sure that your credentials are correct?", exception);
        }
    }

    /**
     * Shuts down the internal {@link com.zaxxer.hikari.pool.HikariPool}.  You should
     * always close this when you no longer need the instance.
     */
    @Override
    public void shutdown()
    {
        this.hikariDataSource.close();
    }

    /**
     * Gets a ready-to-use {@link Connection} from the internal {@link com.zaxxer.hikari.pool.HikariPool}.
     * @return  A Hot-and-ready {@link Connection} instance
     * @throws SQLException  If database access errors occur
     */
    @Override
    public Connection getConnection() throws SQLException
    {
        return this.hikariDataSource.getConnection();
    }
}