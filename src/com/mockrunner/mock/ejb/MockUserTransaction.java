package com.mockrunner.mock.ejb;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * Mock implementation of <code>UserTransaction</code>.
 */
public class MockUserTransaction implements UserTransaction
{
    private boolean beginCalled;
    private boolean commitCalled;
    private boolean rollbackCalled;
    private boolean rollbackOnlyCalled;
    private int transactionTimeout;
    private int beginCalls;
    private int commitCalls;
    private int rollbackCalls;
    private int rollbackOnlyCalls;
    
    public MockUserTransaction()
    {
        reset();
    }
    
    /**
     * Resets the state, i.e. sets the status to
     * <code>Status.STATUS_NO_TRANSACTION</code>
     * and the number of calls to 0.
     */
    public void reset()
    {
        beginCalled = false;
        commitCalled = false;
        rollbackCalled = false;
        rollbackOnlyCalled = false;
        transactionTimeout = 0;
        beginCalls = 0;
        commitCalls = 0;
        rollbackCalls = 0;
        rollbackOnlyCalls = 0;
    }
    
    /**
     * Returns if {@link #begin} was called.
     * @return was {@link #begin} called
     */
    public boolean wasBeginCalled()
    {
        return beginCalled;
    }
    
    /**
     * Returns if {@link #commit} was called.
     * @return was {@link #commit} called
     */
    public boolean wasCommitCalled()
    {
        return commitCalled;
    }
    
    /**
     * Returns if {@link #rollback} was called.
     * @return was {@link #rollback} called
     */
    public boolean wasRollbackCalled()
    {
        return rollbackCalled;
    }
    
    /**
     * Returns if {@link #setRollbackOnly} was called.
     * @return was {@link #setRollbackOnly} called
     */
    public boolean wasRollbackOnlyCalled()
    {
        return rollbackOnlyCalled;
    }
    
    /**
     * Returns the transaction timeout.
     * @return the transaction timeout
     */
    public int getTransactionTimeout()
    {
        return transactionTimeout;
    }
    
    /**
     * Returns the number of overall {@link #begin} calls.
     * @return the number of overall {@link #begin} calls
     */
    public int getNumberBeginCalls()
    {
        return beginCalls;
    }
    
    /**
     * Returns the number of overall {@link #commit} calls.
     * @return the number of overall {@link #commit} calls
     */
    public int getNumberCommitCalls()
    {
        return commitCalls;
    }
    
    /**
     * Returns the number of overall {@link #rollback} calls.
     * @return the number of overall {@link #rollback} calls
     */
    public int getNumberRollbackCalls()
    {
        return rollbackCalls;
    }
    
    /**
     * Returns the number of overall {@link #setRollbackOnly} calls.
     * @return the number of overall {@link #setRollbackOnly} calls
     */
    public int getNumberRollbackOnlyCalls()
    {
        return rollbackOnlyCalls;
    }
    
    /**
     * Returns the status of this transaction.
     * @return the status of this transaction
     */
    public int getStatus() throws SystemException
    {
        if(rollbackCalled) return Status.STATUS_ROLLEDBACK;
        if(commitCalled) return Status.STATUS_COMMITTED;
        if(rollbackOnlyCalled) return Status.STATUS_MARKED_ROLLBACK;
        if(beginCalled) return Status.STATUS_ACTIVE;
        return Status.STATUS_NO_TRANSACTION;
    }
    
    /**
     * Starts the transaction. The status will be
     * <code>Status.STATUS_ACTIVE</code> and the
     * flags regarding <code>wasXYZCalled</code>
     * are reset to <code>false</code>. This method
     * does not reset the number of overall calls.
     */
    public void begin() throws NotSupportedException, SystemException
    {
        beginCalled = true;
        commitCalled = false;
        rollbackCalled = false;
        rollbackOnlyCalled = false;
        beginCalls++;
    }

    /**
     * Commits the transaction.
     */
    public void commit() throws RollbackException, 
                                HeuristicMixedException,
                                HeuristicRollbackException,
                                SecurityException,
                                IllegalStateException,
                                SystemException
    {

        commitCalled = true;
        commitCalls++;
    }

    /**
     * Rolls back the transaction.
     */
    public void rollback() throws IllegalStateException, SecurityException, SystemException
    {
        rollbackCalled = true;
        rollbackCalls++;
    }

    /**
     * Sets the rollback only flag.
     */
    public void setRollbackOnly() throws IllegalStateException, SystemException
    {
        rollbackOnlyCalled = true;
        rollbackOnlyCalls++;
    }

    /**
     * Sets the transaction timeout.
     * @param timeout the transaction timeout
     */
    public void setTransactionTimeout(int timeout) throws SystemException
    {
        transactionTimeout = timeout;
    }
}
