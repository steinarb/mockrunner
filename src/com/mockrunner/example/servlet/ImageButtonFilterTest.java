package com.mockrunner.example.servlet;

import com.mockrunner.mock.MockHttpServletRequest;
import com.mockrunner.servlet.ServletTestCaseAdapter;

public class ImageButtonFilterTest extends ServletTestCaseAdapter
{
    private ImageButtonFilter filter;
    private MockHttpServletRequest request;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        filter = (ImageButtonFilter)createFilter(ImageButtonFilter.class);
        request = getMockObjectFactory().getMockRequest();
    }

    public void testImageButtonParameter()
    {
        request.setupAddParameter("param", "value");
        request.setupAddParameter("image.x", "123");
        request.setupAddParameter("image.y", "456");
        doFilter();
    }
}
