package com.mockrunner.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.sql.SQLException;
import java.util.Arrays;

import com.mockrunner.mock.jdbc.MockArray;
import com.mockrunner.mock.jdbc.MockBlob;
import com.mockrunner.mock.jdbc.MockClob;
import com.mockrunner.mock.jdbc.MockRef;
import com.mockrunner.mock.jdbc.MockStruct;
import com.mockrunner.util.common.ArrayUtil;
import com.mockrunner.util.common.MethodUtil;
import com.mockrunner.util.common.StreamUtil;

/**
 * Util class for <code>PreparedStatement</code> and <code>ResultSet</code>
 * parameters.
 */
public class ParameterUtil
{
    /**
     * Copies a parameter of a <code>PreparedStatement</code>,
     * <code>CallableStatement</code> or <code>ResultSet</code>.
     * <code>InputStream</code> objects, <code>Reader</code> objects 
     * and arrays are copied into new allocated streams or arrays.
     * All other objects are cloned by calling the clone method. 
     * If the object is not cloneable, it is returned unchanged.
     * @param source the parameter to copy
     * @return a copy of the parameter
     */
    public static Object copyParameter(Object source)
    {
        if(null == source) return null;
        if(source.getClass().isArray())
        {
            return ArrayUtil.copyArray(source);
        }
        if(source instanceof InputStream)
        {
            return StreamUtil.copyStream((InputStream)source);
        }
        if(source instanceof Reader)
        {
            return StreamUtil.copyReader((Reader)source);
        }
        if(source instanceof Cloneable)
        {
            try
            {
                return MethodUtil.invoke(source, "clone");
            }
            catch(Exception exc)
            {
                return source;
            }
        }
        return source;
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
     * <code>Array</code>, <code>Blob</code>, <code>Clob</code> or
     * <code>Struct</code> this method can handle these types of objects. 
     * All other objects are compared using the <code>equals</code> method.
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
            return StreamUtil.compareReaders((Reader)source, (Reader)target);
        }
        if(source instanceof MockRef && target instanceof MockRef)
        {
            return compareRef(source, target);
        }
        if(source instanceof MockArray && target instanceof MockArray)
        {
            return compareArray(source, target);
        }
        if(source instanceof MockBlob && target instanceof MockBlob)
        {
            return compareBlob(source, target);
        }
        if(source instanceof MockClob && target instanceof MockClob)
        {
            return compareClob(source, target);
        }
        if(source instanceof MockStruct && target instanceof MockStruct)
        {
            return compareStruct(source, target);
        }
        return source.equals(target);
    }
    
    private static boolean compareClob(Object source, Object target)
    {
        try
        {
            String sourceString = ((MockClob)source).getSubString(1, (int)((MockClob)source).length());
            String targetString = ((MockClob)target).getSubString(1, (int)((MockClob)target).length());
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
            byte[] sourceArray = ((MockBlob)source).getBytes(1, (int)((MockBlob)source).length());
            byte[] targetArray = ((MockBlob)target).getBytes(1, (int)((MockBlob)target).length());
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
            Object[] sourceArray = ArrayUtil.convertToObjectArray(((MockArray)source).getArray());
            Object[] targetArray = ArrayUtil.convertToObjectArray(((MockArray)target).getArray());
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
            return ((MockRef)source).getObject().equals(((MockRef)target).getObject());
        }
        catch(SQLException exc)
        {
            return false;
        }
    }
    
    private static boolean compareStruct(Object source, Object target)
    {
        try
        {
            String sourceName = ((MockStruct)source).getSQLTypeName();
            String targetName = ((MockStruct)target).getSQLTypeName();
            Object[] sourceArray = ((MockStruct)source).getAttributes();
            Object[] targetArray = ((MockStruct)target).getAttributes();
            return (sourceName.equals(targetName)) && (Arrays.equals(sourceArray, targetArray));
        }
        catch(SQLException exc)
        {
            return false;
        }
    }
}

