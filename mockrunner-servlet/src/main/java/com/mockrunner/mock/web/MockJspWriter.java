package com.mockrunner.mock.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;

import com.mockrunner.base.NestedApplicationException;

/**
 * Mock implementation of <code>JspWriter</code>.
 * Collects the output data.
 */
public class MockJspWriter extends JspWriter
{
    private PrintWriter printWriter;
    private Writer writer;
    private HttpServletResponse response;
    private boolean providedWriter;

    public MockJspWriter()
    {
        super(0, true);
        this.writer = new StringWriter();
        printWriter = new PrintWriter(writer);
        response = null;
        providedWriter = false;
    }
    
    public MockJspWriter(HttpServletResponse response) throws IOException
    {
        super(0, true);
        this.writer = response.getWriter();
        this.response = response;
        printWriter = new PrintWriter(writer);
        providedWriter = false;
    }
    
    public MockJspWriter(Writer writer)
    {
        super(0, true);
        this.writer = writer;
        printWriter = new PrintWriter(writer);
        response = null;
        providedWriter = true;
    }

    /**
     * Returns the output.
     * @return the output
     */
    public String getOutputAsString()
    {
        try
        {
            flush();
            if(!providedWriter)
            {
                if(null == response)
                {
                    return writer.toString();
                }
                if(response instanceof MockHttpServletResponse)
                {
                    return ((MockHttpServletResponse)response).getOutputStreamContent();
                }
            }
            return "";
        }
        catch(IOException exc)
        {
            throw new NestedApplicationException(exc);
        }
    }

    /**
     * Delegates to {@link #getOutputAsString}
     */
    public String toString()
    {
        return getOutputAsString();
    }

    /**
     * Clears the output. This method throws an <code>IOException</code>,
     * if a <code>Writer</code> was provided according to spec.
     */
    public void clear() throws IOException
    {
        if(!providedWriter)
        {
            clearWriter();
        }
        else
        {
            throw new IOException("Illegal call if writer is provided.");
        }
    }

    /**
     * Clears the output. This method does nothing,
     * if a <code>Writer</code> was provided according to spec.
     */
    public void clearBuffer() throws IOException
    {
        if(!providedWriter)
        {
            clearWriter();
        }
    }
    
    private void clearWriter() throws IOException
    {
        if(null == response)
        { 
            this.writer = new StringWriter();
            printWriter = new PrintWriter(writer);
        }
        else
        {
            flush();
            response.resetBuffer();
        }
    }

    public void close() throws IOException
    {
        flush();
        printWriter.close();
    }
    
    public int getRemaining()
    {
        return 0;
    }

    public void flush() throws IOException
    {
        printWriter.flush();
    }

    public void newLine() throws IOException
    {
        print(System.getProperty("line.separator"));
    }

    public void print(boolean value) throws IOException
    {
        printWriter.print(value);
    }

    public void print(char value) throws IOException
    {
        printWriter.print(value);
    }

    public void print(char[] value) throws IOException
    {
        printWriter.print(value);
    }

    public void print(double value) throws IOException
    {
        printWriter.print(value);
    }

    public void print(float value) throws IOException
    {
        printWriter.print(value);
    }

    public void print(int value) throws IOException
    {
        printWriter.print(value);
    }

    public void print(long value) throws IOException
    {
        printWriter.print(value);
    }

    public void print(Object value) throws IOException
    {
        printWriter.print(value);
    }

    public void print(String value) throws IOException
    {
        printWriter.print(value);
    }

    public void println() throws IOException
    {
        printWriter.println();
    }

    public void println(boolean value) throws IOException
    {
        printWriter.println(value);
    }

    public void println(char value) throws IOException
    {
        printWriter.println(value);
    }

    public void println(char[] value) throws IOException
    {
        printWriter.println(value);
    }

    public void println(double value) throws IOException
    {
        printWriter.println(value);
    }

    public void println(float value) throws IOException
    {
        printWriter.println(value);
    }

    public void println(int value) throws IOException
    {
        printWriter.println(value);
    }

    public void println(long value) throws IOException
    {
        printWriter.println(value);
    }

    public void println(Object value) throws IOException
    {
        printWriter.println(value);
    }

    public void println(String value) throws IOException
    {
        printWriter.println(value);
    }

    public void write(char[] cbuf, int off, int len) throws IOException
    {
        printWriter.write(cbuf, off, len);
    }
}
