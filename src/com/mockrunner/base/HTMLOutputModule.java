package com.mockrunner.base;

import java.io.BufferedReader;
import java.io.StringReader;

import com.mockrunner.util.XmlUtil;

/**
 * Base class for modules which produce HTML
 * code as output. These modules have to implement
 * {@link #getOutput}. The HTML code is provided
 * in different formats, e.g. as parsed XML
 * documents.
 */
public abstract class HTMLOutputModule
{
    /**
     * Returns the output as a string. Concrete modules implement
     * this method.
     * @return the output
     */
    public abstract String getOutput();
    
    /**
     * Returns the output as a <code>BufferedReader</code>. 
     * @return the output
     */
    public BufferedReader getOutputAsBufferedReader()
    {
        return new BufferedReader(new StringReader(getOutput()));   
    }
    
    /**
     * Parses the output with an HTML parser and returns it
     * as a W3C XML document. Uses the NekoHTML parser. Throws
     * a <code>RuntimeException</code> if the output is not
     * parsable. NekoHTML is quite good in fixing sloppy
     * HTML code. If you want to use another parser, you can
     * use the method {@link com.mockrunner.util.XmlUtil#parse} 
     * to parse the string output yourself. Please note that
     * HTML parsing is not very fast and may slow down
     * your test suite.
     * @return the output as <code>org.w3c.dom.Document</code>
     * @throws RuntimeException if a parsing error occurs
     */
    public org.w3c.dom.Document getOutputAsW3CDocument()
    {
        return XmlUtil.parseHTML(getOutput());
    }
    
    /**
     * Parses the output with an HTML parser and returns it
     * as a JDOM XML document. Uses the NekoHTML parser. Throws
     * a <code>RuntimeException</code> if the output is not
     * parsable. NekoHTML is quite good in fixing sloppy
     * HTML code. If you want to use another parser, you can
     * use the method {@link com.mockrunner.util.XmlUtil#parse} 
     * to parse the string output yourself. Please note that
     * HTML parsing is not very fast and may slow down
     * your test suite.
     * @return the output as <code>org.jdom.Document</code>
     * @throws RuntimeException if a parsing error occurs
     */
    public org.jdom.Document getOutputAsJDOMDocument()
    {
        return XmlUtil.createJDOMDocument(getOutputAsW3CDocument());
    }
    
    /**
     * Parses the output with an HTML parser and returns it
     * as fixed, wellformed XML. Uses the NekoHTML parser. Throws
     * a <code>RuntimeException</code> if the output is not
     * parsable. Please note that HTML parsing is not very fast 
     * and may slow down your test suite.
     * @return the output as wellformed XML
     * @throws RuntimeException if a parsing error occurs
     */
    public String getOutputAsWellformedXML()
    {
        return XmlUtil.createStringFromJDOMDocument(getOutputAsJDOMDocument());
    }
}
