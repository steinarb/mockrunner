package de.mockrunner.base;

import junit.framework.TestCase;

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
}
