package io.github.evancolewright.saphira;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

/**
 * A {@link java.util.function.Consumer} that may throw a {@link SQLException}.
 */
@FunctionalInterface
public interface SQLConsumer<T>
{
    /**
     * Performs this operation on the given argument.
     *
     * @param t The input argument
     * @throws SQLException  If one occurs
     */
    void accept(@NotNull T t) throws SQLException;
}
