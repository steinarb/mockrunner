package com.mockrunner.util;

import java.io.StringReader;

import org.apache.xerces.parsers.DOMParser;
import org.cyberneko.html.HTMLConfiguration;
import org.jdom.input.DOMBuilder;
import org.xml.sax.InputSource;

/**
 * Util class for HTML and XML parsing.
 */
public class XmlUtil
{
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
     * the {@link #parse} method.
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
     * The main purpose of this method is to use a custom
     * HTML parser. If you can live with the NekoHTML parser
     * and its default features, you can use {@link #parseHTML}.
     * @param parser the <code>DOMParser</code>, e.g. the one
     *        returned by {@link #getHTMLParser}
     * @param source the HTML as String
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
}
