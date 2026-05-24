package com.mockrunner.example.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

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
    
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        filter = createFilter(ImageButtonFilter.class);
    }

    @Test
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
