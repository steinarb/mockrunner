package com.mockrunner.mock.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Mock implementation of <code>HttpServletResponse</code>.
 */
public class MockHttpServletResponse implements HttpServletResponse
{
    private PrintWriter writer;
    private MockServletOutputStream outputStream;
    private Map headers;
    
    public MockHttpServletResponse()
    {
        outputStream = new MockServletOutputStream();
        writer = new PrintWriter(outputStream);
		headers = new HashMap();
    }
    
    public String encodeURL(String url)
    {
        return url;
    }

    public String encodeRedirectUrl(String url)
    {
        return url;
    }

    public String encodeRedirectURL(String url)
    {
        return url;
    }

    public String encodeUrl(String url)
    {
        return url;
    }
    
    public PrintWriter getWriter() throws IOException
    {
        return writer;
    }
    
    public ServletOutputStream getOutputStream() throws IOException
    {
        return outputStream;
    }
    
    public String getOutputStreamContent() 
    {
        return outputStream.getContent();
    }
    
    public void addCookie(Cookie arg0)
    {
        // TODO Auto-generated method stub

    }

    public void addDateHeader(String arg0, long arg1)
    {
        // TODO Auto-generated method stub

    }

    public void addHeader(String key, String value)
    {
		List valueList = (List)headers.get(key);
		if(null == valueList)
		{
			valueList = new ArrayList();
			headers.put(key, valueList);
		}
		valueList.add(value);
    }

    public void addIntHeader(String arg0, int arg1)
    {
        // TODO Auto-generated method stub

    }

    public boolean containsHeader(String key)
    {
        return headers.containsKey(key);
    }

    public void sendError(int arg0, String arg1) throws IOException
    {
        // TODO Auto-generated method stub

    }

    public void sendError(int arg0) throws IOException
    {
        // TODO Auto-generated method stub

    }

    public void sendRedirect(String arg0) throws IOException
    {
        // TODO Auto-generated method stub

    }

    public void setDateHeader(String arg0, long arg1)
    {
        // TODO Auto-generated method stub

    }

    public void setHeader(String arg0, String arg1)
    {
        // TODO Auto-generated method stub

    }

    public void setIntHeader(String arg0, int arg1)
    {
        // TODO Auto-generated method stub

    }

    public void setStatus(int arg0, String arg1)
    {
        // TODO Auto-generated method stub

    }

    public void setStatus(int arg0)
    {
        // TODO Auto-generated method stub

    }

    public void flushBuffer() throws IOException
    {
		writer.flush();
		outputStream.flush();
    }

    public int getBufferSize()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getCharacterEncoding()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Locale getLocale()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    public boolean isCommitted()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void reset()
    {
		headers.clear();
		resetBuffer();
    }

    public void resetBuffer()
    {
		outputStream.clearContent();
    }

    public void setBufferSize(int arg0)
    {
        // TODO Auto-generated method stub

    }

    public void setContentLength(int arg0)
    {
        // TODO Auto-generated method stub

    }

    public void setContentType(String arg0)
    {
        // TODO Auto-generated method stub

    }

    public void setLocale(Locale arg0)
    {
        // TODO Auto-generated method stub

    }
}
