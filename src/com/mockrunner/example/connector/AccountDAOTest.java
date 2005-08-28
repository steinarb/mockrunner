package com.mockrunner.example.connector;

import java.util.ArrayList;
import java.util.List;

import com.mockrunner.connector.ConnectorTestCaseAdapter;
import com.mockrunner.connector.GenericFailureInteraction;
import com.mockrunner.connector.IndexedRecordInteraction;
import com.mockrunner.ejb.EJBTestModule;

/**
 * Example test for {@link AccountDAO}. The involved resource adaper
 * works with indexed records, so we can use the
 * {@link com.mockrunner.connector.IndexedRecordInteraction} for
 * the test. We simply prepare some names and the corresponding
 * ids, the simulated resource adapter should return. The
 * {@link com.mockrunner.connector.IndexedRecordInteraction} creates
 * suitable <code>IndexedRecord</code> implementations for the provided
 * lists. If the actual request matches the prepared list data, the
 * corresponding response <code>IndexedRecord</code> is returned.
 * This example test also demonstrates how to test the failure case.
 * The {@link com.mockrunner.connector.GenericFailureInteraction}
 * throws an exception which causes the {@link AccountDAO} to roll back the
 * transaction and to return -1.
 */
public class AccountDAOTest extends ConnectorTestCaseAdapter
{
    private EJBTestModule ejbModule;
    private AccountDAO dao;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        ejbModule = createEJBTestModule();
        ejbModule.bindToContext("java:/ra/db/ConnectionFactory", getConnectorMockObjectFactory().getMockConnectionFactory());
        dao = new AccountDAO();
    }
    
    private void prepareValidInteraction(String firstName, String lastName, int id)
    {
        List request = new ArrayList();
        request.add(firstName);
        request.add(lastName);
        List response = new ArrayList();
        response.add(new Integer(id));
        IndexedRecordInteraction indexedInteraction = new IndexedRecordInteraction(request, response);
        getInteractionHandler().addImplementor(indexedInteraction);
    }
    
    public void testCreateAccountVariousUsers()
    {
        prepareValidInteraction("John", "Doe", 500);
        prepareValidInteraction("Jane", "Doe", 1000);
        prepareValidInteraction("Brian", "Doe", 1500);
        assertEquals(500, dao.createAccount("John", "Doe"));
        assertEquals(1000, dao.createAccount("Jane", "Doe"));
        assertEquals(1500, dao.createAccount("Brian", "Doe"));
        assertEquals(-1, dao.createAccount("Judith", "Doe"));
        verifyAllInteractionsClosed();
        verifyConnectionClosed();
    }
    
    public void testCreateAccountSuccessful()
    {
        prepareValidInteraction("John", "Doe", 500);
        assertEquals(500, dao.createAccount("John", "Doe"));
        verifyLocalTransactionCommitted();
        verifyNumberCreatedIndexedRecords(1);
        verifyAllInteractionsClosed();
        verifyConnectionClosed();
    }
    
    public void testCreateAccountFailure()
    {
        getInteractionHandler().addImplementor(new GenericFailureInteraction());
        assertEquals(-1, dao.createAccount("John", "Doe"));
        verifyLocalTransactionRolledBack();
        verifyNumberCreatedIndexedRecords(1);
        verifyAllInteractionsClosed();
        verifyConnectionClosed();
    }
}
