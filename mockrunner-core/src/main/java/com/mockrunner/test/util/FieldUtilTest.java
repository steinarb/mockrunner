package com.mockrunner.test.util;

import java.lang.reflect.Field;
import java.util.Arrays;

import junit.framework.TestCase;

import com.mockrunner.util.common.FieldUtil;

public class FieldUtilTest extends TestCase
{
    public void testGetFieldsSortedByInheritanceHierarchy() throws Exception
    {
        Field[][] fields = FieldUtil.getFieldsSortedByInheritanceHierarchy(TestSub2.class);
        assertEquals(4, fields.length);
        assertEquals(2, fields[1].length);
        assertEquals(1, fields[2].length);
        assertEquals(1, fields[3].length);
        Field testSuperField1 = TestSuper.class.getDeclaredField("testSuperField1");
        Field testSuperField2 = TestSuper.class.getDeclaredField("testSuperField2");
        Field testSubField2 = TestSub.class.getDeclaredField("testSubField2");
        Field testSub2Field2 = TestSub2.class.getDeclaredField("testSub2Field2");
        assertTrue(Arrays.asList(fields[1]).contains(testSuperField1));
        assertTrue(Arrays.asList(fields[1]).contains(testSuperField2));
        assertTrue(Arrays.asList(fields[2]).contains(testSubField2));
        assertTrue(Arrays.asList(fields[3]).contains(testSub2Field2));
    }
    
    public static class TestSuper
    {
       private int testSuperField1;
       String testSuperField2;
    }
    
    public static class TestSub extends TestSuper
    {
        private static int testSubField1Static;
        protected FieldUtilTest testSubField2;
    }
    
    public static class TestSub2 extends TestSub
    {
        public static TestSuper testSub2Field1Static;
        public Class[] testSub2Field2;
    }
}
