package com.mockrunner.mock.web;

import org.apache.struts.config.impl.ModuleConfigImpl;

/**
 * Mock implementation of <code>ModuleConfig</code>.
 * Just extends <code>ModuleConfigImpl</code> and doesn't
 * add any additional functionality. But it's possible to
 * change the implementation later and overwrite some methods.
 */
public class MockModuleConfig extends ModuleConfigImpl
{
    public MockModuleConfig(String prefix)
    {
        super(prefix);
    }
}
