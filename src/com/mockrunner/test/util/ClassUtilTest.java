package com.mockrunner.test.util;

import java.io.OutputStream;

import com.mockrunner.util.ClassUtil;

import junit.framework.TestCase;

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
    }
    
    public static class TestClass
    {
    
    }
}
