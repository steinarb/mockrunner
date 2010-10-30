package com.mockrunner.mock.web;



/**
 * Mock implementation of <code>TaglibDescriptor</code>.
 */
public class MockTaglibDescriptor // implements TaglibDescriptor
{
    private String taglibLocation;
    private String taglibURI;
    
    public String getTaglibLocation()
    {
        return taglibLocation;
    }

    public String getTaglibURI()
    {
        return taglibURI;
    }

    /**
     * Sets the taglib location.
     * 
     * @param taglibLocation the taglib location
     */
    public void setTaglibLocation(String taglibLocation)
    {
        this.taglibLocation = taglibLocation;
    }

    /**
     * Sets the taglib URI.
     * 
     * @param taglibURI the taglib URI
     */
    public void setTaglibURI(String taglibURI)
    {
        this.taglibURI = taglibURI;
    }
}
