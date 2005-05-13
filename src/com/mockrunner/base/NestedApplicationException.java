package com.mockrunner.base;

import java.io.PrintWriter;
import java.io.StringWriter;

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
        if(nested == null) return null;
        if(!(nested instanceof NestedApplicationException)) return nested;
        return ((NestedApplicationException)nested).getRootCause();
    }
    
    public String getMessage()
    {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        String message = super.getMessage();
        if(null != message)
        {
            printWriter.println(super.getMessage());
        }
        else
        {
            printWriter.println();
        }
        Throwable cause = getRootCause();
        if(null != cause)
        {
            printWriter.print("Cause: ");
            cause.printStackTrace(printWriter);
        }
        writer.flush();
        return writer.toString();
    }
}
