package com.mockrunner.test.connector;

import javax.resource.ResourceException;

import com.mockrunner.connector.FailureInteraction;
import com.mockrunner.connector.InteractionHandler;

import junit.framework.TestCase;

public class FailureInteractionTest extends TestCase
{
    private InteractionHandler interactionHandler;

    protected void setUp() throws Exception
    {
        interactionHandler = new InteractionHandler();
    }

    protected void tearDown() throws Exception
    {
        interactionHandler = null;
    }
    
    public void testEnableAndDisable() throws Exception
    {
        FailureInteraction interaction = new FailureInteraction();
        assertTrue(interaction.canHandle(null, null, null));
        interaction.disable();
        assertFalse(interaction.canHandle(null, null, null));
        interaction.enable();
        assertTrue(interaction.canHandle(null, null, null));
        interactionHandler.addImplementor(interaction);
        try
        {
            interactionHandler.execute(null, null);
            fail();
        } 
        catch(ResourceException exc)
        {
            //expected exception
        }
        interaction.disable();
        interactionHandler.execute(null, null, null);
    }
    
    public void testThrowsException() throws Exception
    {
        FailureInteraction interaction = new FailureInteraction();
        interactionHandler.addImplementor(interaction);
        try
        {
            interactionHandler.execute(null, null, null);
            fail();
        } 
        catch(ResourceException exc)
        {
            //expected exception
        }
        interaction.setThrowException(false);
        assertFalse(interactionHandler.execute(null, null, null));
        assertNull(interactionHandler.execute(null, null));
        interaction.setThrowException(true);
        try
        {
            interactionHandler.execute(null, null);
            fail();
        } 
        catch(ResourceException exc)
        {
            //expected exception
        }
    }
    
    public void testExceptionType() throws Exception
    {
        ResourceException setExc = new ResourceException();
        Exception nested = new Exception();
        setExc.setLinkedException(nested);
        FailureInteraction interaction = new FailureInteraction(true, setExc);
        interactionHandler.addImplementor(interaction);
        try
        {
            interactionHandler.execute(null, null, null);
            fail();
        } 
        catch(ResourceException exc)
        {
            assertSame(setExc, exc);
            assertSame(nested, exc.getLinkedException());
        }
    }
}
