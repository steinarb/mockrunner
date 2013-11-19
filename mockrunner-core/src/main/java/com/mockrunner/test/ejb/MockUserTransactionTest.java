package com.mockrunner.test.ejb;

import javax.transaction.Status;

import com.mockrunner.mock.ejb.MockUserTransaction;

import junit.framework.TestCase;

public class MockUserTransactionTest extends TestCase
{
    public void testBegin() throws Exception
    {
        MockUserTransaction transaction = new MockUserTransaction();
        transaction.commit();
        transaction.setRollbackOnly();
        transaction.setTransactionTimeout(10);
        assertFalse(transaction.wasBeginCalled());
        transaction.begin();
        assertTrue(transaction.wasBeginCalled());
        assertFalse(transaction.wasCommitCalled());
        assertFalse(transaction.wasRollbackCalled());
        assertFalse(transaction.wasRollbackOnlyCalled());
    }
    
    public void testNumberCalls() throws Exception
    {
        MockUserTransaction transaction = new MockUserTransaction();
        transaction.begin();
        transaction.commit();
        transaction.begin();
        transaction.setRollbackOnly();
        transaction.begin();
        transaction.rollback();
        transaction.begin();
        transaction.commit();
        assertEquals(4, transaction.getNumberBeginCalls());
        assertEquals(2, transaction.getNumberCommitCalls());
        assertEquals(1, transaction.getNumberRollbackCalls());
        assertEquals(1, transaction.getNumberRollbackOnlyCalls());
    }
    
    public void testReset() throws Exception
    {
        MockUserTransaction transaction = new MockUserTransaction();
        transaction.begin();
        transaction.commit();
        transaction.begin();
        transaction.setRollbackOnly();
        transaction.begin();
        transaction.rollback();
        transaction.begin();
        transaction.commit();
        transaction.reset();
        assertFalse(transaction.wasBeginCalled());
        assertFalse(transaction.wasCommitCalled());
        assertFalse(transaction.wasRollbackCalled());
        assertFalse(transaction.wasRollbackOnlyCalled());
        assertEquals(0, transaction.getTransactionTimeout());
        assertEquals(0, transaction.getNumberBeginCalls());
        assertEquals(0, transaction.getNumberCommitCalls());
        assertEquals(0, transaction.getNumberRollbackCalls());
        assertEquals(0, transaction.getNumberRollbackOnlyCalls());
    }
    
    public void testGetStatus() throws Exception
    {
        MockUserTransaction transaction = new MockUserTransaction();
        assertEquals(Status.STATUS_NO_TRANSACTION, transaction.getStatus());
        transaction.begin();
        assertEquals(Status.STATUS_ACTIVE, transaction.getStatus());
        transaction.commit();
        assertEquals(Status.STATUS_COMMITTED, transaction.getStatus());
        transaction.begin();
        assertEquals(Status.STATUS_ACTIVE, transaction.getStatus());
        transaction.rollback();
        assertEquals(Status.STATUS_ROLLEDBACK, transaction.getStatus());
        transaction.begin();
        assertEquals(Status.STATUS_ACTIVE, transaction.getStatus());
        transaction.setRollbackOnly();
        assertEquals(Status.STATUS_MARKED_ROLLBACK, transaction.getStatus());
        transaction.rollback();
        assertEquals(Status.STATUS_ROLLEDBACK, transaction.getStatus());
    }
}
