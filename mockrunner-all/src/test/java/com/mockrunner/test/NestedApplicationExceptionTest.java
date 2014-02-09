package com.mockrunner.test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.mockrunner.base.NestedApplicationException;

public class NestedApplicationExceptionTest
{
    private NestedApplicationException nested;
    private Exception exception;
    
    @Before
    public void setUp() throws Exception
    {
        exception = new Exception("TestException");
        nested = new NestedApplicationException(new NestedApplicationException(exception));
    }

    @Test
    public void testGetNested()
    {
        assertNotSame(exception, nested.getNested());
        assertTrue(nested.getNested() instanceof NestedApplicationException);
    }
    
    @Test
    public void testGetRootCause()
    {
        assertSame(exception, nested.getRootCause());
    }
    
    @Test
    public void testGetMessage()
    {
        assertTrue(nested.getMessage().indexOf("TestException") != -1);
    }
}
