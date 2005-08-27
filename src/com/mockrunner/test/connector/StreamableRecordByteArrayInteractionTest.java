package com.mockrunner.test.connector;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;
import javax.resource.cci.Streamable;

import junit.framework.TestCase;

import com.mockrunner.connector.StreamableRecordByteArrayInteraction;
import com.mockrunner.mock.connector.cci.MockIndexedRecord;
import com.mockrunner.mock.connector.cci.MockMappedRecord;
import com.mockrunner.mock.connector.cci.MockStreamableByteArrayRecord;

public class StreamableRecordByteArrayInteractionTest extends TestCase
{
    public void testSetResponseArguments()
    {
        StreamableRecordByteArrayInteraction interaction = new StreamableRecordByteArrayInteraction();
        try
        {
            interaction.setResponse(new byte[0], String.class);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        try
        {
            interaction.setResponse(new byte[0], Streamable.class);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        try
        {
            interaction.setResponse(new ByteArrayInputStream(new byte[0]), Record.class);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        interaction.setResponse(new ByteArrayInputStream(new byte[0]), null);
        interaction.setResponse(new ByteArrayInputStream(new byte[0]), TestRecord.class);
        interaction.setResponse(new ByteArrayInputStream(new byte[0]), MockStreamableByteArrayRecord.class);
        try
        {
            interaction = new StreamableRecordByteArrayInteraction(new byte[0], Integer.class);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        try
        {
            interaction = new StreamableRecordByteArrayInteraction(new byte[0], Record.class);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        interaction = new StreamableRecordByteArrayInteraction(new byte[0], (Class)null);
    }
    
    public void testCanHandle()
    {
        StreamableRecordByteArrayInteraction interaction = new StreamableRecordByteArrayInteraction();
        InteractionSpec spec = new InteractionSpec() {};
        assertTrue(interaction.canHandle(spec, null, null));
        assertTrue(interaction.canHandle(spec, null, new MockStreamableByteArrayRecord()));
        assertFalse(interaction.canHandle(spec, null, new MockIndexedRecord()));
        assertTrue(interaction.canHandle(spec, new MockStreamableByteArrayRecord(), new MockStreamableByteArrayRecord()));
        interaction.setExpectedRequest(new byte[] {1, 2, 3});
        assertFalse(interaction.canHandle(spec, null, new MockStreamableByteArrayRecord()));
        assertFalse(interaction.canHandle(spec, new MockStreamableByteArrayRecord(), new MockStreamableByteArrayRecord()));
        MockStreamableByteArrayRecord request = new MockStreamableByteArrayRecord();
        request.setContent(new byte[] {1, 2, 3});
        assertTrue(interaction.canHandle(spec, request, new MockStreamableByteArrayRecord()));
        request.setContent(new byte[] {1, 2, 3, 4});
        assertFalse(interaction.canHandle(spec, request, new MockStreamableByteArrayRecord()));
        interaction.setExpectedRequest(new ByteArrayInputStream(new byte[] {1, 2, 3, 4}));
        assertTrue(interaction.canHandle(spec, request, new MockStreamableByteArrayRecord()));
        interaction = new StreamableRecordByteArrayInteraction(new byte[] {5, 6, 7}, new MockStreamableByteArrayRecord());
        assertFalse(interaction.canHandle(spec, request, new MockStreamableByteArrayRecord()));
        request.setContent(new byte[] {5, 6, 7});
        assertTrue(interaction.canHandle(spec, request, new MockStreamableByteArrayRecord()));
        interaction = new StreamableRecordByteArrayInteraction(new byte[] {1}, MockStreamableByteArrayRecord.class);
        assertTrue(interaction.canHandle(spec, request, new MockStreamableByteArrayRecord()));
    }
    
    public void testEnableAndDisable()
    {
        StreamableRecordByteArrayInteraction interaction = new StreamableRecordByteArrayInteraction();
        InteractionSpec spec = new InteractionSpec() {};
        assertTrue(interaction.canHandle(spec, null, null));
        interaction.disable();
        assertFalse(interaction.canHandle(spec, null, null));
        interaction.enable();
        assertTrue(interaction.canHandle(spec, null, null));
    }
    
    public void testExecuteReturnsRecord() throws Exception
    {
        StreamableRecordByteArrayInteraction interaction = new StreamableRecordByteArrayInteraction();
        InteractionSpec spec = new InteractionSpec() {};
        interaction.setExpectedRequest(new byte[] {1});
        assertNull(interaction.execute(spec, new MockIndexedRecord()));
        MockStreamableByteArrayRecord request = new MockStreamableByteArrayRecord();
        request.setContent(new byte[] {1});
        MockStreamableByteArrayRecord response = (MockStreamableByteArrayRecord)interaction.execute(spec, request);
        assertNull(response.getContent());
        MockMappedRecord mappedResponse = new MockMappedRecord();
        interaction.setResponse(mappedResponse);
        assertSame(mappedResponse, interaction.execute(spec, request));
        interaction.setResponse(new byte[] {1, 2, 3}, TestRecord.class);
        interaction.setResponse((Record)null);
        assertTrue(interaction.execute(spec, request) instanceof TestRecord);
        interaction.setResponse(new byte[] {1, 2, 3, 4, 5});
        response = (MockStreamableByteArrayRecord)interaction.execute(spec, request);
        assertTrue(Arrays.equals(new byte[] {1, 2, 3, 4, 5}, response.getContent()));
        interaction.setResponse(new TestRecord());
        assertTrue(interaction.execute(spec, request) instanceof TestRecord);
        assertNull(interaction.execute(spec, new MockStreamableByteArrayRecord()));
        interaction = new StreamableRecordByteArrayInteraction(new byte[] {1}, MockStreamableByteArrayRecord.class);
        response = (MockStreamableByteArrayRecord)interaction.execute(spec, request);
        assertTrue(Arrays.equals(new byte[] {1}, response.getContent()));
        interaction = new StreamableRecordByteArrayInteraction(new byte[] {1, 2}, (Class)null);
        response = (MockStreamableByteArrayRecord)interaction.execute(spec, request);
        assertTrue(Arrays.equals(new byte[] {1, 2}, response.getContent()));
        interaction.setResponse(mappedResponse);
        assertSame(mappedResponse, interaction.execute(spec, request));
        interaction.setResponse((Record)null);
        response = (MockStreamableByteArrayRecord)interaction.execute(spec, request);
        assertTrue(Arrays.equals(new byte[] {1, 2}, response.getContent()));
    }
    
    public void testExecuteReturnsBoolean() throws Exception
    {
        StreamableRecordByteArrayInteraction interaction = new StreamableRecordByteArrayInteraction();
        InteractionSpec spec = new InteractionSpec() {};
        interaction.setExpectedRequest(new byte[] {1});
        assertFalse(interaction.execute(spec, new MockIndexedRecord(), null));
        MockStreamableByteArrayRecord request = new MockStreamableByteArrayRecord();
        request.setContent(new byte[] {1});
        assertTrue(interaction.execute(spec, request, null));
        MockStreamableByteArrayRecord response = new MockStreamableByteArrayRecord();
        assertTrue(interaction.execute(spec, request, response));
        assertNull(response.getContent());
        interaction.setResponse(new byte[] {1, 2, 3});
        assertTrue(interaction.execute(spec, request, response));
        assertTrue(Arrays.equals(new byte[] {1, 2, 3}, response.getContent()));
        assertFalse(interaction.execute(spec, request, new MockMappedRecord()));
        assertTrue(interaction.execute(spec, request, new TestRecord()));
        interaction.setResponse((byte[])null);
        interaction.setResponse(new TestRecord());
        response = new MockStreamableByteArrayRecord();
        assertTrue(interaction.execute(spec, request, response));
        assertNull(response.getContent());
        interaction = new StreamableRecordByteArrayInteraction(null, new byte[] {1});
        assertTrue(interaction.execute(spec, request, response));
        assertTrue(Arrays.equals(new byte[] {1}, response.getContent()));
        interaction.setResponse(new TestRecord());
        assertTrue(interaction.execute(spec, request, response));
        assertTrue(Arrays.equals(new byte[] {1}, response.getContent()));
    }
    
    public static class TestRecord implements Record, Streamable
    {
        public String getRecordName()
        {
            return "";
        }

        public String getRecordShortDescription()
        {
            return "";
        }

        public void setRecordName(String name)
        {
            
        }

        public void setRecordShortDescription(String description)
        {
            
        }

        public void read(InputStream stream) throws IOException
        {
            
        }

        public void write(OutputStream stream) throws IOException
        {
            
        }

        public Object clone() throws CloneNotSupportedException
        {
            return super.clone();
        }    
    }
}
