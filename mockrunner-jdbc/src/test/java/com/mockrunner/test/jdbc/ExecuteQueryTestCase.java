package com.mockrunner.test.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.mockrunner.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockResultSet;

/**
 * @author cemartins
 *
 */
public class ExecuteQueryTestCase extends TestCase {
	
	private List<String> isbnNumbers;
	
	protected void setUp() throws Exception {
		isbnNumbers = new ArrayList<String>();
		isbnNumbers.add("980979790734");
		isbnNumbers.add("t84y0rtu8ry");
	}

	
	/**
	 * Test issue #8 filed by KarstenHoehne 
	 * @throws Exception
	 */
	public void testExecuteQueryWithNoResultSet() throws Exception {
		
		List<String> resultList = new ArrayList<String>();
		
		Connection connection = new MockConnection();
        connection.setAutoCommit(false);

        String stmt = "select isbn, quantity from books where isbn = ?";
        PreparedStatement pStmt = connection.prepareStatement(stmt);
        
        for (int c = 0; c < isbnNumbers.size(); c++) {
            pStmt.setString(1, (String) isbnNumbers.get(c));
            ResultSet result = pStmt.executeQuery();
            System.out.println(result);
            while (result.next()) {
                int quantity = result.getInt("quantity");
                System.out.println(quantity);
                if (quantity > 0) {
                    result.updateInt("quantity", quantity - 1);
                    result.updateRow();
                    resultList.add(result.getString("isbn"));
                }
            }
        }
        connection.commit();
        connection.close();
        
	}
	
	public void testExecuteQueryWithResultSetHandler() throws Exception {
		
		List<String> resultList = new ArrayList<String>();
		
		MockConnection connection = new MockConnection();
        connection.setAutoCommit(false);

        String stmt = "select isbn, quantity from books where isbn = ?";
        PreparedStatement pStmt = connection.prepareStatement(stmt);
        
        PreparedStatementResultSetHandler preparedStatementResultSetHandler = connection.getPreparedStatementResultSetHandler();
        
        for (int c = 0; c < isbnNumbers.size(); c++) {
            pStmt.setString(1, (String) isbnNumbers.get(c));
            
            MockResultSet resultSet = new MockResultSet("");
            resultSet.addColumn("isbn");
            resultSet.addColumn("quantity");
    		resultSet.addRow(new String[] { isbnNumbers.get(c), String.valueOf(10 + c)} );
    		
    		preparedStatementResultSetHandler.prepareResultSet(stmt, resultSet, new Object[] {isbnNumbers.get(c)});
            
            ResultSet result = pStmt.executeQuery();
            ((MockResultSet) result).setResultSetConcurrency(ResultSet.CONCUR_UPDATABLE);
            System.out.println(result);
            while (result.next()) {
                int quantity = result.getInt("quantity");
                System.out.println(quantity);
                if (quantity > 0) {
                    result.updateInt("quantity", quantity - 1);
                    result.updateRow();
                    resultList.add(result.getString("isbn"));
                }
            }
        }
        connection.commit();
        connection.close();
        
	}

}
