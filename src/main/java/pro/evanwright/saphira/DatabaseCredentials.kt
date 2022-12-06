package pro.evanwright.saphira

/**
 * Represents credentials to authenticate a [DatabaseClient] to a database.
 */
data class DatabaseCredentials @JvmOverloads constructor(
    val host: String,
    val database: String,
    val username: String,
    val password: String,
    val port: String = "3306"
)