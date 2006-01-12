package com.mockrunner.test.ejb;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

public class TestJNDI
{
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
}
