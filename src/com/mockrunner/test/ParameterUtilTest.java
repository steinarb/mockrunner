package com.mockrunner.test;

import java.io.ByteArrayInputStream;
import java.io.StringReader;

import junit.framework.TestCase;

import com.mockrunner.mock.jdbc.MockArray;
import com.mockrunner.mock.jdbc.MockBlob;
import com.mockrunner.mock.jdbc.MockClob;
import com.mockrunner.mock.jdbc.MockRef;
import com.mockrunner.util.ParameterUtil;

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
        assertTrue(ParameterUtil.compareParameter(new MockRef("test"), new MockRef("test")));
        assertFalse(ParameterUtil.compareParameter(new MockRef(""), new MockRef("xyz")));
        assertTrue(ParameterUtil.compareParameter(new MockArray(new String[] {"", ""}), new MockArray(new String[] {"", ""})));
        assertFalse(ParameterUtil.compareParameter(new MockArray(new String[] {"", ""}), new MockArray(new int[] {1, 1})));
        assertTrue(ParameterUtil.compareParameter(new MockArray(new Object[] {"1", new Integer(2)}), new MockArray(new Object[] {"1", new Integer(2)})));
        assertFalse(ParameterUtil.compareParameter(new MockArray(new Object[] {new Integer(2), "1"}), new MockArray(new Object[] {"1", new Integer(2)})));
        assertTrue(ParameterUtil.compareParameter(new MockBlob(new byte[] {1, 1, 1}), new MockBlob(new byte[] {1, 1, 1})));
        assertFalse(ParameterUtil.compareParameter(new MockBlob(new byte[] {1, 1, 1}), new MockBlob(new byte[] {1, 1})));
        assertTrue(ParameterUtil.compareParameter(new MockBlob(new byte[] {}), new MockBlob(new byte[] {})));
        assertFalse(ParameterUtil.compareParameter(new MockBlob(new byte[] {}), new MockBlob(new byte[] {2})));
        assertFalse(ParameterUtil.compareParameter(new MockBlob(new byte[] {1, 2, 3}), new MockBlob(new byte[] {1, 2, 4})));
        assertTrue(ParameterUtil.compareParameter(new MockClob("123"), new MockClob("123")));
        assertTrue(ParameterUtil.compareParameter(new MockClob(""), new MockClob("")));
        assertFalse(ParameterUtil.compareParameter(new MockClob("1"), new MockClob("")));
    }
    
    public void testCreateHashCodeForParameter()
    {
        assertTrue(ParameterUtil.createHashCodeForParameter("Test") == ParameterUtil.createHashCodeForParameter("Test"));
        assertTrue(ParameterUtil.createHashCodeForParameter(new Long(2)) == ParameterUtil.createHashCodeForParameter(new Long(2)));
        assertTrue(ParameterUtil.createHashCodeForParameter(new Integer(-1)) == ParameterUtil.createHashCodeForParameter(new Integer(-1)));
        assertTrue(ParameterUtil.createHashCodeForParameter(new byte[] {1, 2, 3}) == ParameterUtil.createHashCodeForParameter(new byte[] {1, 2, 3}));
        assertTrue(ParameterUtil.createHashCodeForParameter(new MockClob("123")) == ParameterUtil.createHashCodeForParameter(new MockClob("123")));
        assertTrue(ParameterUtil.createHashCodeForParameter(new MockBlob(new byte[] {1})) == ParameterUtil.createHashCodeForParameter(new MockBlob(new byte[] {1})));
    }
}
