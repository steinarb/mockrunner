package de.mockrunner.example.test;

import de.mockrunner.base.ActionTestCaseAdapter;
import de.mockrunner.example.AuthenticationAction;
import de.mockrunner.example.AuthenticationForm;
import de.mockrunner.example.AuthenticationStrategy;

public class AuthenticationActionTest extends ActionTestCaseAdapter
{
    private MockAuthenticationStrategy strategy;
    private AuthenticationForm form;
    
    public AuthenticationActionTest(String arg0)
    {
        super(arg0);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        strategy = new MockAuthenticationStrategy();
        getMockObjectFactory().getMockServletContext().setAttribute(AuthenticationStrategy.class.getName(), strategy);
        form = (AuthenticationForm)createActionForm(AuthenticationForm.class);
        setValidate(true);
    }

    public void testSuccessfulLogin()
    {
        addRequestParameter("username", "test");
        addRequestParameter("password", "test");
        strategy.setupLoginOk(true);
        actionPerform(AuthenticationAction.class, form);
        verifyNoActionErrors();
        verifyForward("success");
    }
    
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
