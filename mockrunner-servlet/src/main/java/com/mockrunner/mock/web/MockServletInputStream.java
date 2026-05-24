package com.mockrunner.mock.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

/**
 * Mock implementation of <code>ServletInputStream</code>.
 */
public class MockServletInputStream extends ServletInputStream
{
    private ByteArrayInputStream stream;

    public MockServletInputStream(byte[] data)
    {
        stream = new ByteArrayInputStream(data);
    }

    public int read() throws IOException
    {
        return stream.read();
    }

    @Override
    public boolean isFinished() {
        return stream.available() == 0;
    }

    @Override
    public boolean isReady() {
        return stream.available() > 0;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        // No-op
    }
}
