package com.mockrunner.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.mockrunner.example.connector.AccountDAOTest;
import com.mockrunner.example.connector.PersonSearchDAOTest;
import com.mockrunner.example.ejb.BillManagerSessionTest;
import com.mockrunner.example.ejb.LogActionTest;
import com.mockrunner.example.ejb.PaySessionTest;
import com.mockrunner.example.ejb.UserLoginSessionTest;
import com.mockrunner.example.jdbc.BankTest;
import com.mockrunner.example.jdbc.BookstoreTest;
import com.mockrunner.example.jdbc.OrderDBTest;
import com.mockrunner.example.jdbc.PayActionTest;
import com.mockrunner.example.jms.NewsSubscriberTest;
import com.mockrunner.example.jms.PrintMessageServletTest;
import com.mockrunner.example.jms.PrintSessionBeanTest;
import com.mockrunner.example.jms.StockQuotePublisherTest;
import com.mockrunner.example.servlet.ImageButtonFilterTest;
import com.mockrunner.example.servlet.LogoutServletTest;
import com.mockrunner.example.servlet.RedirectServletTest;
import com.mockrunner.example.struts.AuthenticationActionTest;
import com.mockrunner.example.struts.GreetingsActionTest;
import com.mockrunner.example.struts.LogoutActionTest;
import com.mockrunner.example.struts.OrderActionTest;
import com.mockrunner.example.struts.ShoppingCartActionTest;
import com.mockrunner.example.struts.StoreDataActionTest;
import com.mockrunner.example.tag.ConstrainedNumericTextTagTest;
import com.mockrunner.example.tag.FilterImagesTagTest;
import com.mockrunner.example.tag.TableEnumTagTest;

public class AllExampleTests
{
    public static Test suite()
    {
        Logger.getLogger("").setLevel(Level.OFF);
        TestSuite suite = new TestSuite("Test for com.mockrunner.test");
        //$JUnit-BEGIN$ 
        suite.addTest(new TestSuite(ConstrainedNumericTextTagTest.class));
        suite.addTest(new TestSuite(TableEnumTagTest.class));
        suite.addTest(new TestSuite(FilterImagesTagTest.class));
        suite.addTest(new TestSuite(LogoutServletTest.class));
        suite.addTest(new TestSuite(RedirectServletTest.class));
        suite.addTest(new TestSuite(AuthenticationActionTest.class));
        suite.addTest(new TestSuite(StoreDataActionTest.class));
        suite.addTest(new TestSuite(GreetingsActionTest.class));
        suite.addTest(new TestSuite(ShoppingCartActionTest.class));
        suite.addTest(new TestSuite(OrderActionTest.class));
        suite.addTest(new TestSuite(LogoutActionTest.class));
        suite.addTest(new TestSuite(ImageButtonFilterTest.class));
        suite.addTest(new TestSuite(BankTest.class));
        suite.addTest(new TestSuite(PayActionTest.class));
        suite.addTest(new TestSuite(BookstoreTest.class));
        suite.addTest(new TestSuite(OrderDBTest.class));
        suite.addTest(new TestSuite(LogActionTest.class));
        suite.addTest(new TestSuite(PaySessionTest.class));
        suite.addTest(new TestSuite(BillManagerSessionTest.class));
        suite.addTest(new TestSuite(PrintMessageServletTest.class));
        suite.addTest(new TestSuite(StockQuotePublisherTest.class));
        suite.addTest(new TestSuite(PrintSessionBeanTest.class));
        suite.addTest(new TestSuite(NewsSubscriberTest.class));
        suite.addTest(new TestSuite(UserLoginSessionTest.class));
        suite.addTest(new TestSuite(PersonSearchDAOTest.class));
        suite.addTest(new TestSuite(AccountDAOTest.class));
        //$JUnit-END$
        return suite;
    }
}
