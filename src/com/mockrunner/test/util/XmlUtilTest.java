package com.mockrunner.test.util;

import java.io.FileInputStream;
import java.util.List;

import junit.framework.TestCase;

import org.jdom.Document;
import org.jdom.Element;

import com.mockrunner.util.web.XmlUtil;

public class XmlUtilTest extends TestCase
{
    private String source;
    
    private void prepareHTMLFile() throws Exception
    {
        FileInputStream stream = new FileInputStream("src/com/mockrunner/test/util/test.html");
        StringBuffer output = new StringBuffer();
        int nextChar = stream.read();
        while(-1 != nextChar)
        {
            output.append((char)nextChar);
            nextChar = stream.read();
        }
        source = output.toString();
    }
  
    private void prepareHTML()
    {
        StringBuffer output = new StringBuffer();
        output.append("<html>\n");
        output.append("<head>\n");
        output.append("<meta http-equiv=\"refresh\" content=\"");
        output.append("0;URL=http://www.mockrunner.com>\"\n");
        output.append("</head>\n");
        output.append("<body>\n");
        output.append("<h3>");
        output.append("You will be redirected to ");
        output.append("<a href=\"http://www.mockrunner.com\">");
        output.append("http://www.mockrunner.com</a>");
        output.append("</h3>\n");
        output.append("</body>\n");
        output.append("</html>\n");
        source = output.toString();
    }

    public void testParseHTML() throws Exception
    {
        prepareHTML();
        Document document = XmlUtil.createJDOMDocument(XmlUtil.parseHTML(source));
        Element rootElement = document.getRootElement(); 
        assertEquals("html", rootElement.getName());
        List children = rootElement.getChildren();
        assertTrue(children.size() == 2);
        Element headElement = (Element)children.get(0);
        assertEquals("head", headElement.getName());
        Element metaElement = headElement.getChild("meta");
        assertEquals("refresh", metaElement.getAttributeValue("http-equiv"));
        assertEquals("0;URL=http://www.mockrunner.com>", metaElement.getAttributeValue("content"));
        Element bodyElement = (Element)children.get(1);
        Element h3Element = bodyElement.getChild("h3");
        assertEquals("You will be redirected to ", h3Element.getText());
        Element linkElement = h3Element.getChild("a");
        assertEquals("http://www.mockrunner.com", linkElement.getAttributeValue("href"));
        assertEquals("http://www.mockrunner.com", linkElement.getText());
    }
    
    public void testParseHTMLFile() throws Exception
    {
        prepareHTMLFile();
        Document document = XmlUtil.createJDOMDocument(XmlUtil.parseHTML(source));
        Element rootElement = document.getRootElement(); 
        assertEquals("html", rootElement.getName());
        List children = rootElement.getChildren();
        assertTrue(children.size() == 2);
        Element headElement = (Element)children.get(0);
        assertEquals("head", headElement.getName());
        Element bodyElement = (Element)children.get(1);
        assertEquals("body", bodyElement.getName());
        Element table = XmlUtil.getBodyFragmentFromJDOMDocument(document);
        assertEquals("table", table.getName());
    }
}
