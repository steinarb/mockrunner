package com.mockrunner.test;

import com.mockrunner.util.StringUtil;

import junit.framework.TestCase;

public class StringUtilTest extends TestCase
{
    public void testSpilt()
    {
        String test = "This;;is;a;;;test;String;;";
        String[] tokens = StringUtil.split(test, ";");
        assertTrue(tokens.length == 9);
        assertEquals("This", tokens[0]);
        assertEquals("", tokens[1]);
        assertEquals("is", tokens[2]);
        assertEquals("a", tokens[3]);
        assertEquals("", tokens[4]);
        assertEquals("", tokens[5]);
        assertEquals("test", tokens[6]);
        assertEquals("String", tokens[7]);
        assertEquals("", tokens[8]);
        tokens = StringUtil.split(test, "test");
        assertTrue(tokens.length == 2);
        assertEquals("This;;is;a;;;", tokens[0]);
        assertEquals(";String;;", tokens[1]);
    }
}
