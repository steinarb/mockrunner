package com.mockrunner.mock.web;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Mock implementation of <code>Registration</code>.
 */
public class MockRegistration //implements Registration
{
    private String name;
    private String className;
    private Map initParameters;
    
    public MockRegistration(String name, String className)
    {
        this.name = name;
        this.className = className;
        this.initParameters = new HashMap();
    }
    
    public String getName()
    {
        return name;
    }

    public String getClassName()
    {
        return className;
    }
    
    /**
     * Clears the init parameters.
     */
    public void clearInitParameters()
    {
        initParameters.clear();
    }

    public String getInitParameter(String name)
    {
        return (String)initParameters.get(name);
    }

    public Map getInitParameters()
    {
        return initParameters;
    }

    public boolean setInitParameter(String name, String value)
    {
        if(initParameters.containsKey(name)) return false;
        initParameters.put(name, value);
        return true;
    }

    public Set setInitParameters(Map map)
    {
        Set conflicting = new HashSet();
        Iterator names = map.keySet().iterator();
        while(names.hasNext())
        {
            String name = (String)names.next();
            if(null == name)
            {
                throw new IllegalArgumentException("A name in the map is null");
            }
            String value = (String)map.get(name);
            if(null == value)
            {
                throw new IllegalArgumentException("The value for the name " + name + " in the map is null");
            }
            if(initParameters.containsKey(name))
            {
                conflicting.add(name);
            }
        }
        if(conflicting.isEmpty())
        {
            initParameters.putAll(map);
        }
        return conflicting;
    }
}
