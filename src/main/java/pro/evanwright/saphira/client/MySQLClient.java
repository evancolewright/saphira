package pro.evanwright.saphira.client;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import pro.evanwright.saphira.DatabaseClient;
import pro.evanwright.saphira.DatabaseCredentials;
import pro.evanwright.saphira.exception.DatabaseClientInitializationException;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLClient extends DatabaseClient {

    private static final String NEW_MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String LEGACY_MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    private final HikariDataSource hikariDataSource;

    /**
     * Creates a new MySQLClient instance.
     * @param databaseCredentials  The credentials to authenticate to the database
     */
    public MySQLClient(@NotNull DatabaseCredentials databaseCredentials) {
        HikariConfig hikariConfig = new HikariConfig();
        boolean foundMaria = false;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            hikariConfig.setJdbcUrl(String.format("jdbc:mariadb://%s:%s/%s", databaseCredentials.host, databaseCredentials.port, databaseCredentials.database));
            foundMaria = true;
        } catch (ClassNotFoundException e) {
            hikariConfig.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s", databaseCredentials.host, databaseCredentials.port, databaseCredentials.database));
        }
        hikariConfig.setUsername(databaseCredentials.username);
        hikariConfig.setPassword(databaseCredentials.password);

        hikariConfig.setPoolName(databaseCredentials.poolName);

        hikariConfig.addDataSourceProperty("useUnicode", "true");
        hikariConfig.addDataSourceProperty("characterEncoding", "utf8");

        if (databaseCredentials.optimizeHikari) {
            hikariConfig.setMaxLifetime(30000);
            hikariConfig.setIdleTimeout(10000);
            hikariConfig.setMaximumPoolSize(20);
            hikariConfig.setMinimumIdle(3);
            hikariConfig.addDataSourceProperty("cachePrepStmts", true);
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", 250);
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
            hikariConfig.addDataSourceProperty("useServerPrepStmts", true);
            hikariConfig.addDataSourceProperty("cacheCallableStmts", true);
            hikariConfig.addDataSourceProperty("cacheResultSetMetadata", true);
            hikariConfig.addDataSourceProperty("cacheServerConfiguration", true);
            hikariConfig.addDataSourceProperty("useLocalSessionState", true);
            hikariConfig.addDataSourceProperty("elideSetAutoCommits", true);
            hikariConfig.addDataSourceProperty("alwaysSendSetIsolation", false);
        }

        if (!foundMaria) {
            try {
                Class.forName(NEW_MYSQL_DRIVER);
            } catch (ClassNotFoundException exception) {
                try {
                    Class.forName(LEGACY_MYSQL_DRIVER);
                    hikariConfig.setDriverClassName(LEGACY_MYSQL_DRIVER);  // This is required for the legacy driver...
                } catch (ClassNotFoundException exception1) {
                    throw new DatabaseClientInitializationException("Failed to load a suitable MySQL driver!", exception1);
                }
            }
        }

        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    /**
     * Shuts down the internal {@link com.zaxxer.hikari.pool.HikariPool}.  You should
     * always call this when you no longer need the instance.
     */
    @Override
    public void shutdown() {
        this.hikariDataSource.close();
    }

    /**
     * Gets a ready-to-use {@link Connection} from the internal {@link com.zaxxer.hikari.pool.HikariPool}.
     *
     * @return A ready-to-use {@link Connection} instance
     * @throws SQLException If database access errors occur
     */
    @Override
    public Connection getConnection() throws SQLException {
        return this.hikariDataSource.getConnection();
    }
}