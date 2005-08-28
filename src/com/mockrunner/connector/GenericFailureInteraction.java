package com.mockrunner.connector;

import javax.resource.ResourceException;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;

/**
 * This interaction implementor can be used to simulate failures. By default
 * it simply accepts every request and throws a <code>ResourceException</code>
 * for all <code>execute</code> calls. It can be disabled and it can be configured
 * to return <code>false</code> for {@link #execute(InteractionSpec, Record, Record)}
 * and <code>null</code> for {@link #execute(InteractionSpec, Record)} instead of
 * throwing an exception.
 */
public class GenericFailureInteraction implements InteractionImplementor
{
    private boolean enabled;
    private boolean throwException;
    private ResourceException exception;
    
    /**
     * Sets the default values, i.e. throwing a <code>ResourceException</code>.
     */
    public GenericFailureInteraction()
    {
        this(true);
    }
    
    /**
     * Sets if failure values should be returned instead of throwing a
     * <code>ResourceException</code>.
     * @param throwException <code>true</code> thrown an exception,
     *                       <code>false</code> return failure values for <code>execute</code>
     */
    public GenericFailureInteraction(boolean throwException)
    {
        this(throwException, new ResourceException("Simulated test exception"));
    }
    
    /**
     * Sets if failure values should be returned instead of throwing a
     * <code>ResourceException</code> and allows to set the exception
     * that will be thrown if exceptions are enabled.
     * @param throwException <code>true</code> thrown an exception,
     *                       <code>false</code> return failure values for <code>execute</code>
     * @param exception the exception to be thrown
     */
    public GenericFailureInteraction(boolean throwException, ResourceException exception)
    {
        this.enabled = true;
        this.throwException = throwException;
        this.exception = exception;
    }

    /**
     * Enables this implementor. {@link #canHandle(InteractionSpec, Record, Record)}
     * returns <code>true</code>, if this implementor is enabled.
     */
    public void enable()
    {
        this.enabled = true;
    }
    
    /**
     * Disables this implementor. {@link #canHandle(InteractionSpec, Record, Record)}
     * returns <code>false</code>, if this implementor is disabled.
     */
    public void disable()
    {
        this.enabled = false;
    }
    
    /**
     * Sets if failure values should be returned instead of throwing a
     * <code>ResourceException</code>.
     * @param throwException <code>true</code> thrown an exception,
     *                       <code>false</code> return failure values for <code>execute</code>
     */
    public void setThrowException(boolean throwException)
    {
        this.throwException = throwException;
    }
    
    /**
     * Sets the exception that will be thrown if exceptions are enabled.
     * @param exception the exception to be thrown
     */
    public void setException(ResourceException exception)
    {
        this.exception = exception;
    }
    
    /**
     * Returns <code>true</code> if this implementor is enabled and
     * <code>false</code> otherwise.
     */
    public boolean canHandle(InteractionSpec interactionSpec, Record actualRequest, Record actualResponse)
    {
        return enabled;
    }

    /**
     * Throws a <code>ResourceException</code> or returns <code>false</code>.
     * You can use {@link #setThrowException(boolean)} to configure this
     * behaviour.
     */
    public boolean execute(InteractionSpec interactionSpec, Record actualRequest, Record actualResponse) throws ResourceException
    {
        if(throwException)
        {
            throw exception;
        }
        return false;
    }

    /**
     * Throws a <code>ResourceException</code> or returns <code>null</code>.
     * You can use {@link #setThrowException(boolean)} to configure this
     * behaviour.
     */
    public Record execute(InteractionSpec interactionSpec, Record actualRequest) throws ResourceException
    {
        if(throwException)
        {
            throw exception;
        }
        return null;
    }
}
