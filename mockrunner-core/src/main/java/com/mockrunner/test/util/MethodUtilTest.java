package com.mockrunner.test.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
        assertEquals(new Integer(3), MethodUtil.invoke(testObject, "testMethod5", new Integer[] { new Integer(3)}));
        assertFalse(testObject.wasMethod1Called());
        assertTrue(testObject.wasMethod4Called());
        assertTrue(testObject.wasMethod5Called());
    }
    
    public void testAreMethodEqual() throws Exception
    {
        Method method1 = TestObject.class.getMethod("testMethod1", null);
        Method method2 = TestObject.class.getMethod("testMethod2", null);
        Method method4 = TestObject.class.getMethod("testMethod4", new Class[] {String.class});
        Method method5 = TestObject.class.getMethod("testMethod5", new Class[] {Integer[].class});
        Method anotherMethod2 = AnotherTestObject.class.getMethod("testMethod2", null);
        Method anotherMethod4 = AnotherTestObject.class.getMethod("testMethod4", new Class[] {Integer.class});
        Method anotherMethod5 = AnotherTestObject.class.getMethod("testMethod5", new Class[] {Integer[].class});
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
    
    public void testOverrides() throws Exception
    {
        Method method1Super = TestSuper.class.getDeclaredMethod("testMethod1", null);
        Method method2Super = TestSuper.class.getDeclaredMethod("testMethod2", new Class[] { int[].class, String.class});
        Method method4Super = TestSuper.class.getDeclaredMethod("testMethod4", null);
        Method method1SubOverride = TestSub.class.getDeclaredMethod("testMethod1", null);
        Method method2SubOverride = TestSub.class.getDeclaredMethod("testMethod2", new Class[] { int[].class, String.class});
        Method method1SubNotOverride = TestSub.class.getDeclaredMethod("testMethod1", new Class[] { String.class });
        Method method2SubNotOverride = TestSub.class.getDeclaredMethod("testMethod2", new Class[] { int[].class});
        Method method4SubNotOverride = TestSub.class.getDeclaredMethod("testMethod4", null);
        Method methodInterface = TestInterface.class.getDeclaredMethod("testInterface", null);
        Method methodSubInterface = TestSub.class.getDeclaredMethod("testInterface", null);
        assertFalse(MethodUtil.overrides(method1Super, method1Super));
        assertFalse(MethodUtil.overrides(method1Super, method2Super));
        assertFalse(MethodUtil.overrides(method1Super, method1SubNotOverride));
        assertFalse(MethodUtil.overrides(method2Super, method2SubNotOverride));
        assertFalse(MethodUtil.overrides(method1SubOverride, method1Super));
        assertFalse(MethodUtil.overrides(method4Super, method4SubNotOverride));
        assertFalse(MethodUtil.overrides(method4SubNotOverride, method4Super));
        assertFalse(MethodUtil.overrides(methodInterface, methodSubInterface));
        assertTrue(MethodUtil.overrides(method1Super, method1SubOverride));
        assertTrue(MethodUtil.overrides(method2Super, method2SubOverride));
    }
    
    public void testGetMethodsSortedByInheritanceHierarchy() throws Exception
    {
        Method[][] methods = MethodUtil.getMethodsSortedByInheritanceHierarchy(TestSub3.class);
        assertEquals(5, methods.length);
        assertEquals(3, methods[1].length);
        assertEquals(7, methods[2].length);
        assertEquals(3, methods[3].length);
        Method method1Super = TestSuper.class.getDeclaredMethod("testMethod1", null);
        Method method2Super = TestSuper.class.getDeclaredMethod("testMethod2", new Class[] { int[].class, String.class});
        Method method1SubOverride = TestSub.class.getDeclaredMethod("testMethod1", null);
        Method method2SubOverride = TestSub.class.getDeclaredMethod("testMethod2", new Class[] { int[].class, String.class});
        Method method1SubNotOverride = TestSub.class.getDeclaredMethod("testMethod1", new Class[] { String.class });
        Method method2SubNotOverride = TestSub.class.getDeclaredMethod("testMethod2", new Class[] { int[].class});
        Method methodSubInterface = TestSub.class.getDeclaredMethod("testInterface", null);
        Method methodSubtoString = TestSub.class.getDeclaredMethod("toString", null);
        Method methodSub2another = TestSub2.class.getDeclaredMethod("anotherMethod", null); 
        Method methodSub2anotherProtected = TestSub2.class.getDeclaredMethod("anotherProtectedMethod", null);
        Method methodSub2toString = TestSub2.class.getDeclaredMethod("toString", null);
        assertTrue(Arrays.asList(methods[1]).contains(method1Super));
        assertTrue(Arrays.asList(methods[1]).contains(method2Super));
        assertTrue(Arrays.asList(methods[2]).contains(method1SubOverride));
        assertTrue(Arrays.asList(methods[2]).contains(method2SubOverride));
        assertTrue(Arrays.asList(methods[2]).contains(method1SubNotOverride));
        assertTrue(Arrays.asList(methods[2]).contains(method2SubNotOverride));
        assertTrue(Arrays.asList(methods[2]).contains(methodSubInterface));
        assertTrue(Arrays.asList(methods[2]).contains(methodSubtoString));
        assertTrue(Arrays.asList(methods[3]).contains(methodSub2another));
        assertTrue(Arrays.asList(methods[3]).contains(methodSub2anotherProtected));
        assertTrue(Arrays.asList(methods[3]).contains(methodSub2toString));
        assertEquals(0, methods[4].length);
    }
    
    public void testGetOverriddenMethods() throws Exception
    {
        Method method2Super = TestSuper.class.getDeclaredMethod("testMethod2", new Class[] { int[].class, String.class});
        Method method1SubOverride = TestSub.class.getDeclaredMethod("testMethod1", null);
        Method method2SubOverride = TestSub.class.getDeclaredMethod("testMethod2", new Class[] { int[].class, String.class});
        Method method2SubNotOverride = TestSub.class.getDeclaredMethod("testMethod2", new Class[] { int[].class});
        Method methodSubtoString = TestSub.class.getDeclaredMethod("toString", null);
        Method methodSub2another = TestSub2.class.getDeclaredMethod("anotherMethod", null);
        Method methodSub2toString = TestSub2.class.getDeclaredMethod("toString", null);
        Method toString = Object.class.getDeclaredMethod("toString", null);
        Set methods = MethodUtil.getOverriddenMethods(TestSub2.class, new Method[] { method1SubOverride, method2Super, methodSub2another, method2SubNotOverride, toString});
        assertEquals(5, methods.size());
        assertTrue(methods.contains(method2Super));
        assertTrue(methods.contains(method2SubOverride));
        assertTrue(methods.contains(toString));
        assertTrue(methods.contains(methodSubtoString));
        assertTrue(methods.contains(methodSub2toString));
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
        
        public int testMethod5(Integer[] arg)
        {
            method5Called = true;
            return arg[0].intValue();
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
        
        public int testMethod5(Integer[] arg)
        {
            return 0;
        }
    }
    
    public static interface TestInterface
    {
        public Integer testInterface();
    }
    
    public static class TestSuper
    {
        String testMethod1()
        {
            return null;
        }
        
        public void testMethod2(int[] param, String param2)
        {
            
        }
        
        private void testMethod4()
        {
            
        }
    }
    
    public static class TestSub extends TestSuper implements TestInterface
    {
        public Integer testInterface()
        {
            return null;
        }

        public String testMethod1(String param)
        {
            return super.testMethod1();
        }

        protected String testMethod1()
        {
            return super.testMethod1();
        }
        
        public void testMethod2(int[] param)
        {
            super.testMethod2(param, null);
        }

        public void testMethod2(int[] param, String param2)
        {
            super.testMethod2(param, param2);
        }
        
        public static void testMethod3(String param)
        {
            
        }
        
        private void testMethod4()
        {
            
        }
        
        public String toString()
        {
            return super.toString();
        }
    }
    
    public static class TestSub2 extends TestSub
    {
        public String toString()
        {
            return super.toString();
        }
        
        public static void anotherStaticMethod()
        {
            
        }
        
        protected void anotherProtectedMethod()
        {
            
        }

        public void anotherMethod()
        {
            
        }
    }
    
    public class TestSub3 extends TestSub2
    {
        
    }
}
