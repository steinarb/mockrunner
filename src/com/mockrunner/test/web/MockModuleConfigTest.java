package com.mockrunner.test.web;

import org.apache.struts.config.MessageResourcesConfig;

import com.mockrunner.mock.web.MockModuleConfig;

import junit.framework.TestCase;

public class MockModuleConfigTest extends TestCase
{
    public void testClearMessageResourcesConfigs()
    {
        MockModuleConfig moduleConfig = new MockModuleConfig("");
        MessageResourcesConfig messageConfig1 = new MessageResourcesConfig();
        MessageResourcesConfig messageConfig2 = new MessageResourcesConfig();
        MessageResourcesConfig messageConfig3 = new MessageResourcesConfig();
        messageConfig1.setKey("1");
        messageConfig2.setKey("2");
        messageConfig3.setKey("3");
        moduleConfig.addMessageResourcesConfig(messageConfig1);
        moduleConfig.addMessageResourcesConfig(messageConfig2);
        moduleConfig.addMessageResourcesConfig(messageConfig3);
        assertEquals(3, moduleConfig.findMessageResourcesConfigs().length);
        moduleConfig.clearMessageResourcesConfigs();
        assertEquals(0, moduleConfig.findMessageResourcesConfigs().length);
    }
}
