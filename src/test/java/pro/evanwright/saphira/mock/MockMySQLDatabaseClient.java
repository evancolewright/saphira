package pro.evanwright.saphira.mock;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import pro.evanwright.saphira.DatabaseClient;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A mock {@link DatabaseClient} to allow for integration testing MySQL using H2.
 */
public class MockMySQLDatabaseClient extends DatabaseClient {
    private final HikariDataSource dataSource;

    public MockMySQLDatabaseClient() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1");
        hikariConfig.setDriverClassName("org.h2.Driver");

        this.dataSource = new HikariDataSource(hikariConfig);
    }

    @Override
    public void shutdown() {
        if (this.dataSource != null) {
            this.dataSource.close();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
}
