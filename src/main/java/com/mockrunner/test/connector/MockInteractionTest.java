package com.mockrunner.test.connector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.resource.cci.Interaction;
import javax.resource.cci.Record;

import junit.framework.TestCase;

import com.mockrunner.connector.IndexedRecordInteraction;
import com.mockrunner.connector.MappedRecordInteraction;
import com.mockrunner.connector.StreamableRecordByteArrayInteraction;
import com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory;
import com.mockrunner.mock.connector.cci.MockMappedRecord;
import com.mockrunner.mock.connector.cci.MockStreamableByteArrayRecord;

public class MockInteractionTest extends TestCase
{
    private ConnectorMockObjectFactory mockFactory;
    
    protected void setUp() throws Exception
    {
        mockFactory = new ConnectorMockObjectFactory();
    }

    protected void tearDown() throws Exception
    {
        mockFactory = null;
    }
    
    public void testExecuteReturnsBoolean() throws Exception
    {
        StreamableRecordByteArrayInteraction byteArrayInteraction1 = new StreamableRecordByteArrayInteraction();
        byteArrayInteraction1.setExpectedRequest(new byte[] {1, 2, 3});
        byteArrayInteraction1.setResponse(new byte[] {1});
        StreamableRecordByteArrayInteraction byteArrayInteraction2 = new StreamableRecordByteArrayInteraction();
        byteArrayInteraction2.setResponse(new byte[] {2});
        mockFactory.getInteractionHandler().addImplementor(byteArrayInteraction1);
        mockFactory.getInteractionHandler().addImplementor(byteArrayInteraction2);
        Interaction interaction = mockFactory.getMockConnectionFactory().getConnection().createInteraction();
        assertTrue(interaction.execute(null, null, null));
        MockStreamableByteArrayRecord actualRequest = new MockStreamableByteArrayRecord();
        MockStreamableByteArrayRecord actualResponse = new MockStreamableByteArrayRecord();
        actualRequest.setContent(new byte[] {1, 2, 3});
        assertTrue(interaction.execute(null, actualRequest, actualResponse));
        assertTrue(Arrays.equals(new byte[] {1}, actualResponse.getContent()));
        actualRequest.setContent(new byte[] {1, 2, 4});
        assertTrue(interaction.execute(null, actualRequest, actualResponse));
        assertTrue(Arrays.equals(new byte[] {2}, actualResponse.getContent()));
    }
    
    public void testExecuteReturnsRecord() throws Exception
    {
        IndexedRecordInteraction indexedInteraction = new IndexedRecordInteraction();
        TestRecord testRecord = new TestRecord();
        indexedInteraction.setResponse(testRecord);
        mockFactory.getInteractionHandler().addImplementor(indexedInteraction);
        Interaction interaction = mockFactory.getMockConnectionFactory().getConnection().createInteraction();
        assertSame(testRecord, interaction.execute(null, null));
        List expectedList = new ArrayList();
        expectedList.add("1");
        indexedInteraction.setExpectedRequest(expectedList);
        assertNull(interaction.execute(null, null));
        MappedRecordInteraction mappedInteraction = new MappedRecordInteraction();
        TestRecord anotherTestRecord = new TestRecord();
        mappedInteraction.setResponse(anotherTestRecord);
        Map expectedMap = new HashMap();
        expectedMap.put("1", "1");
        mappedInteraction.setExpectedRequest(expectedMap);
        MockMappedRecord actualRequest = new MockMappedRecord();
        actualRequest.put("1", "1");
        mockFactory.getInteractionHandler().addImplementor(mappedInteraction);
        assertSame(anotherTestRecord, interaction.execute(null, actualRequest));
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
}
