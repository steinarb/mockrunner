package com.mockrunner.example.servlet;

import javax.servlet.http.HttpServletRequest;

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
    
    protected void setUp() throws Exception
    {
        super.setUp();
        filter = (ImageButtonFilter)createFilter(ImageButtonFilter.class);
    }

    public void testImageButtonParameter()
    {
        addRequestParameter("param", "value");
        addRequestParameter("image.x", "123");
        addRequestParameter("image.y", "456");
        doFilter();
        HttpServletRequest filteredRequest = (HttpServletRequest)getFilteredRequest();
        assertEquals("value", filteredRequest.getParameter("param"));
        assertEquals("123", filteredRequest.getParameter("image"));
        assertNull(filteredRequest.getParameter("image.x"));
        assertNull(filteredRequest.getParameter("image.y"));
    }
}
