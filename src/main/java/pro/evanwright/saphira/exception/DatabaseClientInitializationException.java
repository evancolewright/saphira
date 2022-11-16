package pro.evanwright.saphira.exception;

import pro.evanwright.saphira.DatabaseClient;

/**
 * An unchecked, wrapper {@link Exception} around all exceptions that can occur
 * when a {@link DatabaseClient} initializes.
 */
public class DatabaseClientInitializationException extends RuntimeException
{
    public DatabaseClientInitializationException(Throwable throwable)
    {
        super(throwable);
    }

    public DatabaseClientInitializationException(String errorMessage, Throwable throwable)
    {
        super(errorMessage, throwable);
    }
}
