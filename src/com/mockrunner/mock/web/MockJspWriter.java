package com.mockrunner.mock.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.jsp.JspWriter;

import com.mockrunner.base.NestedApplicationException;

/**
 * Mock implementation of <code>JspWriter</code>.
 * Collects the output data. If you provide a <code>Writer</code>
 * in the constructor, the output data is written to this
 * provided <code>Writer</code>. The method {@link #getOutputAsString}
 * returns an empty string in this case. Otherwise it returns the
 * collected data.
 */
public class MockJspWriter extends JspWriter
{
    private PrintWriter printWriter;
    private Writer writer;
    private boolean providedWriter;

    public MockJspWriter()
    {
        super(0, true);
        this.writer = new StringWriter();
        initWriter();
        providedWriter = false;
    }
    
    public MockJspWriter(Writer writer)
    {
        super(0, true);
        this.writer = writer;
        initWriter();
        providedWriter = true;
    }

    /**
     * Returns the output or an empty string, if
     * an output <code>Writer</code> was provided
     * in the constructor.
     * @return the output or an empty string
     */
    public String getOutputAsString()
    {
        try
        {
            flush();
            if(!providedWriter)
            {
                return ((StringWriter)writer).toString();
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
            this.writer = new StringWriter();
            initWriter();
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
            this.writer = new StringWriter();
            initWriter();
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

    private void initWriter()
    {
        printWriter = new PrintWriter(writer);
    }
}
