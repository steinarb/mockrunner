package com.mockrunner.test.jms;

import javax.jms.Session;

import junit.framework.TestCase;

import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockQueueConnection;
import com.mockrunner.mock.jms.MockQueueSession;
import com.mockrunner.mock.jms.MockSession;

public class MockSessionTest extends TestCase
{
    public void testGetAcknowledgeMode() throws Exception
    {
        DestinationManager manager = new DestinationManager();
        MockQueueConnection connection = new MockQueueConnection(manager);
        MockSession session =(MockQueueSession)connection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
        assertEquals(Session.CLIENT_ACKNOWLEDGE, session.getAcknowledgeMode());
        session =(MockQueueSession)connection.createQueueSession(false, Session.DUPS_OK_ACKNOWLEDGE);
        assertEquals(Session.DUPS_OK_ACKNOWLEDGE, session.getAcknowledgeMode());
        session =(MockQueueSession)connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        assertEquals(Session.AUTO_ACKNOWLEDGE, session.getAcknowledgeMode());
        session =(MockQueueSession)connection.createQueueSession(false, 1234);
        assertEquals(1234, session.getAcknowledgeMode());
        session =(MockQueueSession)connection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
        assertEquals(0, session.getAcknowledgeMode());
        session =(MockQueueSession)connection.createQueueSession(true, 1234);
        assertEquals(0, session.getAcknowledgeMode());
    }
}
