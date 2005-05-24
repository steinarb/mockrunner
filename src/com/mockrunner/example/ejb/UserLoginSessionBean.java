package com.mockrunner.example.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;

import com.mockrunner.example.ejb.interfaces.UserEntity;
import com.mockrunner.example.ejb.interfaces.UserEntityHome;

/*
 * @ejb:bean name="UserLoginSession"
 *           display-name="UserLoginSessionBean"
 *           type="Stateless"
 *           transaction-type="Container"
 *           jndi-name="de/test/UserLoginSession"
 * 
 * @ejb:ejb-ref ejb-name="UserEntity" view-type="remote" ref-name="ejb/UserEntity"
 **/
/**
 * Facade session bean for {@link UserEntityBean}.
 */
public class UserLoginSessionBean implements SessionBean
{
    private SessionContext sessionContext;
    
    /*
     * @ejb:interface-method
     * @ejb:transaction type="Required"
     **/
    public boolean loginUser(String username, String password)
    {
        try
        {
            InitialContext context = new InitialContext();
            UserEntityHome home = (UserEntityHome)context.lookup("java:comp/env/ejb/UserEntity");
            try
            {  
                UserEntity user = home.findByPrimaryKey(username);
                if(user.getPassword().equals(password))
                {
                    return true;
                }
                return false;
            }  
            catch(FinderException exc)
            {
                return false;
            }  
        }
        catch(Exception exc)
        {   
            sessionContext.setRollbackOnly();
            throw new EJBException(exc);
        }
    }
    
    /*
     * @ejb:interface-method
     * @ejb:transaction type="Required"
     **/
    public boolean createUser(String username, String password)
    {
        try
        {
            InitialContext context = new InitialContext();
            UserEntityHome home = (UserEntityHome)context.lookup("java:comp/env/ejb/UserEntity");
            try
            {
                UserEntity user = home.create(username, password);
                return true;
            } 
            catch(CreateException exc)
            {
                return false;
            }
        }
        catch(Exception exc)
        {   
            sessionContext.setRollbackOnly();
            throw new EJBException(exc);
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
