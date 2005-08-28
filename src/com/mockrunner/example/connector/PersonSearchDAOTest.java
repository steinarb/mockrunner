package com.mockrunner.example.connector;

import java.io.FileInputStream;

import com.mockrunner.connector.ConnectorTestCaseAdapter;
import com.mockrunner.connector.StreamableRecordByteArrayInteraction;
import com.mockrunner.ejb.EJBTestModule;

/**
 * Example test for {@link PersonSearchDAO}. The two files
 * <i>personin.bin</i> and <i>personout.bin</i> are snapshots
 * from a real mainframe communication. Once created, the snapshot
 * files can be used to simulate mainframe access in tests.
 * The <i>personin.bin</i> file represents an empty person with an id of 
 * <i>1</i>, which is the request. The <i>personout.bin</i> contains the 
 * user data for the person with id <i>1</i>. If we search for a user with id 
 * <i>1</i>, the framework recognizes that the actual request matches the expected
 * request and returns the actual response (the <i>personout.bin</i> data).
 * If we pass <i>2</i> as the id, no response is found and <code>findPersonById</code>
 * returns an empty person. This example uses the
 * {@link com.mockrunner.connector.StreamableRecordByteArrayInteraction} which
 * works with byte data. {@link com.mockrunner.connector.StreamableRecordByteArrayInteraction}
 * can always be used when the involved <code>Record</code> classes implement
 * <code>Streamable</code>.
 */
public class PersonSearchDAOTest extends ConnectorTestCaseAdapter
{
    private EJBTestModule ejbModule;
    private PersonSearchDAO dao;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        ejbModule = createEJBTestModule();
        ejbModule.bindToContext("java:/ra/cics/ConnectionFactory", getConnectorMockObjectFactory().getMockConnectionFactory());
        dao = new PersonSearchDAO();
    }
    
    private void prepareInteraction() throws Exception
    {
        StreamableRecordByteArrayInteraction interaction = new StreamableRecordByteArrayInteraction();
        FileInputStream request = new FileInputStream("src/com/mockrunner/example/connector/personin.bin");
        FileInputStream response = new FileInputStream("src/com/mockrunner/example/connector/personout.bin");
        interaction.setExpectedRequest(request);
        interaction.setResponse(response);
        getInteractionHandler().addImplementor(interaction);
        request.close();
        response.close();
    }

    public void testFindPersonByIdFound() throws Exception
    {
        prepareInteraction();
        Person response = dao.findPersonById("1");
        assertEquals("1", response.getId());
        assertEquals("Jane", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals(30, response.getAge());
        verifyConnectionClosed();
        verifyAllInteractionsClosed();
    }
    
    public void testFindPersonByIdNotFound() throws Exception
    {
        prepareInteraction();
        Person response = dao.findPersonById("2");
        assertNull(response.getId());
        verifyConnectionClosed();
        verifyAllInteractionsClosed();
    }
}
