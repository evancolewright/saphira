package pro.evanwright.saphira.exception

/**
 * A wrapped, unchecked [java.sql.SQLException] in an attempt to make the code cleaner
 * and give more flexibility to the user of the API.
 */
class UncheckedSQLException : RuntimeException {
    constructor(throwable: Throwable?) : super(throwable) {}
    constructor(errorMessage: String?, throwable: Throwable?) : super(errorMessage, throwable) {}
}