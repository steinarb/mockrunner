package com.mockrunner.servlet;

import java.io.BufferedReader;

import javax.servlet.http.HttpServlet;

import com.mockrunner.base.BaseTestCase;

public class ServletTestCaseAdapter extends BaseTestCase
{
    private ServletTestModule servletTestModule;
    
    public ServletTestCaseAdapter()
    {
    
    }

    public ServletTestCaseAdapter(String arg0)
    {
        super(arg0);
    }
    
    /**
     * Creates the <code>ServletTestModule</code>. If you
     * overwrite this method, you must call 
     * <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        servletTestModule = createServletTestModule(getMockObjectFactory());
    }
    
    /**
     * Gets the <code>ServletTestModule</code>. 
     * @return the <code>ServletTestModule</code>
     */
    protected ServletTestModule getServletTestModule()
    {
        return servletTestModule;
    }
    
    /**
     * Sets the <code>ServletTestModule</code>. 
     * @param actionTestModule the <code>ServletTestModule</code>
     */
    protected void setActionTestModule(ServletTestModule servletTestModule)
    {
        this.servletTestModule = servletTestModule;
    }
    
    /**
     * Delegates to {@link ServletTestModule#createServlet}
     */
    public HttpServlet createServlet(Class servletClass)
    {
        return servletTestModule.createServlet(servletClass);
    }
    
    /**
     * Delegates to {@link ServletTestModule#getServlet}
     */
    public HttpServlet getServlet()
    {
        return servletTestModule.getServlet();
    }

    /**
     * Delegates to {@link ServletTestModule#init}
     */
    public void init()
    {
        servletTestModule.init();
    }

    /**
     * Delegates to {@link ServletTestModule#doDelete}
     */
    public void doDelete()
    {
        servletTestModule.doDelete();
    }
    
    /**
     * Delegates to {@link ServletTestModule#doGet}
     */  
    public void doGet()
    {
        servletTestModule.doGet();
    }
    
    /**
     * Delegates to {@link ServletTestModule#doOptions}
     */  
    public void doOptions()
    {
        servletTestModule.doOptions();
    }
     
    /**
     * Delegates to {@link ServletTestModule#doPost}
     */      
    public void doPost()
    {
        servletTestModule.doPost();
    }
    
    /**
     * Delegates to {@link ServletTestModule#doPut}
     */      
    public void doPut()
    {
        servletTestModule.doPut();
    }
    
    /**
     * Delegates to {@link ServletTestModule#doTrace}
     */      
    public void doTrace()
    {
        servletTestModule.doTrace();
    }
    
    /**
     * Delegates to {@link ServletTestModule#getOutput}
     */ 
    public String getOutput()
    {
        return servletTestModule.getOutput();
    }

    /**
     * Delegates to {@link ServletTestModule#getOutputAsBufferedReader}
     */ 
    public BufferedReader getOutputAsBufferedReader()
    {
        return servletTestModule.getOutputAsBufferedReader();  
    }
  
    /**
     * Delegates to {@link ServletTestModule#verifyOutput}
     */ 
    public void verifyOutput(String output)
    {
        servletTestModule.verifyOutput(output);
    }
   
    /**
     * Delegates to {@link ServletTestModule#verifyOutputContains}
     */
    public void verifyOutputContains(String output)
    {
        servletTestModule.verifyOutputContains(output);
    }
}
