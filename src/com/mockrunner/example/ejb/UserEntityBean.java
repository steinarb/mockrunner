package com.mockrunner.example.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.NoSuchEntityException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/*
 * @ejb:bean name="UserEntity"
 *           display-name="UserEntity"
 *           type="BMP"
 *           jndi-name="de/test/UserEntity"
 *
 * @ejb:pk class="java.lang.String"
 *
 * @ejb:transaction type="Required"
 * 
 * @ejb:resource-ref res-ref-name="jdbc/MySQLDB"
 *                   res-type="javax.sql.DataSource"
 *                   res-auth="Container"
 *                   res-sharing-scope="Shareable"
 * 
 * @jboss:resource-manager res-man-name="jdbc/MySQLDB" res-man-jndi-name="java:/MySQLDB"
 **/
/**
 * Implementation of a BMP entity bean representing
 * a user with a username and a password.
 */
public class UserEntityBean implements EntityBean
{
    private EntityContext entityContext;
    private DataSource dataSource;
    
    private String username;
    private String password;

    /*
     * @ejb:interface-method
     **/
    public String getPassword()
    {
        return password;
    }
    
    /*
     * @ejb:interface-method
     **/
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    /*
     * @ejb:interface-method
     **/
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    /*
     * @ejb:create-method
     **/
    public String ejbCreate(String username, String password) throws CreateException
    {
        Connection connection = null;
        PreparedStatement statement = null;
        try
        {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("insert into usertable values(?, ?)");
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
            this.username = username;
            this.password = password;
            return username;
        } 
        catch(SQLException exc)
        {
            throw new CreateException(exc.getMessage());
        }
        finally
        {
            try
            {
                if(null != statement) statement.close();
                if(null != connection) connection.close();
            } 
            catch(SQLException exc)
            {
                
            }
        }
    }

    public void ejbPostCreate(String username, String password) throws CreateException
    {

    }

    public String ejbFindByPrimaryKey(String username) throws FinderException
    {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try
        {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("select username from usertable where username=?");
            statement.setString(1, username);
            result = statement.executeQuery();
            if(!result.next())
            {
                throw new ObjectNotFoundException("No user with username " + username + " found");
            }
            return result.getString(1);
        } 
        catch(SQLException exc)
        {
            throw new EJBException(exc);
        }
        finally
        {
            try
            {
                if(null != result) result.close();
                if(null != statement) statement.close();
                if(null != connection) connection.close();
            } 
            catch(SQLException exc)
            {
                
            }
        }
    }

    public Collection ejbFindAll() throws FinderException
    {
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try
        {
            connection = dataSource.getConnection();
            List foundKeys = new ArrayList();
            statement = connection.createStatement();
            result = statement.executeQuery("select username from usertable");
            while(result.next())
            {
                foundKeys.add(result.getString(1));
            }
            return foundKeys;
        } 
        catch(SQLException exc)
        {
            throw new EJBException(exc);
        }
        finally
        {
            try
            {
                if(null != result) result.close();
                if(null != statement) statement.close();
                if(null != connection) connection.close();
            } 
            catch(SQLException exc)
            {
                
            }
        }
    }

    public void ejbLoad() throws EJBException, RemoteException
    {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try
        {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("select * from usertable where username=?");
            statement.setString(1, (String)entityContext.getPrimaryKey());
            result = statement.executeQuery();
            if(result.next())
            {
                this.username = result.getString(1);
                this.password = result.getString(2);
            }
            else
            {
                throw new NoSuchEntityException("Entity for key " + entityContext.getPrimaryKey() + " not found");
            }
        } 
        catch(SQLException exc)
        {
            throw new EJBException(exc);
        }
        finally
        {
            try
            {
                if(null != result) result.close();
                if(null != statement) statement.close();
                if(null != connection) connection.close();
            } 
            catch(SQLException exc)
            {
                
            }
        }
    }

    public void ejbRemove() throws RemoveException, EJBException, RemoteException
    {
        Connection connection = null;
        PreparedStatement statement = null;
        try
        {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("delete from usertable where username=?");
            statement.setString(1, (String)entityContext.getPrimaryKey());
            int updateCount = statement.executeUpdate();
            if(updateCount < 1)
            {
                throw new RemoveException("Delete error for key " + entityContext.getPrimaryKey());
            }
        } 
        catch(SQLException exc)
        {
            throw new EJBException(exc);
        }
        finally
        {
            try
            {
                if(null != statement) statement.close();
                if(null != connection) connection.close();
            } 
            catch(SQLException exc)
            {
                
            }
        }
    }

    public void ejbStore() throws EJBException, RemoteException
    {
        Connection connection = null;
        PreparedStatement statement = null;
        try
        {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("update usertable set password=? where username=?");
            statement.setString(1, this.password);
            statement.setString(2, this.username);
            int updateCount = statement.executeUpdate();
            if(updateCount < 1)
            {
                throw new NoSuchEntityException("Entity for key " + username + " not found");
            }
        } 
        catch(SQLException exc)
        {
            throw new EJBException(exc);
        }
        finally
        {
            try
            {
                if(null != statement) statement.close();
                if(null != connection) connection.close();
            } 
            catch(SQLException exc)
            {
                
            }
        }
    }
    
    public void ejbActivate() throws EJBException, RemoteException
    {

    }
    
    public void ejbPassivate() throws EJBException, RemoteException
    {

    }

    public void setEntityContext(EntityContext entityContext) throws EJBException, RemoteException
    {
        this.entityContext = entityContext;
        try
        {
            InitialContext context = new InitialContext();
            dataSource = (DataSource)context.lookup("java:comp/env/jdbc/MySQLDB");
        }
        catch(Exception exc)
        {
            throw new EJBException(exc);
        }
    }

    public void unsetEntityContext() throws EJBException, RemoteException
    {
        entityContext = null;
        dataSource = null;
    }
}
