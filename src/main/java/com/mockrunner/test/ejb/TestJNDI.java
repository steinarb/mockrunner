package com.mockrunner.test.ejb;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import org.mockejb.jndi.MockContext;
import org.mockejb.jndi.MockContextFactory;

public class TestJNDI
{
    public static void saveProperties(Properties properties)
    {
        String factory = System.getProperty(Context.INITIAL_CONTEXT_FACTORY);
        if(null != factory && !factory.equals(MockContextFactory.class.getName()))
        {
            properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, factory);
        }
        String urlPrefix = System.getProperty(Context.URL_PKG_PREFIXES);
        if(null != urlPrefix && !urlPrefix.equals("org.mockejb.jndi"))
        {
            properties.setProperty(Context.URL_PKG_PREFIXES, factory);
        }
    }
    
    public static void restoreProperties(Properties properties)
    {
        if(null != properties.getProperty(Context.INITIAL_CONTEXT_FACTORY))
        {
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, properties.getProperty(Context.INITIAL_CONTEXT_FACTORY));
        }
        else
        {
            System.getProperties().remove(Context.INITIAL_CONTEXT_FACTORY);
        }
        if(null != properties.getProperty(Context.URL_PKG_PREFIXES))
        {
            System.setProperty(Context.URL_PKG_PREFIXES, properties.getProperty(Context.URL_PKG_PREFIXES));
        }
        else
        {
            System.getProperties().remove(Context.URL_PKG_PREFIXES);
        }
    }
    
    public static void unbind(Context context) throws Exception
    {
        try
        {
            context.unbind("myJNDIName");
        } 
        catch(NamingException exc)
        {
            //ignore
        }
        try
        {
            context.unbind("javax.transaction.UserTransaction");
        } 
        catch(NamingException exc)
        {
            //ignore
        }
        try
        {
            context.unbind("java:comp/UserTransaction");
        } 
        catch(NamingException exc)
        {
            //ignore
        }
    }
    
    public static class TestContextFactory implements InitialContextFactory 
    {
        public Context getInitialContext(Hashtable env) throws NamingException
        {
            return new TestContext();
        }
    }
    
    public static class TestContext implements Context
    {
        public Object addToEnvironment(String propName, Object propVal) throws NamingException
        {
            return null;
        }

        public void bind(Name name, Object obj) throws NamingException
        {
            
        }

        public void bind(String name, Object obj) throws NamingException
        {
            
        }

        public void close() throws NamingException
        {
            
        }

        public Name composeName(Name name, Name prefix) throws NamingException
        {
            return null;
        }

        public String composeName(String name, String prefix) throws NamingException
        {
            return null;
        }

        public Context createSubcontext(Name name) throws NamingException
        {
            return null;
        }

        public Context createSubcontext(String name) throws NamingException
        {
            return null;
        }

        public void destroySubcontext(Name name) throws NamingException
        {
            
        }

        public void destroySubcontext(String name) throws NamingException
        {
            
        }

        public Hashtable getEnvironment() throws NamingException
        {
            return null;
        }

        public String getNameInNamespace() throws NamingException
        {
            return null;
        }

        public NameParser getNameParser(Name name) throws NamingException
        {
            return null;
        }

        public NameParser getNameParser(String name) throws NamingException
        {
            return null;
        }

        public NamingEnumeration list(Name name) throws NamingException
        {
            return null;
        }

        public NamingEnumeration list(String name) throws NamingException
        {
            return null;
        }

        public NamingEnumeration listBindings(Name name) throws NamingException
        {
            return null;
        }

        public NamingEnumeration listBindings(String name) throws NamingException
        {
            return null;
        }

        public Object lookup(Name name) throws NamingException
        {
            return null;
        }

        public Object lookup(String name) throws NamingException
        {
            return "TestObject";
        }

        public Object lookupLink(Name name) throws NamingException
        {
            return null;
        }

        public Object lookupLink(String name) throws NamingException
        {
            return null;
        }

        public void rebind(Name name, Object obj) throws NamingException
        {
            
        }

        public void rebind(String name, Object obj) throws NamingException
        {
            
        }

        public Object removeFromEnvironment(String propName) throws NamingException
        {
            return null;
        }

        public void rename(Name oldName, Name newName) throws NamingException
        {
            
        }

        public void rename(String oldName, String newName) throws NamingException
        {
            
        }

        public void unbind(Name name) throws NamingException
        {
            
        }

        public void unbind(String name) throws NamingException
        {
            
        }    
    }
    
    public static class NullContext extends MockContext
    {
        public NullContext()
        {
            super(null);
        } 
    }
}
