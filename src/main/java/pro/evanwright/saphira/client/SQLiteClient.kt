package pro.evanwright.saphira.client

import pro.evanwright.saphira.DatabaseClient
import pro.evanwright.saphira.exception.DatabaseClientInitializationException
import pro.evanwright.saphira.exception.UncheckedSQLException
import java.io.File
import java.lang.IllegalArgumentException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException

class SQLiteClient(file: File) : DatabaseClient() {
    private override var connection: Connection? = null
    private val connectionString: String

    /**
     * Creates a new SQLiteClient instance.
     * @param file  The database file
     * @throws DatabaseClientInitializationException  If the SQLite driver is not found
     * @throws  IllegalArgumentException  If the file is not a database file.
     */
    init {
        require(file.name.endsWith(".db")) { "File must end with the '.db' extension!" }
        try {
            Class.forName("org.sqlite.JDBC")
        } catch (exception: ClassNotFoundException) {
            throw DatabaseClientInitializationException("Could not load SQLite driver!", exception)
        }
        connectionString = String.format("jdbc:sqlite:%s", file)
    }

    /**
     * Runs the 'VACUUM' command and shuts down the connection instance.
     * @throws UncheckedSQLException If a SQL exception occurs
     */
    @Throws(UncheckedSQLException::class)
    override fun shutdown() {
        update("VACUUM") { preparedStatement: PreparedStatement? -> }
        try {
            if (isConnected) connection!!.close()
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    @get:Throws(SQLException::class)
    private val isConnected: Boolean
        private get() = connection != null && !connection!!.isClosed

    /**
     * Gets the [Connection] from memory or creates a new one if the cached one is closed.
     * @return  A ready-to-use [Connection]
     * @throws SQLException  If database access errors occur
     */
    @Throws(SQLException::class)
    override fun getConnection(): Connection {
        if (!isConnected) connection = DriverManager.getConnection(connectionString)
        return connection!!
    }
}