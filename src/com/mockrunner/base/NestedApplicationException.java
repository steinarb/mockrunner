package com.mockrunner.base;

/**
 * If Mockrunner catches an exception inside application code,
 * it rethrows it as an instance of this class.
 */
public class NestedApplicationException extends RuntimeException
{
    private Throwable nested;
    
    public NestedApplicationException(String message, Throwable nested)
    {
        super(message);
        this.nested = nested;
    }
    
    public NestedApplicationException(Throwable nested)
    {
        this.nested = nested;
    }
    
    /**
     * Returns the nested exception 
     * (which may also be a <code>NestedApplicationException</code>)
     * @return the nested exception
     */
    public Throwable getNested()
    {
        return nested;
    }
    
    /**
     * Returns the root cause, i.e. the first exception that is
     * not an instance of <code>NestedApplicationException</code>.
     * @return the root exception
     */
    public Throwable getRootCause()
    {
        if(!(nested instanceof NestedApplicationException))
        {
            return nested;
        }
        return ((NestedApplicationException)nested).getRootCause();
    }
}
