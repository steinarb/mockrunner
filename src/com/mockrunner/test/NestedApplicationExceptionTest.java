package com.mockrunner.test;

import junit.framework.TestCase;

import com.mockrunner.base.NestedApplicationException;

public class NestedApplicationExceptionTest extends TestCase
{
    private NestedApplicationException nested;
    private Exception exception;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        exception = new Exception("TestException");
        nested = new NestedApplicationException(new NestedApplicationException(exception));
    }
    
    public void testGetNested()
    {
        assertNotSame(exception, nested.getNested());
        assertTrue(nested.getNested() instanceof NestedApplicationException);
    }
    
    public void testGetRootCause()
    {
        assertSame(exception, nested.getRootCause());
    }
    
    public void testGetMessage()
    {
        assertTrue(nested.getMessage().indexOf("TestException") != -1);
    }
}
