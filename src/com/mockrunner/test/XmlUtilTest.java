package com.mockrunner.test;

import java.util.List;

import junit.framework.TestCase;

import org.jdom.Document;
import org.jdom.Element;

import com.mockrunner.util.XmlUtil;


public class XmlUtilTest extends TestCase
{
    private String source;
    
    public XmlUtilTest(String arg0)
    {
        super(arg0);
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
}
