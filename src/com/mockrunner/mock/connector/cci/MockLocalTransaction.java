package com.mockrunner.mock.connector.cci;

import javax.resource.ResourceException;
import javax.resource.cci.LocalTransaction;

/**
 * Mock implementation of <code>LocalTransaction</code>.
 */
public class MockLocalTransaction implements LocalTransaction
{
    private boolean beginCalled;
    private boolean commitCalled;
    private boolean rollbackCalled;
    private int beginCalls;
    private int commitCalls;
    private int rollbackCalls;
    
    public MockLocalTransaction()
    {
        reset();
    }
    
    /**
     * Resets the transaction state. Sets the number of overall 
     * begin, commit and rollback calls to <code>0</code>.
     */
    public void reset()
    {
        beginCalled = false;
        commitCalled = false;
        rollbackCalled = false;
        beginCalls = 0;
        commitCalls = 0;
        rollbackCalls = 0;
    }
    
    /**
     * Starts the transaction. The flags <code>wasCommitCalled</code> 
     * and <code>wasRollbackCalled</code> are reset to <code>false</code>. 
     * This method does not reset the number of overall calls.
     */
    public void begin() throws ResourceException
    {
        beginCalled = true;
        commitCalled = false;
        rollbackCalled = false;
        beginCalls++;
    }

    /**
     * Commits the transaction.
     */
    public void commit() throws ResourceException
    {
        commitCalled = true;
        commitCalls++;
    }

    /**
     * Rolls back the transaction.
     */
    public void rollback() throws ResourceException
    {
        rollbackCalled = true;
        rollbackCalls++;
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
}
