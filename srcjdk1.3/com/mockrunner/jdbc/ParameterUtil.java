package com.mockrunner.jdbc;

import java.io.InputStream;
import java.io.Reader;

import com.mockrunner.mock.jdbc.MockResultSet;
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
     * <code>CallableStatement</code> or a <code>ResultSet</code> value.
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
     * <code>CallableStatement</code>. Can also be used to compare
     * values of a <code>ResultSet</code>. It is used by
     * {@link com.mockrunner.jdbc.PreparedStatementResultSetHandler}
     * for comparing parameters specified in the <code>prepare</code>
     * methods.
     * Since the parameters can be of the type <code>byte[]</code>,
     * <code>InputStream</code> and <code>Reader</code> this method handles 
     * these types of objects. All other objects are compared using the 
     * <code>equals</code> method. The mock versions of <code>Ref</code>,
     * <code>Array</code>, <code>Blob</code>, <code>Clob</code>,
     * <code>Struct</code> etc. all provide a suitable <code>equals</code>
     * implementation.
     * @param source the first parameter
     * @param target the second parameter
     * @return <code>true</code> if <i>source</i> is equal to <i>target</i>,
     *         <code>false</code> otherwise
     */
    public static boolean compareParameter(Object source, Object target)
    {
        if(null == source && null == target) return true;
        if(null == source || null == target) return false;
        if(source instanceof InputStream && target instanceof InputStream)
        {
            return StreamUtil.compareStreams((InputStream)source, (InputStream)target);
        }
        if(source instanceof Reader && target instanceof Reader)
        {
            return StreamUtil.compareReaders((Reader)source, (Reader)target);
        }
        if(source instanceof MockResultSet && target instanceof MockResultSet)
        {
            return ((MockResultSet)source).isEqual((MockResultSet)target);
        }
        if(source.getClass().isArray() && target.getClass().isArray())
        {
            return ArrayUtil.areArraysEqual(source, target);
        }
        return source.equals(target);
    }
}

