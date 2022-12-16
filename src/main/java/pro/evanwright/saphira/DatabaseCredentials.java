package pro.evanwright.saphira;

import org.jetbrains.annotations.NotNull;

/**
 * Represents credentials to authenticate a {@link DatabaseClient} to the database.
 */
public class DatabaseCredentials {
    public final String host, database, username, password, port;

    public DatabaseCredentials(@NotNull String host, @NotNull String database, @NotNull String username, @NotNull String password) {
        this(host, database, username, password, "3306");
    }

    public DatabaseCredentials(@NotNull String host, @NotNull String database, @NotNull String username, @NotNull String password, @NotNull String port) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
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
