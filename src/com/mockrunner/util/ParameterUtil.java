package com.mockrunner.util;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Util class for <code>PreparedStatement</code> and <code>ResultSet</code>
 * parameters.
 */
public class ParameterUtil
{
    /**
     * Returns a suitable hashcode for a <code>PreparedStatement</code>,
     * <code>CallableStatement</code> or <code>ResultSet</code> parameter.
     * Since parameters can be of the type <code>byte[]</code>,
     * <code>InputStream</code>, <code>Reader</code>, <code>Ref</code>,
     * <code>Array</code>, <code>Blob</code> or <code>Clob</code>,
     * this method recognizes these types of objects.
     * If two parameters are equals, this method is guaranteed to
     * return the same int value.
     * @param parameter the parameter
     * @return a suitable hashcode
     */
    public static int createHashCodeForParameter(Object parameter)
    {
        try
        {
            if(null == parameter) return 0;
            if(parameter instanceof Number || parameter instanceof String)
            {
                return parameter.hashCode();
            }
            if(parameter instanceof byte[])
            {
                return new Integer(addBytes((byte[])parameter)).hashCode();
            }
            if(parameter instanceof Blob)
            {
                byte[] array = ((Blob)parameter).getBytes(1, (int)((Blob)parameter).length());
                return new Integer(addBytes(array)).hashCode();
            }
            if(parameter instanceof Clob)
            {
                String string = ((Clob)parameter).getSubString(1, (int)((Clob)parameter).length());
                return string.hashCode();
            }
            if(parameter instanceof InputStream)
            {
                InputStream stream = (InputStream)parameter;
                if(stream.markSupported()) stream.mark(Integer.MAX_VALUE);
                byte[] array = StreamUtil.getStreamAsByteArray(stream);
                if(stream.markSupported()) stream.reset();
                return new Integer(addBytes(array)).hashCode();
            }
            if(parameter instanceof Reader)
            {
                Reader reader = (Reader)parameter;
                if(reader.markSupported()) reader.mark(Integer.MAX_VALUE);
                String string = StreamUtil.getReaderAsString(reader);
                if(reader.markSupported()) reader.reset();
                return string.hashCode();
            }
        }
        catch(Exception exc)
        {
            return 0;
        }
        return 0;
    }
    
    /**
     * Compares two parameters of a <code>PreparedStatement</code> or
     * <code>CallableStatement</code>. You can use it to compare
     * parameters of a <code>ResultSet</code>. It is used by
     * {@link com.mockrunner.jdbc.PreparedStatementResultSetHandler}
     * for comparing parameters specified in the <code>prepare</code>
     * methods.
     * Since the parameters can be of the type <code>byte[]</code>,
     * <code>InputStream</code>, <code>Reader</code>, <code>Ref</code>,
     * <code>Array</code>, <code>Blob</code> or <code>Clob</code>,
     * this method can handle these types of objects. All other objects
     * are compared using the <code>equals</code> method.
     * @param source the first parameter
     * @param target the second parameter
     * @return <code>true</code> if <i>source</i> is equal to <i>target</i>,
     *         <code>false</code> otherwise
     */
    public static boolean compareParameter(Object source, Object target)
    {
        if(null == source && null == target) return true;
        if(null == source || null == target) return false;
        if(source instanceof byte[] && target instanceof byte[])
        {
            return Arrays.equals((byte[])source, (byte[])target);
        }
        if(source instanceof InputStream && target instanceof InputStream)
        {
            return StreamUtil.compareStreams((InputStream)source, (InputStream)target);
        }
        if(source instanceof Reader && target instanceof Reader)
        {
            return StreamUtil.compareReader((Reader)source, (Reader)target);
        }
        if(source instanceof Ref && target instanceof Ref)
        {
            return compareRef(source, target);
        }
        if(source instanceof Array && target instanceof Array)
        {
            return compareArray(source, target);
        }
        if(source instanceof Blob && target instanceof Blob)
        {
            return compareBlob(source, target);
        }
        if(source instanceof Clob && target instanceof Clob)
        {
            return compareClob(source, target);
        }
        return source.equals(target);
    }
    
    private static int addBytes(byte[] data)
    {
        int sum = 0;
        for(int ii = 0; ii < data.length; ii++)
        {
            sum += data[ii];
        }
        return sum;
    }
    
    private static boolean compareClob(Object source, Object target)
    {
        try
        {
            String sourceString = ((Clob)source).getSubString(1, (int)((Clob)source).length());
            String targetString = ((Clob)target).getSubString(1, (int)((Clob)target).length());
            return sourceString.equals(targetString);
        }
        catch(SQLException exc)
        {
            return false;
        }
    }

    private static boolean compareBlob(Object source, Object target)
    {
        try
        {
            byte[] sourceArray = ((Blob)source).getBytes(1, (int)((Blob)source).length());
            byte[] targetArray = ((Blob)target).getBytes(1, (int)((Blob)target).length());
            return Arrays.equals(sourceArray, targetArray);
        }
        catch(SQLException exc)
        {
            return false;
        }
    }

    private static boolean compareArray(Object source, Object target)
    {
        try
        {
            Object[] sourceArray = ArrayUtil.convertToObjectArray(((Array)source).getArray());
            Object[] targetArray = ArrayUtil.convertToObjectArray(((Array)target).getArray());
            return Arrays.equals(sourceArray, targetArray);
        }
        catch(SQLException exc)
        {
            return false;
        }
    }
    
    private static boolean compareRef(Object source, Object target)
    {
        try
        {
            return ((Ref)source).getObject().equals(((Ref)target).getObject());
        }
        catch(SQLException exc)
        {
            return false;
        }
    }  
}

