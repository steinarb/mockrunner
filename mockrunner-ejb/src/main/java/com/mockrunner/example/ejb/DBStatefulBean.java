package com.mockrunner.example.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

/*
 * @ejb:bean name="DBStateful"
 *           display-name="DBStatefulBean"
 *           type="Stateful"
 *           transaction-type="Bean"
 *           jndi-name="com/mockrunner/example/DBStateful"
 **/
/**
 * A simple BMT stateful session bean with a method that
 * executes an SQL statement.
 */
public class DBStatefulBean implements SessionBean
{
    private SessionContext sessionContext;
    private UserTransaction transaction;
    
    /*
     * @ejb:interface-method
     **/
    public void beginTransaction()
    {
        try
        {
            transaction = sessionContext.getUserTransaction();
            transaction.begin();
        } 
        catch(Exception exc)
        {
            throw new EJBException(exc);
        }
    }
    
    /*
     * @ejb:interface-method
     **/
    public void executeSQL(String sql)
    {
        Connection connection = null;
        Statement statement = null;
        try
        {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource)context.lookup("java:/MySQLDB");
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            statement.execute(sql);
        }
        catch(Exception exc)
        {
            throw new EJBException(exc.getMessage());
        }
        finally
        {
            try
            {
                if(null != statement) statement.close();
                if(null != connection) connection.close();
            }
            catch(SQLException ignored)
            {
        
            }
        }
    }
    
    /*
     * @ejb:interface-method
     **/
    public void endTransaction(boolean commit)
    {
        try
        {
            if(commit)
            {
                transaction.commit();
            }
            else
            {
                transaction.rollback();
            }
        } 
        catch(Exception exc)
        {
            try
            {
                transaction.rollback();
            } 
            catch(Exception exc2)
            {
                throw new EJBException(exc2);
            }
        }
    }

    /*
     * @ejb:create-method
     **/
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
