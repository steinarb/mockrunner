package com.mockrunner.base;

import java.io.BufferedReader;
import java.io.StringReader;

import com.mockrunner.mock.web.WebMockObjectFactory;
import com.mockrunner.util.common.StringUtil;
import com.mockrunner.util.web.XmlUtil;

/**
 * Base class for modules which produce HTML
 * code as output. These modules have to implement
 * {@link #getOutput}. The HTML code is provided
 * in different formats, e.g. as parsed XML
 * documents.
 */
public abstract class HTMLOutputModule extends WebTestModule
{
    private boolean caseSensitive;
    
    public HTMLOutputModule(WebMockObjectFactory mockFactory)
    {
        super(mockFactory);
        caseSensitive = true;
    }
    
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
     * Parses the output with the NekoHTML parser and returns it
     * as a W3C XML document. Throws a {@link NestedApplicationException} 
     * if the output is not parsable. NekoHTML is quite good in fixing sloppy
     * HTML code. If you want to use a different parser configuration,
     * you can use the method {@link com.mockrunner.util.web.XmlUtil#parse} 
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
     * Parses the output with the NekoHTML parser and returns it
     * as a JDOM XML document. Throws a {@link NestedApplicationException} 
     * if the output is not parsable. NekoHTML is quite good in fixing sloppy
     * HTML code. If you want to use a different parser configuration,
     * you can use the method {@link com.mockrunner.util.web.XmlUtil#parse} 
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
     * Parses the output with the NekoHTML parser and returns it
     * as fixed, wellformed XML. Throws a {@link NestedApplicationException} 
     * if the output is not parsable. NekoHTML is quite good in fixing sloppy
     * HTML code. If you want to use a different parser configuration,
     * you can use the method {@link com.mockrunner.util.web.XmlUtil#parse} 
     * to parse the string output yourself. Please note that
     * HTML parsing is not very fast and may slow down
     * your test suite.
     * @return the output as wellformed XML
     * @throws RuntimeException if a parsing error occurs
     */
    public String getOutputAsWellformedXML()
    {
        return XmlUtil.createStringFromJDOMDocument(getOutputAsJDOMDocument());
    }
    
    /**
     * Set if {@link #verifyOutput}, {@link #verifyOutputContains}
     * and {@link #verifyOutputRegularExpression}.
     * should compare case sensitive. Default is <code>true</code>.
     * @param caseSensitive enable or disable case sensitivity
     */
    public void setCaseSensitive(boolean caseSensitive)
    {
        this.caseSensitive = caseSensitive;
    }
    
    /**
     * Verifies the tag output.
     * @param expectedOutput the expected output.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyOutput(String expectedOutput)
    {
        String actualOutput = getOutput();
        if(!StringUtil.matchesExact(actualOutput, expectedOutput, caseSensitive))
        {
            throw new VerifyFailedException("actual output: " + actualOutput + " does not match expected output");
        }
    }
    
    /**
     * Verifies if the output contains the specified data.
     * @param expectedOutput the data
     * @throws VerifyFailedException if verification fails
     */
    public void verifyOutputContains(String expectedOutput)
    {
        String actualOutput = getOutput();
        if(!StringUtil.matchesContains(actualOutput, expectedOutput, caseSensitive))
        {
            throw new VerifyFailedException("actual output: " + actualOutput + " does not match expected output");
        }
    }
    
    /**
     * Verifies if the output matches the specified regular expression.
     * @param expression the data
     * @throws VerifyFailedException if verification fails
     */
    public void verifyOutputRegularExpression(String expression)
    {
        String actualOutput = getOutput();
        if(!StringUtil.matchesPerl5(actualOutput, expression, caseSensitive))
        {
            throw new VerifyFailedException("actual output: " + actualOutput + " does not match expected output");
        }
    }
}
