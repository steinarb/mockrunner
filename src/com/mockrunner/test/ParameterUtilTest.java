package com.mockrunner.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

import junit.framework.TestCase;

import com.mockrunner.mock.jdbc.MockArray;
import com.mockrunner.mock.jdbc.MockBlob;
import com.mockrunner.mock.jdbc.MockClob;
import com.mockrunner.mock.jdbc.MockRef;
import com.mockrunner.util.ParameterUtil;
import com.mockrunner.util.StreamUtil;

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
    
    public void testCopyParameter()
    {
        String testString = new String("Test");
        String copyString = (String)ParameterUtil.copyParameter(testString);
        assertTrue(testString == copyString);
        byte[] testArray = new byte[] {1, 2, 3};
        byte[] copyArray = (byte[])ParameterUtil.copyParameter(testArray);
        assertFalse(testArray == copyArray);
        assertTrue(Arrays.equals(testArray, copyArray));
        Reader testReader = new StringReader("Test");
        Reader copyReader = (Reader)ParameterUtil.copyParameter(testReader);
        assertFalse(testReader == copyReader);
        assertTrue(StreamUtil.compareReaders(testReader, copyReader));
        InputStream testStream = new ByteArrayInputStream(new byte[] {1, 2, 3});
        InputStream copyStream = (InputStream)ParameterUtil.copyParameter(testStream);
        assertFalse(testStream == copyStream);
        assertTrue(StreamUtil.compareStreams(testStream, copyStream));
        MockClob testClob = new MockClob("Test");
        MockClob copyClob = (MockClob)ParameterUtil.copyParameter(testClob);
        assertFalse(testClob == copyClob);
        assertTrue(ParameterUtil.compareParameter(testClob, copyClob));
        MockArray testMockArray = new MockArray(new String[] {"", ""});
        MockArray copyMockArray = (MockArray)ParameterUtil.copyParameter(testMockArray);
        assertFalse(testMockArray == copyMockArray);
        assertTrue(ParameterUtil.compareParameter(testMockArray, copyMockArray));
    }
}
