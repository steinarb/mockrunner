package com.mockrunner.test.web;

import com.mockrunner.mock.web.WebMockObjectFactory;
import com.mockrunner.servlet.BasicServletTestCaseAdapter;

public class BasicServletTestCaseAdapterTest extends BasicServletTestCaseAdapter
{
    public void testSetWebFactory()
    {
        WebMockObjectFactory webFactory = new WebMockObjectFactory();
        setWebMockObjectFactory(webFactory);
        assertSame(webFactory, getWebMockObjectFactory());
        setWebMockObjectFactory(null);
        assertNotNull(getWebMockObjectFactory());
        assertNotSame(webFactory, getWebMockObjectFactory());
    }
}
