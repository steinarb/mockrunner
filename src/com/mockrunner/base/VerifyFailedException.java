package com.mockrunner.base;

/**
 * Will be thrown by the <code>verify</code> methods
 * of all test modules.
 */
public class VerifyFailedException extends RuntimeException
{
    public VerifyFailedException()
    {
        super();
    }

    public VerifyFailedException(String message)
    {
        super(message);
    }
}
