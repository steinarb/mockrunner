package com.mockrunner.mock.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.jsp.JspWriter;

import com.mockrunner.base.NestedApplicationException;

/**
 * Mock implementation of <code>JspWriter</code>.
 * Collects the output data. Use {@link #getOutputAsString}
 * to get it.
 */
public class MockJspWriter extends JspWriter
{
    private PrintWriter printWriter;
    private StringWriter stringWriter;

    public MockJspWriter()
    {
        super(0, true);
        initWriters();
    }

    public String getOutputAsString()
    {
        try
        {
            flush();
            return stringWriter.toString();
        }
        catch(IOException exc)
        {
            throw new NestedApplicationException(exc);
        }
    }

    public String toString()
    {
        return getOutputAsString();
    }

    public void clear() throws IOException
    {
        initWriters();
    }

    public void clearBuffer() throws IOException
    {
        initWriters();
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

    public void print(boolean arg0) throws IOException
    {
        printWriter.print(arg0);
    }

    public void print(char arg0) throws IOException
    {
        printWriter.print(arg0);
    }

    public void print(char[] arg0) throws IOException
    {
        printWriter.print(arg0);
    }

    public void print(double arg0) throws IOException
    {
        printWriter.print(arg0);
    }

    public void print(float arg0) throws IOException
    {
        printWriter.print(arg0);
    }

    public void print(int arg0) throws IOException
    {
        printWriter.print(arg0);
    }

    public void print(long arg0) throws IOException
    {
        printWriter.print(arg0);
    }

    public void print(Object arg0) throws IOException
    {
        printWriter.print(arg0);
    }

    public void print(String arg0) throws IOException
    {
        printWriter.print(arg0);
    }

    public void println() throws IOException
    {
        printWriter.println();
    }

    public void println(boolean arg0) throws IOException
    {
        printWriter.println(arg0);
    }

    public void println(char arg0) throws IOException
    {
        printWriter.println(arg0);
    }

    public void println(char[] arg0) throws IOException
    {
        printWriter.println(arg0);
    }

    public void println(double arg0) throws IOException
    {
        printWriter.println(arg0);
    }

    public void println(float arg0) throws IOException
    {
        printWriter.println(arg0);
    }

    public void println(int arg0) throws IOException
    {
        printWriter.println(arg0);
    }

    public void println(long arg0) throws IOException
    {
        printWriter.println(arg0);
    }

    public void println(Object arg0) throws IOException
    {
        printWriter.println(arg0);
    }

    public void println(String arg0) throws IOException
    {
        printWriter.println(arg0);
    }

    public void write(char[] cbuf, int off, int len) throws IOException
    {
        printWriter.write(cbuf, off, len);
    }

    private void initWriters()
    {
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }
}
