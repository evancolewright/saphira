package pro.evanwright.saphira;

import org.jetbrains.annotations.NotNull;

/**
 * Represents credentials to authenticate a {@link DatabaseClient} to the database.
 */
public class DatabaseCredentials {
    public final String poolName, host, database, username, password, port;
    public final boolean optimizeHikari;


    public DatabaseCredentials(@NotNull String host, @NotNull String database, @NotNull String username, @NotNull String password) {
        this("Saphira Connection Pool", host, database, username, password, "3306", true);
    }

    public DatabaseCredentials(@NotNull String host, @NotNull String database, @NotNull String username, @NotNull String password, boolean optimizeHikari) {
        this("Saphira Connection Pool", host, database, username, password, "3306", optimizeHikari);
    }

    public DatabaseCredentials(@NotNull String poolName, @NotNull String host, @NotNull String database, @NotNull String username, @NotNull String password) {
        this(poolName, host, database, username, password, "3306", true);
    }

    public DatabaseCredentials(@NotNull String poolName, @NotNull String host, @NotNull String database, @NotNull String username, @NotNull String password, boolean optimizeHikari) {
        this(poolName, host, database, username, password, "3306", optimizeHikari);
    }

    public DatabaseCredentials(@NotNull String poolName, @NotNull String host, @NotNull String database, @NotNull String username, @NotNull String password, @NotNull String port, boolean optimizeHikari) {
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
                ", password='" + password + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
