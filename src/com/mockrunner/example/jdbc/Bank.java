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
 * Check out {@link com.mockrunner.example.servlet.LogoutServletTest} to see how to test this class.
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
        
        try
        {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select balance from account where id=" + sourceId);
            if(!result.next())
            {
                result.close();
                statement.close();
                connection.rollback();
                return;
            }
            int balance = result.getInt(1);
            result.close();
            statement.close();
            if(balance < amount)
            {
                connection.rollback();
                return;
            }
            PreparedStatement preparedStatment = connection.prepareStatement("update account set balance=balance+? where id=?");
            preparedStatment.setInt(1, -amount);
            preparedStatment.setInt(2, sourceId);
            preparedStatment.executeUpdate();
            preparedStatment.setInt(1, amount);
            preparedStatment.setInt(2, targetId);
            preparedStatment.executeUpdate();
            connection.commit();
            preparedStatment.close();
        }
        catch(SQLException exc)
        {
            connection.rollback();
        }
    }
}
