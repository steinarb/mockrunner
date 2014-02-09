package com.mockrunner.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.mockrunner.example.connector.AccountDAOTest;
import com.mockrunner.example.connector.PersonSearchDAOTest;
import com.mockrunner.example.ejb.BillManagerSessionTest;
import com.mockrunner.example.ejb.DBStatefulTest;
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
import com.mockrunner.example.tag.HtmlTextTagTest;
import com.mockrunner.example.tag.TableEnumTagTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	ConstrainedNumericTextTagTest.class, TableEnumTagTest.class, FilterImagesTagTest.class,
	HtmlTextTagTest.class, LogoutServletTest.class, RedirectServletTest.class,
	AuthenticationActionTest.class, StoreDataActionTest.class, GreetingsActionTest.class,
	ShoppingCartActionTest.class, OrderActionTest.class, LogoutActionTest.class,
	ImageButtonFilterTest.class, BankTest.class, PayActionTest.class, BookstoreTest.class,
	OrderDBTest.class, LogActionTest.class, PaySessionTest.class, DBStatefulTest.class,
	BillManagerSessionTest.class, PrintMessageServletTest.class, StockQuotePublisherTest.class,
	PrintSessionBeanTest.class, NewsSubscriberTest.class, UserLoginSessionTest.class,
	PersonSearchDAOTest.class, AccountDAOTest.class
})
public class AllExampleTests
{
}
