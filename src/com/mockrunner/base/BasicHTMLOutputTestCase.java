package com.mockrunner.base;

import java.io.BufferedReader;

/**
 * Delegator for {@link HTMLOutputModule}. The corresponding
 * adapters extend this class. This class is used for the basic
 * adapter versions.
 */
public abstract class BasicHTMLOutputTestCase extends BasicWebTestCase
{
    public BasicHTMLOutputTestCase()
    {

    }

    public BasicHTMLOutputTestCase(String arg0)
    {
        super(arg0);
    }
    
    protected WebTestModule getWebTestModule()
    {
        return getHTMLOutputModule();
    }

    /**
     * Implemented by concrete subclasses.
     */ 
    protected abstract HTMLOutputModule getHTMLOutputModule();
    
    /**
     * Delegates to {@link HTMLOutputModule#getOutput}
     */ 
    protected String getOutput()
    {
        return getHTMLOutputModule().getOutput();
    }

    /**
     * Delegates to {@link HTMLOutputModule#getOutputAsBufferedReader}
     */
    protected BufferedReader getOutputAsBufferedReader()
    {
        return getHTMLOutputModule().getOutputAsBufferedReader();  
    }

    /**
     * Delegates to {@link HTMLOutputModule#getOutputAsW3CDocument}
     */
    protected org.w3c.dom.Document getOutputAsW3CDocument()
    {
        return getHTMLOutputModule().getOutputAsW3CDocument();
    }

    /**
     * Delegates to {@link HTMLOutputModule#getOutputAsJDOMDocument}
     */
    protected org.jdom.Document getOutputAsJDOMDocument()
    {
        return getHTMLOutputModule().getOutputAsJDOMDocument();
    }

    /**
     * Delegates to {@link HTMLOutputModule#getOutputAsWellformedXML}
     */
    protected String getOutputAsWellformedXML()
    {
        return getHTMLOutputModule().getOutputAsWellformedXML();
    }
    
    /**
     * Delegates to {@link HTMLOutputModule#setCaseSensitive}
     */
    protected void setCaseSensitive(boolean caseSensitive)
    {
        getHTMLOutputModule().setCaseSensitive(caseSensitive);
    }
    
    /**
     * Delegates to {@link HTMLOutputModule#verifyOutput}
     */
    protected void verifyOutput(String expectedOutput)
    {
        getHTMLOutputModule().verifyOutput(expectedOutput);
    }
    
    /**
     * Delegates to {@link HTMLOutputModule#verifyOutputContains}
     */
    protected void verifyOutputContains(String expectedOutput)
    {
        getHTMLOutputModule().verifyOutputContains(expectedOutput);
    }
    
    /**
     * Delegates to {@link HTMLOutputModule#verifyOutputRegularExpression}
     */
    protected void verifyOutputRegularExpression(String expression)
    {
        getHTMLOutputModule().verifyOutputRegularExpression(expression);
    }
}
