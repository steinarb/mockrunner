package com.mockrunner.test.connector;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.resource.cci.InteractionSpec;
import javax.resource.cci.MappedRecord;
import javax.resource.cci.Record;
import javax.resource.cci.Streamable;

import junit.framework.TestCase;

import com.mockrunner.connector.MappedRecordInteraction;
import com.mockrunner.mock.connector.cci.MockIndexedRecord;
import com.mockrunner.mock.connector.cci.MockMappedRecord;

public class MappedRecordInteractionTest extends TestCase
{
    public void testSetResponseArguments()
    {
        MappedRecordInteraction interaction = new MappedRecordInteraction();
        try
        {
            interaction.setResponse(new HashMap(), String.class);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        try
        {
            interaction.setResponse(new HashMap(), Streamable.class);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        } 
        interaction.setResponse(new HashMap(), null);
        interaction.setResponse(new HashMap(), TestRecord.class);
        interaction.setResponse(new HashMap(), MockMappedRecord.class);
        try
        {
            interaction = new MappedRecordInteraction(new HashMap(), Integer.class);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        try
        {
            interaction = new MappedRecordInteraction(new HashMap(), Record.class);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        interaction = new MappedRecordInteraction(new HashMap(), (Class)null);
    }
    
    public void testCanHandle()
    {
        MappedRecordInteraction interaction = new MappedRecordInteraction();
        InteractionSpec spec = new InteractionSpec() {};
        assertTrue(interaction.canHandle(spec, null, null));
        assertTrue(interaction.canHandle(spec, null, new MockMappedRecord()));
        assertFalse(interaction.canHandle(spec, null, new MockIndexedRecord()));
        assertTrue(interaction.canHandle(spec, new MockMappedRecord(), new MockMappedRecord()));
        Map expectedRequestMap = new HashMap();
        expectedRequestMap.put("key1", "value1");
        expectedRequestMap.put("key2", null);
        expectedRequestMap.put("key3", "value3");
        interaction.setExpectedRequest(expectedRequestMap);
        expectedRequestMap.put("key4", "value4");
        assertFalse(interaction.canHandle(spec, null, new MockMappedRecord()));
        assertFalse(interaction.canHandle(spec, new MockMappedRecord(), new MockMappedRecord()));
        MockMappedRecord request = new MockMappedRecord();
        request.put("key1", "value1");
        request.put("key2", null);
        request.put("key3", "value3");
        assertTrue(interaction.canHandle(spec, request, new MockMappedRecord()));
        request.put("key4", "value4");
        assertFalse(interaction.canHandle(spec, request, new MockMappedRecord()));
        interaction.setExpectedRequest(request);
        assertTrue(interaction.canHandle(spec, request, new MockMappedRecord()));
        expectedRequestMap = new HashMap();
        expectedRequestMap.put("key1", new Integer(5));
        expectedRequestMap.put("key2", new Integer(6));
        expectedRequestMap.put("key3", new Integer(7));
        interaction = new MappedRecordInteraction(expectedRequestMap, (Record)new MockMappedRecord());
        assertFalse(interaction.canHandle(spec, request, new MockMappedRecord()));
        request = new MockMappedRecord();
        request.put("key1", new Integer(5));
        request.put("key2", new Integer(1));
        request.put("key3", new Integer(7));
        request.put("key4", new Integer(8));
        assertFalse(interaction.canHandle(spec, request, new MockMappedRecord()));
        request.remove("key4");
        assertFalse(interaction.canHandle(spec, request, new MockMappedRecord()));
        request.put("key2", new Integer(6));
        assertTrue(interaction.canHandle(spec, request, new MockMappedRecord()));
        interaction = new MappedRecordInteraction(new HashMap(), MockMappedRecord.class);
        assertTrue(interaction.canHandle(spec, request, new MockMappedRecord()));
    }
    
    public void testEnableAndDisable()
    {
        MappedRecordInteraction interaction = new MappedRecordInteraction();
        InteractionSpec spec = new InteractionSpec() {};
        assertTrue(interaction.canHandle(spec, null, null));
        interaction.disable();
        assertFalse(interaction.canHandle(spec, null, null));
        interaction.enable();
        assertTrue(interaction.canHandle(spec, null, null));
    }
    
    public void testExecuteReturnsRecord() throws Exception
    {
        MappedRecordInteraction interaction = new MappedRecordInteraction();
        InteractionSpec spec = new InteractionSpec() {};
        Map expectedRequestMap = new HashMap();
        expectedRequestMap.put(new Long(1), "value1");
        interaction.setExpectedRequest(expectedRequestMap);
        assertNull(interaction.execute(spec, new MockIndexedRecord()));
        MockMappedRecord request = new MockMappedRecord();
        request.put(new Long(1), "value1");
        MockMappedRecord response = (MockMappedRecord)interaction.execute(spec, request);
        assertEquals(0, response.size());
        MockIndexedRecord indexedResponse = new MockIndexedRecord();
        interaction.setResponse(indexedResponse);
        assertSame(indexedResponse, interaction.execute(spec, request));
        Map responseMap = new HashMap();
        responseMap.put(new Integer(1), "value1");
        responseMap.put(new Integer(2), "value2");
        responseMap.put(new Integer(3), "value3");
        interaction.setResponse(responseMap, TestRecord.class);
        interaction.setResponse((Record)null);
        assertTrue(interaction.execute(spec, request) instanceof TestRecord);
        responseMap = new HashMap();
        responseMap.put(new Integer(1), "value1");
        responseMap.put(new Integer(2), "value2");
        responseMap.put(new Integer(3), "value3");
        responseMap.put(new Integer(4), "value4");
        responseMap.put(new Integer(5), "value5");
        interaction.setResponse(responseMap);
        response = (MockMappedRecord)interaction.execute(spec, request);
        assertTrue(new HashMap(response).equals(responseMap));
        interaction.setResponse((Record)new TestRecord());
        assertTrue(interaction.execute(spec, request) instanceof TestRecord);
        assertNull(interaction.execute(spec, new MockMappedRecord()));
        responseMap = new HashMap();
        responseMap.put(new Integer(1), "value1");
        interaction = new MappedRecordInteraction(responseMap, MockMappedRecord.class);
        response = (MockMappedRecord)interaction.execute(spec, request);
        assertTrue(new HashMap(response).equals(responseMap));
        responseMap = new HashMap();
        responseMap.put(new Integer(1), "value1");
        responseMap.put(new Integer(2), "value2");
        interaction = new MappedRecordInteraction(responseMap, (Class)null);
        responseMap.put(new Integer(3), "value3");
        response = (MockMappedRecord)interaction.execute(spec, request);
        responseMap.remove(new Integer(3));
        assertTrue(new HashMap(response).equals(responseMap));
        interaction.setResponse(indexedResponse);
        assertSame(indexedResponse, interaction.execute(spec, request));
        interaction.setResponse((Record)null);
        response = (MockMappedRecord)interaction.execute(spec, request);
        assertTrue(new HashMap(response).equals(responseMap));
    }
    
    public void testExecuteReturnsBoolean() throws Exception
    {
        MappedRecordInteraction interaction = new MappedRecordInteraction();
        InteractionSpec spec = new InteractionSpec() {};
        Map expectedRequestMap = new HashMap();
        expectedRequestMap.put(new Long(1), "value1");
        interaction.setExpectedRequest(expectedRequestMap);
        assertFalse(interaction.execute(spec, new MockIndexedRecord(), null));
        MockMappedRecord request = new MockMappedRecord();
        request.put(new Long(1), "value1");
        assertTrue(interaction.execute(spec, request, null));
        MockMappedRecord response = new MockMappedRecord();
        assertTrue(interaction.execute(spec, request, response));
        assertEquals(0, response.size());
        Map responseMap = new HashMap();
        responseMap.put(new Integer(1), "value1");
        responseMap.put(new Integer(2), "value1");
        responseMap.put(new Integer(3), "value3");
        interaction.setResponse(responseMap);
        assertTrue(interaction.execute(spec, request, response));
        assertTrue(new HashMap(response).equals(responseMap));
        assertFalse(interaction.execute(spec, request, new MockIndexedRecord()));
        assertTrue(interaction.execute(spec, request, new TestRecord()));
        interaction.setResponse((Map)null);
        interaction.setResponse((Record)new TestRecord());
        response = new MockMappedRecord();
        assertTrue(interaction.execute(spec, request, response));
        assertEquals(0, response.size());
        responseMap = new HashMap();
        responseMap.put(new Integer(1), "value1");
        interaction = new MappedRecordInteraction(null, responseMap);
        assertTrue(interaction.execute(spec, request, response));
        assertTrue(new HashMap(response).equals(responseMap));
        interaction.setResponse((Record)new TestRecord());
        assertTrue(interaction.execute(spec, request, response));
        assertTrue(new HashMap(response).equals(responseMap));
        responseMap = new HashMap();
        responseMap.put(new Integer(2), "value2");
        interaction.setResponse(responseMap);
        assertTrue(interaction.execute(spec, request, response));
        assertTrue(new HashMap(response).equals(responseMap));
    }
    
    public static class TestRecord implements MappedRecord
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
        
        public void clear()
        {
            
        }

        public boolean containsKey(Object key)
        {
            return false;
        }

        public boolean containsValue(Object value)
        {
            return false;
        }

        public Set entrySet()
        {
            return null;
        }

        public Object get(Object key)
        {
            return null;
        }

        public boolean isEmpty()
        {
            return false;
        }

        public Set keySet()
        {
            return null;
        }

        public Object put(Object key, Object value)
        {
            return null;
        }

        public void putAll(Map map)
        {
            
        }

        public Object remove(Object key)
        {
            return null;
        }

        public int size()
        {
            return 0;
        }

        public Collection values()
        {
            return null;
        }

        public Object clone() throws CloneNotSupportedException
        {
            return super.clone();
        }    
    }
}
