package com.mockrunner.test.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.mockrunner.util.StringUtil;

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
    
    public void testAppendObjectsAsString()
    {
        List list = new ArrayList();
        list.add("Test");
        list.add("Test");
        list.add(new Integer(1000));
        list.add(null);
        list.add(new Long(3));
        StringBuffer buffer = new StringBuffer();
        StringUtil.appendObjectsAsString(buffer, list);
        assertEquals("Test\nTest\n1000\nnull\n3\n", buffer.toString());
    }
}
