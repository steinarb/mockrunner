package com.mockrunner.example.jms;

import com.mockrunner.ejb.EJBTestModule;
import com.mockrunner.jms.JMSTestCaseAdapter;
import com.mockrunner.servlet.ServletTestModule;

public class PrintMessageServletTest extends JMSTestCaseAdapter
{
    private EJBTestModule ejbModule;
    private ServletTestModule servletModule;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        ejbModule = new EJBTestModule(createEJBMockObjectFactory());
        servletModule = new ServletTestModule(createWebMockObjectFactory());
        servletModule.createServlet(PrintMessageServlet.class);
    }

}
