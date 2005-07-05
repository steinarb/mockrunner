package com.mockrunner.mock.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.BatchUpdateException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mockrunner.jdbc.AbstractParameterResultSetHandler;
import com.mockrunner.jdbc.ParameterUtil;
import com.mockrunner.util.common.StringUtil;

/**
 * Mock implementation of <code>PreparedStatement</code>.
 */
public class MockPreparedStatement extends MockStatement implements PreparedStatement
{
    private AbstractParameterResultSetHandler resultSetHandler;
    private Map paramObjects = new HashMap();
    private List batchParameters = new ArrayList();
    private String sql;
    private MockParameterMetaData parameterMetaData;
    
    public MockPreparedStatement(Connection connection, String sql)
    {
        super(connection);
        this.sql = sql;
        prepareParameterMetaData();
    }
    
    public MockPreparedStatement(Connection connection, String sql, int resultSetType, int resultSetConcurrency)
    {
        super(connection, resultSetType, resultSetConcurrency);
        this.sql = sql;
        prepareParameterMetaData();
    }

    public MockPreparedStatement(Connection connection, String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
    {
        super(connection, resultSetType, resultSetConcurrency, resultSetHoldability);
        this.sql = sql;
        prepareParameterMetaData();
    }
    
    public void setPreparedStatementResultSetHandler(AbstractParameterResultSetHandler resultSetHandler)
    {
        super.setResultSetHandler(resultSetHandler);
        this.resultSetHandler = resultSetHandler;
    }
    
    private void prepareParameterMetaData()
    {
        int number = StringUtil.countMatches(sql, "?");
        parameterMetaData = new MockParameterMetaData();
        parameterMetaData.setParameterCount(number);
    }
  
    public String getSQL()
    {
        return sql;
    }
    
    public Map getIndexedParameterMap()
    {
        return Collections.unmodifiableMap(paramObjects);
    }
    
	public Map getParameterMap()
	{
		return getIndexedParameterMap();
	}
    
    public Object getParameter(int index)
    {
        return paramObjects.get(new Integer(index));
    }
    
    public void setObject(int index, Object object) throws SQLException 
    {
        paramObjects.put(new Integer(index), object);
    }
    
    public void setObject(int parameterIndex, Object object, int targetSqlType, int scale) throws SQLException
    {
        setObject(parameterIndex, object);
    }

    public void setObject(int parameterIndex, Object object, int targetSqlType) throws SQLException
    {
        setObject(parameterIndex, object);
    }
    
    public void addBatch() throws SQLException
    {
        batchParameters.add(new HashMap(paramObjects));
    }

    public void clearParameters() throws SQLException
    {
        paramObjects.clear();
    }

    public boolean execute() throws SQLException
    {
        boolean callExecuteQuery = isQuery(getSQL());
        if(callExecuteQuery)
        {
            executeQuery();
        }
        else
        {
            executeUpdate();
        }
        return callExecuteQuery;
    }
    
    public ResultSet executeQuery() throws SQLException
    {
        return executeQuery(paramObjects);
    }
    
    protected ResultSet executeQuery(Map params) throws SQLException
    {
        SQLException exception = resultSetHandler.getSQLException(sql, params);
        if(null != exception)
        {
            throw exception;
        }
        exception = resultSetHandler.getSQLException(sql);
        if(null != exception)
        {
            throw exception;
        }
        MockResultSet result = resultSetHandler.getResultSet(getSQL(), params);
		resultSetHandler.addParameterMapForExecutedStatement(getSQL(), getParameterMapCopy(params));
        if(null != result)
        {
            resultSetHandler.addExecutedStatement(getSQL());
            result = cloneResultSet(result);
            resultSetHandler.addReturnedResultSet(result);
            setResultSet(result);
            return result;
        }
        return super.executeQuery(getSQL());
    }

    public int executeUpdate() throws SQLException
    {
        return executeUpdate(paramObjects);
    }
    
    protected int executeUpdate(Map params) throws SQLException
    {
        SQLException exception = resultSetHandler.getSQLException(sql, params);
        if(null != exception)
        {
            throw exception;
        }
        exception = resultSetHandler.getSQLException(sql);
        if(null != exception)
        {
            throw exception;
        }
        Integer updateCount = resultSetHandler.getUpdateCount(getSQL(), params);
		resultSetHandler.addParameterMapForExecutedStatement(getSQL(), getParameterMapCopy(params));
        if(null != updateCount)
        {
            resultSetHandler.addExecutedStatement(getSQL());
            int updateCountInt = updateCount.intValue();
            setUpdateCount(updateCountInt);
            return updateCountInt;
        }
        return super.executeUpdate(getSQL());
    }
    
    public int[] executeBatch() throws SQLException
    {        
        return executeBatch(this.batchParameters);
    }
    
    protected int[] executeBatch(List batchParams) throws SQLException
    {
        int[] results = new int[batchParams.size()];
        if(isQuery(getSQL()))
        {
            throw new BatchUpdateException("SQL " + getSQL() + " returned a ResultSet.", null);
        }
        for(int ii = 0; ii < results.length; ii++)
        {
            Map currentParameters = (Map)batchParams.get(ii);
            results[ii] = executeUpdate(currentParameters);
        }
        return results;
    }

    public ResultSetMetaData getMetaData() throws SQLException
    {
        return new MockResultSetMetaData();
    }

    public ParameterMetaData getParameterMetaData() throws SQLException
    {
        return parameterMetaData;
    }

    public void setArray(int parameterIndex, Array array) throws SQLException
    {
        setObject(parameterIndex, array);
    }

    public void setAsciiStream(int parameterIndex, InputStream stream, int length) throws SQLException
    {
        setObject(parameterIndex, stream);
    }

    public void setBigDecimal(int parameterIndex, BigDecimal bigDecimal) throws SQLException
    {
        setObject(parameterIndex, bigDecimal);
    }

    public void setBinaryStream(int parameterIndex, InputStream stream, int length) throws SQLException
    {
        setObject(parameterIndex, stream);
    }

    public void setBlob(int parameterIndex, Blob blob) throws SQLException
    {
        setObject(parameterIndex, blob);
    }

    public void setBoolean(int parameterIndex, boolean bool) throws SQLException
    {
        setObject(parameterIndex, new Boolean(bool));
    }

    public void setByte(int parameterIndex, byte byteValue) throws SQLException
    {
        setObject(parameterIndex, new Byte(byteValue));
    }

    public void setBytes(int parameterIndex, byte[] byteArray) throws SQLException
    {
        setObject(parameterIndex, byteArray);
    }

    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException
    {
        setObject(parameterIndex, reader);
    }

    public void setClob(int parameterIndex, Clob clob) throws SQLException
    {
        setObject(parameterIndex, clob);
    }

    public void setDate(int parameterIndex, Date date, Calendar calendar) throws SQLException
    {
        setObject(parameterIndex, date);
    }

    public void setDate(int parameterIndex, Date date) throws SQLException
    {
        setObject(parameterIndex, date);
    }

    public void setDouble(int parameterIndex, double doubleValue) throws SQLException
    {
        setObject(parameterIndex, new Double(doubleValue));
    }

    public void setFloat(int parameterIndex, float floatValue) throws SQLException
    {
        setObject(parameterIndex, new Float(floatValue));
    }

    public void setInt(int parameterIndex, int intValue) throws SQLException
    {
        setObject(parameterIndex, new Integer(intValue));
    }

    public void setLong(int parameterIndex, long longValue) throws SQLException
    {
        setObject(parameterIndex, new Long(longValue));
    }

    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException
    {
        setObject(parameterIndex, null);
    }

    public void setNull(int parameterIndex, int sqlType) throws SQLException
    {
        setObject(parameterIndex, null);
    }

    public void setRef(int parameterIndex, Ref ref) throws SQLException
    {
        setObject(parameterIndex, ref);
    }

    public void setShort(int parameterIndex, short shortValue) throws SQLException
    {
        setObject(parameterIndex, new Short(shortValue));
    }

    public void setString(int parameterIndex, String string) throws SQLException
    {
        setObject(parameterIndex, string);
    }

    public void setTime(int parameterIndex, Time time, Calendar calendar) throws SQLException
    {
        setObject(parameterIndex, time);
    }

    public void setTime(int parameterIndex, Time time) throws SQLException
    {
        setObject(parameterIndex, time);
    }

    public void setTimestamp(int parameterIndex, Timestamp timeStamp, Calendar cal) throws SQLException
    {
        setObject(parameterIndex, timeStamp);
    }

    public void setTimestamp(int parameterIndex, Timestamp timeStamp) throws SQLException
    {
        setObject(parameterIndex, timeStamp);
    }

    public void setUnicodeStream(int parameterIndex, InputStream stream, int length) throws SQLException
    {
        setObject(parameterIndex, stream);
    }

    public void setURL(int parameterIndex, URL url) throws SQLException
    {
        setObject(parameterIndex, url);
    }
    
    private Map getParameterMapCopy(Map actualParameters)
    {
    	Map copyParameters = new HashMap();
    	Iterator keys = actualParameters.keySet().iterator();
    	while(keys.hasNext())
    	{
    		Object key = keys.next();
    		Object actualParameter = actualParameters.get(key);
    		Object copyParameter = ParameterUtil.copyParameter(actualParameter);
			copyParameters.put(key, copyParameter);
    	}
    	return copyParameters;
    }
}
