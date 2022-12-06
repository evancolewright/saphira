package pro.evanwright.saphira.query

import pro.evanwright.saphira.exception.UncheckedSQLException
import java.math.BigDecimal
import java.sql.Date
import java.sql.ResultSet
import java.sql.SQLException

/**
 * A wrapper around [ResultSet] that eliminates those
 * ridiculous checked [SQLException] by wrapping them with an [UncheckedSQLException].
 */
class QueryResults(private val resultSet: ResultSet) {
    /**
     * @see ResultSet.getString
     */
    @Throws(UncheckedSQLException::class)
    fun getString(columnIndex: Int): String {
        return try {
            resultSet.getString(columnIndex)
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see ResultSet.getString
     */
    @Throws(UncheckedSQLException::class)
    fun getString(columnLabel: String?): String {
        return try {
            resultSet.getString(columnLabel)
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see ResultSet.getInt
     */
    @Throws(UncheckedSQLException::class)
    fun getInt(columnIndex: Int): Int {
        return try {
            resultSet.getInt(columnIndex)
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see ResultSet.getInt
     */
    @Throws(UncheckedSQLException::class)
    fun getInt(columnLabel: String?): Int {
        return try {
            resultSet.getInt(columnLabel)
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see ResultSet.getFloat
     */
    @Throws(UncheckedSQLException::class)
    fun getFloat(columnIndex: Int): Float {
        return try {
            resultSet.getInt(columnIndex).toFloat()
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see ResultSet.getFloat
     */
    @Throws(UncheckedSQLException::class)
    fun getFloat(columnLabel: String?): Float {
        return try {
            resultSet.getInt(columnLabel).toFloat()
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see ResultSet.getDouble
     */
    @Throws(UncheckedSQLException::class)
    fun getDouble(columnIndex: Int): Double {
        return try {
            resultSet.getDouble(columnIndex)
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see ResultSet.getDouble
     */
    @Throws(UncheckedSQLException::class)
    fun getDouble(columnLabel: String?): Double {
        return try {
            resultSet.getDouble(columnLabel)
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see ResultSet.getBigDecimal
     */
    @Throws(UncheckedSQLException::class)
    fun getBigDecimal(columnIndex: Int): BigDecimal {
        return try {
            resultSet.getBigDecimal(columnIndex)
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see ResultSet.getBigDecimal
     */
    @Throws(UncheckedSQLException::class)
    fun getBigDecimal(columnLabel: String?): BigDecimal {
        return try {
            resultSet.getBigDecimal(columnLabel)
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see ResultSet.getByte
     */
    @Throws(UncheckedSQLException::class)
    fun getByte(columnIndex: Int): Byte {
        return try {
            resultSet.getByte(columnIndex)
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see ResultSet.getByte
     */
    @Throws(UncheckedSQLException::class)
    fun getByte(columnLabel: String?): Byte {
        return try {
            resultSet.getByte(columnLabel)
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see ResultSet.getDate
     */
    @Throws(UncheckedSQLException::class)
    fun getDate(columnIndex: Int): Date {
        return try {
            resultSet.getDate(columnIndex)
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see ResultSet.getDate
     */
    @Throws(UncheckedSQLException::class)
    fun getDate(columnLabel: String?): Date {
        return try {
            resultSet.getDate(columnLabel)
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see ResultSet.getBoolean
     */
    @Throws(UncheckedSQLException::class)
    fun getBoolean(columnIndex: Int): Boolean {
        return try {
            resultSet.getBoolean(columnIndex)
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see ResultSet.getBoolean
     */
    @Throws(UncheckedSQLException::class)
    fun getBoolean(columnLabel: String?): Boolean {
        return try {
            resultSet.getBoolean(columnLabel)
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see ResultSet.getObject
     */
    @Throws(UncheckedSQLException::class)
    fun getObject(columnIndex: Int): Any {
        return try {
            resultSet.getObject(columnIndex)
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see ResultSet.getObject
     */
    @Throws(UncheckedSQLException::class)
    fun getObject(columnLabel: String?): Any {
        return try {
            resultSet.getObject(columnLabel)
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see ResultSet.getShort
     */
    @Throws(UncheckedSQLException::class)
    fun getShort(columnIndex: Int): Any {
        return try {
            resultSet.getShort(columnIndex)
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see ResultSet.getShort
     */
    @Throws(UncheckedSQLException::class)
    fun getShort(columnLabel: String?): Short {
        return try {
            resultSet.getShort(columnLabel)
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see java.sql.ResultSet.absolute
     */
    @Throws(UncheckedSQLException::class)
    fun absolute(row: Int): Boolean {
        return try {
            resultSet.absolute(row)
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see java.sql.ResultSet.afterLast
     */
    @Throws(UncheckedSQLException::class)
    fun afterLast() {
        try {
            resultSet.afterLast()
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see java.sql.ResultSet.beforeFirst
     */
    @Throws(UncheckedSQLException::class)
    fun beforeFirst() {
        try {
            resultSet.beforeFirst()
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see java.sql.ResultSet.first
     */
    @Throws(UncheckedSQLException::class)
    fun first(): Boolean {
        return try {
            resultSet.first()
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see java.sql.ResultSet.getRow
     */
    @get:Throws(UncheckedSQLException::class)
    val row: Int
        get() = try {
            resultSet.row
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }

    /**
     * @see java.sql.ResultSet.isAfterLast
     */
    @get:Throws(UncheckedSQLException::class)
    val isAfterLast: Boolean
        get() = try {
            resultSet.isAfterLast
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }

    /**
     * @see java.sql.ResultSet.isBeforeFirst
     */
    @get:Throws(UncheckedSQLException::class)
    val isBeforeFirst: Boolean
        get() = try {
            resultSet.isBeforeFirst
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }

    /**
     * @see java.sql.ResultSet.isFirst
     */
    @get:Throws(UncheckedSQLException::class)
    val isFirst: Boolean
        get() = try {
            resultSet.isFirst
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }

    /**
     * @see java.sql.ResultSet.isLast
     */
    @get:Throws(UncheckedSQLException::class)
    val isLast: Boolean
        get() = try {
            resultSet.isLast
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }

    /**
     * @see java.sql.ResultSet.last
     */
    @Throws(UncheckedSQLException::class)
    fun last(): Boolean {
        return try {
            resultSet.last()
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see java.sql.ResultSet.next
     */
    @Throws(UncheckedSQLException::class)
    operator fun next(): Boolean {
        return try {
            resultSet.next()
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see java.sql.ResultSet.previous
     */
    @Throws(UncheckedSQLException::class)
    fun previous(): Boolean {
        return try {
            resultSet.previous()
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see java.sql.ResultSet.relative
     */
    @Throws(UncheckedSQLException::class)
    fun relative(rows: Int): Boolean {
        return try {
            resultSet.relative(rows)
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }

    /**
     * @see java.sql.ResultSet.wasNull
     */
    @Throws(UncheckedSQLException::class)
    fun wasNull(): Boolean {
        return try {
            resultSet.wasNull()
        } catch (exception: SQLException) {
            throw UncheckedSQLException(exception)
        }
    }
}