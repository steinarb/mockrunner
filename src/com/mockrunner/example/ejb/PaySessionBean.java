package com.mockrunner.example.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/*
 * @ejb:bean name="PaySession"
 *           display-name="PaySessionBean"
 *           type="Stateless"
 *           transaction-type="Container"
 *           jndi-name="com/mockrunner/example/PaySession"
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
 * This is the EJB-vesrion of 
 * {@link com.mockrunner.example.jdbc.PayAction}.
 * It throws an <code>Exception</code> in case of error.
 */
public class PaySessionBean implements SessionBean
{
    private SessionContext sessionContext;
    
    /*
     * @ejb:interface-method
     * @ejb:transaction type="Required"
     */
    public void payBill(String customerId, String billId, double amount) throws Exception
    {
        InitialContext context = new InitialContext();
        DataSource dataSource = (DataSource)context.lookup("java:comp/env/jdbc/MySQLDB");
        Connection connection = dataSource.getConnection();
        try
        {
            String name = getName(connection, customerId);
            if(null == name)
            {
                sessionContext.setRollbackOnly();
                throw new EJBException("Unknown customer error");
            }
            checkBillIntegrity(connection, customerId, billId, amount);
            markBillAsPaid(connection, customerId, billId, amount);
            System.out.println(amount + " paid from customer " + name);
        }
        catch(SQLException exc)
        {
            sessionContext.setRollbackOnly();
            throw new EJBException("Database error " + exc.getMessage());
        }
        finally
        {
            connection.close();
        }
    }
    
    private String getName(Connection connection, String customerId) throws SQLException
    {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("select name from customers where id=" + customerId);  
        String name = null;
        if(result.next())
        {
            name = result.getString("name");
        }
        result.close();
        statement.close();
        return name;
    }
    
    private void checkBillIntegrity(Connection connection, String customerId, String billId, double amount) throws Exception
    {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("select * from openbills where id=" + billId);
        try
        {
            if(false == result.next())
            {
                sessionContext.setRollbackOnly();
                throw new Exception("Unknown bill error");
            }
            if(!result.getString("customerid").equals(customerId))
            {
                sessionContext.setRollbackOnly();
                throw new Exception("Wrong bill for customer");
            }
            if(result.getDouble("amount") != amount)
            {
                sessionContext.setRollbackOnly();
                throw new Exception("Wrong amount for bill");
            }
        }
        finally
        {
            result.close();
            statement.close();    
        }
    }
    
    private void markBillAsPaid(Connection connection, String customerId, String billId, double amount) throws SQLException
    {
       Statement statement = connection.createStatement();
       statement.executeUpdate("delete from openbills where id=" + billId);
       statement.executeUpdate("insert into paidbills values(" + billId + "," + customerId + "," + amount +  ")");
       statement.close();
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
