package pro.evanwright.saphira.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pro.evanwright.saphira.exception.UncheckedSQLException;
import pro.evanwright.saphira.mock.MockMySQLDatabaseClient;
import pro.evanwright.saphira.query.QueryResult;

import java.util.Optional;

public class MySQLDatabaseClientTests {
    private MockMySQLDatabaseClient mockMySQLDatabaseClient;

    @BeforeEach
    public void setUp() {
        mockMySQLDatabaseClient = new MockMySQLDatabaseClient();

        try {
            mockMySQLDatabaseClient.update("DROP TABLE IF EXISTS Users");
            mockMySQLDatabaseClient.update("CREATE TABLE Users (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(255), " +
                    "dob VARCHAR(255))");
            mockMySQLDatabaseClient.update("INSERT INTO Users (name, dob) VALUES (?, ?)", "John Doe", "1990-01-01");
        } catch (UncheckedSQLException e) {
            Assertions.fail("Failed to set up database state for test", e);
        }
    }

    @AfterEach
    public void tearDown() {
        mockMySQLDatabaseClient.shutdown();
    }

    @Test
    public void queryTest() {
        final String selectSql = "SELECT * FROM Users ORDER BY id ASC;";

        QueryResult result = mockMySQLDatabaseClient.query(selectSql);

        Assertions.assertTrue(result.next());
        Assertions.assertEquals("John Doe", result.getString("name"));
        Assertions.assertEquals("1990-01-01", result.getString("dob"));
    }

    @Test
    public void executeUpdateTest() {
        final String updateSql = "UPDATE Users SET name = ? WHERE id = ?";
        int rowsAffected = mockMySQLDatabaseClient.update(updateSql, "Jane Doe", 1);

        Assertions.assertEquals(1, rowsAffected);

        final String selectSql = "SELECT * FROM Users WHERE id = 1";
        QueryResult result = mockMySQLDatabaseClient.query(selectSql);
        Assertions.assertTrue(result.next());
        Assertions.assertEquals("Jane Doe", result.getString("name"));
    }

    @Test
    public void executeBatchTest() {
        final String batchSql = "INSERT INTO Users (name, dob) VALUES (?, ?)";
        int rowsAffected = mockMySQLDatabaseClient.executeBatch(batchSql, ps -> {
            ps.setString(1, "Billy");
            ps.setString(2, "1991-02-02");
            ps.addBatch();
            ps.setString(1, "Bob");
            ps.setString(2, "1992-03-03");
            ps.addBatch();
            ps.setString(1, "Francis");
            ps.setString(2, "1993-04-04");
            ps.addBatch();
        });

        Assertions.assertEquals(3, rowsAffected);

        final String selectSql = "SELECT * FROM Users ORDER BY id ASC;";

        QueryResult result = mockMySQLDatabaseClient.query(selectSql);

        int count = 0;
        while (result.next()) {
            count++;
        }

        Assertions.assertEquals(4, count); // 4 includes initial user added in setup
    }

    @Test
    public void executeTransactionTest() {
        int rowsAffected = mockMySQLDatabaseClient.executeTransaction(() -> {
            int insertCount = mockMySQLDatabaseClient.update("INSERT INTO Users (name, dob) VALUES (?, ?)", "Alice", "1994-05-12");
            int updateCount = mockMySQLDatabaseClient.update("UPDATE Users SET name = ?, dob = ? WHERE id = ?", "Johnny Doe", "1994-05-12", 1);

            return insertCount + updateCount;
        });

        Assertions.assertEquals(2, rowsAffected);

        QueryResult result = mockMySQLDatabaseClient.query("SELECT * FROM Users WHERE name = 'Johnny Doe'");
        Assertions.assertTrue(result.next());
        Assertions.assertEquals("1994-05-12", result.getString("dob"));

        QueryResult result2 = mockMySQLDatabaseClient.query("SELECT * FROM Users WHERE name = 'Alice'");
        Assertions.assertTrue(result2.next(), "No results found for Alice");
        Assertions.assertEquals("Alice", result2.getString("name"));
        Assertions.assertEquals("1994-05-12", result2.getString("dob"));
    }

    @Test
    public void executeTransactionFailureTest() {
        Assertions.assertThrows(UncheckedSQLException.class, () -> {
            mockMySQLDatabaseClient.executeTransaction(() -> {
                mockMySQLDatabaseClient.update("INSERT INTO Users (name, dob) VALUES (?, ?)", "Alice", "1994-05-12");
                mockMySQLDatabaseClient.update("INSERT INTO Users (name, dob) VALUES (?, ?)", "Bob", "1994-05-12");
                throw new UncheckedSQLException("I just really want to fail today");
            });
        });

        Optional<Long> rowCount = mockMySQLDatabaseClient.query("SELECT COUNT(id) FROM Users").getFirstColValue();
        rowCount.ifPresent(count -> Assertions.assertEquals(1, count));
    }
}
