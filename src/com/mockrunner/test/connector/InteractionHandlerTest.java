package com.mockrunner.test.connector;

import javax.resource.ResourceException;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;

import com.mockrunner.connector.InteractionHandler;
import com.mockrunner.connector.InteractionImplementor;

import junit.framework.TestCase;

public class InteractionHandlerTest extends TestCase
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
    
    public void testExecuteReturnsBoolean() throws Exception
    {
        assertFalse(interactionHandler.execute(new TestInteractionSpec(), new TestRecord(), new TestRecord()));
        TestInteractionImplementor implementor1 = new TestInteractionImplementor(false);
        TestInteractionImplementor implementor2 = new TestInteractionImplementor(true);
        implementor2.setBooleanReturnValue(false);
        TestInteractionImplementor implementor3 = new TestInteractionImplementor(false);
        interactionHandler.addImplementor(implementor1);
        interactionHandler.addImplementor(implementor2);
        interactionHandler.addImplementor(implementor3);
        assertFalse(interactionHandler.execute(new TestInteractionSpec(), new TestRecord(), new TestRecord()));
        assertFalse(implementor1.wasExecuteCalled());
        assertTrue(implementor2.wasExecuteCalled());
        assertFalse(implementor3.wasExecuteCalled());
        interactionHandler.clearImplementors();
        implementor1 = new TestInteractionImplementor(false);
        implementor2 = new TestInteractionImplementor(false);
        interactionHandler.addImplementor(implementor1);
        interactionHandler.addImplementor(implementor2);
        assertFalse(interactionHandler.execute(new TestInteractionSpec(), new TestRecord(), new TestRecord()));
        assertFalse(implementor1.wasExecuteCalled());
        assertFalse(implementor2.wasExecuteCalled());
        interactionHandler.clearImplementors();
        implementor1 = new TestInteractionImplementor(true);
        implementor2 = new TestInteractionImplementor(false);
        interactionHandler.addImplementor(implementor1);
        interactionHandler.addImplementor(implementor2);
        assertTrue(interactionHandler.execute(new TestInteractionSpec(), new TestRecord(), new TestRecord()));
        assertTrue(implementor1.wasExecuteCalled());
        assertFalse(implementor2.wasExecuteCalled());
    }
    
    public void testExecuteReturnsRecord() throws Exception
    {
        assertNull(interactionHandler.execute(new TestInteractionSpec(), new TestRecord()));
        TestInteractionImplementor implementor1 = new TestInteractionImplementor(false);
        TestInteractionImplementor implementor2 = new TestInteractionImplementor(true);
        TestRecord testRecord = new TestRecord();
        implementor2.setRecordReturnValue(testRecord);
        TestInteractionImplementor implementor3 = new TestInteractionImplementor(false);
        interactionHandler.addImplementor(implementor1);
        interactionHandler.addImplementor(implementor2);
        interactionHandler.addImplementor(implementor3);
        assertSame(testRecord, interactionHandler.execute(new TestInteractionSpec(), new TestRecord()));
        assertFalse(implementor1.wasExecuteCalled());
        assertTrue(implementor2.wasExecuteCalled());
        assertFalse(implementor3.wasExecuteCalled());
        interactionHandler.clearImplementors();
        implementor1 = new TestInteractionImplementor(false);
        implementor2 = new TestInteractionImplementor(false);
        interactionHandler.addImplementor(implementor1);
        interactionHandler.addImplementor(implementor2);
        assertNull(interactionHandler.execute(new TestInteractionSpec(), new TestRecord()));
        assertFalse(implementor1.wasExecuteCalled());
        assertFalse(implementor2.wasExecuteCalled());
        interactionHandler.clearImplementors();
        implementor1 = new TestInteractionImplementor(true);
        implementor2 = new TestInteractionImplementor(false);
        interactionHandler.addImplementor(implementor1);
        interactionHandler.addImplementor(implementor2);
        assertNull(interactionHandler.execute(new TestInteractionSpec(), new TestRecord()));
        assertTrue(implementor1.wasExecuteCalled());
        assertFalse(implementor2.wasExecuteCalled());
    }
    
    private class TestInteractionSpec implements InteractionSpec
    {
        
    }
    
    private class TestRecord implements Record
    {
        public String getRecordName()
        {
            return null;
        }

        public String getRecordShortDescription()
        {
            return null;
        }

        public void setRecordName(String name)
        {
            
        }

        public void setRecordShortDescription(String description)
        {
            
        }

        public Object clone() throws CloneNotSupportedException
        {
            return null;
        }
    }
    
    private class TestInteractionImplementor implements InteractionImplementor
    {
        private boolean doProcess = false;
        private boolean wasExecuteCalled = false;
        private boolean booleanReturnValue = true;
        private Record recordReturnValue = null;
        
        public TestInteractionImplementor(boolean doProcess)
        {
            this.doProcess = doProcess;
        }
        
        public void setBooleanReturnValue(boolean booleanReturnValue)
        {
            this.booleanReturnValue = booleanReturnValue;
        }

        public void setRecordReturnValue(Record recordReturnValue)
        {
            this.recordReturnValue = recordReturnValue;
        } 

        public boolean canHandle(InteractionSpec interactionSpec, Record actualRequest, Record actualResponse)
        {
            return doProcess;
        }

        public Record execute(InteractionSpec interactionSpec, Record actualRequest) throws ResourceException
        {
            wasExecuteCalled = true;
            return recordReturnValue;
        }

        public boolean execute(InteractionSpec is, Record request, Record response) throws ResourceException
        {
            wasExecuteCalled = true;
            return booleanReturnValue;
        }

        public boolean wasExecuteCalled()
        {
            return wasExecuteCalled;
        }
    }
}
