package com.mockrunner.test.web;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllWebTests
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for com.mockrunner.web.test");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(ActionTestModuleTest.class));
        suite.addTest(new TestSuite(MockHttpSessionTest.class));
        suite.addTest(new TestSuite(MockHttpServletResponseTest.class));
        suite.addTest(new TestSuite(MockServletContextTest.class));
        suite.addTest(new TestSuite(TagUtilTest.class));
        suite.addTest(new TestSuite(NestedTagTest.class));
        suite.addTest(new TestSuite(TagLifecycleTest.class));
        suite.addTest(new TestSuite(ServletTestModuleTest.class));
        suite.addTest(new TestSuite(MockPageContextTest.class));
        suite.addTest(new TestSuite(WebMockObjectFactoryTest.class));
        //$JUnit-END$
        return suite;
    }
}
