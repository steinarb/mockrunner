package com.mockrunner.struts;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.struts.util.MessageResources;

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
        this(new HashMap());
    }
    
    public MapMessageResources(Map messages)
    {
        super(null, "", true);
        this.messages = messages;
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
