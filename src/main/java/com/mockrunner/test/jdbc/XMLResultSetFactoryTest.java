package com.mockrunner.test.jdbc;

import java.io.File;

import junit.framework.TestCase;

import com.mockrunner.jdbc.XMLResultSetFactory;
import com.mockrunner.mock.jdbc.MockResultSet;

public class XMLResultSetFactoryTest extends TestCase 
{
    /**
     * Test for the Sybase Dialect of the XMLResultSetFactory
     */
    public void testSybaseCreate() 
    {
        XMLResultSetFactory goodSybaseXMLRSF = new XMLResultSetFactory("src/com/mockrunner/test/jdbc/xmltestresult.xml");
        MockResultSet goodMRS = goodSybaseXMLRSF.create("Good-ResultSet-ID");
        assertNotNull(goodSybaseXMLRSF.getXMLFile());
        doTestGoodResultSet(goodSybaseXMLRSF, goodMRS);
        
        goodSybaseXMLRSF = new XMLResultSetFactory("/com/mockrunner/test/jdbc/xmltestresult.xml");
        goodMRS = goodSybaseXMLRSF.create("Good-ResultSet-ID");
        assertNotNull(goodSybaseXMLRSF.getXMLFile());
        doTestGoodResultSet(goodSybaseXMLRSF, goodMRS);
        
        goodSybaseXMLRSF = new XMLResultSetFactory(new File("src/com/mockrunner/test/jdbc/xmltestresult.xml"));
        goodMRS = goodSybaseXMLRSF.create("Good-ResultSet-ID");
        assertNotNull(goodSybaseXMLRSF.getXMLFile());
        doTestGoodResultSet(goodSybaseXMLRSF, goodMRS);
    }
    
    private void doTestGoodResultSet(XMLResultSetFactory goodSybaseXMLRSF, MockResultSet goodMRS)
    {
        assertEquals("Dialects should be equal!", XMLResultSetFactory.SYBASE_DIALECT, goodSybaseXMLRSF.getDialect());
        assertEquals("There should be 2 columns!", 2, goodMRS.getColumnCount());
        assertEquals("There should be 3 rows!", 3, goodMRS.getRowCount());
    }
    
    /**
     * Test for a bad create where there is no actual file 
     * passed to the XMLResultSetFactory
     */
    public void testBadCreate() 
    {
        try
        {
            XMLResultSetFactory badXMLRSF = new XMLResultSetFactory("src/com/mockrunner/test/jdbc/nonexisting.xml");
            assertNull(badXMLRSF.getXMLFile());
            badXMLRSF.create("Bad-ResultSet-ID");
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            XMLResultSetFactory badXMLRSF = new XMLResultSetFactory(new File("src/com/mockrunner/test/jdbc/nonexisting.xml"));
            assertNull(badXMLRSF.getXMLFile());
            badXMLRSF.create("Bad-ResultSet-ID");
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
    }
    
    public void testGetXMLFile()
    {
        XMLResultSetFactory factory = new XMLResultSetFactory("src/com/mockrunner/test/jdbc/xmltestresult.xml");
        assertEquals(new File("src/com/mockrunner/test/jdbc/xmltestresult.xml"), factory.getXMLFile());
        factory = new XMLResultSetFactory(new File("src/com/mockrunner/test/jdbc/xmltestresult.xml"));
        assertEquals(new File("src/com/mockrunner/test/jdbc/xmltestresult.xml"), factory.getXMLFile());
        factory = new XMLResultSetFactory("badfile");
        try
        {
            factory.getXMLFile();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        factory = new XMLResultSetFactory(new File("badfile"));
        try
        {
            factory.getXMLFile();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
    }
}
