package com.mockrunner.ejb;

import com.mockrunner.base.BaseTestCase;

public class EJBTestCaseAdapter extends BaseTestCase
{
    private EJBTestModule ejbTestModule;
    
    public EJBTestCaseAdapter()
    {

    }

    public EJBTestCaseAdapter(String arg0)
    {
        super(arg0);
    }

    /**
     * Creates the <code>EJBTestModule</code>. If you
     * overwrite this method, you must call 
     * <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        ejbTestModule = createEJBTestModule(getJDBCMockObjectFactory());
    }
}
