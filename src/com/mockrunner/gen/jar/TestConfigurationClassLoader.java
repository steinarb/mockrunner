package com.mockrunner.gen.jar;

import java.net.URL;
import java.net.URLClassLoader;

public class TestConfigurationClassLoader extends URLClassLoader
{
    public TestConfigurationClassLoader(URL[] urls, ClassLoader parent)
    {
        super(urls, parent);
    }
    
    protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException
    {
        Class clazz = null;
        try
        {
            clazz = findLoadedClass(name);
            if(null == clazz)
            {
                clazz = findClass(name);
            }
        }
        catch (ClassNotFoundException exc)
        {
            clazz = getParent().loadClass(name);

        }
        if(resolve) 
        {
            resolveClass(clazz);
        }
        return clazz;
    }
}
