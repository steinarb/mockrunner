package com.mockrunner.example.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This example simulates the order of some books. It iterates through
 * a <code>List</code> of ISBN numbers. If the current quantity is at least
 * one, it reduces the quantity and adds the corresponding ISBN number to
 * the result <code>List</code>.
 * 
 * This example uses one table <i>books</i> with at least the columns
 * <i>isbn</i> and <i>quantity</i>.
 */
public class Bookstore
{
    public static List order(Connection connection, List isbnNumbers) throws SQLException
    {
        ArrayList resultList = new ArrayList();
        Statement statement = null;
        ResultSet result = null;
        try
        {
            connection.setAutoCommit(false);
            StringBuffer query = new StringBuffer("select isbn, quantity from books where (");
            for(int ii = 0; ii < isbnNumbers.size(); ii++)
            {
                query.append("isbn='" + isbnNumbers.get(ii)+ "'");
                if(ii < isbnNumbers.size() - 1)
                {
                    query.append(" or ");
                }
            }
            query.append(")");
            statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            result = statement.executeQuery(query.toString());
            while(result.next())
            {
                int quantity = result.getInt("quantity");
                if(quantity > 0)
                {
                    result.updateInt("quantity", quantity - 1);
                    result.updateRow();
                    resultList.add(result.getString("isbn"));
                }
            }
            connection.commit();
        }
        catch(Exception exc)
        {
            connection.rollback();
        }
        if(null != result) result.close();
        if(null != statement) statement.close();
        return resultList;
    }  
}
