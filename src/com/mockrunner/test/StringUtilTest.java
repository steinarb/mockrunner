package com.mockrunner.test;

import com.mockrunner.util.StringUtil;

import junit.framework.TestCase;

public class StringUtilTest extends TestCase
{
    public void testSplit()
    {
        String test = "This;;is;a;;  ;test; String;;";
        String[] tokens = StringUtil.split(test, ";", true);
        assertTrue(tokens.length == 9);
        assertEquals("This", tokens[0]);
        assertEquals(null, tokens[1]);
        assertEquals("is", tokens[2]);
        assertEquals("a", tokens[3]);
        assertEquals(null, tokens[4]);
        assertEquals(null, tokens[5]);
        assertEquals("test", tokens[6]);
        assertEquals("String", tokens[7]);
        assertEquals(null, tokens[8]);
        tokens = StringUtil.split(test, "test", false);
        assertTrue(tokens.length == 2);
        assertEquals("This;;is;a;;  ;", tokens[0]);
        assertEquals("; String;;", tokens[1]);
    }
}
