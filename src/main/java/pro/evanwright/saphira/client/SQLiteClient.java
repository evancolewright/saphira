package pro.evanwright.saphira.client;

import pro.evanwright.saphira.DatabaseClient;
import pro.evanwright.saphira.exception.DatabaseClientInitializationException;
import pro.evanwright.saphira.exception.UncheckedSQLException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteClient extends DatabaseClient
{
    private Connection connection;
    private final String connectionString;

    /**
     * Creates a new SQLiteClient instance.
     * @param file  The database file
     * @throws DatabaseClientInitializationException  If the SQLite driver is not found
     * @throws  IllegalArgumentException  If the file is not a database file.
     */
    public SQLiteClient(@NotNull File file) throws DatabaseClientInitializationException, IllegalArgumentException
    {
        if (!file.getName().endsWith(".db"))
            throw new IllegalArgumentException("File must end with the '.db' extension!");

        try
        {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException exception)
        {
            throw new DatabaseClientInitializationException("Could not load SQLite driver!", exception);
        }

        this.connectionString = String.format("jdbc:sqlite:%s", file);
    }

    /**
     * Runs the 'VACUUM' command and shuts down the connection instance.
     * @throws UncheckedSQLException If a SQL exception occurs
     */
    @Override
    public void shutdown() throws UncheckedSQLException
    {
        update("VACUUM", preparedStatement -> {});
        try
        {
            if (isConnected())
                connection.close();
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    private boolean isConnected() throws SQLException
    {
        return this.connection != null && !this.connection.isClosed();
    }

    /**
     * Gets the {@link Connection} from memory or creates a new one if the cached one is closed.
     * @return  A ready-to-use {@link Connection}
     * @throws SQLException  If database access errors occur
     */
    @Override
    public Connection getConnection() throws SQLException
    {
        if (!isConnected())
            this.connection = DriverManager.getConnection(this.connectionString);
        return this.connection;
    }
}
