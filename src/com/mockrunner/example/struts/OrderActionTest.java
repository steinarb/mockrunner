package com.mockrunner.example.struts;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import com.mockrunner.mock.web.ActionMockObjectFactory;
import com.mockrunner.struts.ActionTestModule;

/**
 * Example test for the {@link OrderAction}.
 * This is an example how to write tests if you do not
 * subclass {@link com.mockrunner.struts.ActionTestCaseAdapter} or
 * {@link com.mockrunner.struts.BasicActionTestCaseAdapter}.
 */
public class OrderActionTest extends TestCase
{
    private ActionMockObjectFactory mockFactory;
    private ActionTestModule module;
    private MockOrderManager orderManager;
    private OrderForm form;
        
    protected void setUp() throws Exception
    {
        super.setUp();
        orderManager = new MockOrderManager();
        mockFactory = new ActionMockObjectFactory();
        module = new ActionTestModule(mockFactory);
        ServletContext context = mockFactory.getMockServletContext();
        context.setAttribute(OrderManager.class.getName(), orderManager);
        form = (OrderForm)module.createActionForm(OrderForm.class);
        module.setValidate(true);
    }
    
    public void testSuccessfulOrder()
    {
        form.setId("testProduct");
        form.setAmount(10);
        orderManager.setStock("testProduct", 20);
        module.actionPerform(OrderAction.class, form);
        module.verifyNoActionErrors();
        module.verifyNoActionMessages();
        module.verifyForward("success");
    }
    
    public void testFailureOrder()
    {
        module.addRequestParameter("id", "testProduct");
        module.addRequestParameter("amount", "10");
        orderManager.setStock("testProduct", 5);
        module.actionPerform(OrderAction.class, form);
        module.verifyNumberActionErrors(1);
        module.verifyActionErrorPresent("not.enough.in.stock");
        module.verifyActionErrorValue("not.enough.in.stock", "testProduct");
        module.verifyNoActionMessages();
        module.verifyForward("failure");
    }
}
