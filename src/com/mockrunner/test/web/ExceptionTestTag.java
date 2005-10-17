package com.mockrunner.test.web;

import javax.servlet.jsp.tagext.TryCatchFinally;

public class ExceptionTestTag extends TestBodyTag implements TryCatchFinally
{
    private boolean doCatchCalled = false;
    private boolean doFinallyCalled = false;
    private Throwable caughtException = null;
    private Throwable throwException = null;
    
    public ExceptionTestTag()
    {
        throwException = null;
    }
    
    public ExceptionTestTag(Throwable throwException)
    {
        this.throwException = throwException;
    }

    public Throwable getCaughtException()
    {
        return caughtException;
    }

    public boolean wasDoCatchCalled()
    {
        return doCatchCalled;
    }

    public boolean wasDoFinallyCalled()
    {
        return doFinallyCalled;
    }

    public void doCatch(Throwable exc) throws Throwable
    {
        doCatchCalled = true;
        caughtException = exc;
        if(null != throwException)
        {
            throw throwException;
        }
    }

    public void doFinally()
    {
        doFinallyCalled = true;
        if(null != throwException && throwException instanceof RuntimeException)
        {
            throw ((RuntimeException)throwException);
        }
    }
}
