package com.mockrunner.base;

import junit.framework.TestCase;

/**
 * Base class for all adapters.
 */
public abstract class BaseTestCase extends TestCase
{
    private MockObjectFactory mockFactory;

    public BaseTestCase(String arg0)
    {
        super(arg0);
    }

    /**
     * Creates the <code>MockObjectFactory</code>. If you
     * overwrite this method, you must call 
     * <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        mockFactory = createMockObjectFactory();
    }

    /**
     * Creates a <code>MockObjectFactory</code>. 
     * @return the created <code>MockObjectFactory</code>
     */
    protected MockObjectFactory createMockObjectFactory()
    {
        MockObjectFactory factory = new MockObjectFactory();
        return factory;
    }

    /**
     * Creates a <code>MockObjectFactory</code> based on another on.
     * The created <code>MockObjectFactory</code> will have its own
     * request and session objects, but the two factories will share
     * one <code>ServletContext</code>. Especially important for
     * multithreading tests.
     * @return the created <code>MockObjectFactory</code>
     */
    protected MockObjectFactory createMockObjectFactory(MockObjectFactory otherFactory)
    {
        MockObjectFactory factory = new MockObjectFactory(otherFactory);
        return factory;
    }

    /**
     * Gets the current <code>MockObjectFactory</code>.
     * @return the <code>MockObjectFactory</code>
     */
    protected MockObjectFactory getMockObjectFactory()
    {
        return mockFactory;
    }
    
    /**
     * Creates an <code>ActionTestModule</code> with the specified
     * <code>MockObjectFactory</code>.
     * @return the created <code>ActionTestModule</code>
     */
    protected ActionTestModule createActionTestModule(MockObjectFactory mockFactory)
    {
        return new ActionTestModule(mockFactory);
    }
    
    /**
     * Creates an <code>ActionTestModule</code> based on the current
     * <code>MockObjectFactory</code>.
     * Same as <code>createActionTestModule(getMockObjectFactory())</code>.
     * @return the created <code>ActionTestModule</code>
     */
    protected ActionTestModule createActionTestModule()
    {
        return new ActionTestModule(getMockObjectFactory());
    }
    
    /**
     * Creates an <code>TagTestModule</code> with the specified
     * <code>MockObjectFactory</code>.
     * @return the created <code>TagTestModule</code>
     */
    protected TagTestModule createTagTestModule(MockObjectFactory mockFactory)
    {
        return new TagTestModule(mockFactory);
    }
    
    /**
     * Creates an <code>TagTestModule</code> based on the current
     * <code>MockObjectFactory</code>.
     * Same as <code>createTagTestModule(getMockObjectFactory())</code>.
     * @return the created <code>TagTestModule</code>
     */
    protected TagTestModule createTagTestModule()
    {
        return new TagTestModule(getMockObjectFactory());
    }
}
