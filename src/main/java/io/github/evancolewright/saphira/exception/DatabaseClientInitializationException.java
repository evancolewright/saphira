package io.github.evancolewright.saphira.exception;

/**
 * An unchecked, wrapper {@link Exception} around all exceptions that can occur
 * when a {@link io.github.evancolewright.saphira.DatabaseClient} initializes.
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
