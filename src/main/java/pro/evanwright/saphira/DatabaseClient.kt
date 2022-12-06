package pro.evanwright.saphira

import pro.evanwright.saphira.exception.UncheckedSQLException
import pro.evanwright.saphira.query.QueryResults
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.stream.Collectors
import javax.sql.rowset.RowSetProvider

abstract class DatabaseClient {
    private val threadPool = Executors.newCachedThreadPool()

    /**
     * Executes a SQL DML statement and returns the number of rows that were altered.
     *
     * @param sqlStatement The SQL statement to execute
     * @param psPreparer   The preparer that prepares the SQL statement
     * @return The number of rows altered
     * @throws UncheckedSQLException If a [SQLException] occurs
     */
    @Throws(UncheckedSQLException::class)
    fun update(sqlStatement: String, psPreparer: SQLConsumer<PreparedStatement?>?): Int {
        try {
            connection.use { connection ->
                connection.prepareStatement(sqlStatement).use { statement ->
                    psPreparer?.accept(statement)
                    return statement.executeUpdate()
                }
            }
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * Does the same thing as [DatabaseClient.update] except
     * does everything asynchronously and returns a [CompletableFuture].
     *
     * @see DatabaseClient.update
     */
    @Throws(UncheckedSQLException::class)
    fun updateAsync(sqlStatement: String, psPreparer: SQLConsumer<PreparedStatement?>): CompletableFuture<Int> {
        return CompletableFuture.supplyAsync({ update(sqlStatement, psPreparer) }, threadPool)
    }

    /**
     * Queries the database for results and returns a [QueryResults] instance.
     *
     * @param sqlStatement The SQL statement to execute
     * @param psPreparer   The preparer that prepares the SQL statement
     * @return The results of the query
     * @throws UncheckedSQLException If a [SQLException] occurs
     * @see QueryResults
     */
    @Throws(UncheckedSQLException::class)
    fun query(sqlStatement: String, psPreparer: SQLConsumer<PreparedStatement?>?): QueryResults {
        try {
            connection.use { connection ->
                connection.prepareStatement(sqlStatement).use { statement ->
                    psPreparer?.accept(statement)
                    statement.executeQuery().use { resultSet ->
                        val cachedRowSet = RowSetProvider.newFactory().createCachedRowSet()
                        cachedRowSet.populate(resultSet)
                        return QueryResults(cachedRowSet)
                    }
                }
            }
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * Does the same thing as [DatabaseClient.query] except
     * does everything asynchronously and returns a [CompletableFuture].
     *
     * @see DatabaseClient.query
     */
    fun queryAsync(
        sqlStatement: String,
        psPreparer: SQLConsumer<PreparedStatement?>?
    ): CompletableFuture<QueryResults> {
        return CompletableFuture.supplyAsync({ query(sqlStatement, psPreparer) }, threadPool)
    }

    /**
     * Submits a batch of commands to the database.  You must include all
     * [PreparedStatement.addBatch] calls inside the preparer.
     *
     * @param sqlStatement The SQL statement to execute
     * @return The results of the query
     * @throws UncheckedSQLException If a [SQLException] occurs
     */
    fun executeBatch(sqlStatement: String, psPreparer: SQLConsumer<PreparedStatement?>): IntArray {
        try {
            connection.use { connection ->
                connection.prepareStatement(sqlStatement).use { statement ->
                    connection.autoCommit = false
                    psPreparer.accept(statement)
                    val count = statement.executeBatch()
                    connection.commit()
                    return count
                }
            }
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * Does the same thing as [DatabaseClient.executeBatch] except
     * does everything asynchronously and converts the int[] returned from [PreparedStatement.executeBatch]
     * to a [<] and returns as a [CompletableFuture].
     *
     * @see DatabaseClient.executeBatch
     */
    fun executeBatchAsync(
        sqlStatement: String,
        psPreparer: SQLConsumer<PreparedStatement?>
    ): CompletableFuture<List<Int>> {
        return CompletableFuture.supplyAsync({
            Arrays.stream(executeBatch(sqlStatement, psPreparer)).boxed().collect(Collectors.toList())
        }, threadPool)
    }

    abstract fun shutdown()

    @get:Throws(SQLException::class)
    abstract val connection: Connection
}