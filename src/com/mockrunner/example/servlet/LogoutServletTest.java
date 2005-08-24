package com.mockrunner.example.servlet;

import javax.servlet.http.HttpServletRequest;

import com.mockrunner.servlet.BasicServletTestCaseAdapter;

/**
 * Example test for {@link LogoutServlet}.
 * Demonstrates the usage of {@link com.mockrunner.servlet.ServletTestModule} 
 * and {@link com.mockrunner.servlet.BasicServletTestCaseAdapter}
 * with and without a filter.
 */
public class LogoutServletTest extends BasicServletTestCaseAdapter
{
    protected void setUp() throws Exception
    {
        super.setUp();
        createServlet(LogoutServlet.class);
    }
    
    public void testDoLogout() throws Exception
    {
        addRequestParameter("logout", "true");
        doPost();
        assertFalse(getWebMockObjectFactory().getMockSession().isValid());
    }
    
    public void testDoLogoutWithFilteredImageButton() throws Exception
    {
        addRequestParameter("logout.x", "11");
        addRequestParameter("logout.y", "11");
        doPost();
        assertTrue(getWebMockObjectFactory().getMockSession().isValid());
        createFilter(ImageButtonFilter.class);
        setDoChain(true);
        doPost();
        assertFalse(getWebMockObjectFactory().getMockSession().isValid());
        HttpServletRequest filteredRequest = (HttpServletRequest)getFilteredRequest();
        assertEquals("11", filteredRequest.getParameter("logout"));
        assertNull(filteredRequest.getParameter("logout.x"));
        assertNull(filteredRequest.getParameter("logout.y"));
    }
}
