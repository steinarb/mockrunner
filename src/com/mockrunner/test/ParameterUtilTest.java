package com.mockrunner.test;

import java.io.ByteArrayInputStream;
import java.io.StringReader;

import com.mockrunner.util.ParameterUtil;

import junit.framework.TestCase;

public class ParameterUtilTest extends TestCase
{
    public void testCompareParameter()
    {
        assertTrue(ParameterUtil.compareParameter("test", "test"));
        assertFalse(ParameterUtil.compareParameter(new Double(1), new Double(2)));
        assertTrue(ParameterUtil.compareParameter(new byte[] {1, 2, 3}, new byte[] {1, 2, 3}));
        assertFalse(ParameterUtil.compareParameter(new byte[] {1, 2, 4}, new byte[] {1, 2, 3}));
        assertTrue(ParameterUtil.compareParameter(new ByteArrayInputStream(new byte[] {1, 2, 3}), new ByteArrayInputStream(new byte[] {1, 2, 3})));
        assertTrue(ParameterUtil.compareParameter(new StringReader("xyz"), new StringReader("xyz")));
        assertFalse(ParameterUtil.compareParameter(new StringReader("xyz"), new StringReader("")));
        assertFalse(ParameterUtil.compareParameter(new Object(), new Object()));
    }
}
