package com.mockrunner.example.struts;

import org.apache.commons.validator.ValidatorResources;

import com.mockrunner.struts.BasicActionTestCaseAdapter;
import com.mockrunner.struts.MapMessageResources;

/**
 * Example test for the {@link GreetingsAction}.
 * Please note that we cache the <code>ValidatorResources</code>
 * in a static field. You don't need to do this, but the parsing
 * of the files before every test method will slow down your tests.
 */
public class GreetingsActionTest extends BasicActionTestCaseAdapter
{
    private static ValidatorResources validatorRes = null;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        if(null == validatorRes)
        {
            String[] files = new String[2];
            files[0] = "src/com/mockrunner/example/struts/validator-rules.xml";
            files[1] = "src/com/mockrunner/example/struts/validation.xml";
            validatorRes = createValidatorResources(files);
        }
        setValidatorResources(validatorRes);
        MapMessageResources resources = new MapMessageResources();
        resources.putMessages("src/com/mockrunner/example/struts/Application.properties");
        setResources(resources);
        setValidate(true);
    }
    
    public void testSuccesfulGreetings()
    {
        getActionMockObjectFactory().getMockServletContext().setAttribute("counter", new Integer(0));
        getActionMockObjectFactory().getMockActionMapping().setPath("/greetings");
        addRequestParameter("name", "testname");
        actionPerform(GreetingsAction.class, GreetingsValidatorForm.class);
        assertEquals("Hello testname, you are visitor 1", getRequestAttribute("greetings"));
        getActionMockObjectFactory().getMockServletContext().setAttribute("counter", new Integer(6));
        verifyNoActionErrors();
        actionPerform(GreetingsAction.class, GreetingsValidatorForm.class);
        assertEquals("Hello testname, you are visitor 7", getRequestAttribute("greetings"));
        verifyNoActionErrors();
        verifyForward("success");
    }
    
    public void testValidationError()
    {
        getActionMockObjectFactory().getMockServletContext().setAttribute("counter", new Integer(0));
        getActionMockObjectFactory().getMockActionMapping().setPath("/greetings"); 
        actionPerform(GreetingsAction.class, GreetingsValidatorForm.class);
        verifyNumberActionErrors(1);
        verifyActionErrorPresent("errors.required");
        addRequestParameter("name", "y");
        actionPerform(GreetingsAction.class, GreetingsValidatorForm.class);
        verifyNumberActionErrors(1);
        verifyActionErrorPresent("errors.minlength");
    }
}
