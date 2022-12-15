package pro.evanwright.saphira;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pro.evanwright.saphira.client.MySQLClient;
import pro.evanwright.saphira.query.QueryResults;

public class QueryDatabaseTest
{
    MySQLClient mySQLClient;

    @Before
    public void setup()
    {
        mySQLClient = new MySQLClient(new DatabaseCredentials(
                System.getProperty("host", "127.0.0.1"), System.getProperty("database"),
                System.getProperty("username"), System.getProperty("password"),
                System.getProperty("port", "3306")
        ));
        mySQLClient.update("CREATE TABLE Test (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100));", (ps) -> {});
    }

    @After
    public void cleanup()
    {
        mySQLClient.update("DROP TABLE Test;", (preparedStatement) -> {});
    }

    @Test
    public void testQuery()
    {
        mySQLClient.update("INSERT INTO Test (name) VALUES ('Evan');", (ps) -> {});
        QueryResults results = mySQLClient.query("SELECT * FROM Test", (ps) -> {});
        results.next();
        assert(results.getString("name").equals("Evan"));
    }
}
