package com.mockrunner.example.jms;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This example servlet registers a listener for a
 * test queue. On each request a customer id is
 * extracted an sent as a text message to the queue.
 * The example simulates an online bank. The user
 * can request a printout of his account data.
 * This is done in an asynchronous manner, so
 * the user gets an immediate response.
 */
public class PrintMessageServlet extends HttpServlet
{
    public void init() throws ServletException
    {
        try
        {
            InitialContext initialContext = new InitialContext();
            QueueConnectionFactory queueFactory = (QueueConnectionFactory)initialContext.lookup("java:/ConnectionFactory");
            QueueConnection queueConnection = queueFactory.createQueueConnection();
            QueueSession queueSession = queueConnection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
            Queue queue = (Queue)initialContext.lookup("queue/testQueue");
            QueueReceiver receiver = queueSession.createReceiver(queue);
            receiver.setMessageListener(new PrintMessageListener());
            queueConnection.start();
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String customerId = request.getParameter("customerId");
        QueueConnection queueConnection = null;
        QueueSession queueSession = null;
        QueueSender queueSender = null;
        try
        {   
            InitialContext initialContext = new InitialContext();
            QueueConnectionFactory queueFactory = (QueueConnectionFactory)initialContext.lookup("java:/ConnectionFactory");
            queueConnection = queueFactory.createQueueConnection();
            queueSession = queueConnection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
            Queue queue = (Queue)initialContext.lookup("queue/testQueue");
            TextMessage message = queueSession.createTextMessage(customerId);
            queueSender = queueSession.createSender(queue);
            queueSender.send(message);
            response.getWriter().write("Print request for " + customerId + " successfully sent");
        }
        catch(Exception exc)
        {
            response.getWriter().write("Error sending print request for " + customerId);
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
}
