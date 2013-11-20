package com.mockrunner.example.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;

import com.mockrunner.example.ejb.interfaces.BillEntity;
import com.mockrunner.example.ejb.interfaces.BillEntityHome;

/*
 * @ejb:bean name="BillManagerSession"
 *           display-name="BillManagerSessionBean"
 *           type="Stateless"
 *           transaction-type="Container"
 *           jndi-name="de/test/BillManagerSession"
 * 
 * @ejb:ejb-ref ejb-name="BillEntity" view-type="remote" ref-name="ejb/BillEntity"
 **/
/**
 * This simple EJB finds all {@link BillEntityBean} objects
 * which are not paid and marks them as paid.
 */
public class BillManagerSessionBean implements SessionBean
{
    private SessionContext sessionContext;
    
    /*
     * @ejb:interface-method
     * @ejb:transaction type="Required"
     **/
    public void markAsPaid()
    {
        try
        {
            InitialContext context = new InitialContext();
            BillEntityHome home = (BillEntityHome)context.lookup("java:comp/env/ejb/BillEntity");
            Collection unpaid = home.findUnpaid();
            Iterator unpaidIterator = unpaid.iterator();
            while(unpaidIterator.hasNext())
            {
                BillEntity next = (BillEntity)unpaidIterator.next();
                next.setPaid(true);
            }
        }
        catch(Exception exc)
        {   
            sessionContext.setRollbackOnly();
            throw new EJBException(exc.getMessage());
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
