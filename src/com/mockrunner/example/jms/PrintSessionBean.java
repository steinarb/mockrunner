package com.mockrunner.example.jms;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

/*
 * @ejb:bean name="PrintSession"
 *           display-name="PrintSessionBean"
 *           type="Stateless"
 *           transaction-type="Container"
 *           jndi-name="com/mockrunner/example/PrintSession"
 */
/**
 * This is the EJB version of {@link PrintMessageServlet}.
 * The receiver is {@link PrintMessageDrivenBean}.
 */
public class PrintSessionBean implements SessionBean
{
    private SessionContext sessionContext;
    
    /*
     * @ejb:interface-method
     * @ejb:transaction type="Required"
     */
    public void sendMessage(String customerId)
    {
        QueueConnection queueConnection = null;
        QueueSession queueSession = null;
        QueueSender queueSender = null;
        try
        {   
            InitialContext initialContext = new InitialContext();
            QueueConnectionFactory queueFactory = (QueueConnectionFactory)initialContext.lookup("java:/ConnectionFactory");
            queueConnection = queueFactory.createQueueConnection();
            queueSession = queueConnection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
            Queue queue = (Queue)initialContext.lookup("queue/testQueue");
            TextMessage message = queueSession.createTextMessage(customerId);
            queueSender = queueSession.createSender(queue);
            queueSender.send(message);
        }
        catch(Exception exc)
        {
            sessionContext.setRollbackOnly();
        }
        finally
        {
            try
            {
                if(null != queueSender) queueSender.close();
                if(null != queueSession) queueSession.close();
                if(null != queueConnection) queueConnection.close();
            }
            catch(JMSException exc)
            {
                exc.printStackTrace();
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
