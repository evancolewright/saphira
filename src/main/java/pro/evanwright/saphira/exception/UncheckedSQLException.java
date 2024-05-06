package pro.evanwright.saphira.exception;

/**
 * A wrapped, unchecked {@link java.sql.SQLException}
 */
public class UncheckedSQLException extends RuntimeException {
    public UncheckedSQLException(Throwable throwable) {
        super(throwable);
    }

    public UncheckedSQLException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
