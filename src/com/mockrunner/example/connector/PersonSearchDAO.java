package com.mockrunner.example.connector;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.Interaction;
import javax.resource.cci.InteractionSpec;

/**
 * A DAO that communicates with the CICS backend to find a person
 * by id.
 * Please note that the inner class <code>ECIInteractionSpec</code> is
 * only necessary because we cannot ship the suitable IBM classes
 * along with this simple example. In reality, the class
 * <code>com.ibm.connector2.cics.ECIInteractionSpec</code>
 * should be used.
 */
public class PersonSearchDAO
{
    private ConnectionFactory connectionFactory;

    public PersonSearchDAO()
    {
        try
        {
            InitialContext context = new InitialContext();
            connectionFactory = (ConnectionFactory)context.lookup("java:/ra/cics/ConnectionFactory");
        } 
        catch(NamingException exc)
        {
            throw new RuntimeException("Failed to create ConnectionFactory " + exc.getMessage());
        }
    }

    public Person findPersonById(String id)
    {
        Connection connection = null;
        Person request = new Person();
        request.setId(id);
        Person response = new Person();
        try
        {
            connection = connectionFactory.getConnection();
            Interaction interaction = connection.createInteraction();
            ECIInteractionSpec interactionSpec = new ECIInteractionSpec();
            interactionSpec.setFunctionName("PER3AC");
            interactionSpec.setInteractionVerb(ECIInteractionSpec.SYNC_SEND_RECEIVE);
            interactionSpec.setCommareaLength(32);
            interaction.execute(interactionSpec, request, response);
        } 
        catch(ResourceException exc)
        {
            exc.printStackTrace();
        }
        finally
        {
            try
            {
                if(null != connection) connection.close();
            } 
            catch(ResourceException exc)
            {
                exc.printStackTrace();
            }
        }
        return response;
    }
    
    /*
     * Replacement for com.ibm.connector2.cics.ECIInteractionSpec.
     * Only exists for the sake of this simple demonstration.
     */
    private class ECIInteractionSpec implements InteractionSpec
    {
        private String functionName;
        private int commareaLength;
        private int interactionVerb;
        
        public int getCommareaLength()
        {
            return commareaLength;
        }
        
        public void setCommareaLength(int commareaLength)
        {
            this.commareaLength = commareaLength;
        }
        
        public String getFunctionName()
        {
            return functionName;
        }
        
        public void setFunctionName(String functionName)
        {
            this.functionName = functionName;
        }
        
        public int getInteractionVerb()
        {
            return interactionVerb;
        }
        
        public void setInteractionVerb(int interactionVerb)
        {
            this.interactionVerb = interactionVerb;
        }
    }
}
