package com.mockrunner.example.servlet;

import com.mockrunner.base.BaseTestCase;

/**
 * Example test for {@link LogoutServlet}.
 * There is no <code>ServletTestModule</code> yet but it will be
 * included in future releases.
 */
public class LogoutServletTest extends BaseTestCase
{
    private LogoutServlet servlet;
    
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
