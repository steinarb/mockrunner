package com.mockrunner.test.web;

import java.io.IOException;
import java.io.StringWriter;

import com.mockrunner.mock.web.MockJspWriter;

import junit.framework.TestCase;

public class MockJspWriterTest extends TestCase
{
    public void testWithDefaultWriter() throws Exception
    {
        MockJspWriter writer = new MockJspWriter();
        writer.print("test1");
        writer.clear();
        writer.print("test2");
        writer.clearBuffer();
        writer.print("test3");
        assertEquals("test3", writer.getOutputAsString());
    }
    
    public void testWithProvidedWriter() throws Exception
    {
        StringWriter providedWriter = new StringWriter();
        MockJspWriter writer = new MockJspWriter(providedWriter);
        writer.print("test1");
        try
        {
            writer.clear();
            fail();
        } 
        catch(IOException exc)
        {
            //should throw exception
        }
        writer.print("test2");
        writer.clearBuffer();
        writer.print("test3");
        assertEquals("", writer.getOutputAsString());
        writer.flush();
        assertEquals("test1test2test3", providedWriter.toString());
    }
}
