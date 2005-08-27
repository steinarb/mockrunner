package com.mockrunner.test.connector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.resource.cci.IndexedRecord;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;
import javax.resource.cci.Streamable;

import junit.framework.TestCase;

import com.mockrunner.connector.IndexedRecordInteraction;
import com.mockrunner.mock.connector.cci.MockIndexedRecord;
import com.mockrunner.mock.connector.cci.MockMappedRecord;

public class IndexedRecordInteractionTest extends TestCase
{
    public void testSetResponseArguments()
    {
        IndexedRecordInteraction interaction = new IndexedRecordInteraction();
        try
        {
            interaction.setResponse(new ArrayList(), String.class);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        try
        {
            interaction.setResponse(new ArrayList(), Streamable.class);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        } 
        interaction.setResponse(new ArrayList(), null);
        interaction.setResponse(new ArrayList(), TestRecord.class);
        interaction.setResponse(new ArrayList(), MockIndexedRecord.class);
        try
        {
            interaction = new IndexedRecordInteraction(new ArrayList(), Integer.class);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        try
        {
            interaction = new IndexedRecordInteraction(new ArrayList(), Record.class);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        interaction = new IndexedRecordInteraction(new ArrayList(), (Class)null);
    }
    
    public void testCanHandle()
    {
        IndexedRecordInteraction interaction = new IndexedRecordInteraction();
        InteractionSpec spec = new InteractionSpec() {};
        assertTrue(interaction.canHandle(spec, null, null));
        assertTrue(interaction.canHandle(spec, null, new MockIndexedRecord()));
        assertFalse(interaction.canHandle(spec, null, new MockMappedRecord()));
        assertTrue(interaction.canHandle(spec, new MockIndexedRecord(), new MockIndexedRecord()));
        List expectedRequestList = new ArrayList();
        expectedRequestList.add(null);
        expectedRequestList.add("2");
        expectedRequestList.add("3");
        interaction.setExpectedRequest(expectedRequestList);
        expectedRequestList.add("4");
        assertFalse(interaction.canHandle(spec, null, new MockIndexedRecord()));
        assertFalse(interaction.canHandle(spec, new MockIndexedRecord(), new MockIndexedRecord()));
        MockIndexedRecord request = new MockIndexedRecord();
        request.add(null);
        request.add("2");
        request.add("3");
        assertTrue(interaction.canHandle(spec, request, new MockIndexedRecord()));
        request.add("4");
        assertFalse(interaction.canHandle(spec, request, new MockIndexedRecord()));
        interaction.setExpectedRequest(request);
        assertTrue(interaction.canHandle(spec, request, new MockIndexedRecord()));
        expectedRequestList = new ArrayList();
        expectedRequestList.add(new Integer(5));
        expectedRequestList.add(new Integer(6));
        expectedRequestList.add(new Integer(7));
        interaction = new IndexedRecordInteraction(expectedRequestList, (Record)new MockIndexedRecord());
        assertFalse(interaction.canHandle(spec, request, new MockIndexedRecord()));
        request = new MockIndexedRecord();
        request.add(new Integer(5));
        request.add(new Integer(1));
        request.add(new Integer(7));
        request.add(new Integer(8));
        assertFalse(interaction.canHandle(spec, request, new MockIndexedRecord()));
        request.remove(new Integer(8));
        assertFalse(interaction.canHandle(spec, request, new MockIndexedRecord()));
        request.set(1, new Integer(6));
        assertTrue(interaction.canHandle(spec, request, new MockIndexedRecord()));
        interaction = new IndexedRecordInteraction(new ArrayList(), MockIndexedRecord.class);
        assertTrue(interaction.canHandle(spec, request, new MockIndexedRecord()));
    }
    
    public void testEnableAndDisable()
    {
        IndexedRecordInteraction interaction = new IndexedRecordInteraction();
        InteractionSpec spec = new InteractionSpec() {};
        assertTrue(interaction.canHandle(spec, null, null));
        interaction.disable();
        assertFalse(interaction.canHandle(spec, null, null));
        interaction.enable();
        assertTrue(interaction.canHandle(spec, null, null));
    }  
    
    public void testExecuteReturnsRecord() throws Exception
    {
        IndexedRecordInteraction interaction = new IndexedRecordInteraction();
        InteractionSpec spec = new InteractionSpec() {};
        List expectedRequestList = new ArrayList();
        expectedRequestList.add(new Long(1));
        interaction.setExpectedRequest(expectedRequestList);
        assertNull(interaction.execute(spec, new MockMappedRecord()));
        MockIndexedRecord request = new MockIndexedRecord();
        request.add(new Long(1));
        MockIndexedRecord response = (MockIndexedRecord)interaction.execute(spec, request);
        assertEquals(0, response.size());
        MockMappedRecord mappedResponse = new MockMappedRecord();
        interaction.setResponse(mappedResponse);
        assertSame(mappedResponse, interaction.execute(spec, request));
        List responseList = new ArrayList();
        responseList.add(new Integer(1));
        responseList.add(new Integer(2));
        responseList.add(new Integer(3));
        interaction.setResponse(responseList, TestRecord.class);
        interaction.setResponse((Record)null);
        assertTrue(interaction.execute(spec, request) instanceof TestRecord);
        responseList = new ArrayList();
        responseList.add(new Integer(1));
        responseList.add(new Integer(2));
        responseList.add(new Integer(3));
        responseList.add(new Integer(4));
        responseList.add(new Integer(5));
        interaction.setResponse(responseList);
        response = (MockIndexedRecord)interaction.execute(spec, request);
        assertTrue(new ArrayList(response).equals(responseList));
        interaction.setResponse((Record)new TestRecord());
        assertTrue(interaction.execute(spec, request) instanceof TestRecord);
        assertNull(interaction.execute(spec, new MockIndexedRecord()));
        responseList = new ArrayList();
        responseList.add(new Integer(1));
        interaction = new IndexedRecordInteraction(responseList, MockIndexedRecord.class);
        response = (MockIndexedRecord)interaction.execute(spec, request);
        assertTrue(new ArrayList(response).equals(responseList));
        responseList = new ArrayList();
        responseList.add(new Integer(1));
        responseList.add(new Integer(2));
        interaction = new IndexedRecordInteraction(responseList, (Class)null);
        responseList.add(new Integer(3));
        response = (MockIndexedRecord)interaction.execute(spec, request);
        responseList.remove(new Integer(3));
        assertTrue(new ArrayList(response).equals(responseList));
        interaction.setResponse(mappedResponse);
        assertSame(mappedResponse, interaction.execute(spec, request));
        interaction.setResponse((Record)null);
        response = (MockIndexedRecord)interaction.execute(spec, request);
        assertTrue(new ArrayList(response).equals(responseList));
    }
    
    public void testExecuteReturnsBoolean() throws Exception
    {
        IndexedRecordInteraction interaction = new IndexedRecordInteraction();
        InteractionSpec spec = new InteractionSpec() {};
        List expectedRequestList = new ArrayList();
        expectedRequestList.add(new Long(1));
        interaction.setExpectedRequest(expectedRequestList);
        assertFalse(interaction.execute(spec, new MockMappedRecord(), null));
        MockIndexedRecord request = new MockIndexedRecord();
        request.add(new Long(1));
        assertTrue(interaction.execute(spec, request, null));
        MockIndexedRecord response = new MockIndexedRecord();
        assertTrue(interaction.execute(spec, request, response));
        assertEquals(0, response.size());
        List responseList = new ArrayList();
        responseList.add(new Integer(1));
        responseList.add(new Integer(2));
        responseList.add(null);
        interaction.setResponse(responseList);
        assertTrue(interaction.execute(spec, request, response));
        assertTrue(new ArrayList(response).equals(responseList));
        assertFalse(interaction.execute(spec, request, new MockMappedRecord()));
        assertTrue(interaction.execute(spec, request, new TestRecord()));
        interaction.setResponse((List)null);
        interaction.setResponse((Record)new TestRecord());
        response = new MockIndexedRecord();
        assertTrue(interaction.execute(spec, request, response));
        assertEquals(0, response.size());
        responseList = new ArrayList();
        responseList.add(new Integer(1));
        interaction = new IndexedRecordInteraction(null, responseList);
        assertTrue(interaction.execute(spec, request, response));
        assertTrue(new ArrayList(response).equals(responseList));
        interaction.setResponse((Record)new TestRecord());
        assertTrue(interaction.execute(spec, request, response));
        assertTrue(new ArrayList(response).equals(responseList));
    }
    
    public static class TestRecord implements IndexedRecord
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
       
        public void add(int index, Object value)
        {
            
        }

        public boolean add(Object value)
        {
            return false;
        }

        public boolean addAll(Collection values)
        {
            return false;
        }

        public boolean addAll(int index, Collection values)
        {
            return false;
        }

        public void clear()
        {
            
        }

        public boolean contains(Object object)
        {
            return false;
        }

        public boolean containsAll(Collection collection)
        {
            return false;
        }

        public Object get(int index)
        {
            return null;
        }

        public int indexOf(Object object)
        {
            return 0;
        }

        public boolean isEmpty()
        {
            return false;
        }

        public Iterator iterator()
        {
            return null;
        }

        public int lastIndexOf(Object object)
        {
            return 0;
        }

        public ListIterator listIterator()
        {
            return null;
        }

        public ListIterator listIterator(int index)
        {
            return null;
        }

        public Object remove(int index)
        {
            return null;
        }

        public boolean remove(Object object)
        {
            return false;
        }

        public boolean removeAll(Collection collection)
        {
            return false;
        }

        public boolean retainAll(Collection collection)
        {
            return false;
        }

        public Object set(int index, Object value)
        {
            return null;
        }

        public int size()
        {
            return 0;
        }

        public List subList(int fromIndex, int toIndex)
        {
            return null;
        }

        public Object[] toArray()
        {
            return null;
        }

        public Object[] toArray(Object[] array)
        {
            return null;
        }

        public Object clone() throws CloneNotSupportedException
        {
            return super.clone();
        }    
    }
}
