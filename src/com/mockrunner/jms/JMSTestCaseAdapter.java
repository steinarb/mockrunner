package com.mockrunner.jms;

import com.mockrunner.base.BaseTestCase;

/**
 * Delegator for {@link JMSTestModule}. You can
 * subclass this adapter or use {@link JMSTestModule}
 * directly (so your test case can use another base
 * class).
 */
public class JMSTestCaseAdapter extends BaseTestCase
{
    private JMSTestModule jmsTestModule;
    
    public JMSTestCaseAdapter()
    {

    }

    public JMSTestCaseAdapter(String arg0)
    {
        super(arg0);
    }
    
    protected void tearDown() throws Exception
    {
        super.tearDown();
        jmsTestModule = null;
    }

    /**
     * Creates the <code>JMSTestModule</code>. If you
     * overwrite this method, you must call 
     * <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        jmsTestModule = createJMSTestModule(getJMSMockObjectFactory());
    }

    /**
     * Gets the <code>JMSTestModule</code>. 
     * @return the <code>JMSTestModule</code>
     */
    protected JMSTestModule getJMSTestModule()
    {
        return jmsTestModule;
    }

    /**
     * Sets the <code>JMSTestModule</code>. 
     * @param jmsTestModule the <code>JMSTestModule</code>
     */
    protected void setJMSTestModule(JMSTestModule jmsTestModule)
    {
        this.jmsTestModule = jmsTestModule;
    }
}
