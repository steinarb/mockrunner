package com.mockrunner.example.struts;

import org.junit.Before;
import org.junit.Test;

import com.mockrunner.struts.BasicActionTestCaseAdapter;

/**
 * Example test for the {@link AuthenticationAction}.
 * Demonstrates most of the features of {@link com.mockrunner.struts.ActionTestModule} 
 * and {@link com.mockrunner.struts.BasicActionTestCaseAdapter}.
 */
public class AuthenticationActionTest extends BasicActionTestCaseAdapter
{
    private MockAuthenticationStrategy strategy;
    private AuthenticationForm form;
    
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        strategy = new MockAuthenticationStrategy();
        getActionMockObjectFactory().getMockServletContext().setAttribute(AuthenticationStrategy.class.getName(), strategy);
        form = (AuthenticationForm)createActionForm(AuthenticationForm.class);
        setValidate(true);
    }

    @Test
    public void testSuccessfulLogin()
    {
        addRequestParameter("username", "test");
        addRequestParameter("password", "test");
        strategy.setupLoginOk(true);
        actionPerform(AuthenticationAction.class, form);
        verifyNoActionErrors();
        verifyNumberActionMessages(1);
        verifyActionMessagePresent("auth.login.successful");
        verifyForward("success");
    }
    
    @Test
    public void testEmptyInput()
    {
        form.setUsername("");
        form.setPassword("test");
        strategy.setupLoginOk(true);
        actionPerform(AuthenticationAction.class, form);
        verifyNumberActionErrors(1);
        verifyActionErrorPresent("field.value.missing");
        verifyActionErrorValue("field.value.missing", "username");
        form.setUsername("test");
        form.setPassword("");
        actionPerform(AuthenticationAction.class, form);
        verifyNumberActionErrors(1);
        verifyActionErrorPresent("field.value.missing");
        verifyActionErrorValue("field.value.missing", "password");
        strategy.setupLoginOk(false);
        strategy.setupUserKnown(false);
        actionPerform(AuthenticationAction.class, form);
        verifyActionErrorNotPresent("auth.username.unknown");
    }
    
    @Test
    public void testUnknownUser()
    {
        form.setUsername("test");
        form.setPassword("test");
        strategy.setupLoginOk(false);
        strategy.setupUserKnown(false);
        actionPerform(AuthenticationAction.class, form);
        verifyNumberActionErrors(1);
        verifyActionErrorPresent("auth.username.unknown");
        verifyActionErrorValue("auth.username.unknown", "test");
        verifyForward("failure");
    }
    
    @Test
    public void testWrongPassword()
    {
        form.setUsername("test");
        form.setPassword("test");
        strategy.setupLoginOk(false);
        strategy.setupUserKnown(true);
        strategy.setupPasswordOk(false);
        actionPerform(AuthenticationAction.class, form);
        verifyNumberActionErrors(1);
        verifyActionErrorPresent("auth.password.wrong");
        verifyForward("failure");
    }
    
    @Test
    public void testGeneralError()
    {
        form.setUsername("test");
        form.setPassword("test");
        strategy.setupLoginOk(false);
        strategy.setupUserKnown(true);
        strategy.setupPasswordOk(true);
        actionPerform(AuthenticationAction.class, form);
        verifyNumberActionErrors(1);
        verifyActionErrorPresent("auth.general.error");
        verifyForward("failure");
    }
}
