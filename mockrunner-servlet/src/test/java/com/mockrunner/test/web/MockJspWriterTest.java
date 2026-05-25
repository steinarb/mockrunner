package com.mockrunner.test.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockJspWriter;

class MockJspWriterTest
{
    @Test
    void testWithDefaultWriter() throws Exception
    {
        MockJspWriter writer = new MockJspWriter();
        writer.print("test1");
        writer.clear();
        writer.print("test2");
        writer.clearBuffer();
        writer.print("test3");
        assertEquals("test3", writer.getOutputAsString());
    }

    @Test
    void testWithProvidedWriter() throws Exception
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

    @Test
    void testWithProvidedResponse() throws Exception
    {
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockJspWriter writer = new MockJspWriter(response);
        writer.print("test1");
        assertEquals("test1", writer.getOutputAsString());
        assertEquals("test1", response.getOutputStreamContent());
        response.getWriter().print("test2");
        assertEquals("test1test2", writer.getOutputAsString());
        assertEquals("test1test2", response.getOutputStreamContent());
        writer.clear();
        assertEquals("", writer.getOutputAsString());
        assertEquals("", response.getOutputStreamContent());
        writer.print("test1");
        writer.clearBuffer();
        assertEquals("", writer.getOutputAsString());
        assertEquals("", response.getOutputStreamContent());
    }
}
