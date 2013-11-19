package com.mockrunner.mock.web;


/**
 * Mock implementation of <code>Registration</code>.
 */
public class MockRegistration //implements Registration, Registration.Dynamic
{
    /*private String name;
    private String className;
    private Map initParameters;
    private boolean isAsyncSupported;
    
    public MockRegistration()
    {
        this(null, null);
    }
    
    public MockRegistration(String name, String className)
    {
        this.name = name;
        this.className = className;
        this.initParameters = new HashMap();
        this.isAsyncSupported = false;
    }
    
    *//**
     * Returns the name.
     * 
     * @return the name
     *//*
    public String getName()
    {
        return name;
    }

    *//**
     * Returns the class name.
     * 
     * @return the class name
     *//*
    public String getClassName()
    {
        return className;
    }
    
    *//**
     * Sets the name.
     * 
     * @param name the name
     *//*
    public void setName(String name)
    {
        this.name = name;
    }

    *//**
     * Sets the class name.
     * 
     * @param className the class name
     *//*
    public void setClassName(String className)
    {
        this.className = className;
    }

    *//**
     * Clears the init parameters.
     *//*
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
        return Collections.unmodifiableMap(initParameters);
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

    *//**
     * Returns if asynchronous operations are supported.
     * 
     * @return <code>true</code> if asynchronous operations are supported, <code>false</code> otherwiese
     *//*
    public boolean isAsyncSupported()
    {
        return isAsyncSupported;
    }

    public void setAsyncSupported(boolean isAsyncSupported)
    {
        this.isAsyncSupported = isAsyncSupported;
    }*/
}
