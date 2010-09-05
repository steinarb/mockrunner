package com.mockrunner.mock.web;


/**
 * Mock implementation of <code>ServletRegistration</code>.
 */
public class MockServletRegistration extends MockRegistration //implements ServletRegistration, ServletRegistration.Dynamic
{
    /*private String runAsRole;
    private Set mappings;
    private int loadOnStartup;
    private MultipartConfigElement multipartConfigElement;
    private ServletSecurityElement servletSecurityElement;
    private Set securityMappings;
    
    public MockServletRegistration()
    {
        this(null, null);
    }
    
    public MockServletRegistration(String name, String className)
    {
        super(name, className);
        this.runAsRole = null;
        this.mappings = new HashSet();
        this.loadOnStartup = -1;
        this.securityMappings = new HashSet();
 
    }

    public Set addMapping(String[] urlPatterns)
    {
        if(null == urlPatterns)
        {
            throw new IllegalArgumentException("mappings is null");
        }
        if(urlPatterns.length == 0)
        {
            throw new IllegalArgumentException("mappings is empty");
        }
        Set conflicts = new HashSet();
        for(int ii = 0; ii < urlPatterns.length; ii++)
        {
            if(mappings.contains(urlPatterns[ii]))
            {
                conflicts.add(urlPatterns[ii]);
            }
        }
        if(conflicts.isEmpty())
        {
            mappings.addAll(Arrays.asList(urlPatterns));
        }
        return conflicts;
    }

    public Collection getMappings()
    {
        return Collections.unmodifiableCollection(mappings);
    }
    
    *//**
     * Clears the mappings.
     *//*
    public void clearMappings()
    {
        mappings.clear();
    }

    *//**
     * Returns the multipart config.
     * 
     * @return the multipart config
     *//*
    public MultipartConfigElement getMultipartConfig()
    {
        return multipartConfigElement;
    }

    *//**
     * Sets the multipart config.
     * 
     * @param multipartConfigElement the multipart config
     *//*
    public void setMultipartConfig(MultipartConfigElement multipartConfigElement)
    {
        this.multipartConfigElement = multipartConfigElement;
    }

    *//**
     * Returns the applied servlet security.
     * 
     * @return the servlet security
     *//*
    public ServletSecurityElement getServletSecurity()
    {
        return servletSecurityElement;
    }

    *//**
     * Sets the servlet security. Simply keeps the passed value without keeping track of
     * mappings. The method returns the <code>Set</code> set by {@link #setServletSecurityMappings}.
     * 
     * @param servletSecurityElement the servlet security
     * @return the <code>Set</code> set by {@link #setServletSecurityMappings}
     *//*
    public Set setServletSecurity(ServletSecurityElement servletSecurityElement)
    {
        this.servletSecurityElement = servletSecurityElement;
        return securityMappings;
    }
    
    *//**
     * Sets the mappings that should be returned by {@link #setServletSecurity}.
     * These mappings are configured otherwise, which is not supported here.
     * 
     * @param securityMappings the mappings
     *//*
    public void setServletSecurityMappings(Set securityMappings)
    {
        this.securityMappings = securityMappings;
    }

    *//**
     * Returns the load on startup value.
     * 
     * @return the load on startup value
     *//*
    public int getLoadOnStartup()
    {
        return loadOnStartup;
    }

    *//**
     * Sets the load on startup value.
     * 
     * @param loadOnStartup the load on startup value
     *//*
    public void setLoadOnStartup(int loadOnStartup)
    {
        this.loadOnStartup = loadOnStartup;
    }

    *//**
     * Sets the runAs role.
     * 
     * @param runAsRole the runAs role
     *//*
    public void setRunAsRole(String runAsRole)
    {
        this.runAsRole = runAsRole;
    }

    *//**
     * Returns the runAs role.
     * 
     * @return the runAs role
     *//*
    public String getRunAsRole()
    {
        return runAsRole;
    }*/
}
