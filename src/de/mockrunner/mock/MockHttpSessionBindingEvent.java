package de.mockrunner.mock;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * Mock implementation of <code>HttpSessionBindingEvent</code>.
 */
public class MockHttpSessionBindingEvent extends HttpSessionBindingEvent
{
    public MockHttpSessionBindingEvent(HttpSession session, String name, Object value)
    {
        super(session, name, value);
    }

    public String getName()
    {
        return super.getName();
    }

    public HttpSession getSession()
    {
        return super.getSession();
    }

    public Object getValue()
    {
        return super.getValue();
    }
}
