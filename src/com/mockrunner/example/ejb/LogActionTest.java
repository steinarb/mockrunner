package com.mockrunner.example.ejb;

import org.mockejb.MockContainer;
import org.mockejb.MockContext;
import org.mockejb.SessionBeanDescriptor;

import com.mockrunner.example.ejb.interfaces.LogSession;
import com.mockrunner.example.ejb.interfaces.LogSessionHome;
import com.mockrunner.struts.ActionTestCaseAdapter;

public class LogActionTest extends ActionTestCaseAdapter
{
    protected void setUp() throws Exception
    {
        super.setUp();
        SessionBeanDescriptor beanDescriptor = new SessionBeanDescriptor("com/mockrunner/example/LogSession", LogSessionHome.class, LogSession.class, LogSessionBean.class);
        MockContainer.deploy(beanDescriptor);
        MockContext.add("java:/DefaultDS", getMockObjectFactory().getMockDataSource());
    }
    
    public void testLogAction()
    {
        getMockObjectFactory().getMockRequest().setupAddParameter("message", "testmessage");
        actionPerform(LogAction.class);
    }
    
}
