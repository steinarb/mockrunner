package com.mockrunner.test.jdbc;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import com.mockrunner.mock.jdbc.MockBlob;

import junit.framework.TestCase;

public class MockBlobTest extends TestCase
{
    private MockBlob blob;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        blob = new MockBlob(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11});
    }

    public void testGetData() throws Exception
    {
        byte[] data = blob.getBytes(1, 3);
        assertTrue(Arrays.equals(data, new byte[] {1, 2, 3}));
        data = blob.getBytes(1, 12);
        assertTrue(Arrays.equals(data, new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 0}));
        InputStream stream = blob.getBinaryStream();
        int nextByte;
        byte count = 1;
        while(-1 != (nextByte = stream.read()))
        {
            assertEquals((byte)nextByte, count);
            count++;
        }
        assertTrue(new MockBlob(new byte[] {}).getBytes(1, 0).length == 0);
    }
    
    public void testPosition() throws Exception
    {
        byte[] searchBytes = new byte[] {8, 9};
        assertEquals(8, blob.position(searchBytes, 1));
        searchBytes = new byte[] {12};
        assertEquals(-1, blob.position(searchBytes, 1));
        MockBlob searchBlob = new MockBlob(new byte[] {4, 5, 6});
        assertEquals(4, blob.position(searchBlob, 1));
    }
    
    public void testUpdateData() throws Exception
    {
        blob.setBytes(11, new byte[] {12});
        byte[] data = blob.getBytes(1, 11);
        assertEquals(12, data[10]);
        blob.setBytes(14, new byte[] {12});
        assertTrue(blob.length() == 14);
        data = blob.getBytes(1, 14);
        assertEquals(12, data[10]);
        assertEquals(0, data[11]);
        assertEquals(0, data[12]);
        assertEquals(12, data[13]);
        OutputStream stream = blob.setBinaryStream(2);
        stream.write(3);
        data = blob.getBytes(1, 14);
        assertEquals(1, data[0]);
        assertEquals(3, data[1]);
        assertEquals(3, data[2]);
        stream.write(new byte[] {4, 5, 6});
        data = blob.getBytes(1, 14);
        assertEquals(1, data[0]);
        assertEquals(3, data[1]);
        assertEquals(4, data[2]);
        assertEquals(5, data[3]);
        assertEquals(6, data[4]);
        stream = blob.setBinaryStream(1);
        OutputStream anotherAtream = blob.setBinaryStream(16);
        stream.write(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
        anotherAtream.write(16);
        data = blob.getBytes(1, 16);
        assertTrue(Arrays.equals(data, new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16}));
    }
    
    public void testEquals() throws Exception
    {
        MockBlob blob1 = new MockBlob(new byte[] {1, 2, 3});
        assertFalse(blob1.equals(null));
        assertTrue(blob1.equals(blob1));
        MockBlob blob2 = new MockBlob(new byte[] {1, 2, 3, 4});
        assertFalse(blob1.equals(blob2));
        assertFalse(blob2.equals(blob1));
        blob2 = new MockBlob(new byte[] {1, 2, 3});
        assertTrue(blob1.equals(blob2));
        assertTrue(blob2.equals(blob1));
        assertEquals(blob1.hashCode(), blob2.hashCode());
    }
    
    public void testClone() throws Exception
    {
        MockBlob cloneBlob = (MockBlob)blob.clone();
        assertTrue(blob.length() == cloneBlob.length());
        blob.setBytes(1, new byte[] {2});
        byte[] data = blob.getBytes(1, 11);
        assertTrue(Arrays.equals(data, new byte[] {2, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}));
        data = cloneBlob.getBytes(1, 11);
        assertTrue(Arrays.equals(data, new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}));
    }
}
