package com.mockrunner.struts;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;

/**
 * This implementation of <code>MessageResources</code>
 * takes is messages from a <code>Map</code> and can be
 * used for testing purposes.
 * Note: This implementation ignores the specified <code>Locale</code>.
 */
public class MapMessageResources extends MessageResources
{
    private Map messages;
    
    public MapMessageResources()
    {
        this(null);
    }
    
    public MapMessageResources(Map messages)
    {
        this(messages, null, "", true);  
    }
    
    public MapMessageResources(Map messages, MessageResourcesFactory factory, String config)
    {
        super(factory, config);
        this.messages = messages;
        if(null == this.messages) this.messages = new HashMap();
    }

    public MapMessageResources(Map messages, MessageResourcesFactory factory, String config, boolean returnNull)
    {
        super(factory, config, returnNull);
        this.messages = messages;
        if(null == this.messages) this.messages = new HashMap();
    }

    public String getMessage(Locale locale, String key)
    {
        return (String)messages.get(key);
    }
    
    public void setMessage(String key, String value)
    {
        messages.put(key, value);
    }
    
    public void setMessages(Map messages)
    {
        this.messages.putAll(messages);
    }
    
    public void clear()
    {
        messages.clear();
    }
}
