package com.mockrunner.mock.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;

/**
 * Mock implementation of <code>ServletOutputStream</code>.
 */
public class MockServletOutputStream extends ServletOutputStream
{
    private ByteArrayOutputStream buffer;
    
    public MockServletOutputStream()
    {
        buffer = new ByteArrayOutputStream();
    }
    
    public void write(int value) throws IOException
    {
        buffer.write(value);
    }
    
    public String getContent()
    {
        return buffer.toString();
    }
    
    public byte[] getBinaryContent()
    {
        return buffer.toByteArray();
    }
    
    public void clearContent()
    {
        buffer = new ByteArrayOutputStream();
    }
}
