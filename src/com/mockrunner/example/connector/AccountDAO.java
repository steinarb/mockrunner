package com.mockrunner.example.connector;

import java.util.Iterator;

import javax.naming.InitialContext;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.IndexedRecord;
import javax.resource.cci.Interaction;
import javax.resource.cci.LocalTransaction;
import javax.resource.cci.RecordFactory;

/**
 * A DAO that calls a database stored procedure over an resource adapter
 * using local transactions. The stored procedure takes the name of a
 * person and creates an account for this person returning the account id.
 * The stored procedure may be defined like this:
 * <br>
 * <code>PROCEDURE CRTACCT(IN firstname VARCHAR, IN lastname VARCHAR, OUT id INTEGER)</code>
 */
public class AccountDAO
{
    public int createAccount(String firstName, String lastName)
    {
        Connection connection = null;
        LocalTransaction transaction = null;
        try
        {
            InitialContext context = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory)context.lookup("java:/ra/db/ConnectionFactory");
            connection = connectionFactory.getConnection();
            Interaction interaction = connection.createInteraction();
            CCIInteractionSpec spec = new CCIInteractionSpec();
            spec.setFunctionName("CRTACCT");
            spec.setSchema("testschema");
            spec.setCatalog(null);
            RecordFactory recordFactory = connectionFactory.getRecordFactory();
            IndexedRecord inputRecord = recordFactory.createIndexedRecord("Input");
            inputRecord.add(firstName);
            inputRecord.add(lastName);
            transaction = connection.getLocalTransaction();
            transaction.begin();
            IndexedRecord outputRecord = (IndexedRecord)interaction.execute(spec, inputRecord);
            transaction.commit();
            if(null != outputRecord)
            {
                Iterator iterator = outputRecord.iterator();
                if(iterator.hasNext())
                {
                    Integer value = (Integer)iterator.next();
                    return value.intValue();
                }
            }
        }
        catch(Exception exc)
        {
            handleException(exc, transaction);
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
        return -1;
    }

    private void handleException(Exception exc, LocalTransaction transaction)
    {
        if(null != transaction)
        {
            try
            {
                transaction.rollback();
            } 
            catch(ResourceException txExc)
            {
                txExc.printStackTrace();
            }
        }
    }
}
