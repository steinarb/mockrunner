package com.mockrunner.mock.connector.cci;

import javax.resource.ResourceException;
import javax.resource.cci.ConnectionMetaData;

/**
 * Mock implementation of <code>ConnectionMetaData</code>.
 */
public class MockConnectionMetaData implements ConnectionMetaData
{
    private String eisProductName = "Mockrunner";
    private String eisProductVersion = "";
    private String userName = "";
    
    public String getEISProductName() throws ResourceException
    {
        return eisProductName;
    }

    public String getEISProductVersion() throws ResourceException
    {
        return eisProductVersion;
    }

    public String getUserName() throws ResourceException
    {
        return userName;
    }
    
    public void setEisProductVersion(String eisProductVersion)
    {
        this.eisProductVersion = eisProductVersion;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }
}
