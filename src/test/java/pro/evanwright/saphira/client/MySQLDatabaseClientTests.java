package pro.evanwright.saphira.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pro.evanwright.saphira.mock.MockMySQLDatabaseClient;
import pro.evanwright.saphira.query.QueryResults;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLDatabaseClientTests {
    private MockMySQLDatabaseClient mockMySQLDatabaseClient;

    @BeforeEach
    public void setUp() {
        mockMySQLDatabaseClient = new MockMySQLDatabaseClient();

        try (Connection connection = mockMySQLDatabaseClient.getConnection()) {
            connection.createStatement().execute("DROP TABLE IF EXISTS Users");
            connection.createStatement().execute("CREATE TABLE Users (\n" +
                    "    id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "    name VARCHAR(255)\n" +
                    ")");
        } catch (SQLException e) {
            e.printStackTrace();
            Assertions.fail("Failed to set up database state for test");
        }
    }

    @AfterEach
    public void tearDown() {
        mockMySQLDatabaseClient.shutdown();
    }

    @Test
    public void executeBatchTest() {
        final String batchSQL = "INSERT INTO Users (Username) VALUES (?)";
        int rowsAffected = mockMySQLDatabaseClient.executeBatch(batchSQL, ps -> {
            ps.setString(1, "Billy");
            ps.addBatch();
            ps.setString(1, "Bob");
            ps.addBatch();
            ps.setString(1, "Francis");
            ps.addBatch();
        });

        Assertions.assertEquals(3, rowsAffected);
    }
}
