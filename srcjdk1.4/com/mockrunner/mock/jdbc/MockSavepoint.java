package com.mockrunner.mock.jdbc;

import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * Mock implementation of <code>Savepoint</code>.
 */
public class MockSavepoint implements Savepoint
{
    private static int idCount = 0;
    
    private String name;
    private int id;
    private int number;
    private boolean released;
    private boolean rolledback;
    
    public MockSavepoint(int number)
    {
        this("", number);
    }
    
    public MockSavepoint(String name, int number)
    {
        this.name = name;
        this.id = idCount++;
        this.number = number;
        released = false;
        rolledback = false;
    }
    
    public int getSavepointId() throws SQLException
    {
        return id;
    }

    public String getSavepointName() throws SQLException
    {
        return name;
    }
    
    public int getNumber()
    {
        return number;
    }
 
    public boolean isReleased()
    {
        return released;
    }
    
    /**
     * @deprecated use {@link #isRolledBack}
     */
    public boolean isRollbacked()
    {
        return isRolledBack();
    }

    public boolean isRolledBack()
    {
        return rolledback;
    }

    public void setReleased(boolean released)
    {
        this.released = released;
    }
    
    /**
     * @deprecated use {@link #setRolledBack}
     */
    public void setRollbacked(boolean rollbacked)
    {
        setRolledBack(rollbacked);
    }

    public void setRolledBack(boolean rollbacked)
    {
        this.rolledback = rollbacked;
    }
}
