package com.mockrunner.mock.web;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspEngineInfo;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;

import org.apache.jasper.runtime.JspApplicationContextImpl;

/**
 * This implementation of <code>JspFactory</code>
 * provides full support of the <b>Unified Expression Language</b>
 * API. Expression evaluation is available only for the <b>Unified 
 * Expression Language</b> API, not for the pre JSP 2.1
 * <code>javax.servlet.jsp.el</code> Expression Language API.
 */
public class JasperJspFactory extends JspFactory
{
    private WebMockObjectFactory mockFactory;
    private JspEngineInfo engineInfo;
    private JspApplicationContextImpl applicationContext;

    public JasperJspFactory()
    {
        engineInfo = new MockJspEngineInfo();
    }
    
    /**
     * Configures this implementation for EL support.
     * Use
     * <code>getWebMockObjectFactory().setDefaultJspFactory(new JasperJspFactory().configure(getWebMockObjectFactory()));</code>
     * to set this implementation as the default factory.
     * @return this instance for convenience
     */
    public JasperJspFactory configure(WebMockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
        applicationContext = JspApplicationContextImpl.getInstance(mockFactory.getMockServletContext());
        mockFactory.getMockPageContext().setELContext(applicationContext.createELContext(mockFactory.getMockPageContext()));
        return this;
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
        return mockFactory.getMockPageContext();
    }

    public void releasePageContext(PageContext pageContext)
    {

    }
}
