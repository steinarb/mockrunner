package com.mockrunner.example.test;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.example.LogoutServlet;

/**
 * Example test for {@link com.mockrunner.example.LogoutServlet}.
 * There is no <code>ServletTestModule</code> yet but it will be
 * included in future releases.
 */
public class LogoutServletTest extends BaseTestCase
{
    private LogoutServlet servlet;
    
    public LogoutServletTest(String arg0)
    {
        super(arg0);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        servlet = new LogoutServlet();
        servlet.init(getMockObjectFactory().getMockServletConfig());
    }
    
    public void testDoNoLogout() throws Exception
    {
        getMockObjectFactory().getMockRequest().setupAddParameter("logout", "false");
        servlet.doPost(getMockObjectFactory().getMockRequest(), getMockObjectFactory().getMockResponse());
        assertTrue(getMockObjectFactory().getMockSession().isValid());
    }
    
    public void testDoLogout() throws Exception
    {
        getMockObjectFactory().getMockRequest().setupAddParameter("logout", "true");
        servlet.doPost(getMockObjectFactory().getMockRequest(), getMockObjectFactory().getMockResponse());
        assertFalse(getMockObjectFactory().getMockSession().isValid());
    }
}
