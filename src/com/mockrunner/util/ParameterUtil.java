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
     * Compares two parameters of a <code>PreparedStatement</code> or
     * <code>CallableStatement</code>. You can use it to compare
     * parameters of a <code>ResultSet</code>. It is used by
     * {@link com.mockrunner.mock.jdbc.PreparedStatementResultSetHandler}
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

