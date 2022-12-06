package pro.evanwright.saphira.client

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import pro.evanwright.saphira.DatabaseClient
import pro.evanwright.saphira.DatabaseCredentials
import pro.evanwright.saphira.exception.DatabaseClientInitializationException
import java.sql.Connection
import java.sql.SQLException

class MySQLClient(databaseCredentials: DatabaseCredentials) : DatabaseClient() {
    private var hikariDataSource: HikariDataSource? = null

    init {
        val hikariConfig = HikariConfig()
        hikariConfig.jdbcUrl =
            "jdbc:mysql://" + databaseCredentials.host + ":" + databaseCredentials.port + "/" + databaseCredentials.database
        hikariConfig.username = databaseCredentials.username
        hikariConfig.password = databaseCredentials.password
        hikariConfig.addDataSourceProperty("useUnicode", "true")
        hikariConfig.addDataSourceProperty("characterEncoding", "utf8")
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true")
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250")
        try {
            // Try to use the new driver
            Class.forName("com.mysql.cj.jdbc.Driver")
        } catch (exception: ClassNotFoundException) {
            try {
                // Fallback to the old driver
                Class.forName("com.mysql.jdbc.Driver")
            } catch (exception1: ClassNotFoundException) {
                // You are SOL
                throw DatabaseClientInitializationException("Failed to load a suitable MySQL driver!", exception1)
            }
        }
        try {
            hikariDataSource = HikariDataSource(hikariConfig)
        } catch (exception: Exception) {
            throw DatabaseClientInitializationException(
                "Failed to start the HikariPool.  Are you sure that your credentials are correct?",
                exception
            )
        }
    }

    /**
     * Shuts down the internal [com.zaxxer.hikari.pool.HikariPool].  You should
     * always close this when you no longer need the instance.
     */
    override fun shutdown() {
        hikariDataSource!!.close()
    }

    /**
     * Gets a ready-to-use [Connection] from the internal [com.zaxxer.hikari.pool.HikariPool].
     * @return  A Hot-and-ready [Connection] instance
     * @throws SQLException  If database access errors occur
     */
    @get:Throws(SQLException::class)
    override val connection: Connection
        get() = hikariDataSource!!.connection
}