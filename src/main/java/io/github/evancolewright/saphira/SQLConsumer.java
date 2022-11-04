package io.github.evancolewright.saphira;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLConsumer<T>
{
    void accept(@NotNull T t) throws SQLException;
}
