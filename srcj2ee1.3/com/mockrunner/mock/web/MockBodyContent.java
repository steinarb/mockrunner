package com.mockrunner.mock.web;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;

/**
 * Mock implementation of <code>BodyContent</code>.
 */
public class MockBodyContent extends BodyContent
{
    private MockJspWriter body;
    
    public MockBodyContent(JspWriter writer)
    {
        super(writer);
        body = new MockJspWriter();
    }
    
    public String getOutputAsString()
    {
        return getString();
    }

    public String toString()
    {
        return getString();
    }

    public Reader getReader()
    {
        return new StringReader(getString());
    }

    public String getString()
    {
        return body.getOutputAsString();
    }

    public void writeOut(Writer writer) throws IOException
    {
        writer.write(getString());
    }
    
    public void clearBody()
    {
        body = new MockJspWriter();
    }

    public void newLine() throws IOException
    {
        body.newLine();
    }

    public void print(boolean arg0) throws IOException
    {
        body.print(arg0);
    }

    public void print(char arg0) throws IOException
    {
        body.print(arg0);
    }

    public void print(int arg0) throws IOException
    {
        body.print(arg0);
    }

    public void print(long arg0) throws IOException
    {
        body.print(arg0);
    }

    public void print(float arg0) throws IOException
    {
        body.print(arg0);
    }

    public void print(double arg0) throws IOException
    {
        body.print(arg0);
    }

    public void print(char[] arg0) throws IOException
    {
        body.print(arg0);
    }

    public void print(String arg0) throws IOException
    {
        body.print(arg0);
    }

    public void print(Object arg0) throws IOException
    {
        body.print(arg0);
    }

    public void println() throws IOException
    {
        body.println();
    }

    public void println(boolean arg0) throws IOException
    {
        body.println(arg0);
    }

    public void println(char arg0) throws IOException
    {
        body.println(arg0);
    }

    public void println(int arg0) throws IOException
    {
        body.println(arg0);
    }

    public void println(long arg0) throws IOException
    {
        body.println(arg0);
    }

    public void println(float arg0) throws IOException
    {
        body.println(arg0);
    }
    
    public void println(double arg0) throws IOException
    {
        body.println(arg0);
    }

    public void println(char[] arg0) throws IOException
    {
        body.println(arg0);
    }

    public void println(String arg0) throws IOException
    {
        body.println(arg0);
    }

    public void println(Object arg0) throws IOException
    {
        body.println(arg0);
    }

    public void clear() throws IOException
    {
        body.clear();
    }

    public void clearBuffer() throws IOException
    {
        body.clearBuffer();
    }

    public void close() throws IOException
    {
        body.close();
    }

    public int getRemaining()
    {
        return body.getRemaining();
    }

    public void write(char[] cbuf, int off, int len) throws IOException
    {
        body.write(cbuf, off, len);
    }
}
