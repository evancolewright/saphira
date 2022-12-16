package pro.evanwright.saphira.exception;

import pro.evanwright.saphira.DatabaseClient;

/**
 * A wrapper around all exceptions that can occur when a {@link DatabaseClient} initializes.
 */
public class DatabaseClientInitializationException extends RuntimeException {
    public DatabaseClientInitializationException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

    public DatabaseClientInitializationException(String errorMessage) {
        super(errorMessage);
    }
}
