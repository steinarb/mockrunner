package de.mockrunner.mock;

import javax.servlet.ServletConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspWriter;

/**
 * Mock implementation of <code>PageContext</code>.
 */
public class MockPageContext extends org.apache.struts.mock.MockPageContext
{
    private MockJspWriter jspWriter;

    public MockPageContext(ServletConfig config, ServletRequest request, ServletResponse response)
    {
        super(config, request, response);
        jspWriter = new MockJspWriter();
    }

    public JspWriter getOut()
    {
        return jspWriter;
    }
}
