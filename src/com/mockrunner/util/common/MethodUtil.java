package com.mockrunner.util.common;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    
    /**
     * Invokes the method with the specified name on the specified object
     * and throws a {@link com.mockrunner.base.NestedApplicationException},
     * if the invocation fails. The method must be public and must have
     * exactly one paremeter of the type specified by the given
     * <code>parameter</code>.
     * @param object the object the method is invoked from
     * @param methodName the name of the method
     * @param parameter the parameter, must not be <code>null</code>
     * @return the result of the method invocation
     */
    public static Object invoke(Object object, String methodName, Object parameter)
    {
        try
        {
            Method method = object.getClass().getMethod(methodName, new Class[] {parameter.getClass()});
            return method.invoke(object, new Object[] {parameter});
        } 
        catch(Exception exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Returns if the two specified methods are equal as
     * defined by <code>Method.equals()</code> except that
     * the methods can be defined by different classes.
     * @param method1 the first method to compare
     * @param method2 the second method to compare
     * @return <code>true</code> if the methods are equal, <code>false</code>
     *         otherwise
     * @throws NullPointerException if one of the methods is <code>null</code>
     */
    public static boolean areMethodsEqual(Method method1, Method method2)
    {
        if(method1.equals(method2)) return true;
        if(!method2.getName().equals(method1.getName())) return false;
        if(!method1.getReturnType().equals(method2.getReturnType())) return false;
        return Arrays.equals(method1.getParameterTypes(), method2.getParameterTypes());
    }
    
    /**
     * Returns if <code>method2</code> overrides <code>method1</code>.
     * @param method1 method to be overridden
     * @param method2 overriding method
     * @return <code>true</code> if <code>method2</code> overrides <code>method1</code>, <code>false</code>
     *         otherwise
     * @throws NullPointerException if one of the methods is <code>null</code>
     */
    public static boolean overrides(Method method1, Method method2)
    {
        if(method1.equals(method2)) return false;
        if(!method1.getDeclaringClass().isAssignableFrom(method2.getDeclaringClass())) return false;
        if(!method2.getName().equals(method1.getName())) return false;
        if(method1.getDeclaringClass().isInterface()) return false;
        return Arrays.equals(method1.getParameterTypes(), method2.getParameterTypes());
    }
    
    /**
     * Returns the declared methods of the specified class whose names are matching
     * the specified regular expression.
     * @param theClass the class whose methods are examined
     * @param expr the regular expression
     * @return the matching methods
     */
    public static Method[] getMatchingDeclaredMethods(Class theClass, String expr)
    {
        Method[] methods = theClass.getDeclaredMethods();
        List resultList = new ArrayList();
        for(int ii = 0; ii < methods.length; ii++)
        {
            if(StringUtil.matchesPerl5(methods[ii].getName(), expr, true))
            {
                resultList.add(methods[ii]);
            }
        }
        return (Method[])resultList.toArray(new Method[resultList.size()]);
    }
    
    /**
     * Returns all non-static methods declared by the specified class and its
     * superclasses. The returned array contains the methods of all classes
     * in the inheritance hierarchy, starting with the methods of the
     * most general superclass, which is <code>java.lang.Object</code>.
     * @param theClass the class whose methods are examined
     * @return the array of method arrays
     */
    public static Method[][] getPublicMethodsSortedByInheritanceHierarchy(Class theClass)
    {
        List hierarchyList = new ArrayList();
        Class[] hierarchyClasses = ClassUtil.getInheritanceHierarchy(theClass);
        for(int ii = 0; ii < hierarchyClasses.length; ii++)
        {
            addMethodsForClass(hierarchyList, hierarchyClasses[ii]);
        }
        return (Method[][])hierarchyList.toArray(new Method[hierarchyList.size()][]);
    }
    
    private static void addMethodsForClass(List hierarchyList, Class clazz)
    {
        List methodList = new ArrayList();
        Method[] methods = clazz.getDeclaredMethods();
        for(int ii = 0; ii < methods.length; ii++)
        {
            if(!Modifier.isStatic(methods[ii].getModifiers()))
            {
                methodList.add(methods[ii]);
            }
        }
        hierarchyList.add(methodList.toArray(new Method[methodList.size()]));
    }
}
