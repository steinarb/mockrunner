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
    private String body;
    
    public MockBodyContent(JspWriter writer)
    {
        super(writer);
        clearBody();
    }
    
    public void setBody(String body)
    {
        this.body = body;
    }

    public Reader getReader()
    {
        return new StringReader(getString());
    }

    public String getString()
    {
        return body;
    }

    public void writeOut(Writer writer) throws IOException
    {
        writer.write(getString());
    }
    
    public void clearBody()
    {
        body = "";
    }

    public void newLine() throws IOException
    {
        
    }

    public void print(boolean arg0) throws IOException
    {
        
    }

    public void print(char arg0) throws IOException
    {
        
    }

    public void print(int arg0) throws IOException
    {
        
    }

    public void print(long arg0) throws IOException
    {
        
    }

    public void print(float arg0) throws IOException
    {
        
    }

    public void print(double arg0) throws IOException
    {
        
    }

    public void print(char[] arg0) throws IOException
    {
        
    }

    public void print(String arg0) throws IOException
    {
        
    }

    public void print(Object arg0) throws IOException
    {
        
    }

    public void println() throws IOException
    {
        
    }

    public void println(boolean arg0) throws IOException
    {
        
    }

    public void println(char arg0) throws IOException
    {
        
    }

    public void println(int arg0) throws IOException
    {
        
    }

    public void println(long arg0) throws IOException
    {
        
    }

    public void println(float arg0) throws IOException
    {
        
    }
    
    public void println(double arg0) throws IOException
    {
        
    }

    public void println(char[] arg0) throws IOException
    {
        
    }

    public void println(String arg0) throws IOException
    {
        
    }

    public void println(Object arg0) throws IOException
    {
        
    }

    public void clear() throws IOException
    {
        
    }

    public void clearBuffer() throws IOException
    {
        
    }

    public void close() throws IOException
    {
        
    }

    public int getRemaining()
    {
        return 0;
    }

    public void write(char[] cbuf, int off, int len) throws IOException
    {
        
    }
}
