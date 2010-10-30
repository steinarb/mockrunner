package com.mockrunner.mock.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Mock implementation of <code>JspPropertyGroupDescriptor</code>.
 */
public class MockJspPropertyGroupDescriptor // implements JspPropertyGroupDescriptor
{
    private String buffer;
    private String defaultContentType;
    private String deferredSyntaxAllowedAsLiteral;
    private String elIgnored;
    private String errorOnUndeclaredNamespace;
    private List includeCodas;
    private List includePreludes;
    private String isXml;
    private String pageEncoding;
    private String scriptingInvalid;
    private String trimDirectiveWhitespaces;
    private Collection urlPatterns;
    
    public MockJspPropertyGroupDescriptor()
    {
        reset();
    }
    
    public void reset()
    {
        buffer = "8kb";
        defaultContentType = "text/html";
        deferredSyntaxAllowedAsLiteral = "false";
        elIgnored = "false";
        errorOnUndeclaredNamespace = "false";
        includeCodas = new ArrayList();
        includePreludes = new ArrayList();
        isXml = "false";
        pageEncoding = "ISO-8859-1";
        scriptingInvalid = "false";
        trimDirectiveWhitespaces = "false";
        urlPatterns = new ArrayList();
    }

    public String getBuffer()
    {
        return buffer;
    }

    public String getDefaultContentType()
    {
        return defaultContentType;
    }

    public String getDeferredSyntaxAllowedAsLiteral()
    {
        return deferredSyntaxAllowedAsLiteral;
    }

    public String getElIgnored()
    {
        return elIgnored;
    }

    public String getErrorOnUndeclaredNamespace()
    {
        return errorOnUndeclaredNamespace;
    }

    public Collection getIncludeCodas()
    {
        return new ArrayList(includeCodas);
    }

    public Collection getIncludePreludes()
    {
        return new ArrayList(includePreludes);
    }

    public String getIsXml()
    {
        return isXml;
    }

    public String getPageEncoding()
    {
        return pageEncoding;
    }

    public String getScriptingInvalid()
    {
        return scriptingInvalid;
    }

    public String getTrimDirectiveWhitespaces()
    {
        return trimDirectiveWhitespaces;
    }

    public Collection getUrlPatterns()
    {
        return new ArrayList(urlPatterns);
    }

    /**
     * Sets the buffer size.
     * 
     * @param buffer the buffer size
     */
    public void setBuffer(String buffer)
    {
        this.buffer = buffer;
    }

    /**
     * Sets the default content type.
     * 
     * @param defaultContentType the default content type
     */
    public void setDefaultContentType(String defaultContentType)
    {
        this.defaultContentType = defaultContentType;
    }

    /**
     * Sets if the deferred character sequence is allowed as literal text.
     * 
     * @param deferredSyntaxAllowedAsLiteral is the deferred character sequence allowed as literal text
     */
    public void setDeferredSyntaxAllowedAsLiteral(String deferredSyntaxAllowedAsLiteral)
    {
        this.deferredSyntaxAllowedAsLiteral = deferredSyntaxAllowedAsLiteral;
    }

    /**
     * Sets if EL evaluation is disabled or enabled.
     * 
     * @param elIgnored is EL evaluation disabled or enabled
     */
    public void setElIgnored(String elIgnored)
    {
        this.elIgnored = elIgnored;
    }

    /**
     * Sets if an error should be raised on undeclared namespace.
     * 
     * @param errorOnUndeclaredNamespace should an error should be raised on undeclared namespace
     */
    public void setErrorOnUndeclaredNamespace(String errorOnUndeclaredNamespace)
    {
        this.errorOnUndeclaredNamespace = errorOnUndeclaredNamespace;
    }
    
    /**
     * Adds an include coda to the collection of include codas.
     * 
     * @param includeCode the include coda to add
     */
    public void addIncludeCoda(String includeCode)
    {
        this.includeCodas.add(includeCode);
    }
    
    /**
     * Clears the collection of include codas.
     */
    public void clearIncludeCodas()
    {
        this.includeCodas.clear();
    }
    
    /**
     * Adds an include prelude to the collection of include preludes.
     * 
     * @param includePrelude the include prelude to add
     */
    public void addIncludePrelude(String includePrelude)
    {
        this.includePreludes.add(includePrelude);
    }
    
    /**
     * Clears the collection of include preludes.
     */
    public void clearIncludePreludes()
    {
        this.includePreludes.clear();
    }

    /**
     * Sets if it is an XML JSP document.
     * 
     * @param isXml is it an XML JSP document
     */
    public void setIsXml(String isXml)
    {
        this.isXml = isXml;
    }

    /**
     * Sets the page encoding.
     * 
     * @param pageEncoding the page encoding
     */
    public void setPageEncoding(String pageEncoding)
    {
        this.pageEncoding = pageEncoding;
    }

    /**
     * Sets if scripting is invalid.
     * 
     * @param scriptingInvalid is scripting invalid
     */
    public void setScriptingInvalid(String scriptingInvalid)
    {
        this.scriptingInvalid = scriptingInvalid;
    }

    /**
     * Sets if directive whitespaces should be trimmed.
     * 
     * @param trimDirectiveWhitespaces should directive whitespaces be trimmed
     */
    public void setTrimDirectiveWhitespaces(String trimDirectiveWhitespaces)
    {
        this.trimDirectiveWhitespaces = trimDirectiveWhitespaces;
    }

    /**
     * Adds an URL pattern to the collection of URL patterns.
     * 
     * @param urlPattern the URL pattern to add
     */
    public void addUrlPattern(String urlPattern)
    {
        this.urlPatterns.add(urlPattern);
    }
    
    /**
     * Clears the collection of URL patterns.
     */
    public void clearUrlPatterns()
    {
        this.urlPatterns.clear();
    }
}
