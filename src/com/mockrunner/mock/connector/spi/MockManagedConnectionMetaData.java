package com.mockrunner.mock.connector.spi;

import javax.resource.ResourceException;
import javax.resource.spi.ManagedConnectionMetaData;

/**
 * Mock implementation of <code>ManagedConnectionMetaData</code>.
 */
public class MockManagedConnectionMetaData implements ManagedConnectionMetaData 
{
	private String eisProductName = "Mockrunner";
    private String eisProductVersion = "";
    private int maxConnections = 1;
    private String userName = "";
    
    public String getEISProductName() throws ResourceException 
    {
		return eisProductName;
	}

	public String getEISProductVersion() throws ResourceException 
    {
		return eisProductVersion;
	}

	public int getMaxConnections() throws ResourceException 
    {
		return maxConnections;
	}

	public String getUserName() throws ResourceException 
    {
		return userName;
	}

    public void setEisProductName(String eisProductName)
    {
        this.eisProductName = eisProductName;
    }

    public void setEisProductVersion(String eisProductVersion)
    {
        this.eisProductVersion = eisProductVersion;
    }

    public void setMaxConnections(int maxConnections)
    {
        this.maxConnections = maxConnections;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }
}
