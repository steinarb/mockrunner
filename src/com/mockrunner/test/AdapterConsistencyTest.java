package com.mockrunner.test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.mockrunner.ejb.EJBTestCaseAdapter;
import com.mockrunner.ejb.EJBTestModule;
import com.mockrunner.jdbc.JDBCTestCaseAdapter;
import com.mockrunner.jdbc.JDBCTestModule;
import com.mockrunner.jms.JMSTestCaseAdapter;
import com.mockrunner.jms.JMSTestModule;
import com.mockrunner.servlet.ServletTestCaseAdapter;
import com.mockrunner.servlet.ServletTestModule;
import com.mockrunner.struts.ActionTestCaseAdapter;
import com.mockrunner.struts.ActionTestModule;
import com.mockrunner.tag.TagTestCaseAdapter;
import com.mockrunner.tag.TagTestModule;

public class AdapterConsistencyTest extends TestCase
{
    private Map adapterMap;
    private Map exceptionMap;
    private Map failureMap;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        failureMap = new HashMap();
        adapterMap = new HashMap();
        exceptionMap = new HashMap();
        initializeModules();
    }

    private void initializeModules()
    {
        adapterMap.put(ServletTestModule.class, ServletTestCaseAdapter.class);
        List exceptions = new ArrayList();
        exceptions.add("getOutput");
        exceptionMap.put(ServletTestModule.class, exceptions);
        
        adapterMap.put(TagTestModule.class, TagTestCaseAdapter.class);
        exceptions = new ArrayList();
        exceptions.add("getOutput");
        exceptionMap.put(TagTestModule.class, exceptions);
        
        adapterMap.put(ActionTestModule.class, ActionTestCaseAdapter.class);
        exceptions = new ArrayList();
        exceptionMap.put(ActionTestModule.class, exceptions);
        
        adapterMap.put(EJBTestModule.class, EJBTestCaseAdapter.class);
        exceptions = new ArrayList();
        exceptionMap.put(EJBTestModule.class, exceptions);
        
        adapterMap.put(JDBCTestModule.class, JDBCTestCaseAdapter.class);
        exceptions = new ArrayList();
        exceptionMap.put(JDBCTestModule.class, exceptions);
        
        adapterMap.put(JMSTestModule.class, JMSTestCaseAdapter.class);
        exceptions = new ArrayList();
        exceptionMap.put(JMSTestModule.class, exceptions);
    }
    
    public void testAdapterConsistency()
    {
        Iterator modules = adapterMap.keySet().iterator();
        while(modules.hasNext())
        {
            List currentFailureList = new ArrayList();
            Class currentModule = (Class)modules.next();
            Class currentAdapter = (Class)adapterMap.get(currentModule);
            failureMap.put(currentModule, currentFailureList);
            Method[] adapterMethods = currentAdapter.getDeclaredMethods();
            Method[] moduleMethods = currentModule.getDeclaredMethods();
            for(int ii = 0; ii < moduleMethods.length; ii++)
            {
                Method currentMethod = moduleMethods[ii];
                if(shouldMethodBeChecked(currentMethod, currentModule))
                {
                    if(!isMethodDeclared(currentMethod, adapterMethods))
                    {
                        currentFailureList.add(currentMethod);
                    }
                }
            }
        }
        dumpFailures();
    }
    
    private boolean shouldMethodBeChecked(Method theMethod, Class module)
    {
        if(!Modifier.isPublic(theMethod.getModifiers())) return false;
        List exceptionMethods = (List)exceptionMap.get(module);
        if(null != exceptionMethods && exceptionMethods.contains(theMethod.getName()))
        {
            return false;
        }
        return true;
    }
    
    private boolean isMethodDeclared(Method theMethod, Method[] otherClassMethods)
    {
        for(int ii = 0; ii < otherClassMethods.length; ii++)
        {
            Method currentMethod = otherClassMethods[ii];
            if(areMethodsEqual(theMethod, currentMethod))
            {
                return true;
            }
        }
        return false;
    }
    
    private boolean areMethodsEqual(Method aMethod, Method anotherMethod)
    {
        if(Modifier.isPrivate(anotherMethod.getModifiers())) return false;
        if(!aMethod.getName().equals(anotherMethod.getName())) return false;
        if(!aMethod.getReturnType().equals(anotherMethod.getReturnType())) return false;
        Class[] aMethodParams = aMethod.getParameterTypes();
        Class[] anotherMethodParams = anotherMethod.getParameterTypes();
        return Arrays.equals(aMethodParams, anotherMethodParams);
    }
    
    private void dumpFailures()
    {
        boolean isOk = true;
        Iterator modules = failureMap.keySet().iterator();
        while(modules.hasNext())
        {
            Class currentModule = (Class)modules.next();
            List currentFailureList = (List)failureMap.get(currentModule);
            if(!currentFailureList.isEmpty())
            {
                isOk = false;
                System.out.println("Missing methods in adapter of module: " + currentModule.getName());
                System.out.println();
                for(int ii = 0; ii < currentFailureList.size(); ii++)
                {
                    Method currentMethod = (Method)currentFailureList.get(ii);
                    Class[] params = currentMethod.getParameterTypes();
                    System.out.println("Method " + ii + ":");
                    System.out.println("Name: " + currentMethod.getName());
                    System.out.println("Return type: " + currentMethod.getReturnType().getName());
                    if(null != params && params.length > 0)
                    {
                        System.out.print("Parameter: ");
                        for(int yy = 0; yy < params.length; yy++)
                        {
                            Class currentParam = params[yy];
                            System.out.print(currentParam.getName() + ";");
                        }
                        System.out.println();
                    }
                    System.out.println();
                }
            }
        }
        assertTrue(isOk);
    }
}
