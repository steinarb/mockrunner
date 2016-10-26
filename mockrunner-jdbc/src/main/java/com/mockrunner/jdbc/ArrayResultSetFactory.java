package com.mockrunner.jdbc;

import com.mockrunner.mock.jdbc.MockResultSet;

/**
 * A <code>ResultSetFactory</code> implementation which will produce 
 * <code>MockResultSet</code> instances based on information given as 
 * <code>String</code> arrays.
 * 
 * <p>
 * <code>StringValuesTable</code> and <code>ArrayResultSetFactory</code> can 
 * provide easy set up of unit test fixtures and assertion of outcomes with the
 * same data structures, without any need for external sources of test data:
 * </p>
 * 
 * <p>
 * <pre>
 *  private static final String _SQL_SELECT_ALL_EMPLOYEES = 
 *      "SELECT * FROM employee";
 *  private StringValuesTable <b>_employeeQueryResults</b>;
 *  ArrayResultSetFactory <b>_arrayResultSetFactory</b>;
 *  private Employee[] _employees;
 *  
 *  protected void setUp() throws Exception {
 *    super.setUp();
 *    <b>_employeeQueryResults</b> = new StringValuesTable(
 *        "employeeQueryResults", 
 *        new String[] {
 *            "id", "lastname", "firstname",
 *        }, 
 *        new String[][] {
 *            new String[] {"1", "gibbons", "peter"},
 *            new String[] {"2", "lumbergh", "bill"},
 *            new String[] {"3", "waddams", "milton"},
 *        }
 *    );
 *    _employees = new Employee[3] {
 *        new Employee(
 *            <b>_employeeQueryResults.getItem(1, "id")</b>,
 *            <b>_employeeQueryResults.getItem(1, "lastname")</b>,
 *            <b>_employeeQueryResults.getItem(1, "firstname")</b>,
 *        ),
 *        ...
 *    };
 *    ...
 *  }
 *    
 *  public void testGetEmployees() throws Exception {
 *    PreparedStatementResultSetHandler preparedStatementResultSetHandler = 
 *        getPreparedStatementResultSetHandler();
 *    <b>_arrayResultSetFactory</b> = 
 *        new ArrayResultSetFactory(<b>_employeeQueryResults</b>);
 *    MockResultSet resultSet = 
 *        preparedStatementResultSetHandler.createResultSet(
 *            <b>_employeeQueryResults.getName()</b>, 
 *            <b>arrayResultSetFactory</b>);
 *    preparedStatementResultSetHandler.prepareResultSet(
 *        _SQL_SELECT_ALL_EMPLOYEES, resultSet);
 *        
 *    // execute query, perhaps calling method on an EmployeeDAO...
 *    
 *    assertEquals(
 *        <b>_employeeQueryResults.getNumberOfRows()</b>, 
 *        resultsList.size());
 *    for (int i = 0; i < _employees.length; i++) {
 *       assertTrue(resultsList.contains(_employees[i]));
 *    }
 *    MockResultSet mockResultSet = 
 *        preparedStatementResultSetHandler.getResultSet(
 *            SQL_SELECT_ALL_EMPLOYEES);
 *    int rows = mockResultSet.getRowCount();
 *    for (int row = 1; row <= rows; row++) {
 *      verifyResultSetRow(
 *          <b>_employeeQueryResults.getName()</b>, 
 *          row, <b>_employeeQueryResults.getRow(row)</b>);
 *    }
 *    verifySQLStatementExecuted(_SQL_SELECT_ALL_EMPLOYEES);
 *    verifyAllResultSetsClosed();
 *    verifyAllStatementsClosed();
 *    verifyConnectionClosed();     
 *  }
 * </pre>
 * </p>
* 
 * @author Erick G. Reid
 */
public class ArrayResultSetFactory implements ResultSetFactory
{
    private String[] columnNames = new String[0];
    private String[][] stringMatrix = new String[0][0];

    /**
     * Creates a new <code>ArrayResultSetFactory</code> that will produce
     * result sets based on information in the given
     * <code>StringValuesTable</code>.
     * 
     * @param stringValuesTable the <code>StringValuesTable</code> to use. This argument
     *                          cannot be <code>null</code>.
     */
    public ArrayResultSetFactory(StringValuesTable stringValuesTable)
    {
        if (stringValuesTable != null)
        {
            this.stringMatrix = stringValuesTable.getStringMatrix();
            this.columnNames = stringValuesTable.getColumnNames();
            return;
        }
        throw new IllegalArgumentException("the string table cannot be null");
    }

    /**
     * Creates a new <code>ArrayResultSetFactory</code> with the given matrix
     * for data representation.
     * 
     * @param stringMatrix the data representation for the result sets this factory will
     *                     produce. This argument cannot be <code>null</code>, must
     *                     not contain any null values, and each array in the matrix must
     *                     contain the same number of elements as the first (<code>stringMatrix[0].length == stringMatrix[n].length</code>
     *                     for any given valid row number, <code>n</code>). Further,
     *                     this matrix must, at a minimum represent <code>1</code> row
     *                     and <code>1</code> column of items (<code>stringMatrix.length >= 1</code>,
     *                     and <code>stringMatrix[0].length >= 1</code>).
     */
    public ArrayResultSetFactory(String[][] stringMatrix)
    {
        this.stringMatrix = StringValuesTable.verifyStringMatrix(stringMatrix);
    }

    /**
     * Creates a new <code>ArrayResultSetFactory</code> with the given set of
     * column names and the given matrix for data representation.
     * 
     * @param columnNames the column names for the result sets this factory will
     *                    produce. This argument may be <code>null</code> if no column
     *                    names are desired, but if a non-<code>null</code> array
     *                    reference is given, the array cannot contain any
     *                    <code>null</code> nor duplicate elements, and must have the
     *                    same number of elements as there are columns in the given
     *                    string matrix (<code>stringMatrix[n]</code> for any given
     *                    valid row number, <code>n</code>).
     * @param stringMatrix the data representation for the result sets this factory will
     *                     produce. This argument cannot be <code>null</code>, must
     *                     not contain any null values, and each array in the matrix must
     *                     contain the same number of elements as the first (<code>stringMatrix[0].length == stringMatrix[n].length</code>
     *                     for any given valid row number, <code>n</code>). Further,
     *                     this matrix must, at a minimum represent <code>1</code> row
     *                     and <code>1</code> column of items (<code>stringMatrix.length >= 1</code>,
     *                     and <code>stringMatrix[0].length >= 1</code>).
     */
    public ArrayResultSetFactory(String[] columnNames, String[][] stringMatrix)
    {
        this.stringMatrix = StringValuesTable.verifyStringMatrix(stringMatrix);
        if (columnNames != null)
        {
            this.columnNames = StringValuesTable.verifyColumnNames(columnNames, stringMatrix);
        }
    }

    /**
     * Returns a <code>MockResultSet</code> with the given ID, containing
     * values based on the elements given at construction.
     * 
     * @param id the ID for the result set. This argument cannot be
     *           <code>null</code>.
     */
    public MockResultSet create(String id)
    {
        if (id != null)
        {
            MockResultSet resultSet = new MockResultSet(id);
            if (columnNames != null)
            {
                for (String columnName : columnNames) {
                    resultSet.addColumn(columnName);
                }
            }
            for (String[] aStringMatrix : stringMatrix) {
                resultSet.addRow(aStringMatrix);
            }
            return resultSet;
        }
        throw new IllegalArgumentException("the result set ID cannot be null");
    }
}
