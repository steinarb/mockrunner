package com.mockrunner.ejb;

import com.mockrunner.mock.ejb.EJBMockObjectFactory;

/**
 * Module for EJB tests.
 */
public class EJBTestModule
{
    private EJBMockObjectFactory mockFactory;
    
    public EJBTestModule(EJBMockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
    }
}
