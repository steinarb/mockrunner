package com.mockrunner.test.web;

import com.mockrunner.mock.web.WebMockObjectFactory;
import com.mockrunner.tag.BasicTagTestCaseAdapter;

public class BasicTagTestCaseAdapterTest extends BasicTagTestCaseAdapter
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
