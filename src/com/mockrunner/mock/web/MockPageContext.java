package com.mockrunner.mock.web;

import java.io.IOException;
import java.util.Stack;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspWriter;
//import javax.servlet.jsp.el.ExpressionEvaluator;
//import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.tagext.BodyContent;

/**
 * Mock implementation of <code>PageContext</code>.
 */
public class MockPageContext extends org.apache.struts.mock.MockPageContext
{
    private JspWriter jspWriter;
    private Stack outStack;
    private Exception exception;
    private Object page;
    //private ExpressionEvaluator evaluator;
    //private VariableResolver resolver;

    public MockPageContext(ServletConfig config, ServletRequest request, ServletResponse response)
    {
        super(config, request, response);
        jspWriter = new MockJspWriter();
        outStack = new Stack();
    }
    
    /**
     * This method allows to set custom implementations
     * of <code>JspWriter</code>. Per default, {@link MockJspWriter}
     * is used.
     * @param jspWriter the <code>JspWriter</code>
     */
    public void setJspWriter(JspWriter jspWriter)
    {
        this.jspWriter = jspWriter;
    }
  
    public JspWriter getOut()
    {
        return jspWriter;
    }
    
    public Exception getException() 
    {
        return exception;
    }
    
    public void setException(Exception exception) 
    {
        this.exception = exception;
    }
    
    public Object getPage() 
    {
        return page;
    }
    
    public void setPage(Object page) 
    {
        this.page = page;
    }
    
    public void handlePageException(Exception exc) 
    {
        
    }

    public void handlePageException(Throwable thr) 
    {
    
    }
    
    public void forward(String path) 
    {
    
    }

    public void include(String path)
    {
    
    }
    
    public void include(String path, boolean flush) throws ServletException, IOException
    {

    }

    public void initialize(Servlet servlet, ServletRequest request,
                           ServletResponse response, String errorPageURL,
                           boolean needsSession, int bufferSize,
                           boolean autoFlush) 
    {
        
    }

    public JspWriter popBody() 
    {
        jspWriter = (JspWriter)outStack.pop();
        return jspWriter;
    }
    
    public BodyContent pushBody() 
    {
        outStack.push(jspWriter);
        MockBodyContent bodyContent = new MockBodyContent(jspWriter);
        jspWriter = bodyContent;
        return bodyContent;
    }

    public void release() 
    {
        jspWriter = new MockJspWriter();
        outStack = new Stack();
    }
    /*
    public void setExpressionEvaluator(ExpressionEvaluator evaluator)
    {
        this.evaluator = evaluator;
    }

    public void setVariableResolver(VariableResolver resolver)
    {
        this.resolver = resolver;
    }
    
    public ExpressionEvaluator getExpressionEvaluator()
    {
        return evaluator;
    }

    public VariableResolver getVariableResolver()
    {
        return resolver;
    }*/
}
