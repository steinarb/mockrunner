package com.mockrunner.base;

import java.io.BufferedReader;

/**
 * Delegator for {@link HTMLOutputModule}. The corresponding
 * adapters extend this class.
 */
public abstract class HTMLOutputTestCase extends BaseTestCase
{
    public HTMLOutputTestCase()
    {

    }

    public HTMLOutputTestCase(String arg0)
    {
        super(arg0);
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
}
