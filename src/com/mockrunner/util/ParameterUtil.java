package com.mockrunner.util;

import java.io.InputStream;
import java.io.Reader;
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
     * <code>InputStream</code>, <code>Reader</code> or other objects,
     * this methods compares byte arrays or streams if the input parameters
     * have the corresponding type. Otherwise the <code>equals</code>
     * method is called.
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
        return source.equals(target);
    }  
}

