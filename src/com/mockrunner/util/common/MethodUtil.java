package com.mockrunner.util.common;

import java.lang.reflect.Method;

import com.mockrunner.base.NestedApplicationException;

public class MethodUtil
{
    /**
     * Invokes the method with the specified name on the specified object
     * and throws a {@link com.mockrunner.base.NestedApplicationException},
     * if the invocation fails. The method must be public and must not
     * have any parameters.
     * @param object the object the method is invoked from
     * @param methodName the name of the method
     * @return the result of the method invocation
     */
    public static Object invoke(Object object, String methodName)
    {
        try
        {
            Method method = object.getClass().getMethod(methodName, null);
            return method.invoke(object, null);
        } 
        catch(Exception exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
}
