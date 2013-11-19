package com.mockrunner.gen.jar;

import java.net.URL;
import java.net.URLClassLoader;

/*
 * Used by Mockrunner test suite to run the tests against different jars
 */
public class TestConfigurationClassLoader extends URLClassLoader
{
    public TestConfigurationClassLoader(URL[] urls, ClassLoader parent)
    {
        super(urls, parent);
    }
    
    protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException
    {
        Class clazz = findLoadedClass(name);
        if(null != clazz) return clazz;
        try
        {
            clazz = findClass(name);
        }
        catch(ClassNotFoundException exc)
        {
            /*
             * Dirty hack to get around CGLib 2.0.2 (and up) issues.
             * CGLib tries to load some internal enhanced classes
             * with the classloader before recreating them. The system 
             * classloader returns the enhanced version of the previous 
             * run which causes a ClassCastException. The ClassNotFoundException
             * forces CGLib to recreate this classes.
             * This is not necessary for CGGLIB 2.0 and previous.
             */
            if(name.indexOf("ByCGLIB$") != -1)
            {
                throw exc;
            }
            clazz = getParent().loadClass(name);
        }
        if(resolve) 
        {
            resolveClass(clazz);
        }
        return clazz;
    }
}
