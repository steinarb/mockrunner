package com.mockrunner.test.connector;

import com.mockrunner.mock.connector.cci.MockLocalTransaction;

import junit.framework.TestCase;

public class MockLocalTransactionTest extends TestCase
{
    private MockLocalTransaction transaction;

    protected void setUp() throws Exception
    {
        transaction = new MockLocalTransaction();
    }

    protected void tearDown() throws Exception
    {
        transaction = null;
    }
    
    public void testWasCalledMethods() throws Exception
    {
        assertFalse(transaction.wasBeginCalled());
        assertFalse(transaction.wasCommitCalled());
        assertFalse(transaction.wasRollbackCalled());
        transaction.begin();
        assertTrue(transaction.wasBeginCalled());
        assertFalse(transaction.wasCommitCalled());
        assertFalse(transaction.wasRollbackCalled());
        transaction.commit();
        assertTrue(transaction.wasBeginCalled());
        assertTrue(transaction.wasCommitCalled());
        assertFalse(transaction.wasRollbackCalled());
        transaction.begin();
        assertTrue(transaction.wasBeginCalled());
        assertFalse(transaction.wasCommitCalled());
        assertFalse(transaction.wasRollbackCalled());
        transaction.rollback();
        assertTrue(transaction.wasBeginCalled());
        assertFalse(transaction.wasCommitCalled());
        assertTrue(transaction.wasRollbackCalled());
        transaction.reset();
        assertFalse(transaction.wasBeginCalled());
        assertFalse(transaction.wasCommitCalled());
        assertFalse(transaction.wasRollbackCalled());
    }
    
    public void testgetNumberMethods() throws Exception
    {
        assertEquals(0, transaction.getNumberBeginCalls());
        assertEquals(0, transaction.getNumberCommitCalls());
        assertEquals(0, transaction.getNumberRollbackCalls());
        transaction.begin();
        assertEquals(1, transaction.getNumberBeginCalls());
        assertEquals(0, transaction.getNumberCommitCalls());
        assertEquals(0, transaction.getNumberRollbackCalls());
        transaction.begin();
        assertEquals(2, transaction.getNumberBeginCalls());
        assertEquals(0, transaction.getNumberCommitCalls());
        assertEquals(0, transaction.getNumberRollbackCalls());
        transaction.rollback();
        assertEquals(2, transaction.getNumberBeginCalls());
        assertEquals(0, transaction.getNumberCommitCalls());
        assertEquals(1, transaction.getNumberRollbackCalls());
        transaction.commit();
        transaction.commit();
        assertEquals(2, transaction.getNumberBeginCalls());
        assertEquals(2, transaction.getNumberCommitCalls());
        assertEquals(1, transaction.getNumberRollbackCalls());
        transaction.commit();
        assertEquals(2, transaction.getNumberBeginCalls());
        assertEquals(3, transaction.getNumberCommitCalls());
        assertEquals(1, transaction.getNumberRollbackCalls());
        transaction.begin();
        assertEquals(3, transaction.getNumberBeginCalls());
        assertEquals(3, transaction.getNumberCommitCalls());
        assertEquals(1, transaction.getNumberRollbackCalls());
        transaction.reset();
        assertEquals(0, transaction.getNumberBeginCalls());
        assertEquals(0, transaction.getNumberCommitCalls());
        assertEquals(0, transaction.getNumberRollbackCalls());
    }
}
