package com.mockrunner.mock;

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
    private MockJspWriter bodyWriter;
    
    public MockBodyContent(JspWriter writer)
    {
        super(writer);
        bodyWriter = new MockJspWriter();
    }

    public Reader getReader()
    {
        return new StringReader(bodyWriter.getOutputAsString());
    }

    public String getString()
    {
        return bodyWriter.getOutputAsString();
    }

    public void writeOut(Writer writer) throws IOException
    {
        writer.write(getString());
    }
    
    public void clearBody()
    {
        bodyWriter = new MockJspWriter();
    }

    public void newLine() throws IOException
    {
        bodyWriter.newLine();
    }

    public void print(boolean arg0) throws IOException
    {
        bodyWriter.print(arg0);
    }

    public void print(char arg0) throws IOException
    {
        bodyWriter.print(arg0);
    }

    public void print(int arg0) throws IOException
    {
        bodyWriter.print(arg0);
    }

    public void print(long arg0) throws IOException
    {
        bodyWriter.print(arg0);
    }

    public void print(float arg0) throws IOException
    {
        bodyWriter.print(arg0);
    }

    public void print(double arg0) throws IOException
    {
        bodyWriter.print(arg0);
    }

    public void print(char[] arg0) throws IOException
    {
        bodyWriter.print(arg0);
    }

    public void print(String arg0) throws IOException
    {
        bodyWriter.print(arg0);
    }

    public void print(Object arg0) throws IOException
    {
        bodyWriter.print(arg0);
    }

    public void println() throws IOException
    {
        bodyWriter.println();
    }

    public void println(boolean arg0) throws IOException
    {
        bodyWriter.println(arg0);
    }

    public void println(char arg0) throws IOException
    {
        bodyWriter.println(arg0);
    }

    public void println(int arg0) throws IOException
    {
        bodyWriter.println(arg0);
    }

    public void println(long arg0) throws IOException
    {
        bodyWriter.println(arg0);
    }

    public void println(float arg0) throws IOException
    {
        bodyWriter.println(arg0);
    }
    
    public void println(double arg0) throws IOException
    {
        bodyWriter.println(arg0);
    }

    public void println(char[] arg0) throws IOException
    {
        bodyWriter.println(arg0);
    }

    public void println(String arg0) throws IOException
    {
        bodyWriter.println(arg0);
    }

    public void println(Object arg0) throws IOException
    {
        bodyWriter.println(arg0);
    }

    public void clear() throws IOException
    {
        bodyWriter.clear();
    }

    public void clearBuffer() throws IOException
    {
        bodyWriter.clearBuffer();
    }

    public void close() throws IOException
    {
        bodyWriter.close();
    }

    public int getRemaining()
    {
        return bodyWriter.getRemaining();
    }

    public void write(char[] cbuf, int off, int len) throws IOException
    {
        bodyWriter.write(cbuf, off, len);
    }
}
