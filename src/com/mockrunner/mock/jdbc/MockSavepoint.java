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
    private boolean rollbacked;
    
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
        rollbacked = false;
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

    public boolean isRollbacked()
    {
        return rollbacked;
    }

    public void setReleased(boolean released)
    {
        this.released = released;
    }

    public void setRollbacked(boolean rollbacked)
    {
        this.rollbacked = rollbacked;
    }
}
