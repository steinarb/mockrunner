package com.mockrunner.example.servlet;

import javax.servlet.http.HttpServletRequest;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.servlet.ServletTestCaseAdapter;

/**
 * Example test for the {@link ImageButtonFilter}.
 * Demonstrates the usage of the filter test features in
 * {@link com.mockrunner.servlet.ServletTestModule} 
 * resp. {@link com.mockrunner.servlet.ServletTestCaseAdapter}.
 */
public class ImageButtonFilterTest extends ServletTestCaseAdapter
{
    private ImageButtonFilter filter;
    private MockHttpServletRequest request;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        filter = (ImageButtonFilter)createFilter(ImageButtonFilter.class);
        request = getWebMockObjectFactory().getMockRequest();
    }

    public void testImageButtonParameter()
    {
        request.setupAddParameter("param", "value");
        request.setupAddParameter("image.x", "123");
        request.setupAddParameter("image.y", "456");
        doFilter();
        HttpServletRequest filteredRequest = (HttpServletRequest)getFilteredRequest();
        assertEquals("value", filteredRequest.getParameter("param"));
        assertEquals("123", filteredRequest.getParameter("image"));
        assertNull(filteredRequest.getParameter("image.x"));
        assertNull(filteredRequest.getParameter("image.y"));
    }
}
