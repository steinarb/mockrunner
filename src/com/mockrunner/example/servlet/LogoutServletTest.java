package com.mockrunner.example.servlet;

import com.mockrunner.servlet.ServletTestCaseAdapter;

/**
 * Example test for {@link LogoutServlet}.
 * Demonstrates the usage of {@link com.mockrunner.servlet.ServletTestModule} 
 * resp. {@link com.mockrunner.servlet.ServletTestCaseAdapter}
 * with and without a filter.
 */
public class LogoutServletTest extends ServletTestCaseAdapter
{
    protected void setUp() throws Exception
    {
        super.setUp();
        createServlet(LogoutServlet.class);
    }
    
    public void testDoLogout() throws Exception
    {
        getMockObjectFactory().getMockRequest().setupAddParameter("logout", "true");
        doPost();
        assertFalse(getMockObjectFactory().getMockSession().isValid());
    }
    
    public void testDoLogoutWithFilteredImageButton() throws Exception
    {
        getMockObjectFactory().getMockRequest().setupAddParameter("logout.x", "11");
        getMockObjectFactory().getMockRequest().setupAddParameter("logout.y", "11");
        doPost();
        assertTrue(getMockObjectFactory().getMockSession().isValid());
        createFilter(ImageButtonFilter.class);
        setDoChain(true);
        doPost();
        assertFalse(getMockObjectFactory().getMockSession().isValid());
    }
}
