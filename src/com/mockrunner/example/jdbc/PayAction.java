package com.mockrunner.example.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * This example simulates the paying of a bill. It checks the id of the customer,
 * the id of the bill and the amount. If an error occurs the transaction is
 * rolled back and an <code>ActionError</code> is created.
 * 
 * This action uses three tables. The table <i>customers</i> has two columns,
 * <i>id</i> and <i>name</i>. The table <i>openbills</i> has three columns,
 * <i>id</i>, <i>customerid</i> and <i>amount</i>. The table <i>paidbills</i>
 * is equivalent to <i>openbills</i>. If a bill is successfully paid, the
 * action deletes the corresponding row from <i>openbills</i> and inserts
 * it into <i>paidbills</i>.
 */
public class PayAction extends Action
{
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception
    {
        PayForm payForm = (PayForm)form;
        ActionMessages errors = new ActionMessages();
        Connection connection = initializeDatabase(); 
        try
        {
            String name = getName(connection, payForm);
            if(null == name)
            {
                createErrorAndRollback(request, connection, errors, "unknown.customer.error");
                return mapping.findForward("failure");
            }
            if(!checkBillIntegrity(request, connection, errors, payForm))
            {
                return mapping.findForward("failure");
            }
            markBillAsPaid(connection, payForm);
            connection.commit();
            System.out.println(payForm.getAmount() + " paid from customer " + name);
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
            createErrorAndRollback(request, connection, errors, "general.database.error");
            return mapping.findForward("failure");
        }
        finally
        {
            connection.close();
        }
        return mapping.findForward("success");
    }

    private Connection initializeDatabase() throws Exception
    {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test");
        connection.setAutoCommit(false);
        return connection;
    }

    private void createErrorAndRollback(HttpServletRequest request, Connection connection, ActionMessages errors, String errorKey) throws SQLException
    {
        ActionMessage error = new ActionMessage(errorKey);
        errors.add(ActionMessages.GLOBAL_MESSAGE, error);
        saveErrors(request, errors);
        connection.rollback();
    }
    
    private String getName(Connection connection, PayForm payForm) throws SQLException
    {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("select name from customers where id='" + payForm.getCustomerId() + "'");  
        String name = null;
        if(result.next())
        {
            name = result.getString("name");
        }
        result.close();
        statement.close();
        return name;
    }
    
    private void markBillAsPaid(Connection connection, PayForm payForm) throws SQLException
    {
       Statement statement = connection.createStatement();
       statement.executeUpdate("delete from openbills where id='" + payForm.getBillId() + "'");
       statement.executeUpdate("insert into paidbills values('" + payForm.getBillId() + "','" + payForm.getCustomerId() + "'," + payForm.getAmount() +  ")");
       statement.close();
    }
    
    private boolean checkBillIntegrity(HttpServletRequest request, Connection connection, ActionMessages errors, PayForm payForm) throws SQLException
    {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("select * from openbills where id='" + payForm.getBillId() + "'");
        try
        {
            if(false == result.next())
            {
                createErrorAndRollback(request, connection, errors, "unknown.bill.error");
                return false;
            }
            if(!result.getString("customerid").equals(payForm.getCustomerId()))
            {
                createErrorAndRollback(request, connection, errors, "wrong.bill.for.customer");
                return false;
            }
            if(result.getDouble("amount") != payForm.getAmount())
            {
                createErrorAndRollback(request, connection, errors, "wrong.amount.for.bill");
                return false;
            }
        }
        finally
        {
            result.close();
            statement.close();    
        }
        return true;
    }
}
