package com.mockrunner.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.mockrunner.example.servlet.ImageButtonFilterTest;
import com.mockrunner.example.servlet.LogoutServletTest;
import com.mockrunner.example.servlet.RedirectServletTest;
import com.mockrunner.example.struts.AuthenticationActionTest;
import com.mockrunner.example.struts.OrderActionTest;
import com.mockrunner.example.struts.StoreDataActionTest;
import com.mockrunner.example.tag.ConstrainedNumericTextTagTest;
import com.mockrunner.example.tag.TableEnumTagTest;

public class AllTests
{

    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for com.mockrunner.test");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(ActionTestModuleTest.class));
        suite.addTest(new TestSuite(JDBCTestModuleTest.class));
        suite.addTest(new TestSuite(MockHttpSessionTest.class));
        suite.addTest(new TestSuite(MockObjectFactoryTest.class));
        suite.addTest(new TestSuite(TagUtilTest.class));
        suite.addTest(new TestSuite(NestedTagTest.class));
        suite.addTest(new TestSuite(TagLifecycleTest.class));
        suite.addTest(new TestSuite(ServletTestModuleTest.class));
        suite.addTest(new TestSuite(MockPageContextTest.class));
        suite.addTest(new TestSuite(MockBlobTest.class));
        suite.addTest(new TestSuite(XmlUtilTest.class));
        suite.addTest(new TestSuite(StreamUtilTest.class));
        suite.addTest(new TestSuite(CollectionUtilTest.class));
        suite.addTest(new TestSuite(ConstrainedNumericTextTagTest.class));
        suite.addTest(new TestSuite(TableEnumTagTest.class));
        suite.addTest(new TestSuite(LogoutServletTest.class));
        suite.addTest(new TestSuite(RedirectServletTest.class));
        suite.addTest(new TestSuite(AuthenticationActionTest.class));
        suite.addTest(new TestSuite(StoreDataActionTest.class));
        suite.addTest(new TestSuite(OrderActionTest.class));
        suite.addTest(new TestSuite(ImageButtonFilterTest.class));
        //$JUnit-END$
        return suite;
    }
}
