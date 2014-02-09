package com.mockrunner.example.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import com.mockrunner.servlet.BasicServletTestCaseAdapter;

/**
 * Example test for {@link LogoutServlet}.
 * Demonstrates the usage of {@link com.mockrunner.servlet.ServletTestModule} 
 * and {@link com.mockrunner.servlet.BasicServletTestCaseAdapter}
 * with and without a filter.
 */
public class LogoutServletTest extends BasicServletTestCaseAdapter
{
	@Before
    public void setUp() throws Exception
    {
        super.setUp();
        createServlet(LogoutServlet.class);
    }
    
	@Test
    public void testDoLogout() throws Exception
    {
        addRequestParameter("logout", "true");
        doPost();
        assertFalse(getWebMockObjectFactory().getMockSession().isValid());
    }
    
	@Test
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
