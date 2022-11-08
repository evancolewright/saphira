package io.github.evancolewright.saphira.query;

import io.github.evancolewright.saphira.exception.UncheckedSQLException;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A wrapper around {@link ResultSet} that eliminates those
 * ridiculous checked {@link SQLException} by wrapping them with an {@link UncheckedSQLException}.
 */
public class QueryResults
{
    private final ResultSet resultSet;

    public QueryResults(ResultSet resultSet)
    {
        this.resultSet = resultSet;
    }

    /**
     * @see ResultSet#getString(int)
     */
    public String getString(int columnIndex) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getString(columnIndex);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see ResultSet#getString(String)
     */
    public String getString(String columnLabel) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getString(columnLabel);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see ResultSet#getInt(int)
     */
    public int getInt(int columnIndex) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getInt(columnIndex);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see ResultSet#getInt(String)
     */
    public int getInt(String columnLabel) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getInt(columnLabel);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see ResultSet#getFloat(int)
     */
    public float getFloat(int columnIndex) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getInt(columnIndex);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see ResultSet#getFloat(String)
     */
    public float getFloat(String columnLabel) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getInt(columnLabel);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see ResultSet#getDouble(int)
     */
    public double getDouble(int columnIndex) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getDouble(columnIndex);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see ResultSet#getDouble(String)
     */
    public double getDouble(String columnLabel) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getDouble(columnLabel);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see ResultSet#getBigDecimal(int)
     */
    public BigDecimal getBigDecimal(int columnIndex) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getBigDecimal(columnIndex);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see ResultSet#getBigDecimal(String)
     */
    public BigDecimal getBigDecimal(String columnLabel) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getBigDecimal(columnLabel);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see ResultSet#getByte(int)
     */
    public byte getByte(int columnIndex) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getByte(columnIndex);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see ResultSet#getByte(String)
     */
    public byte getByte(String columnLabel) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getByte(columnLabel);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see ResultSet#getDate(int)
     */
    public Date getDate(int columnIndex) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getDate(columnIndex);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see ResultSet#getDate(String)
     */
    public Date getDate(String columnLabel) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getDate(columnLabel);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see ResultSet#getBoolean(int)
     */
    public boolean getBoolean(int columnIndex) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getBoolean(columnIndex);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see ResultSet#getBoolean(String)
     */
    public boolean getBoolean(String columnLabel) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getBoolean(columnLabel);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see ResultSet#getObject(int)
     */
    public Object getObject(int columnIndex) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getObject(columnIndex);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see ResultSet#getObject(String)
     */
    public Object getObject(String columnLabel) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getObject(columnLabel);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see ResultSet#getShort(int)
     */
    public Object getShort(int columnIndex) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getShort(columnIndex);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see ResultSet#getShort(String)
     */
    public short getShort(String columnLabel) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getShort(columnLabel);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see java.sql.ResultSet#absolute(int)
     */
    public boolean absolute(int row) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.absolute(row);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see java.sql.ResultSet#afterLast()
     */
    public void afterLast() throws UncheckedSQLException
    {
        try
        {
            this.resultSet.afterLast();
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see java.sql.ResultSet#beforeFirst()
     */
    public void beforeFirst() throws UncheckedSQLException
    {
        try
        {
            this.resultSet.beforeFirst();
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see java.sql.ResultSet#first()
     */
    public boolean first() throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.first();
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see java.sql.ResultSet#getRow()
     */
    public int getRow() throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.getRow();
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see java.sql.ResultSet#isAfterLast()
     */
    public boolean isAfterLast() throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.isAfterLast();
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see java.sql.ResultSet#isBeforeFirst()
     */
    public boolean isBeforeFirst() throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.isBeforeFirst();
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see java.sql.ResultSet#isFirst()
     */
    public boolean isFirst() throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.isFirst();
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see java.sql.ResultSet#isLast()
     */
    public boolean isLast() throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.isLast();
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see java.sql.ResultSet#last()
     */
    public boolean last() throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.last();
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see java.sql.ResultSet#next()
     */
    public boolean next() throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.next();
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see java.sql.ResultSet#previous()
     */
    public boolean previous() throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.previous();
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see java.sql.ResultSet#relative(int)
     */
    public boolean relative(int rows) throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.relative(rows);
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }

    /**
     * @see java.sql.ResultSet#wasNull()
     */
    public boolean wasNull() throws UncheckedSQLException
    {
        try
        {
            return this.resultSet.wasNull();
        } catch (SQLException exception)
        {
            throw new UncheckedSQLException(exception);
        }
    }
}
