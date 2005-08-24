package com.mockrunner.example.servlet;

import javax.servlet.http.HttpServletRequest;

import com.mockrunner.servlet.BasicServletTestCaseAdapter;

/**
 * Example test for the {@link ImageButtonFilter}.
 * Demonstrates the usage of the filter test features in
 * {@link com.mockrunner.servlet.ServletTestModule} 
 * and {@link com.mockrunner.servlet.BasicServletTestCaseAdapter}.
 */
public class ImageButtonFilterTest extends BasicServletTestCaseAdapter
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
