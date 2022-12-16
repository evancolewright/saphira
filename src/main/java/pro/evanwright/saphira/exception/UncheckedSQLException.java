package pro.evanwright.saphira.exception;

/**
 * A wrapped, unchecked {@link java.sql.SQLException} in an attempt to make the code cleaner
 * and give more flexibility to the user of the API.
 */
public class UncheckedSQLException extends RuntimeException {
    public UncheckedSQLException(Throwable throwable) {
        super(throwable);
    }

    public UncheckedSQLException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
