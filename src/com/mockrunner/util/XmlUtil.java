package com.mockrunner.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import org.apache.xerces.parsers.DOMParser;
import org.cyberneko.html.HTMLConfiguration;
import org.jdom.Element;
import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.ls.DOMInput;
import org.xml.sax.InputSource;

/**
 * Util class for HTML and XML parsing.
 */
public class XmlUtil
{
    /**
     * Convinience method for HTML fragments. Returns the body
     * as JDOM <code>Element</code>.
     * 
     * If an HTML documents looks like this:
     * <pre>
     * &lt;html&gt;
     * &lt;head&gt;
     * &lt;/head&gt;
     * &lt;body&gt;
     * &lt;h1&gt;
     * &lt;/h1&gt;
     * &lt;/body&gt;
     * &lt;/html&gt;
     * </pre>
     * 
     * the method returns the h1 tag as <code>Element</code>.
     * @param document the <code>org.jdom.Document</code>
     * @return the body <code>Element</code>
     */
    public static Element getBodyFragmentJDOMDocument(org.jdom.Document document)
    {
        Element element = document.getRootElement().getChild("BODY");
        if(null == element)
        {
            element = document.getRootElement().getChild("body");
        }
        if(null != element)
        {
            List childs = element.getChildren();
            if(null != childs && childs.size() > 0) return (Element)childs.get(0);
        }
        return null;
    }
    
    /**
     * Returns the documents XML content as a string.
     * @param document the <code>org.jdom.Document</code>
     * @return the output as string
     */
    public static String createStringFromJDOMDocument(org.jdom.Document document)
    {
        try
        {
            XMLOutputter outputter = new XMLOutputter();
            StringWriter writer = new StringWriter();
            outputter.output(document, writer);
            writer.flush();
            return writer.toString();
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    /**
     * Creates a JDOM <code>Document</code> from a specified
     * W3C <code>Document</code>.
     * @param document the <code>org.w3c.dom.Document</code>
     * @return the <code>org.jdom.Document</code>
     */
    public static org.jdom.Document createJDOMDocument(org.w3c.dom.Document document)
    {
        return new DOMBuilder().build(document);
    }
    
    /**
     * Returns a parser suitable for parsing HTML documents.
     * The NekoHTML parser is used with some settings to
     * preserve case of tag names. This method is used by {@link #parseHTML}.
     * @return instance of <code>org.apache.xerces.parsers.DOMParser</code>
     *         with Neko configuration
     */
    public static DOMParser getHTMLParser()
    {
        try
        {
            HTMLConfiguration config = new HTMLConfiguration();
            config.setProperty("http://cyberneko.org/html/properties/names/elems", "match");
            config.setProperty("http://cyberneko.org/html/properties/names/attrs", "no-change");
            DOMParser parser = new DOMParser(config);
            return parser;
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    /**
     * Parses the specified HTML with the NekoHTML parser.
     * If you want to use another HTML parser or configure
     * the NekoHTML parser with special features, you can use
     * the <code>parse</code> methods.
     * @param source the HTML as String
     * @return the parsed document as org.w3c.dom.Document
     */
    public static org.w3c.dom.Document parseHTML(String source)
    {
        try
        {
            return parse(getHTMLParser(), source);
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    /**
     * Parses the specified XML with the specified parser.
     * The main purpose of this method is to use the NekoHTML 
     * parser with custom features and properties. If you can live
     * with the settings provided by Mockrunner, you can use 
     * {@link #parseHTML}.
     * @param parser the parser (must extend 
     *               <code>org.apache.xerces.parsers.DOMParser</code>), 
     *               e.g. the one returned by {@link #getHTMLParser}
     * @param source the XML as String
     * @return the parsed document as org.w3c.dom.Document
     */
    public static org.w3c.dom.Document parse(DOMParser parser, String source)
    {
        try
        {
            parser.parse(new InputSource(new StringReader(source)));
            return parser.getDocument();
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    /**
     * Parses the specified XML with the specified parser.
     * The main purpose of this method is to use a custom parser.
     * @param parser the parser
     * @param data the data as <code>DOMInput</code>
     * @return the parsed document as org.w3c.dom.Document
     */
    public static org.w3c.dom.Document parse(org.w3c.dom.ls.DOMParser parser, DOMInput data)
    {
        try
        {
            return parser.parse(data);
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }
}
