package com.mockrunner.example.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This example calls a stored procedure that returns all the names
 * for a specified date.
 */
public class OrderDB
{
    public static List getNames(Date date) throws SQLException
    {
        Connection connection = DriverManager.getConnection("jdbc:db2://127.0.0.1/test");
        CallableStatement statement = connection.prepareCall("call getnames(?)");
        statement.setDate(1, date);
        ResultSet result = statement.executeQuery();
        List resultList = new ArrayList();
        while(result.next())
        {
            resultList.add(result.getString("name"));
        }
        result.close();
        statement.close();
        connection.close();
        return resultList;
    }
}
