package com.mockrunner.mock.web;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspEngineInfo;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;

/**
 * Mock implementation of <code>JspFactory</code>.
 * This implementation returns <code>null</code>
 * for {@link #getJspApplicationContext}. For full
 * support of <code>JspApplicationContext</code> use
 * {@link JasperJspFactory}.
 */
public class MockJspFactory extends JspFactory
{
    private JspEngineInfo engineInfo;
    private JspApplicationContext applicationContext;
    private PageContext pageContext;

    public MockJspFactory()
    {
        engineInfo = new MockJspEngineInfo();
    }
    
    /**
     * Set the <code>JspEngineInfo</code>. Per default, 
     * {@link MockJspEngineInfo} is used.
     * @param engineInfo the <code>JspEngineInfo</code>
     */
    public void setEngineInfo(JspEngineInfo engineInfo)
    {
        this.engineInfo = engineInfo;
    }

    /**
     * Set the <code>JspApplicationContext</code>.
     * @param applicationContext the <code>JspApplicationContext</code>
     */
    public void setJspApplicationContext(JspApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }

    /**
     * Set the <code>PageContext</code>.
     * @param pageContext the <code>PageContext</code>
     */
    public void setPageContext(PageContext pageContext)
    {
        this.pageContext = pageContext;
    }

    /**
     * Returns the <code>PageContext</code>.
     * @return the <code>PageContext</code>
     */
    public PageContext getPageContext()
    {
        return pageContext;
    }

    public JspEngineInfo getEngineInfo()
    {
        return engineInfo;
    }

    public JspApplicationContext getJspApplicationContext(ServletContext context)
    {
        return applicationContext;
    }

    public PageContext getPageContext(Servlet servlet, ServletRequest request, ServletResponse response, String errorPageURL, boolean needsSession, int buffer, boolean autoflush)
    {
        return pageContext;
    }

    public void releasePageContext(PageContext pageContext)
    {

    }
}
