package de.mockrunner.base;

import junit.framework.TestCase;

/**
 * Base class for all test cases. Does
 * mock factory handling.
 */
public abstract class BaseTestCase extends TestCase
{
    private MockObjectFactory mockFactory;

    public BaseTestCase(String arg0)
    {
        super(arg0);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        mockFactory = createMockObjectFactory();
    }

    protected MockObjectFactory createMockObjectFactory()
    {
        MockObjectFactory factory = new MockObjectFactory();
        return factory;
    }

    protected MockObjectFactory createMockObjectFactory(MockObjectFactory otherFactory)
    {
        MockObjectFactory factory = new MockObjectFactory(otherFactory);
        return factory;
    }

    protected MockObjectFactory getMockObjectFactory()
    {
        return mockFactory;
    }
    
    protected ActionTestModule createActionTestModule(MockObjectFactory mockFactory)
    {
        return new ActionTestModule(mockFactory);
    }
    
    protected ActionTestModule createActionTestModule()
    {
        return new ActionTestModule(getMockObjectFactory());
    }
}
