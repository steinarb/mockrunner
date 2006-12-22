package com.mockrunner.mock.jdbc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
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
import com.mockrunner.util.common.ArrayUtil;
import com.mockrunner.util.common.StreamUtil;
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
    private boolean returnGeneratedKeys = false;
    
    public MockPreparedStatement(Connection connection, String sql)
    {
        this(connection, sql, false);
    }
    
    public MockPreparedStatement(Connection connection, String sql, boolean returnGeneratedKeys)
    {
        super(connection);
        this.sql = sql;
        this.returnGeneratedKeys = returnGeneratedKeys;
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
        resultSetHandler.addParameterMapForExecutedStatement(getSQL(), getParameterMapCopy(params));
        if(resultSetHandler.hasMultipleResultSets(getSQL(), params))
        {
            MockResultSet[] results = resultSetHandler.getResultSets(getSQL(), params);
            if(null != results)
            {
                resultSetHandler.addExecutedStatement(getSQL());
                return cloneAndSetMultipleResultSets(results, params);
            }
        }
        else
        {
            MockResultSet result = resultSetHandler.getResultSet(getSQL(), params);
            if(null != result)
            {
                resultSetHandler.addExecutedStatement(getSQL());
                return cloneAndSetSingleResultSet(result, params);
            }
        }
        ResultSet superResultSet = super.executeQuery(getSQL());
        setGeneratedKeysResultSet(sql, params);
        return superResultSet;
    }
    
    private MockResultSet cloneAndSetSingleResultSet(MockResultSet result, Map params)
    {
        result = cloneResultSet(result);
        if(null != result)
        {
            resultSetHandler.addReturnedResultSet(result);
        }
        setResultSets(new MockResultSet[] {result});
        setGeneratedKeysResultSet(sql, params);
        return result;
    }
    
    private MockResultSet cloneAndSetMultipleResultSets(MockResultSet[] results, Map params)
    {
        results = cloneResultSets(results);
        if(null != results)
        {
            resultSetHandler.addReturnedResultSets(results);
        }
        setResultSets(results);
        setGeneratedKeysResultSet(sql, params);
        if(null != results && results.length > 0)
        {
            return results[0];
        }
        return null;
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
        resultSetHandler.addParameterMapForExecutedStatement(getSQL(), getParameterMapCopy(params));
        if(resultSetHandler.hasMultipleUpdateCounts(getSQL(), params))
        {
            Integer[] updateCounts = resultSetHandler.getUpdateCounts(getSQL(), params);
            if(null != updateCounts)
            {
                resultSetHandler.addExecutedStatement(getSQL());
                return setMultipleUpdateCounts((int[])ArrayUtil.convertToPrimitiveArray(updateCounts), params);
            }
        }
        else
        {
            Integer updateCount = resultSetHandler.getUpdateCount(getSQL(), params);
            if(null != updateCount)
            {
                resultSetHandler.addExecutedStatement(getSQL());
                return setSingleUpdateCount(updateCount.intValue(), params);
            }
        }
        int superUpdateCount = super.executeUpdate(getSQL());
        setGeneratedKeysResultSet(sql, params);
        return superUpdateCount;
    }
    
    private int setSingleUpdateCount(int updateCount, Map params)
    {
        setUpdateCounts(new int[] {updateCount});
        setGeneratedKeysResultSet(sql, params);
        return updateCount;
    }
    
    private int setMultipleUpdateCounts(int[] updateCounts, Map params)
    {
        setUpdateCounts(updateCounts);
        setGeneratedKeysResultSet(sql, params);
        if(null != updateCounts && updateCounts.length > 0)
        {
            return updateCounts[0];
        }
        return 0;
    }
    
    public int[] executeBatch() throws SQLException
    {        
        return executeBatch(this.batchParameters);
    }
    
    protected int[] executeBatch(List batchParams) throws SQLException
    {
        int[] results = new int[batchParams.size()];
        SQLException exception = null;
        for(int ii = 0; ii < results.length; ii++)
        {
            if(isQuery(getSQL()))
            {
                exception = prepareFailedResult(results, ii, "SQL " + getSQL() + " in the list of batches returned a ResultSet.", null);
            }
            else
            {
                try
                {
                    Map currentParameters = (Map)batchParams.get(ii);
                    results[ii] = executeUpdate(currentParameters);
                } 
                catch(SQLException exc)
                {
                    exception = prepareFailedResult(results, ii, null, exc);
                }
            }
            if(null != exception && !resultSetHandler.getContinueProcessingOnBatchFailure())
            {
                throw exception;
            }
        }
        if(null != exception)
        {
            throw new BatchUpdateException(exception.getMessage(), exception.getSQLState(), exception.getErrorCode(), results);
        }
        return results;
    }

    private void setGeneratedKeysResultSet(String sql, Map params)
    {
        MockResultSet generatedKeys = resultSetHandler.getGeneratedKeys(sql, params);
        if(returnGeneratedKeys)
        {
            if(null != generatedKeys)
            {
                setLastGeneratedKeysResultSet(generatedKeys);
            }
            else
            {
                setLastGeneratedKeysResultSet(determineGeneratedKeysResultSet(sql));
            }
        }
        else
        {
            setLastGeneratedKeysResultSet(null);
        }
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
    
    public void setAsciiStream(int parameterIndex, InputStream stream) throws SQLException
    {
        setBinaryStream(parameterIndex, stream);
    }

    public void setAsciiStream(int parameterIndex, InputStream stream, int length) throws SQLException
    {
        setBinaryStream(parameterIndex, stream, length);
    }
    
    public void setAsciiStream(int parameterIndex, InputStream stream, long length) throws SQLException
    {
        setBinaryStream(parameterIndex, stream, length);
    }
   
    public void setBinaryStream(int parameterIndex, InputStream stream) throws SQLException
    {
        byte[] data = StreamUtil.getStreamAsByteArray(stream);
        setObject(parameterIndex, new ByteArrayInputStream(data));
    }

    public void setBinaryStream(int parameterIndex, InputStream stream, int length) throws SQLException
    {
        byte[] data = StreamUtil.getStreamAsByteArray(stream, length);
        setObject(parameterIndex, new ByteArrayInputStream(data));
    }

    public void setBinaryStream(int parameterIndex, InputStream stream, long length) throws SQLException
    {
        setBinaryStream(parameterIndex, stream, (int)length);  
    }
    
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException
    {
        String data = StreamUtil.getReaderAsString(reader);
        setObject(parameterIndex, new StringReader(data));
    }
    
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException
    {
        String data = StreamUtil.getReaderAsString(reader, (int)length);
        setObject(parameterIndex, new StringReader(data));
    }

    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException
    {
        setCharacterStream(parameterIndex, reader, (int)length);
    }
    
    public void setNCharacterStream(int parameterIndex, Reader reader) throws SQLException
    {
        setCharacterStream(parameterIndex, reader);
    }

    public void setNCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException
    {
        setCharacterStream(parameterIndex, reader, length);
    }

    public void setBigDecimal(int parameterIndex, BigDecimal bigDecimal) throws SQLException
    {
        setObject(parameterIndex, bigDecimal);
    }

    public void setBlob(int parameterIndex, Blob blob) throws SQLException
    {
        setObject(parameterIndex, blob);
    }

    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException
    {
        byte[] data = StreamUtil.getStreamAsByteArray(inputStream);
        setBlob(parameterIndex, new MockBlob(data));
    }
    
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException
    {
        byte[] data = StreamUtil.getStreamAsByteArray(inputStream, (int)length);
        setBlob(parameterIndex, new MockBlob(data));
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

    public void setClob(int parameterIndex, Clob clob) throws SQLException
    {
        setObject(parameterIndex, clob);
    }
    
    public void setClob(int parameterIndex, Reader reader) throws SQLException
    {
        String data = StreamUtil.getReaderAsString(reader);
        setClob(parameterIndex, new MockClob(data));
    }

    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException
    {
        String data = StreamUtil.getReaderAsString(reader, (int)length);
        setClob(parameterIndex, new MockClob(data));
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

    public void setNString(int parameterIndex, String string) throws SQLException
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
