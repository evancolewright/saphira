package io.github.evancolewright.saphira;

import org.jetbrains.annotations.NotNull;

public enum DatabaseDriver
{
    MYSQL("com.mysql.cj.jdbc.Driver", "jdbc:mysql"),
    POSTGRES("org.postgresql.Driver", "jdbc:postgresql"),
    MARIADB("org.mariadb.jdbc.Driver", "jdbc:mariadb");

    private final String driverClassName;
    private final String jdbcUrlPrefix;

    DatabaseDriver(@NotNull String driverClassName, @NotNull String jdbcUrlPrefix)
    {
        this.driverClassName = driverClassName;
        this.jdbcUrlPrefix = jdbcUrlPrefix;
    }

    public String getDriverClassName()
    {
        return this.driverClassName;
    }

    public String getJdbcUrlPrefix()
    {
        return this.jdbcUrlPrefix;
    }
}
