package pro.evanwright.saphira

import java.sql.SQLException

/**
 * A [java.util.function.Consumer] that may throw a [SQLException].
 */
fun interface SQLConsumer<T> {
    /**
     * Performs this operation on the given argument.
     *
     * @param t The input argument
     * @throws SQLException  If one occurs
     */
    @Throws(SQLException::class)
    fun accept(t: T)
}