package com.mockrunner.servlet;

import java.io.BufferedReader;
import java.io.StringReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.mockrunner.base.MockObjectFactory;
import com.mockrunner.base.VerifyFailedException;

/**
 * Module for servlet tests. 
 */
public class ServletTestModule
{
    private MockObjectFactory mockFactory;
    private HttpServlet servlet;
      
    public ServletTestModule(MockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
    }
    
    /**
     * Creates a servlet and initializes it. <i>servletClass</i> must
     * be of the type <code>HttpServlet</code>, otherwise a
     * <code>RuntimeException</code> will be thrown.
     * @param servletClass the class of the servlet
     * @return instance of <code>HttpServlet</code>
     */
    public HttpServlet createServlet(Class servletClass)
    {
        try
        {
            servlet = (HttpServlet)servletClass.newInstance();
            servlet.init(mockFactory.getMockServletConfig());
            return servlet;
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    /**
     * Returns the current servlet.
     * @return the servlet
     */
    public HttpServlet getServlet()
    {
        return servlet;
    }
    
    /**
     * Calls the servlet <code>init</code> method. Is automatically
     * done when calling {@link #createServlet}.
     */
    public void init()
    {
        try
        {
            servlet.init();
        }
        catch(ServletException exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    /**
     * Calls the servlet <code>doDelete</code> method.
     */
    public void doDelete()
    {
        mockFactory.getMockRequest().setupGetMethod("DELETE");
        callService();
    }
    
    /**
     * Calls the servlet <code>doGet</code> method.
     */          
    public void doGet()
    {
        mockFactory.getMockRequest().setupGetMethod("GET");
        callService();
    }
    
    /**
     * Calls the servlet <code>doOptions</code> method.
     */          
    public void doOptions()
    {
        mockFactory.getMockRequest().setupGetMethod("OPTIONS");
        callService();
    }
    
    /**
     * Calls the servlet <code>doPost</code> method.
     */         
    public void doPost()
    {
        mockFactory.getMockRequest().setupGetMethod("POST");
        callService();
    }
    
    /**
     * Calls the servlet <code>doPut</code> method.
     */         
    public void doPut()
    {
        mockFactory.getMockRequest().setupGetMethod("PUT");
        callService();
    }
    
    /**
     * Calls the servlet <code>doTrace</code> method.
     */          
    public void doTrace()
    {
        mockFactory.getMockRequest().setupGetMethod("TRACE");
        callService();
    }
    
    /**
     * Returns the servlet output as a string. Flushes the output
     * before returning it.
     * @return the servlet output
     */
    public String getOutput()
    {
        try
        {
            mockFactory.getMockResponse().getWriter().flush();    
        }
        catch(Exception exc)
        {
        
        }
        return mockFactory.getMockResponse().getOutputStreamContents();
    }
    
    /**
     * Returns the servlet output as a <code>BufferedReader</code>. 
     * Flushes the output before returning it.
     * @return the servlet output
     */
    public BufferedReader getOutputAsBufferedReader()
    {
        return new BufferedReader(new StringReader(getOutput()));   
    }
    
    /**
     * Verifies the servlet output.
     * @param output the expected output.
     * @throws VerifyFailedException if verification fails
     */  
    public void verifyOutput(String output)
    {
        String actualOutput = getOutput();
        if(!output.equals(actualOutput))
        {
            throw new VerifyFailedException("actual output: " + actualOutput + " does not match expected output");
        }
    }
    
    /**
     * Verifies if the servlet output contains the specified data.
     * @param output the data
     * @throws VerifyFailedException if verification fails
     */   
    public void verifyOutputContains(String output)
    {
        String actualOutput = getOutput();
        if(-1 == actualOutput.indexOf(output))
        {
            throw new VerifyFailedException("actual output: " + actualOutput + " does not match expected output");
        }
    }
    
    private void callService()
    {
        try
        {
            servlet.service(mockFactory.getMockRequest(), mockFactory.getMockResponse());
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }      
}
