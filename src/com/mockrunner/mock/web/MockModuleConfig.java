package com.mockrunner.mock.web;

import org.apache.struts.config.MessageResourcesConfig;
import org.apache.struts.config.impl.ModuleConfigImpl;

/**
 * Mock implementation of <code>ModuleConfig</code>.
 */
public class MockModuleConfig extends ModuleConfigImpl
{
    public MockModuleConfig(String prefix)
    {
        super(prefix);
    }
    
    /**
     * Clears all message resource configs.
     */
    public void clearMessageResourcesConfigs()
    {
        MessageResourcesConfig[] configs = findMessageResourcesConfigs();
        if(null != configs)
        {
            for(int ii = 0; ii < configs.length; ii++)
            {
                removeMessageResourcesConfig(configs[ii]);
            }
        }
    }
}
