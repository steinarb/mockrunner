package com.mockrunner.test.gen.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllGenUtilTests
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for com.mockrunner.test.gen.util");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(JavaLineProcessorTest.class));
        suite.addTest(new TestSuite(JavaLineAssemblerTest.class));
        suite.addTest(new TestSuite(JavaClassGeneratorTest.class));
        suite.addTest(new TestSuite(PackageImportSorterTest.class));
        suite.addTest(new TestSuite(BCELClassAnalyzerTest.class));
        //$JUnit-END$
        return suite;
    }
}
