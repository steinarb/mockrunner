package com.mockrunner.example.struts;

import com.mockrunner.struts.ActionTestCaseAdapter;
import com.mockrunner.struts.MapMessageResources;

/**
 * Example test for the {@link GreetingsAction}.
 */
public class GreetingsActionTest extends ActionTestCaseAdapter
{
    protected void setUp() throws Exception
    {
        super.setUp();
        MapMessageResources resources = new MapMessageResources();
        resources.putMessages("src/com/mockrunner/example/struts/Application.properties");
        setResources(resources);
    }
    
    public void testGreetings()
    {
        getWebMockObjectFactory().getMockServletContext().setAttribute("counter", new Integer(0));
        addRequestParameter("name", "testname");
        actionPerform(GreetingsAction.class);
        assertEquals("Hello testname, you are visitor 1", getWebMockObjectFactory().getMockRequest().getAttribute("greetings"));
        getWebMockObjectFactory().getMockServletContext().setAttribute("counter", new Integer(6));
        actionPerform(GreetingsAction.class);
        assertEquals("Hello testname, you are visitor 7", getWebMockObjectFactory().getMockRequest().getAttribute("greetings"));
        verifyNoActionErrors();
        verifyForward("success");
    }
}
