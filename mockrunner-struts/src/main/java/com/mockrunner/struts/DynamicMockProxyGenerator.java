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
    private Set methodsToDuplicate;
    
    public DynamicMockProxyGenerator(Class proxiedClass, Object delegate, Method[] methodsToIntercept, Method[] methodsToDuplicate)
    {
        this(proxiedClass, delegate, methodsToIntercept, methodsToDuplicate, null);
    }

    public DynamicMockProxyGenerator(Class proxiedClass, Object delegate, Method[] methodsToIntercept, Method[] methodsToDuplicate, Class additionalInterface)
    {
        this.proxiedClass = proxiedClass;
        this.delegate = delegate;
        this.additionalInterface = additionalInterface;
        this.methodsToIntercept = new HashSet();
        this.methodsToIntercept.addAll(Arrays.asList(methodsToIntercept));
        this.methodsToDuplicate = new HashSet();
        this.methodsToDuplicate.addAll(Arrays.asList(methodsToDuplicate));
    }

    public Object createProxy()
    {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(proxiedClass);
        if(null != additionalInterface)
        {
            enhancer.setInterfaces(new Class[] { additionalInterface });
        }
        Method[] targetInterceptMethods = getActualTargetMethods(delegate, methodsToIntercept);
        Method[] targetDuplicateMethods = getActualTargetMethods(delegate, methodsToDuplicate);
        enhancer.setCallback(new DelegatingInterceptor(delegate, targetInterceptMethods, targetDuplicateMethods));
        return enhancer.create();
    }
    
    private Method[] getActualTargetMethods(Object delegate, Set providedMethods)
    {
        Method[] methods = delegate.getClass().getMethods();
        Set actualMethods = new HashSet();
        Set tempProvidedMethods = new HashSet(providedMethods);
        for (Method method : methods) {
            findAndAddMethod(tempProvidedMethods, method, actualMethods);
        }
        return (Method[])actualMethods.toArray(new Method[actualMethods.size()]);
    }
    
    private void findAndAddMethod(Set providedMethods, Method currentMethod, Set actualMethods)
    {
        Iterator iterator = providedMethods.iterator();
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
        private Method[] methodsToDuplicate;
        
        public DelegatingInterceptor(Object delegate, Method[] methodsToIntercept, Method[] methodsToDuplicate)
        {
            this.delegate = delegate;
            this.methodsToIntercept = methodsToIntercept;
            this.methodsToDuplicate = methodsToDuplicate;
        }
        
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable
        {
            for (Method aMethodsToIntercept : methodsToIntercept) {
                if (MethodUtil.areMethodsEqual(method, aMethodsToIntercept)) {
                    return aMethodsToIntercept.invoke(delegate, args);
                }
            }
            for (Method aMethodsToDuplicate : methodsToDuplicate) {
                if (MethodUtil.areMethodsEqual(method, aMethodsToDuplicate)) {
                    aMethodsToDuplicate.invoke(delegate, args);
                }
            }
            return proxy.invokeSuper(obj, args);
        }
    }
}
