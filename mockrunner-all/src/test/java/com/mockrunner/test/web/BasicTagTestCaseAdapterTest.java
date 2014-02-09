package com.mockrunner.test.web;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.mockrunner.mock.web.WebMockObjectFactory;
import com.mockrunner.tag.BasicTagTestCaseAdapter;

public class BasicTagTestCaseAdapterTest extends BasicTagTestCaseAdapter
{
	@Test
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
