package com.mockrunner.example.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This example class simulates a bank. It can be used to
 * transfer an amount of money from one account to another.
 * It uses a table with the name <i>account</i>. The first column
 * is the account <i>id</i>, the second stores the current <i>balance</i>.
 * The SQL to create the table is
 * 
 * <code>create table account(id int not null primary key,balance int not null)</code>.
 */
public class Bank
{
    private Connection connection;
    
    public void connect() throws SQLException
    {
        disconnect();
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test");
        connection.setAutoCommit(false);
    }
    
    public void disconnect() throws SQLException
    {
        if(null != connection)
        {
            connection.close();
            connection = null;
        }        
    }
    
    public void transfer(int sourceId, int targetId, int amount) throws SQLException
    {  
        PreparedStatement preparedStatement = null;
        try
        {
            if(!isValid(sourceId, amount)) return;
            preparedStatement = connection.prepareStatement("update account set balance=balance+? where id=?");
            preparedStatement.setInt(1, -amount);
            preparedStatement.setInt(2, sourceId);
            preparedStatement.executeUpdate();
            preparedStatement.setInt(1, amount);
            preparedStatement.setInt(2, targetId);
            preparedStatement.executeUpdate();
            connection.commit();
        }
        catch(SQLException exc)
        {
            connection.rollback();
        }
        finally
        {
            if(null != preparedStatement) preparedStatement.close();
        }
    }
    
    private boolean isValid(int sourceId, int amount) throws SQLException
    {
        Statement statement = null;
        ResultSet result = null;
        try
        {
            statement = connection.createStatement();
            result = statement.executeQuery("select balance from account where id=" + sourceId);
            if(!result.next())
            {
                connection.rollback();
                return false;
            }
            int balance = result.getInt(1);
            if(balance < amount)
            {
                connection.rollback();
                return false;
            }
            return true;
        }
        catch(SQLException exc)
        {
            connection.rollback();
            return false;
        }
        finally
        {
            if(null != result) result.close();
            if(null != statement) statement.close();
        }
    }
}
