package com.mockrunner.struts;

import java.util.Map;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;

/**
 * Factory for {@link MapMessageResources}.
 * If you want this factory to be the default
 * factory used in your actions in the tests,
 * you should call
 * <code>MessageResourcesFactory.setFactoryClass("com.mockrunner.struts.MapMessageResourcesFactory")</code>
 */
public class MapMessageResourcesFactory extends MessageResourcesFactory
{
    private static Map theMessages;
    
    /**
     * Sets the messages <code>Map</code> for returned
     * {@link MapMessageResources} objects.
     * @param messages the message <code>Map</code>
     */
    public static void setMessageMap(Map messages)
    {
        theMessages = messages;
    }
    
    public MessageResources createResources(String config)
    {
        return new MapMessageResources(theMessages, this, config, this.returnNull);
    }
}
