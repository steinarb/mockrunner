package com.mockrunner.tag;

/**
 * This class encapsulates the data for a dynamic tag attribute.
 * You can add it to the paramater map of any
 * {@link com.mockrunner.tag.NestedTag} instance.
 * It is not necessary  to use an instance of this class to set dynamic 
 * attributes. If the attribute is set directly (like normal attributes),
 * the URI will be set to <code>null</code>, i.e. the attribute has
 * the default namespace.
 */
public class DynamicAttribute
{
    private String uri;
    private Object value;
    
    public DynamicAttribute()
    {
        
    }
    
    public DynamicAttribute(String uri, Object value)
    {
        this.uri = uri;
        this.value = value;
    }
    
    /**
     * Returns the namespace of the attribute.
     * @return the namespace of the attribute
     */
    public String getUri()
    {
        return uri;
    }
    
    /**
     * Sets the namespace of the attribute.
     * @param uri the namespace of the attribute
     */
    public void setUri(String uri)
    {
        this.uri = uri;
    }
    
    /**
     * Returns the value of the attribute.
     * @return the value of the attribute
     */
    public Object getValue()
    {
        return value;
    }
    
    /**
     * Sets the value of the attribute.
     * @param value the value of the attribute
     */
    public void setValue(Object value)
    {
        this.value = value;
    }
}
