package com.mockrunner.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.mockrunner.servlet.ServletTestCaseAdapter;
import com.mockrunner.servlet.ServletTestModule;

public class AdapterConsistencyTest extends TestCase
{
    private Map adapterMap;
    private Map failures;
    private List exceptions;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        failures = new HashMap();
        exceptions = new ArrayList();
        adapterMap = new HashMap();
        adapterMap.put(ServletTestModule.class, ServletTestCaseAdapter.class);
    }
    
    public void testAdapterConsistency()
    {
        Iterator modules = adapterMap.keySet().iterator();
        while(modules.hasNext())
        {
            List currentFailureList = new ArrayList();
            Class currentModule = (Class)modules.next();
            Class currentAdapter = (Class)adapterMap.get(currentModule);
            failures.put(currentModule, currentFailureList);
            Method[] adapterMethods = currentAdapter.getDeclaredMethods();
            Method[] moduleMethods = currentModule.getDeclaredMethods();
            for(int ii = 0; ii < moduleMethods.length; ii++)
            {
                Method currentMethod = moduleMethods[ii];
                if(!isMethodDeclared(currentMethod, adapterMethods))
                {
                    currentFailureList.add(currentMethod);
                }
            }
        }
        dumpFailures();
    }
    
    //TODO: complete this stuff
    private boolean isMethodDeclared(Method theMethod, Method[] otherClassMethods)
    {
        for(int ii = 0; ii < otherClassMethods.length; ii++)
        {
            Method currentMethod = otherClassMethods[ii];
        }
        return true;
    }
    
    private void dumpFailures()
    {
    
    }
}
