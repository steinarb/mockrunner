package com.mockrunner.mock.web;

import javax.servlet.jsp.JspEngineInfo;

/**
 * Mock implementation of <code>JspEngineInfo</code>.
 */
public class MockJspEngineInfo extends JspEngineInfo
{
    private String specificationVersion;

    public MockJspEngineInfo()
    {
        specificationVersion = "2.1";
    }
    
    public void setSpecificationVersion(String specificationVersion)
    {
        this.specificationVersion = specificationVersion;
    }

    public String getSpecificationVersion()
    {
        return specificationVersion;
    }
}
