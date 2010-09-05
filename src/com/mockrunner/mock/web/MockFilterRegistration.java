package com.mockrunner.mock.web;

/**
 * Mock implementation of <code>FilterRegistration</code>.
 */
public class MockFilterRegistration extends MockRegistration //implements FilterRegistration, FilterRegistration.Dynamic
{
/*    private List servletNameRegistrationMappings;
    private List urlPatternRegistrationMappings;
    
    public MockFilterRegistration()
    {
        this(null, null);
    }
    
    public MockFilterRegistration(String name, String className)
    {
        super(name, className);
        servletNameRegistrationMappings = new ArrayList();
        urlPatternRegistrationMappings = new ArrayList();
    }
    
    public void addMappingForServletNames(EnumSet dispatcherTypes, boolean isMatchAfter, String[] servletNames)
    {
        if(null == servletNames)
        {
            throw new IllegalArgumentException("servletNames is null");
        }
        if(servletNames.length == 0)
        {
            throw new IllegalArgumentException("servletNames is empty");
        }
        if(null == dispatcherTypes)
        {
            dispatcherTypes = EnumSet.of(DispatcherType.REQUEST);
        }
        servletNameRegistrationMappings.add(new ServletNameFilterRegistrationMapping(servletNames, dispatcherTypes, isMatchAfter));
    }

    public void addMappingForUrlPatterns(EnumSet dispatcherTypes, boolean isMatchAfter, String[] urlPatterns)
    {
        if(null == urlPatterns)
        {
            throw new IllegalArgumentException("urlPatterns is null");
        }
        if(urlPatterns.length == 0)
        {
            throw new IllegalArgumentException("urlPatterns is empty");
        }
        if(null == dispatcherTypes)
        {
            dispatcherTypes = EnumSet.of(DispatcherType.REQUEST);
        }
        urlPatternRegistrationMappings.add(new URLPatternFilterRegistrationMapping(urlPatterns, dispatcherTypes, isMatchAfter));
    }

    public Collection getServletNameMappings()
    {
        List servletNameMappings = new ArrayList();
        for(int ii = 0; ii < servletNameRegistrationMappings.size(); ii++)
        {
            ServletNameFilterRegistrationMapping currentMapping = (ServletNameFilterRegistrationMapping)servletNameRegistrationMappings.get(ii);
            servletNameMappings.addAll(Arrays.asList(currentMapping.getServletNames()));
        }
        return servletNameMappings;
    }

    public Collection getUrlPatternMappings()
    {
        List urlPatternMappings = new ArrayList();
        for(int ii = 0; ii < urlPatternRegistrationMappings.size(); ii++)
        {
            URLPatternFilterRegistrationMapping currentMapping = (URLPatternFilterRegistrationMapping)urlPatternRegistrationMappings.get(ii);
            urlPatternMappings.addAll(Arrays.asList(currentMapping.getURLPatterns()));
        }
        return urlPatternMappings;
    }
    
    *//**
     * Returns the list of serlvet name mappings as {@link URLPatternFilterRegistrationMapping} objects.
     * 
     * @return the list of {@link URLPatternFilterRegistrationMapping} objects
     *//*
    public List getServletNameRegistrationMappings()
    {
        return Collections.unmodifiableList(servletNameRegistrationMappings);
    }
    
    *//**
     * Returns the list of URL pattern mappings as {@link ServletNameFilterRegistrationMapping} objects.
     * 
     * @return the list of {@link ServletNameFilterRegistrationMapping} objects
     *//*
    public List getURLPatternRegistrationMapping()
    {
        return Collections.unmodifiableList(urlPatternRegistrationMappings);
    }
    
    *//**
     * Clears the servlet name mappings.
     *//*
    public void clearMappingsForServletNames()
    {
        servletNameRegistrationMappings.clear();
    }
    
    *//**
     * Clears the URL pattern mappings.
     *//*
    public void clearMappingsForUrlPatterns()
    {
        urlPatternRegistrationMappings.clear();
    }

    *//**
     * Wraps a filter mapping.
     *//*
    public abstract static class FilterRegistrationMapping
    {
        private Set dispatcherTypes;
        private boolean matchAfter;
        
        public FilterRegistrationMapping(Set dispatcherTypes, boolean matchAfter)
        {
            this.dispatcherTypes = dispatcherTypes;
            this.matchAfter = matchAfter;
        }

        *//**
         * Returns the dispatcher types for this pattern.
         * 
         * @return the dispatcher types
         *//*
        public Set getDispatcherTypes()
        {
            return dispatcherTypes;
        }

        *//**
         * Returns if the mapping should be matched before or after declared mappings.
         * 
         * @return <true> if the mapping should be matched after declared mappings, <code>false</code> otherwise
         *//*
        public boolean isMatchAfter()
        {
            return matchAfter;
        }
    }
    
    *//**
     * Wraps a mapping for servlet names.
     *//*
    public static class ServletNameFilterRegistrationMapping extends FilterRegistrationMapping
    {
        private String[] servletNames;
        
        public ServletNameFilterRegistrationMapping(String[] servletNames, Set dispatcherTypes, boolean matchAfter)
        {
            super(dispatcherTypes, matchAfter);
            this.servletNames = servletNames;
        }

        *//**
         * Returns the servlet names for this pattern.
         * 
         * @return the servlet names
         *//*
        public String[] getServletNames()
        {
            return servletNames;
        }
    }
    
    *//**
     * Wraps a mapping for URL patterns.
     *//*
    public static class URLPatternFilterRegistrationMapping extends FilterRegistrationMapping
    {
        private String[] urlPatterns;
        
        public URLPatternFilterRegistrationMapping(String[] urlPatterns, Set dispatcherTypes, boolean matchAfter)
        {
            super(dispatcherTypes, matchAfter);
            this.urlPatterns = urlPatterns;
        }

        *//**
         * Returns the URL patterns for this mapping.
         * 
         * @return the URL pattern
         *//*
        public String[] getURLPatterns()
        {
            return urlPatterns;
        } 
    }*/
}
