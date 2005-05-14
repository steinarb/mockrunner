package com.mockrunner.mock.jdbc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.sql.BatchUpdateException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mockrunner.jdbc.AbstractOutParameterResultSetHandler;
import com.mockrunner.util.common.StreamUtil;

/**
 * Mock implementation of <code>CallableStatement</code>.
 */
public class MockCallableStatement extends MockPreparedStatement implements CallableStatement
{
    private AbstractOutParameterResultSetHandler resultSetHandler;
    private Map paramObjects = new HashMap();
    private Set registeredOutParameterSetIndexed = new HashSet();
    private Set registeredOutParameterSetNamed = new HashSet();
    private List batchParameters = new ArrayList();
    private Map lastOutParameters = null;
    private boolean wasNull = false;
    
    public MockCallableStatement(Connection connection, String sql)
    {
        super(connection, sql);
    }

    public MockCallableStatement(Connection connection, String sql, int resultSetType, int resultSetConcurrency)
    {
        super(connection, sql, resultSetType, resultSetConcurrency);
    }

    public MockCallableStatement(Connection connection, String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
    {
        super(connection, sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }
    
    public void setCallableStatementResultSetHandler(AbstractOutParameterResultSetHandler resultSetHandler)
    {
        super.setPreparedStatementResultSetHandler(resultSetHandler);
        this.resultSetHandler = resultSetHandler;
    }
    
    public Map getNamedParameterMap()
    {
        return Collections.unmodifiableMap(paramObjects);
    }
    
    public Map getParameterMap()
    {
        Map parameterMap = new HashMap(getIndexedParameterMap());
        parameterMap.putAll(getNamedParameterMap());
        return Collections.unmodifiableMap(parameterMap);
    }
    
    public Object getParameter(String name)
    {
        return paramObjects.get(name);
    }
    
    public Set getNamedRegisteredOutParameterSet()
    {
        return Collections.unmodifiableSet(registeredOutParameterSetNamed);
    }
    
    public boolean isOutParameterRegistered(int index)
    {
        return registeredOutParameterSetIndexed.contains(new Integer(index));
    }
    
    public Set getIndexedRegisteredOutParameterSet()
    {
        return Collections.unmodifiableSet(registeredOutParameterSetIndexed);
    }
    
    public boolean isOutParameterRegistered(String parameterName)
    {
        return registeredOutParameterSetNamed.contains(parameterName);
    }
    
    public void clearRegisteredOutParameter()
    {
        registeredOutParameterSetIndexed.clear();
        registeredOutParameterSetNamed.clear();
    }
    
    public ResultSet executeQuery() throws SQLException
    {
        ResultSet resultSet = executeQuery(getParameterMap());
        lastOutParameters = getOutParameterMap();
        return resultSet;
    }
    
    public int executeUpdate() throws SQLException
    {
        int updateCount = executeUpdate(getParameterMap());
        lastOutParameters = getOutParameterMap();
        return updateCount;
    }
    
    public void addBatch() throws SQLException
    {
        batchParameters.add(new HashMap(getParameterMap()));
    }

    public int[] executeBatch() throws SQLException
    {
        int[] results = new int[batchParameters.size()];
        if(isQuery(getSQL()))
        {
            throw new BatchUpdateException("SQL " + getSQL() + " returned a ResultSet.", null);
        }
        for(int ii = 0; ii < results.length; ii++)
        {
            Map currentParameters = (Map)batchParameters.get(ii);
            results[ii] = executeUpdate(currentParameters);
        }
        return results;
    }
    
    public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException
    {
        registeredOutParameterSetIndexed.add(new Integer(parameterIndex));
    }

    public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException
    {
        registerOutParameter(parameterIndex, sqlType);
    }

    public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException
    {
        registerOutParameter(parameterIndex, sqlType);
    }
    
    public void registerOutParameter(String parameterName, int sqlType) throws SQLException
    {
        registeredOutParameterSetNamed.add(parameterName);
    }
    
    public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException
    {
        registerOutParameter(parameterName, sqlType);
    }
    
    public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException
    {
        registerOutParameter(parameterName, sqlType);
    }
        
    public boolean wasNull() throws SQLException
    {
        return wasNull;
    }

    public byte getByte(int parameterIndex) throws SQLException
    {
        Object value = getObject(parameterIndex);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).byteValue();
            return new Byte(value.toString()).byteValue();
        }
        return 0;
    }

    public double getDouble(int parameterIndex) throws SQLException
    {
        Object value = getObject(parameterIndex);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).doubleValue();
            return new Double(value.toString()).doubleValue();
        }
        return 0;
    }

    public float getFloat(int parameterIndex) throws SQLException
    {
        Object value = getObject(parameterIndex);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).floatValue();
            return new Float(value.toString()).floatValue();
        }
        return 0;
    }

    public int getInt(int parameterIndex) throws SQLException
    {
        Object value = getObject(parameterIndex);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).intValue();
            return new Integer(value.toString()).intValue();
        }
        return 0;
    }

    public long getLong(int parameterIndex) throws SQLException
    {
        Object value = getObject(parameterIndex);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).longValue();
            return new Long(value.toString()).longValue();
        }
        return 0;
    }

    public short getShort(int parameterIndex) throws SQLException
    {
        Object value = getObject(parameterIndex);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).shortValue();
            return new Short(value.toString()).shortValue();
        }
        return 0;
    }

    public boolean getBoolean(int parameterIndex) throws SQLException
    {
        Object value = getObject(parameterIndex);
        if(null != value)
        {
            if(value instanceof Boolean) return ((Boolean)value).booleanValue();
            return new Boolean(value.toString()).booleanValue();
        }
        return false;
    }

    public byte[] getBytes(int parameterIndex) throws SQLException
    {
        Object value = getObject(parameterIndex);
        if(null != value)
        {
            if(value instanceof byte[]) return (byte[])value;
            return value.toString().getBytes();
        }
        return null;
    }

    public Object getObject(int parameterIndex) throws SQLException
    {
        wasNull = false;
        Object returnValue = null;
        if(null != lastOutParameters)
        {
            returnValue = lastOutParameters.get(new Integer(parameterIndex));
        }
        if(null == returnValue) wasNull = true;
        return returnValue;
    }

    public String getString(int parameterIndex) throws SQLException
    {
        Object value = getObject(parameterIndex);
        if(null != value) return value.toString();
        return null;
    }
    
    public byte getByte(String parameterName) throws SQLException
    {
        Object value = getObject(parameterName);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).byteValue();
            return new Byte(value.toString()).byteValue();
        }
        return 0;
    }

    public double getDouble(String parameterName) throws SQLException
    {
        Object value = getObject(parameterName);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).doubleValue();
            return new Double(value.toString()).doubleValue();
        }
        return 0;
    }

    public float getFloat(String parameterName) throws SQLException
    {
        Object value = getObject(parameterName);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).floatValue();
            return new Float(value.toString()).floatValue();
        }
        return 0;
    }

    public int getInt(String parameterName) throws SQLException
    {
        Object value = getObject(parameterName);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).intValue();
            return new Integer(value.toString()).intValue();
        }
        return 0;
    }

    public long getLong(String parameterName) throws SQLException
    {
        Object value = getObject(parameterName);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).longValue();
            return new Long(value.toString()).longValue();
        }
        return 0;
    }

    public short getShort(String parameterName) throws SQLException
    {
        Object value = getObject(parameterName);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).shortValue();
            return new Short(value.toString()).shortValue();
        }
        return 0;
    }

    public boolean getBoolean(String parameterName) throws SQLException
    {
        Object value = getObject(parameterName);
        if(null != value)
        {
            if(value instanceof Boolean) return ((Boolean)value).booleanValue();
            return new Boolean(value.toString()).booleanValue();
        }
        return false;
    }

    public byte[] getBytes(String parameterName) throws SQLException
    {
        Object value = getObject(parameterName);
        if(null != value)
        {
            if(value instanceof byte[]) return (byte[])value;
            return value.toString().getBytes();
        }
        return null;
    }

    public void setByte(String parameterName, byte byteValue) throws SQLException
    {
        setObject(parameterName, new Byte(byteValue));
    }

    public void setDouble(String parameterName, double doubleValue) throws SQLException
    {
        setObject(parameterName, new Double(doubleValue));
    }

    public void setFloat(String parameterName, float floatValue) throws SQLException
    {
        setObject(parameterName, new Float(floatValue));
    }

    public void setInt(String parameterName, int intValue) throws SQLException
    {
        setObject(parameterName, new Integer(intValue));
    }

    public void setNull(String parameterName, int sqlType) throws SQLException
    {
        setObject(parameterName, null);
    }

    public void setLong(String parameterName, long longValue) throws SQLException
    {
        setObject(parameterName, new Long(longValue));
    }

    public void setShort(String parameterName, short shortValue) throws SQLException
    {
        setObject(parameterName, new Short(shortValue));
    }

    public void setBoolean(String parameterName, boolean booleanValue) throws SQLException
    {
        setObject(parameterName, new Boolean(booleanValue));
    }

    public void setBytes(String parameterName, byte[] byteArray) throws SQLException
    {
        setObject(parameterName, byteArray);
    }

    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException
    {
        Object value = getObject(parameterIndex);
        if(null != value)
        {
            if(value instanceof Number) return new BigDecimal(((Number)value).doubleValue());
            return new BigDecimal(value.toString());
        }
        return null;
    }

    public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException
    {
        return getBigDecimal(parameterIndex);
    }

    public URL getURL(int parameterIndex) throws SQLException
    {
        Object value = getObject(parameterIndex);
        if(null != value)
        {
            if(value instanceof URL) return (URL)value;
            try
            {
                return new URL(value.toString());
            }
            catch(MalformedURLException exc)
            {
            
            }
        }
        return null;
    }

    public Array getArray(int parameterIndex) throws SQLException
    {
        Object value = getObject(parameterIndex);
        if(null != value)
        {
            if(value instanceof Array) return (Array)value;
            return new MockArray(value);
        }
        return null;
    }

    public Blob getBlob(int parameterIndex) throws SQLException
    {
        Object value = getObject(parameterIndex);
        if(null != value)
        {
            if(value instanceof Blob) return (Blob)value;
            return new MockBlob(getBytes(parameterIndex));
        }
        return null;
    }

    public Clob getClob(int parameterIndex) throws SQLException
    {
        Object value = getObject(parameterIndex);
        if(null != value)
        {
            if(value instanceof Clob) return (Clob)value;
            return new MockClob(getString(parameterIndex));
        }
        return null;
    }

    public Date getDate(int parameterIndex) throws SQLException
    {
        Object value = getObject(parameterIndex);
        if(null != value)
        {
            if(value instanceof Date) return (Date)value;
            return Date.valueOf(value.toString());
        }
        return null;
    }

    public Ref getRef(int parameterIndex) throws SQLException
    {
        Object value = getObject(parameterIndex);
        if(null != value)
        {
            if(value instanceof Ref) return (Ref)value;
            return new MockRef(value);
        }
        return null;
    }

    public Time getTime(int parameterIndex) throws SQLException
    {
        Object value = getObject(parameterIndex);
        if(null != value)
        {
            if(value instanceof Time) return (Time)value;
            return Time.valueOf(value.toString());
        }
        return null;
    }

    public Timestamp getTimestamp(int parameterIndex) throws SQLException
    {
        Object value = getObject(parameterIndex);
        if(null != value)
        {
            if(value instanceof Timestamp) return (Timestamp)value;
            return Timestamp.valueOf(value.toString());
        }
        return null;
    }

    public void setAsciiStream(String parameterName, InputStream stream, int length) throws SQLException
    {
        setBinaryStream(parameterName, stream, length);
    }

    public void setBinaryStream(String parameterName, InputStream stream, int length) throws SQLException
    {
        byte[] data = StreamUtil.getStreamAsByteArray(stream, length);
        setObject(parameterName, new ByteArrayInputStream(data));
    }

    public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException
    {
        String data = StreamUtil.getReaderAsString(reader, length);
        setObject(parameterName, new StringReader(data));
    }

    public Object getObject(String parameterName) throws SQLException
    {
        wasNull = false;
        Object returnValue = null;
        if(null != lastOutParameters)
        {
            returnValue = lastOutParameters.get(parameterName);
        }
        if(null == returnValue) wasNull = true;
        return returnValue;
    }

    public void setObject(String parameterName, Object object) throws SQLException
    {
        paramObjects.put(parameterName, object);
    }

    public void setObject(String parameterName, Object object, int targetSqlType) throws SQLException
    {
        setObject(parameterName, object);
    }

    public void setObject(String parameterName, Object object, int targetSqlType, int scale) throws SQLException
    {
        setObject(parameterName, object);
    }

    public Object getObject(int parameterIndex, Map map) throws SQLException
    {
        return getObject(parameterIndex);
    }

    public String getString(String parameterName) throws SQLException
    {
        Object value = getObject(parameterName);
        if(null != value) return value.toString();
        return null;
    }

    public void setNull(String parameterName, int sqlType, String typeName) throws SQLException
    {
        setNull(parameterName, sqlType);
    }

    public void setString(String parameterName, String string) throws SQLException
    {
        setObject(parameterName, string);
    }

    public BigDecimal getBigDecimal(String parameterName) throws SQLException
    {
        Object value = getObject(parameterName);
        if(null != value)
        {
            if(value instanceof Number) return new BigDecimal(((Number)value).doubleValue());
            return new BigDecimal(value.toString());
        }
        return null;
    }

    public void setBigDecimal(String parameterName, BigDecimal bigDecimal) throws SQLException
    {
        setObject(parameterName, bigDecimal);
    }

    public URL getURL(String parameterName) throws SQLException
    {
        Object value = getObject(parameterName);
        if(null != value)
        {
            if(value instanceof URL) return (URL)value;
            try
            {
                return new URL(value.toString());
            }
            catch(MalformedURLException exc)
            {
            
            }
        }
        return null;
    }

    public void setURL(String parameterName, URL url) throws SQLException
    {
        setObject(parameterName, url);
    }

    public Array getArray(String parameterName) throws SQLException
    {
        Object value = getObject(parameterName);
        if(null != value)
        {
            if(value instanceof Array) return (Array)value;
            return new MockArray(value);
        }
        return null;
    }

    public Blob getBlob(String parameterName) throws SQLException
    {
        Object value = getObject(parameterName);
        if(null != value)
        {
            if(value instanceof Blob) return (Blob)value;
            return new MockBlob(getBytes(parameterName));
        }
        return null;
    }

    public Clob getClob(String parameterName) throws SQLException
    {
        Object value = getObject(parameterName);
        if(null != value)
        {
            if(value instanceof Clob) return (Clob)value;
            return new MockClob(getString(parameterName));
        }
        return null;
    }

    public Date getDate(String parameterName) throws SQLException
    {
        Object value = getObject(parameterName);
        if(null != value)
        {
            if(value instanceof Date) return (Date)value;
            return Date.valueOf(value.toString());
        }
        return null;
    }

    public void setDate(String parameterName, Date date) throws SQLException
    {
        setObject(parameterName, date);
    }

    public Date getDate(int parameterIndex, Calendar calendar) throws SQLException
    {
        return getDate(parameterIndex);
    }

    public Ref getRef(String parameterName) throws SQLException
    {
        Object value = getObject(parameterName);
        if(null != value)
        {
            if(value instanceof Ref) return (Ref)value;
            return new MockRef(value);
        }
        return null;
    }

    public Time getTime(String parameterName) throws SQLException
    {
        Object value = getObject(parameterName);
        if(null != value)
        {
            if(value instanceof Time) return (Time)value;
            return Time.valueOf(value.toString());
        }
        return null;
    }

    public void setTime(String parameterName, Time time) throws SQLException
    {
        setObject(parameterName, time);
    }

    public Time getTime(int parameterIndex, Calendar calendar) throws SQLException
    {
        return getTime(parameterIndex);
    }

    public Timestamp getTimestamp(String parameterName) throws SQLException
    {
        Object value = getObject(parameterName);
        if(null != value)
        {
            if(value instanceof Timestamp) return (Timestamp)value;
            return Timestamp.valueOf(value.toString());
        }
        return null;
    }

    public void setTimestamp(String parameterName, Timestamp timestamp) throws SQLException
    {
        setObject(parameterName, timestamp);
    }

    public Timestamp getTimestamp(int parameterIndex, Calendar calendar) throws SQLException
    {
        return getTimestamp(parameterIndex);
    }

    public Object getObject(String parameterName, Map map) throws SQLException
    {
        return getObject(parameterName);
    }

    public Date getDate(String parameterName, Calendar calendar) throws SQLException
    {
        return getDate(parameterName);
    }

    public Time getTime(String parameterName, Calendar calendar) throws SQLException
    {
        return getTime(parameterName);
    }

    public Timestamp getTimestamp(String parameterName, Calendar calendar) throws SQLException
    {
        return getTimestamp(parameterName);
    }

    public void setDate(String parameterName, Date date, Calendar calendar) throws SQLException
    {
        setDate(parameterName, date);
    }

    public void setTime(String parameterName, Time time, Calendar calendar) throws SQLException
    {
        setTime(parameterName, time);
    }

    public void setTimestamp(String parameterName, Timestamp timestamp, Calendar calendar) throws SQLException
    {
        setTimestamp(parameterName, timestamp);
    }
    
    private Map getOutParameterMap()
    {
        Map outParameter = resultSetHandler.getOutParameter(getSQL(), getParameterMap());
        if(null == outParameter)
        {
            outParameter = resultSetHandler.getOutParameter(getSQL());
        }
        if(null == outParameter)
        {
            outParameter = resultSetHandler.getGlobalOutParameter();
        }
        if(resultSetHandler.getMustRegisterOutParameters())
        {
            return filterNotRegisteredParameters(outParameter);
        }
        return outParameter;
    }
    
    private Map filterNotRegisteredParameters(Map outParameter)
    {
        Map filteredMap = new HashMap();
        Iterator keys = outParameter.keySet().iterator();
        while(keys.hasNext())
        {
            Object nextKey = keys.next();
            if(registeredOutParameterSetIndexed.contains(nextKey) || registeredOutParameterSetNamed.contains(nextKey))
            {
                filteredMap.put(nextKey, outParameter.get(nextKey));
            }
        }
        return Collections.unmodifiableMap(filteredMap);
    }
}
