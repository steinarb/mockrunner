package com.mockrunner.connector;

import java.util.Iterator;
import java.util.Vector;

import javax.resource.ResourceException;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;

/**
 * This class can be used to add implementations of {@link InteractionImplementor}.
 * The {@link com.mockrunner.mock.connector.cci.MockInteraction} delegates the 
 * <code>execute</code> calls to this class to find a suitable 
 * {@link InteractionImplementor} that can handle the request.
 * The <code>execute</code> method of the first {@link InteractionImplementor} 
 * that returns <code>true</code> for {@link InteractionImplementor#canHandle} will
 * be called.
 */
public class InteractionHandler 
{
    private Vector implementors = null;

    public InteractionHandler()
    {
        implementors = new Vector();
    }

    /**
     * Add an implementation of {@link InteractionImplementor}, e.g.
     * {@link StreamableRecordByteArrayInteraction} or {@link WSIFInteraction}.
     * You can add more than one {@link InteractionImplementor}, the first
     * one that can handle the request will be called.
     * @param implementor the {@link InteractionImplementor}
     */
    public void addImplementor(InteractionImplementor implementor)
    {
        implementors.add(implementor);
    }

    /**
     * Clears the list of current {@link InteractionImplementor} objects.
     */
    public void clearImplementors()
    {
        implementors.clear();
    }

    /**
     * Delegator for {@link com.mockrunner.mock.connector.cci.MockInteraction}.
     * Dispatches the call to the {@link InteractionImplementor} that
     * returns <code>true</code> for {@link InteractionImplementor#canHandle}.
     */
    public Record execute(InteractionSpec is, Record request) throws ResourceException
    {
        Iterator iter = implementors.iterator();
        while (iter.hasNext())
        {
            InteractionImplementor ii = (InteractionImplementor) iter.next();
            if (ii.canHandle(is, request, null))
            {
                return ii.execute(is, request);
            }
        }
        return null;
    }

    /**
     * Delegator for {@link com.mockrunner.mock.connector.cci.MockInteraction}.
     * Dispatches the call to the {@link InteractionImplementor} that
     * returns <code>true</code> for {@link InteractionImplementor#canHandle}.
     */
    public boolean execute(InteractionSpec is, Record request, Record response) throws ResourceException
    {
        Iterator iter = implementors.iterator();
        while (iter.hasNext())
        {
            InteractionImplementor ii = (InteractionImplementor) iter.next();
            if (ii.canHandle(is, request, response))
            {
                return ii.execute(is, request, response);
            }
        }
        // do I need to throw here?
        return false;
    }
}
