package pro.evanwright.saphira;

import org.jetbrains.annotations.NotNull;

/**
 * A data container that stores remote databases credentials to be passed to a {@link DatabaseClient} instance.
 */
public class DatabaseSettings {
    private static final String DEFAULT_POOL_NAME = "Saphira Connection Pool";
    private static final String DEFAULT_PORT = "3306";

    public final String poolName, host, database, username, password, port;
    public final boolean optimizeHikari;

    public DatabaseSettings(@NotNull String host, @NotNull String database, @NotNull String username, @NotNull String password) {
        this(DEFAULT_POOL_NAME, host, database, username, password, DEFAULT_PORT, true);
    }

    public DatabaseSettings(@NotNull String host, @NotNull String database, @NotNull String username, @NotNull String password, boolean optimizeHikari) {
        this(DEFAULT_POOL_NAME, host, database, username, password, DEFAULT_PORT, optimizeHikari);
    }

    public DatabaseSettings(@NotNull String poolName, @NotNull String host, @NotNull String database, @NotNull String username, @NotNull String password) {
        this(poolName, host, database, username, password, DEFAULT_PORT, true);
    }

    public DatabaseSettings(@NotNull String poolName, @NotNull String host, @NotNull String database, @NotNull String username, @NotNull String password, boolean optimizeHikari) {
        this(poolName, host, database, username, password, DEFAULT_PORT, optimizeHikari);
    }

    public DatabaseSettings(@NotNull String poolName, @NotNull String host, @NotNull String database, @NotNull String username, @NotNull String password, @NotNull String port, boolean optimizeHikari) {
        this.poolName = poolName;
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
        this.optimizeHikari = optimizeHikari;
    }

    @Override
    public String toString() {
        return "DatabaseCredentials{" +
                "host='" + host + '\'' +
                ", database='" + database + '\'' +
                ", username='" + username + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
