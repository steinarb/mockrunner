package com.mockrunner.test.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.util.common.MethodUtil;

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
    
    public void testAreMethodEqual() throws Exception
    {
        Method method1 = TestObject.class.getMethod("testMethod1", null);
        Method method2 = TestObject.class.getMethod("testMethod2", null);
        Method method4 = TestObject.class.getMethod("testMethod4", new Class[] {String.class});
        Method method5 = TestObject.class.getMethod("testMethod5", new Class[] {Integer.class});
        Method anotherMethod2 = AnotherTestObject.class.getMethod("testMethod2", null);
        Method anotherMethod4 = AnotherTestObject.class.getMethod("testMethod4", new Class[] {Integer.class});
        Method anotherMethod5 = AnotherTestObject.class.getMethod("testMethod5", new Class[] {Integer.class});
        try
        {
            MethodUtil.areMethodsEqual(null, method1);
            fail();
        } 
        catch(NullPointerException exc)
        {
            //should throw exception
        }
        try
        {
            MethodUtil.areMethodsEqual(method2, null);
            fail();
        } 
        catch(NullPointerException exc)
        {
            //should throw exception
        }
        assertTrue(MethodUtil.areMethodsEqual(method1, method1));
        assertFalse(MethodUtil.areMethodsEqual(method1, method2));
        assertFalse(MethodUtil.areMethodsEqual(method2, anotherMethod2));
        assertFalse(MethodUtil.areMethodsEqual(method2, anotherMethod4));
        assertFalse(MethodUtil.areMethodsEqual(method4, anotherMethod4));
        assertTrue(MethodUtil.areMethodsEqual(method5, anotherMethod5));
    }
    
    public void testGetMatchingDeclaredMethods() throws Exception
    {
        Method[] methods = MethodUtil.getMatchingDeclaredMethods(TestObject.class, "test.*");
        assertEquals(5, methods.length);
        List methodNames = new ArrayList();
        for(int ii = 0; ii < methods.length; ii++)
        {
            methodNames.add(methods[ii].getName());
        }
        assertTrue(methodNames.contains("testMethod1"));
        assertTrue(methodNames.contains("testMethod2"));
        assertTrue(methodNames.contains("testMethod3"));
        assertTrue(methodNames.contains("testMethod4"));
        assertTrue(methodNames.contains("testMethod5"));
        methods = MethodUtil.getMatchingDeclaredMethods(TestObject.class, "wasMethod.Called");
        assertEquals(5, methods.length);
        methodNames = new ArrayList();
        for(int ii = 0; ii < methods.length; ii++)
        {
            methodNames.add(methods[ii].getName());
        }
        assertTrue(methodNames.contains("wasMethod1Called"));
        assertTrue(methodNames.contains("wasMethod2Called"));
        assertTrue(methodNames.contains("wasMethod3Called"));
        assertTrue(methodNames.contains("wasMethod4Called"));
        assertTrue(methodNames.contains("wasMethod5Called"));
        methods = MethodUtil.getMatchingDeclaredMethods(TestObject.class, "wasMethod5Called");
        assertEquals(1, methods.length);
    }
    
    public class Super
    {
        public void testSuperMethod()
        {
            
        }
    }
    
    public class TestObject extends Super
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
    
    public class AnotherTestObject
    {
        public int testMethod2()
        {
            return 0;
        }
        
        public void testMethod4(Integer arg)
        {
            
        }
        
        public int testMethod5(Integer arg)
        {
            return 0;
        }
    }
}
