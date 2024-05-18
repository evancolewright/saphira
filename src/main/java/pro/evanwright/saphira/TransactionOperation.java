package pro.evanwright.saphira;

import java.sql.SQLException;

/**
 * {@link FunctionalInterface} for representing a transactional operation.
 * @param <T> The type returned from the function
 */
@FunctionalInterface
public interface TransactionOperation<T> {
    /**
     * Executes the operation
     * @throws SQLException If one occurs
     */
    T execute() throws SQLException;
}
