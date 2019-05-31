package com.mockrunner.test.util;

import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

//import com.mockrunner.jms.JMSTestModule;
import com.mockrunner.util.common.ClassUtil;

public class ClassUtilTest extends TestCase
{
    public void testGetPackageName()
    {
        assertEquals("com.mockrunner.test.util", ClassUtil.getPackageName(this.getClass()));
        assertEquals("java.lang", ClassUtil.getPackageName("".getClass()));
        assertEquals("java.io", ClassUtil.getPackageName(OutputStream.class));
        assertEquals("com.mockrunner.test.util", ClassUtil.getPackageName(TestClass.class));
    }
    
    public void testGetClassName()
    {
        assertEquals("ClassUtilTest", ClassUtil.getClassName(this.getClass()));
        assertEquals("String", ClassUtil.getClassName("".getClass()));
        assertEquals("OutputStream", ClassUtil.getClassName(OutputStream.class));
        assertEquals("ClassUtilTest$TestClass", ClassUtil.getClassName(TestClass.class));
        assertEquals("OutputStream[][]", ClassUtil.getClassName(OutputStream[][].class));
        assertEquals("int[]", ClassUtil.getClassName(int[].class));
    }
    
    public void testIsKeyword()
    {
        assertTrue(ClassUtil.isKeyword("void"));
        assertTrue(ClassUtil.isKeyword("volatile"));
        assertFalse(ClassUtil.isKeyword("Boolean"));
        assertFalse(ClassUtil.isKeyword("xyz"));
    }
    
    public void testGetArgumentName()
    {
        assertEquals("voidValue", ClassUtil.getArgumentName(Void.class));
        assertEquals("booleanValue", ClassUtil.getArgumentName(Boolean.TYPE));
        assertEquals("string", ClassUtil.getArgumentName(String.class));
        assertEquals("classUtilTest", ClassUtil.getArgumentName(ClassUtilTest.class));
//        assertEquals("jmsTestModule", ClassUtil.getArgumentName(JMSTestModule.class));
        assertEquals("url", ClassUtil.getArgumentName(URL.class));
        assertEquals("urls", ClassUtil.getArgumentName(URL[].class));
//        assertEquals("jmsTestModules", ClassUtil.getArgumentName(JMSTestModule[].class));
        assertEquals("strings", ClassUtil.getArgumentName(String[].class));
        assertEquals("intValues", ClassUtil.getArgumentName(int[][][].class));
        assertEquals("doubleValues", ClassUtil.getArgumentName(Double[][].class));
    }
    
    public void testGetImplementedInterfaces()
    {
        Class[] interfaces = ClassUtil.getImplementedInterfaces(TestInterface.class);
        assertEquals(0, interfaces.length);
        interfaces = ClassUtil.getImplementedInterfaces(TestClass.class);
        assertEquals(0, interfaces.length);
        interfaces = ClassUtil.getImplementedInterfaces(Sub2.class);
        assertEquals(3, interfaces.length);
        Set interfaceSet = new HashSet();
        interfaceSet.add(interfaces[0]);
        interfaceSet.add(interfaces[1]);
        interfaceSet.add(interfaces[2]);
        assertTrue(interfaceSet.contains(TestInterface.class));
        assertTrue(interfaceSet.contains(Serializable.class));
        assertTrue(interfaceSet.contains(Cloneable.class));
    }
    
    public void testGetInheritanceHierarchy()
    {
        Class[] classes = ClassUtil.getInheritanceHierarchy(Object.class);
        assertTrue(Arrays.equals(classes, new Class[] { Object.class }));
        classes = ClassUtil.getInheritanceHierarchy(Super.class);
        assertTrue(Arrays.equals(classes, new Class[] { Object.class, Super.class }));
        classes = ClassUtil.getInheritanceHierarchy(Sub2.class);
        assertTrue(Arrays.equals(classes, new Class[] { Object.class, Super.class, Sub1.class, Sub2.class }));
    }
    
    public interface TestInterface
    {
        
    }
    
    public static class TestClass
    {
    
    }
    
    public static class Super implements Serializable, Cloneable
    {
        
    }
    
    public static class Sub1 extends Super implements Serializable
    {
        
    }
    
    public static class Sub2 extends Sub1 implements Cloneable, TestInterface
    {
        
    }
}
