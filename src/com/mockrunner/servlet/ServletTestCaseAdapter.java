package com.mockrunner.servlet;

import javax.servlet.http.HttpServlet;

import com.mockrunner.base.HTMLOutputModule;
import com.mockrunner.base.HTMLOutputTestCase;

/**
 * Delegator for {@link ServletTestModule}. You can
 * subclass this adapter or use {@link ServletTestModule}
 * directly (so your test case can use another base
 * class).
 */
public class ServletTestCaseAdapter extends HTMLOutputTestCase
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
     * Returns the <code>ServletTestModule</code> as 
     * <code>HTMLOutputModule</code>.
     * @return the <code>HTMLOutputModule</code>
     */
    protected HTMLOutputModule getHTMLOutputModule()
    {
        return servletTestModule;
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
