package com.mockrunner.example.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/*
 * @ejb:bean name="TestSession"
 *           display-name="TestSessionBean"
 *           type="Stateless"
 *           transaction-type="Container"
 *           jndi-name="com/mockrunner/example/LogSession"
 * 
 * @ejb:resource-ref res-ref-name="jdbc/MySQLDB"
 *                   res-type="javax.sql.DataSource"
 *                   res-auth="Container"
 *                   res-sharing-scope="Shareable"
 * 
 * @jboss:resource-manager res-man-name="jdbc/MySQLDB"
 *                         res-man-jndi-name="java:/MySQLDB"
 */
/**
 * This simple example EJB can be used to write
 * log messages to a database.
 */
public class LogSessionBean implements SessionBean
{
    private SessionContext sessionContext;
    
    /*
     * @ejb:interface-method
     * @ejb:transaction type="Required"
     */
    /**
     * Creates the database <i>logtable</i>
     */
    public void createLogTable()
    {
        Connection connection = null;
        Statement statement = null;
        try
        {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource)context.lookup("java:comp/env/jdbc/MySQLDB");
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            statement.execute("create table logtable(" +
                              "timestamp bigint not null," +
                              "threadid varchar not null," + 
                              "message varchar not null)");
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
            sessionContext.setRollbackOnly();
            throw new EJBException(exc.getMessage());
        }
        finally
        {
            try
            {
                if(null != statement) statement.close();
                if(null != connection) connection.close();
            }
            catch(SQLException sqlExc)
            {
        
            }
        }
    }
    
    /*
     * @ejb:interface-method
     * @ejb:transaction type="Required"
     */
    /**
     * Writes the specified message into the <i>logtable</i>
     * along with the timestamp and the current thread name.
     * @param message the message
     */
    public void logMessage(String message)
    {
        Connection connection = null;
        PreparedStatement statement = null;
        try
        {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource)context.lookup("java:comp/env/jdbc/MySQLDB");
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("insert into logtable values(?, ?, ?)");
            statement.setLong(1, System.currentTimeMillis());
            statement.setString(2, Thread.currentThread().getName());
            statement.setString(3, message);
            statement.executeUpdate();
        }
        catch(Exception exc)
        {
            sessionContext.setRollbackOnly();
            throw new EJBException(exc.getMessage());
        }
        finally
        {
            try
            {
                if(null != statement) statement.close();
                if(null != connection) connection.close();
            }
            catch(SQLException sqlExc)
            {
            
            }
        }
    }
    
    /*
     * @ejb:create-method
     */
    public void ejbCreate() throws CreateException
    {
        
    }
    
    public void ejbActivate() throws EJBException, RemoteException
    {

    }

    public void ejbPassivate() throws EJBException, RemoteException
    {

    }

    public void ejbRemove() throws EJBException, RemoteException
    {

    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException, RemoteException
    {
        this.sessionContext = sessionContext;
    }
}
