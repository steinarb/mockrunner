package com.mockrunner.struts;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;

/**
 * This implementation of <code>MessageResources</code>
 * takes the messages from a <code>Map</code> and can be
 * used for testing purposes. The <code>Map</code> can
 * also be filled with the contents of a property file.
 * Note: This implementation ignores the specified <code>Locale</code>.
 */
public class MapMessageResources extends MessageResources
{
    private final static Log log = LogFactory.getLog(MapMessageResources.class);
    private Map messages;
    
    /**
     * Creates an empty resources object
     */
    public MapMessageResources()
    {
        this(null);
    }
    
    /**
     * Creates a resources object based on the specified
     * map.
     * @param messages the map of messages
     */
    public MapMessageResources(Map messages)
    {
        this(messages, null, "", true);  
    }
    
    /**
     * Creates a resources object based on the specified
     * map.
     * @param messages the map of messages
     * @param factory the MessageResourcesFactory that created us
     * @param config the configuration parameter
     */
    public MapMessageResources(Map messages, MessageResourcesFactory factory, String config)
    {
        super(factory, config);
        this.messages = messages;
        if(null == this.messages) this.messages = new HashMap();
    }

    /**
     * Creates a resources object based on the specified
     * map.
     * @param messages the map of messages
     * @param factory the MessageResourcesFactory that created us
     * @param config the configuration parameter
     * @param returnNull the returnNull property
     */
    public MapMessageResources(Map messages, MessageResourcesFactory factory, String config, boolean returnNull)
    {
        super(factory, config, returnNull);
        this.messages = messages;
        if(null == this.messages) this.messages = new HashMap();
    }

    /**
     * Returns the message for the specified key. The locale
     * is ignored.
     * @param locale the locale (ignored)
     * @param key the message key
     * @return the message
     */
    public String getMessage(Locale locale, String key)
    {
        return (String)messages.get(key);
    }
    
    /**
     * Adds a message for the specified key.
     * @param key the message key
     * @param value the message
     */
    public void putMessage(String key, String value)
    {
        messages.put(key, value);
    }
    
    /**
     * Adds all messages in the specified map.
     * @param messages the message map
     */
    public void putMessages(Map messages)
    {
        this.messages.putAll(messages);
    }
    
    /**
     * Loads a property file and adds all messages
     * from the file.
     * @param propertyFileName the file name
     */
    public void putMessages(String propertyFileName)
    {
        putMessages(new File(propertyFileName));
    }
    
    /**
     * Loads a property file and adds all messages
     * from the file.
     * @param propertyFile the file
     */
    public void putMessages(File propertyFile)
    {
        try
        {
            FileInputStream inputStream = new FileInputStream(propertyFile);
            Properties properties = new Properties();
            properties.load(inputStream);
            putMessages(properties);
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
        }
    }
    
    /**
     * Clears all messages.
     */
    public void clear()
    {
        super.formats.clear();
        messages.clear();
    }
}
