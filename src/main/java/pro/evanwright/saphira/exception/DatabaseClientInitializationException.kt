package pro.evanwright.saphira.exception

/**
 * An unchecked, wrapper [Exception] around all exceptions that can occur
 * when a [DatabaseClient] initializes.
 */
class DatabaseClientInitializationException : RuntimeException {
    constructor(throwable: Throwable?) : super(throwable) {}
    constructor(errorMessage: String?, throwable: Throwable?) : super(errorMessage, throwable) {}
}