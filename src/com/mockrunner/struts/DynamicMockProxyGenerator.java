package com.mockrunner.struts;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import com.mockrunner.util.common.MethodUtil;

/**
 * Helper class to generate CGLib proxies. Not meant for application use.
 */
public class DynamicMockProxyGenerator
{
    private Class proxiedClass;
    private Object delegate;
    private Class additionalInterface;
    private Set methodsToIntercept;
    
    public DynamicMockProxyGenerator(Class proxiedClass, Object delegate, Method[] methodsToIntercept)
    {
        this(proxiedClass, delegate, methodsToIntercept, null);
    }

    public DynamicMockProxyGenerator(Class proxiedClass, Object delegate, Method[] methodsToIntercept, Class additionalInterface)
    {
        this.proxiedClass = proxiedClass;
        this.delegate = delegate;
        this.additionalInterface = additionalInterface;
        this.methodsToIntercept = new HashSet();
        this.methodsToIntercept.addAll(Arrays.asList(methodsToIntercept));
    }

    public Object createProxy()
    {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(proxiedClass);
        if(null != additionalInterface)
        {
            enhancer.setInterfaces(new Class[] { additionalInterface });
        }
        Method[] targetMethods = getActualTargetMethods(delegate, methodsToIntercept);
        enhancer.setCallback(new DelegatingInterceptor(delegate, targetMethods));
        return enhancer.create();
    }
    
    private Method[] getActualTargetMethods(Object delegate, Set methodsToIntercept)
    {
        Method[] methods = delegate.getClass().getMethods();
        Set actualMethods = new HashSet();
        Set tempMethodsToIntercept = new HashSet(methodsToIntercept);
        for(int ii = 0; ii < methods.length; ii++)
        {
            findAndMethod(tempMethodsToIntercept, methods[ii], actualMethods);
        }
        return (Method[])actualMethods.toArray(new Method[actualMethods.size()]);
    }
    
    private void findAndMethod(Set methodsToIntercept, Method currentMethod, Set actualMethods)
    {
        Iterator iterator = methodsToIntercept.iterator();
        while(iterator.hasNext())
        {
            Method currentMethodToIntercept = (Method)iterator.next();
            if(MethodUtil.areMethodsEqual(currentMethod, currentMethodToIntercept))
            {
                actualMethods.add(currentMethod);
                iterator.remove();
                return;
            }
        }
    }
    
    private static class DelegatingInterceptor implements MethodInterceptor
    { 
        private Object delegate;
        private Method[] methodsToIntercept;
        
        public DelegatingInterceptor(Object delegate, Method[] methodsToIntercept)
        {
            this.delegate = delegate;
            this.methodsToIntercept = methodsToIntercept;
        }
        
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable
        {
            for(int ii = 0; ii < methodsToIntercept.length; ii++)
            {
                if(MethodUtil.areMethodsEqual(method, methodsToIntercept[ii]))
                {
                    return methodsToIntercept[ii].invoke(delegate, args);
                }
            }
            return proxy.invokeSuper(obj, args);
        }
    }
}
