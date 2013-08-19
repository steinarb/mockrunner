package com.mockrunner.test.web;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.mockrunner.struts.DynamicMockProxyGenerator;

import junit.framework.TestCase;

public class DynamicMockProxyGeneratorTest extends TestCase
{
    private Delegator delegator;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        delegator = new Delegator();
    }
    protected void tearDown() throws Exception
    {
        super.tearDown();
        delegator = null;
    }
    
    public void testProperInstance()
    {
        DynamicMockProxyGenerator generator = new DynamicMockProxyGenerator(DynamicMockProxyGeneratorTest.class, delegator, Delegator.class.getDeclaredMethods(), new Method[0], Serializable.class);
        Object proxy = generator.createProxy();
        assertTrue(proxy instanceof Serializable);
        assertTrue(proxy instanceof DynamicMockProxyGeneratorTest);
    }
    
    public void testDelegationAndDuplication()
    {
        DynamicMockProxyGenerator generator = new DynamicMockProxyGenerator(DynamicMockProxyGeneratorTest.class, delegator, Delegator.class.getDeclaredMethods(), Super.class.getDeclaredMethods());
        DynamicMockProxyGeneratorTest proxy = (DynamicMockProxyGeneratorTest)generator.createProxy();
        proxy.method1("method1");
        assertEquals(3, proxy.method2("method2", (short)5));
        assertEquals("test", proxy.method3());
        assertTrue(delegator.wasMethod1Called());
        assertTrue(delegator.wasMethod2Called());
        assertEquals("method1", delegator.getMethod1Param());
        assertEquals("method2", delegator.getMethod2Param1());
        assertEquals(5, delegator.getMethod2Param2());
        assertEquals("DynamicMockProxyGeneratorTest", proxy.superMethod(5));
        assertTrue(delegator.wasSuperMethodCalled());
        assertEquals(5, delegator.getSuperMethodParam());
    }
    
    public void method1(String name)
    {

    }
    
    public int method2(String name, short value)
    {
        return 0;
    }
    
    public String method3()
    {
        return "test";
    }
    
    public String superMethod(int value)
    {
        return "DynamicMockProxyGeneratorTest";
    }
    
    public static class Super
    {
        private boolean wasSuperMethodCalled = false;
        private int superMethodParam = 0;
        
        public String superMethod(int value)
        {
            wasSuperMethodCalled = true;
            superMethodParam = value;
            return "Super";
        }
        
        public boolean wasSuperMethodCalled()
        {
            return wasSuperMethodCalled;
        }

        public int getSuperMethodParam()
        {
            return superMethodParam;
        }
    }
    
    public static class Delegator extends Super
    {
        private boolean wasMethod1Called = false;
        private boolean wasMethod2Called = false;
        private String method1Param = null;
        private String method2Param1 = null;
        private short method2Param2 = 0;
        
        public void method1(String name)
        {
            method1Param = name;
            wasMethod1Called = true;
        }
        
        public int method2(String name, short value)
        {
            method2Param1 = name;
            method2Param2 = value;
            wasMethod2Called = true;
            return 3;
        }
        
        public String getMethod1Param()
        {
            return method1Param;
        }
        
        public String getMethod2Param1()
        {
            return method2Param1;
        }
        
        public short getMethod2Param2()
        {
            return method2Param2;
        }
        
        public boolean wasMethod1Called()
        {
            return wasMethod1Called;
        }
        
        public boolean wasMethod2Called()
        {
            return wasMethod2Called;
        }
    }
}
