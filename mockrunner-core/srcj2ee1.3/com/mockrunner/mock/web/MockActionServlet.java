package com.mockrunner.mock.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.struts.action.ActionServlet;

/**
 * This mock version of the Struts <code>ActionServlet</code>
 * is necessary, because some Struts methods use it to
 * get the <code>ServletContext</code> and other things.
 */
public class MockActionServlet extends ActionServlet
{
    private ServletConfig config;
    private ServletContext context;

    /**
     * Returns the <code>ServletConfig</code>.
     * @return the <code>ServletConfig</code>
     */
    public ServletConfig getServletConfig()
    {
        return config;
    }

    /**
     * Returns the <code>ServletContext</code>.
     * @return the <code>ServletContext</code>
     */
    public ServletContext getServletContext()
    {
        return context;
    }

    /**
     * Set the <code>ServletConfig</code>.
     * @param config the <code>ServletConfig</code>
     */
    public void setServletConfig(ServletConfig config)
    {
        this.config = config;
    }

    /**
     * Set the <code>ServletContext</code>.
     * @param context the <code>ServletContext</code>
     */
    public void setServletContext(ServletContext context)
    {
        this.context = context;
    }
}
