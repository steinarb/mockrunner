package com.mockrunner.mock.connector.cci;

import javax.resource.cci.ResourceAdapterMetaData;

/**
 * Mock implementation of <code>ResourceAdapterMetaData</code>.
 */
public class MockResourceAdapterMetaData implements ResourceAdapterMetaData
{
    private String adapterName = "Mockrunner Adapter";
    private String shortDescription = "Mockrunner Adapter";
    private String vendorName = "Mockrunner";
    private String adapterVersion = "";
    private String[] specsSupported = new String[0];
    private String specVersion = "1.5";
    private boolean supportsExecuteWithInAndOut = true;
    private boolean supportsExecuteWithInOnly = true;
    private boolean supportsLocalTransactionDemarcation = true;
    
    public String getAdapterName()
    {
        return adapterName;
    }

    public String getAdapterShortDescription()
    {
        return shortDescription;
    }

    public String getAdapterVendorName()
    {
        return vendorName;
    }

    public String getAdapterVersion()
    {
        return adapterVersion;
    }

    public String[] getInteractionSpecsSupported()
    {
        return specsSupported;
    }

    public String getSpecVersion()
    {
        return specVersion;
    }

    public boolean supportsExecuteWithInputAndOutputRecord()
    {
        return supportsExecuteWithInAndOut;
    }

    public boolean supportsExecuteWithInputRecordOnly()
    {
        return supportsExecuteWithInOnly;
    }

    public boolean supportsLocalTransactionDemarcation()
    {
        return supportsLocalTransactionDemarcation;
    }
    
    public void setAdapterName(String adapterName)
    {
        this.adapterName = adapterName;
    }

    public void setAdapterShortDescription(String shortDescription)
    {
        this.shortDescription = shortDescription;
    }

    public void setAdapterVendorName(String vendorName)
    {
        this.vendorName = vendorName;
    }

    public void setAdapterVersion(String adapterVersion)
    {
        this.adapterVersion = adapterVersion;
    }

    public void setInteractionSpecsSupported(String[] specsSupported)
    {
        this.specsSupported = specsSupported;
    }

    public void setSpecVersion(String specVersion)
    {
        this.specVersion = specVersion;
    }

    public void setSupportsExecuteWithInputAndOutputRecord(boolean supportsExecuteWithInAndOut)
    {
        this.supportsExecuteWithInAndOut = supportsExecuteWithInAndOut;
    }

    public void setSupportsExecuteWithInputRecordOnly(boolean supportsExecuteWithInOnly)
    {
        this.supportsExecuteWithInOnly = supportsExecuteWithInOnly;
    }

    public void setSupportsLocalTransactionDemarcation(boolean supportsLocalTransactionDemarcation)
    {
        this.supportsLocalTransactionDemarcation = supportsLocalTransactionDemarcation;
    }
}
