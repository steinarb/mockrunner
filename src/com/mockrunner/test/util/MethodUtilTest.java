package com.mockrunner.test.util;

import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.util.common.MethodUtil;

import junit.framework.TestCase;

public class MethodUtilTest extends TestCase
{
    public void testInvoke()
    {
        TestObject testObject = new TestObject();
        assertNull(MethodUtil.invoke(testObject, "testMethod1"));
        assertEquals("testMethod2", MethodUtil.invoke(testObject, "testMethod2"));
        try
        {
            MethodUtil.invoke(testObject, "testMethod3");
            fail();
        } 
        catch(NestedApplicationException exc)
        {
            //should throw exception
        }
        try
        {
            MethodUtil.invoke(testObject, "testMethod4");
            fail();
        } 
        catch(NestedApplicationException exc)
        {
            //should throw exception
        }
        assertTrue(testObject.wasMethod1Called());
        assertTrue(testObject.wasMethod2Called());
        assertFalse(testObject.wasMethod3Called());
        assertFalse(testObject.wasMethod4Called());
    }
    
    public void testInvokeWithParameter()
    {
        TestObject testObject = new TestObject();
        try
        {
            MethodUtil.invoke(testObject, "testMethod1", null);
            fail();
        } 
        catch(NestedApplicationException exc)
        {
            //should throw exception
        }
        try
        {
            MethodUtil.invoke(testObject, "testMethod1", "test");
            fail();
        } 
        catch(NestedApplicationException exc)
        {
            //should throw exception
        }
        assertNull(MethodUtil.invoke(testObject, "testMethod4", "test"));
        assertEquals(new Integer(3), MethodUtil.invoke(testObject, "testMethod5", new Integer(3)));
        assertFalse(testObject.wasMethod1Called());
        assertTrue(testObject.wasMethod4Called());
        assertTrue(testObject.wasMethod5Called());
    }
    
    public class TestObject
    {
        private boolean method1Called = false;
        private boolean method2Called = false;
        private boolean method3Called = false;
        private boolean method4Called = false;
        private boolean method5Called = false;
        
        public void testMethod1()
        {
            method1Called = true;
        }
        
        public String testMethod2()
        {
            method2Called = true;
            return "testMethod2";
        }
        
        protected void testMethod3()
        {
            method3Called = true;
        }
        
        public void testMethod4(String arg)
        {
            method4Called = true;
        }
        
        public int testMethod5(Integer arg)
        {
            method5Called = true;
            return arg.intValue();
        }
        
        public boolean wasMethod1Called()
        {
            return method1Called;
        }
        
        public boolean wasMethod2Called()
        {
            return method2Called;
        }
        
        public boolean wasMethod3Called()
        {
            return method3Called;
        }
        
        public boolean wasMethod4Called()
        {
            return method4Called;
        }
        
        public boolean wasMethod5Called()
        {
            return method5Called;
        }
    }
}
