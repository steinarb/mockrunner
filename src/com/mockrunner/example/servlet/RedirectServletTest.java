package com.mockrunner.example.servlet;

import java.io.BufferedReader;

import com.mockrunner.servlet.ServletTestCaseAdapter;

/**
 * Example test for {@link RedirectServlet}.
 */
public class RedirectServletTest extends ServletTestCaseAdapter
{
    protected void setUp() throws Exception
    {
        super.setUp();
        createServlet(RedirectServlet.class);
    }
    
    public void testServletOutput() throws Exception
    {
        getMockObjectFactory().getMockRequest().setupAddParameter("redirecturl", "http://www.mockrunner.com");
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
}
