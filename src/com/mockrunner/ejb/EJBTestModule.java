package com.mockrunner.ejb;

import org.mockejb.MockContainer;
import org.mockejb.SessionBeanDescriptor;

import com.mockrunner.mock.ejb.EJBMockObjectFactory;

/**
 * Module for EJB tests.
 */
public class EJBTestModule
{
    private EJBMockObjectFactory mockFactory;
    private String impSuffix;
    private String homeSuffix;
    private String remoteSuffix;
    private String homeInterfacePackage;
    private String remoteInterfacePackage;
    
    public EJBTestModule(EJBMockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
        impSuffix = "Bean";
        homeSuffix = "Home";
        remoteSuffix = "";
    }
    
    public void setImplementationSuffix(String impSuffix)
    {
        this.impSuffix = impSuffix;
    }
    
    public void setRemoteSuffix(String remoteSuffix)
    {
        this.remoteSuffix = remoteSuffix;
    }
    
    public void setHomeSuffix(String homeSuffix)
    {
        this.homeSuffix = homeSuffix;
    }
    
    public void setInterfacePackage(String interfacePackage)
    {
        setHomeInterfacePackage(interfacePackage);
        setRemoteInterfacePackage(interfacePackage);
    }
    
    public void setHomeInterfacePackage(String homeInterfacePackage)
    {
        this.homeInterfacePackage = homeInterfacePackage;
    }
    
    public void setRemoteInterfacePackage(String remoteInterfacePackage)
    {
        this.remoteInterfacePackage = remoteInterfacePackage;
    }
    
    public void deploy(SessionBeanDescriptor descriptor)
    {
        MockContainer.deploy(descriptor);
    }
}
