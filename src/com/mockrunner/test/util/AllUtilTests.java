package com.mockrunner.test.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllUtilTests
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for com.mockrunner.test.util");
        //$JUnit-BEGIN$
		suite.addTest(new TestSuite(StreamUtilTest.class));
		suite.addTest(new TestSuite(ArrayUtilTest.class));
		suite.addTest(new TestSuite(CollectionUtilTest.class));
		suite.addTest(new TestSuite(StringUtilTest.class));
		suite.addTest(new TestSuite(FileUtilTest.class));
		suite.addTest(new TestSuite(ClassUtilTest.class));
        suite.addTest(new TestSuite(CaseAwareMapTest.class));
        suite.addTest(new TestSuite(MethodUtilTest.class));
        suite.addTest(new TestSuite(XmlUtilTest.class));
        //$JUnit-END$
        return suite;
    }
}
