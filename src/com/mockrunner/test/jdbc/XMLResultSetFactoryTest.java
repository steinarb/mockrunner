package com.mockrunner.test.jdbc;

import junit.framework.TestCase;

import com.mockrunner.jdbc.XMLResultSetFactory;
import com.mockrunner.mock.jdbc.MockResultSet;

public class XMLResultSetFactoryTest extends TestCase 
{
    private XMLResultSetFactory goodSybaseXMLRSF;
    private XMLResultSetFactory badXMLRSF;
    private String goodXMLLocation = "src/com/mockrunner/test/jdbc/xmltestresult.xml";
    private String badXMLLocation = "src/com/mockrunner/test/jdbc/nonexisting.xml";
    
    /**
     * Fixture to create instances of all XMLResultSetFactory 
     * objects you need to test your different dialects.
     */
    protected void setUp() throws Exception 
    {
        goodSybaseXMLRSF = new XMLResultSetFactory(goodXMLLocation);
        badXMLRSF = new XMLResultSetFactory(badXMLLocation);
        super.setUp();
    }
    
    /**
     * Fixture to remove instances of all XMLResultSetFactory 
     * objects you need to test your different dialects.
     */
    protected void tearDown() throws Exception 
    {
        goodSybaseXMLRSF = null;
        badXMLRSF = null;
        super.tearDown();
    }

    /**
     * Test for the Sybase Dialect of the XMLResultSetFactory
     */
    public void testSybaseCreate() 
    {
        MockResultSet goodMRS = goodSybaseXMLRSF.create("Good-ResultSet-ID");
        
        //Test good MockResultSet
        assertNotNull("File should not be null!", goodSybaseXMLRSF.getXMLFile());
        assertEquals("Dialects should be equal!", XMLResultSetFactory.SYBASE_DIALECT, goodSybaseXMLRSF.getDialect());
        assertEquals("There should be 2 columns!", 2, goodMRS.getColumnCount());
        assertEquals("There should be 3 rows!", 3, goodMRS.getRowCount());
    }
    
    /**
     * Test for a bad create where there is no actual file 
     * passed to the XMLResultSetFactory constructure resulting 
     * in defaults.
     */
    public void testBadCreate() 
    {
        MockResultSet badMRS = badXMLRSF.create("Bad-ResultSet-ID");
        
        //Test bad MockResultSet
        assertNull("File should be null!", badXMLRSF.getXMLFile());
        assertEquals("Dialects should be equal!", XMLResultSetFactory.SYBASE_DIALECT, goodSybaseXMLRSF.getDialect());
        assertEquals("There should be 0 columns!", 0, badMRS.getColumnCount());
        assertEquals("There should be 0 rows!", 0, badMRS.getRowCount());
    }
}
