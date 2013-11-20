package com.mockrunner.example.servlet;

import java.io.BufferedReader;

import org.jdom.Element;

import com.mockrunner.servlet.BasicServletTestCaseAdapter;

/**
 * Example test for {@link RedirectServlet}.
 * Demonstrates the usage of {@link com.mockrunner.servlet.ServletTestModule} 
 * and {@link com.mockrunner.servlet.BasicServletTestCaseAdapter}.
 * Demonstrates the testing of output data as string as well as parsed
 * HTML data (<code>testServletOutputAsXML</code>).
 * 
 */
public class RedirectServletTest extends BasicServletTestCaseAdapter
{
    protected void setUp() throws Exception
    {
        super.setUp();
        createServlet(RedirectServlet.class);
    }
    
    public void testServletOutput() throws Exception
    {
        addRequestParameter("redirecturl", "http://www.mockrunner.com");
        doPost();
        BufferedReader reader = getOutputAsBufferedReader();
        assertEquals("<html>", reader.readLine().trim());
        assertEquals("<head>", reader.readLine().trim());
        reader.readLine();
        assertEquals("</head>", reader.readLine().trim());
        assertEquals("<body>", reader.readLine().trim());
        reader.readLine();
        assertEquals("</body>", reader.readLine().trim());
        assertEquals("</html>", reader.readLine().trim());
        verifyOutputContains("URL=http://www.mockrunner.com");
    }
    
    public void testServletOutputAsXML() throws Exception
    {
        addRequestParameter("redirecturl", "http://www.mockrunner.com");
        doPost();
        Element root = getOutputAsJDOMDocument().getRootElement();
        assertEquals("html", root.getName());
        Element head = root.getChild("head");
        Element meta = head.getChild("meta");
        assertEquals("refresh", meta.getAttributeValue("http-equiv"));
        assertEquals("0;URL=http://www.mockrunner.com", meta.getAttributeValue("content"));
    }
}
