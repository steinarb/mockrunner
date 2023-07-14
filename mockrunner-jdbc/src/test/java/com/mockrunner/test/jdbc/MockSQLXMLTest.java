package com.mockrunner.test.jdbc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

import org.jdom2.Document;
import org.jdom2.input.DOMBuilder;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

import com.mockrunner.mock.jdbc.MockSQLXML;
import com.mockrunner.util.common.StreamUtil;

public class MockSQLXMLTest extends TestCase
{
    private MockSQLXML stringSQLXML;
    private MockSQLXML readerSQLXML;
    private MockSQLXML inputStreamSQLXML;
    private MockSQLXML w3cDocumentSQLXML;

    private void prepareTestSQLXMLObjects() throws Exception
    {
        stringSQLXML = new MockSQLXML(getFirstTestXMLAsString());
        readerSQLXML = new MockSQLXML(getTestXMLAsReader(getFirstTestXMLAsString()));
        inputStreamSQLXML = new MockSQLXML(getTestXMLAsInputStream(getFirstTestXMLAsString()));
        w3cDocumentSQLXML = new MockSQLXML(getTestXMLAsW3CDocument(getFirstTestXMLAsString()));
    }
    
    private Reader getTestXMLAsReader(String xml) throws Exception
    {
        return new StringReader(xml);
    }
    
    private InputStream getTestXMLAsInputStream(String xml) throws Exception
    {
        return new ByteArrayInputStream(xml.getBytes("UTF-8"));
    }
    
    private org.w3c.dom.Document getTestXMLAsW3CDocument(String xml) throws Exception
    {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(xml.getBytes("ISO-8859-1")));
    }
    
    private void prepareSecondTestXMLForSAX(SAXResult saxResult) throws Exception
    {
        ContentHandler contentHandler = saxResult.getHandler();
        contentHandler.startDocument();
        AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute("", "att1", "att1", "CDATA", "1");
        attributes.addAttribute("", "att2", "att2", "CDATA", "2");
        contentHandler.startElement("", "xyz", "xyz", attributes);
        contentHandler.characters("Hello World".toCharArray(), 0, "Hello World".toCharArray().length);
        contentHandler.endElement("", "xyz", "xyz");
        contentHandler.endDocument();
    }
    
    private void prepareSecondTestXMLForStAX(StAXResult staxResult) throws Exception
    {
        XMLStreamWriter streamWriter = staxResult.getXMLStreamWriter();
        streamWriter.writeStartDocument();
        streamWriter.writeStartElement("xyz");
        streamWriter.writeAttribute("att1", "1");
        streamWriter.writeAttribute("att2", "2");
        streamWriter.writeCharacters("Hello World");
        streamWriter.writeEndElement();
        streamWriter.writeEndDocument();
    }
    
    private String getFirstTestXMLAsString()
    {
        return "<test>" +
               "<subelement att=\"1\">" + 
               "text" +
               "</subelement>" + 
               "</test>";
    }
    
    private String getSecondTestXMLAsString()
    {
        return "<xyz att1=\"1\" att2=\"2\">" +
               "Hello World" +
               "</xyz>";
    }
    
    private XMLOutputter getCompareOutputter()
    {
        Format format = Format.getCompactFormat();
        format.setOmitDeclaration(true);
        format.setOmitEncoding(true);
        XMLOutputter outputter = new XMLOutputter(format);
        return outputter;
    }
    
    private void assertXMLEqualsTestXML(String xml, String testXML) throws Exception
    {
        SAXBuilder builder = new SAXBuilder();
        Document expected = builder.build(new StringReader(testXML));
        Document testee = builder.build(new StringReader(xml));
        XMLOutputter outputter = getCompareOutputter();
        String expectedString = outputter.outputString(expected);
        String testeeString = outputter.outputString(testee);
        assertEquals(expectedString, testeeString);
    }
    
    private void assertXMLEqualsTestXML(org.w3c.dom.Document xml, String testXML) throws Exception
    {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document expected = saxBuilder.build(new StringReader(testXML));
        DOMBuilder domBuilder = new DOMBuilder();
        Document testee = domBuilder.build(xml);
        XMLOutputter outputter = getCompareOutputter();
        String expectedString = outputter.outputString(expected);
        String testeeString = outputter.outputString(testee);
        assertEquals(expectedString, testeeString);
    }
    
    private void assertXMLEqualsFirstTestXML(XMLStreamReader xml) throws Exception
    {
        xml.nextTag();
        assertEquals("test", xml.getLocalName());
        assertEquals(XMLStreamConstants.START_ELEMENT, xml.getEventType());
        assertEquals(0, xml.getAttributeCount());
        xml.nextTag();
        assertEquals("subelement", xml.getLocalName());
        assertEquals(XMLStreamConstants.START_ELEMENT, xml.getEventType());
        assertEquals(1, xml.getAttributeCount());
        assertEquals("1", xml.getAttributeValue(null, "att"));
        xml.next();
        assertEquals("text", xml.getText());
        assertEquals(XMLStreamConstants.CHARACTERS, xml.getEventType());
        xml.nextTag();
        assertEquals("subelement", xml.getLocalName());
        assertEquals(XMLStreamConstants.END_ELEMENT, xml.getEventType());
        xml.nextTag();
        assertEquals("test", xml.getLocalName());
        assertEquals(XMLStreamConstants.END_ELEMENT, xml.getEventType());
    }
    
    private void assertXMLEqualsSecondTestXML(XMLStreamReader xml) throws Exception
    {
        xml.nextTag();
        assertEquals("xyz", xml.getLocalName());
        assertEquals(XMLStreamConstants.START_ELEMENT, xml.getEventType());
        assertEquals(2, xml.getAttributeCount());
        assertEquals("1", xml.getAttributeValue(null, "att1"));
        assertEquals("2", xml.getAttributeValue(null, "att2"));
        xml.next();
        assertEquals("Hello World", xml.getText());
        assertEquals(XMLStreamConstants.CHARACTERS, xml.getEventType());
        xml.nextTag();
        assertEquals("xyz", xml.getLocalName());
        assertEquals(XMLStreamConstants.END_ELEMENT, xml.getEventType());
    }

    public void testGetMethods() throws Exception
    {
        prepareTestSQLXMLObjects();
        doTestGetString(stringSQLXML, getFirstTestXMLAsString());
        doTestGetString(readerSQLXML, getFirstTestXMLAsString());
        doTestGetString(inputStreamSQLXML, getFirstTestXMLAsString());
        doTestGetString(w3cDocumentSQLXML, getFirstTestXMLAsString());
        prepareTestSQLXMLObjects();
        doTestGetCharacterStream(stringSQLXML, getFirstTestXMLAsString());
        doTestGetCharacterStream(readerSQLXML, getFirstTestXMLAsString());
        doTestGetCharacterStream(inputStreamSQLXML, getFirstTestXMLAsString());
        doTestGetCharacterStream(w3cDocumentSQLXML, getFirstTestXMLAsString());
        prepareTestSQLXMLObjects();
        doTestGetBinaryStream(stringSQLXML, getFirstTestXMLAsString());
        doTestGetBinaryStream(readerSQLXML, getFirstTestXMLAsString());
        doTestGetBinaryStream(inputStreamSQLXML, getFirstTestXMLAsString());
        doTestGetBinaryStream(w3cDocumentSQLXML, getFirstTestXMLAsString());
        prepareTestSQLXMLObjects();
        doTestGetContentAsDocument(stringSQLXML, getFirstTestXMLAsString());
        doTestGetContentAsDocument(readerSQLXML, getFirstTestXMLAsString());
        doTestGetContentAsDocument(inputStreamSQLXML, getFirstTestXMLAsString());
        doTestGetContentAsDocument(w3cDocumentSQLXML, getFirstTestXMLAsString());
        prepareTestSQLXMLObjects();
        doTestGetStreamSource(stringSQLXML, getFirstTestXMLAsString());
        doTestGetStreamSource(readerSQLXML, getFirstTestXMLAsString());
        doTestGetStreamSource(inputStreamSQLXML, getFirstTestXMLAsString());
        doTestGetStreamSource(w3cDocumentSQLXML, getFirstTestXMLAsString());
        prepareTestSQLXMLObjects();
        doTestGetDOMSource(stringSQLXML, getFirstTestXMLAsString());
        doTestGetDOMSource(readerSQLXML, getFirstTestXMLAsString());
        doTestGetDOMSource(inputStreamSQLXML, getFirstTestXMLAsString());
        doTestGetDOMSource(w3cDocumentSQLXML, getFirstTestXMLAsString());
        prepareTestSQLXMLObjects();
        doTestGetSAXSource(stringSQLXML, getFirstTestXMLAsString());
        doTestGetSAXSource(readerSQLXML, getFirstTestXMLAsString());
        doTestGetSAXSource(inputStreamSQLXML, getFirstTestXMLAsString());
        doTestGetSAXSource(w3cDocumentSQLXML, getFirstTestXMLAsString());
        prepareTestSQLXMLObjects();
        doTestGetStAXSource(stringSQLXML, true);
        doTestGetStAXSource(readerSQLXML, true);
        doTestGetStAXSource(inputStreamSQLXML, true);
        doTestGetStAXSource(w3cDocumentSQLXML, true);
    }
    
    public void testSetString() throws Exception
    {
        prepareTestSQLXMLObjects();
        assertTrue(stringSQLXML.isWriteable());
        assertTrue(readerSQLXML.isWriteable());
        assertTrue(inputStreamSQLXML.isWriteable());
        assertTrue(w3cDocumentSQLXML.isWriteable());
        stringSQLXML.setString(getSecondTestXMLAsString());
        readerSQLXML.setString(getSecondTestXMLAsString());
        inputStreamSQLXML.setString(getSecondTestXMLAsString());
        w3cDocumentSQLXML.setString(getSecondTestXMLAsString());
        doTestGetString(stringSQLXML, getSecondTestXMLAsString());
        doTestGetString(readerSQLXML, getSecondTestXMLAsString());
        doTestGetString(inputStreamSQLXML, getSecondTestXMLAsString());
        doTestGetString(w3cDocumentSQLXML, getSecondTestXMLAsString());
        assertFalse(stringSQLXML.isWriteable());
        assertFalse(readerSQLXML.isWriteable());
        assertFalse(inputStreamSQLXML.isWriteable());
        assertFalse(w3cDocumentSQLXML.isWriteable());
    }
    
    public void testSetBinaryStream() throws Exception
    {
        prepareTestSQLXMLObjects();
        assertTrue(stringSQLXML.isWriteable());
        assertTrue(readerSQLXML.isWriteable());
        assertTrue(inputStreamSQLXML.isWriteable());
        assertTrue(w3cDocumentSQLXML.isWriteable());
        OutputStream stringStream = stringSQLXML.setBinaryStream();
        OutputStream readerStream = readerSQLXML.setBinaryStream();
        OutputStream inputStreamStream = inputStreamSQLXML.setBinaryStream();
        OutputStream w3cDocumentStream = w3cDocumentSQLXML.setBinaryStream();
        stringStream.write(getSecondTestXMLAsString().getBytes("UTF-8"));
        readerStream.write(getSecondTestXMLAsString().getBytes("UTF-8"));
        inputStreamStream.write(getSecondTestXMLAsString().getBytes("UTF-8"));
        w3cDocumentStream.write(getSecondTestXMLAsString().getBytes("UTF-8"));
        doTestGetDOMSource(stringSQLXML, getSecondTestXMLAsString());
        doTestGetDOMSource(readerSQLXML, getSecondTestXMLAsString());
        doTestGetDOMSource(inputStreamSQLXML, getSecondTestXMLAsString());
        doTestGetDOMSource(w3cDocumentSQLXML, getSecondTestXMLAsString());
        assertFalse(stringSQLXML.isWriteable());
        assertFalse(readerSQLXML.isWriteable());
        assertFalse(inputStreamSQLXML.isWriteable());
        assertFalse(w3cDocumentSQLXML.isWriteable());
    }
    
    public void testSetCharacterStream() throws Exception
    {
        prepareTestSQLXMLObjects();
        assertTrue(stringSQLXML.isWriteable());
        assertTrue(readerSQLXML.isWriteable());
        assertTrue(inputStreamSQLXML.isWriteable());
        assertTrue(w3cDocumentSQLXML.isWriteable());
        Writer stringWriter = stringSQLXML.setCharacterStream();
        Writer readerWriter = readerSQLXML.setCharacterStream();
        Writer inputStreamWriter = inputStreamSQLXML.setCharacterStream();
        Writer w3cDocumentWriter = w3cDocumentSQLXML.setCharacterStream();
        stringWriter.write(getSecondTestXMLAsString());
        readerWriter.write(getSecondTestXMLAsString());
        inputStreamWriter.write(getSecondTestXMLAsString());
        w3cDocumentWriter.write(getSecondTestXMLAsString());
        doTestGetBinaryStream(stringSQLXML, getSecondTestXMLAsString());
        doTestGetBinaryStream(readerSQLXML, getSecondTestXMLAsString());
        doTestGetBinaryStream(inputStreamSQLXML, getSecondTestXMLAsString());
        doTestGetBinaryStream(w3cDocumentSQLXML, getSecondTestXMLAsString());
        assertFalse(stringSQLXML.isWriteable());
        assertFalse(readerSQLXML.isWriteable());
        assertFalse(inputStreamSQLXML.isWriteable());
        assertFalse(w3cDocumentSQLXML.isWriteable());
    }
    
    public void testSetStreamResult() throws Exception
    {
        prepareTestSQLXMLObjects();
        assertTrue(stringSQLXML.isWriteable());
        assertTrue(readerSQLXML.isWriteable());
        assertTrue(inputStreamSQLXML.isWriteable());
        assertTrue(w3cDocumentSQLXML.isWriteable());
        StreamResult stringResult = stringSQLXML.setResult(StreamResult.class);
        StreamResult readerResult = readerSQLXML.setResult(StreamResult.class);
        StreamResult inputStreamResult = inputStreamSQLXML.setResult(StreamResult.class);
        StreamResult w3cDocumentResult = w3cDocumentSQLXML.setResult(StreamResult.class);
        stringResult.getOutputStream().write(getSecondTestXMLAsString().getBytes("UTF-8"));
        readerResult.getOutputStream().write(getSecondTestXMLAsString().getBytes("UTF-8"));
        inputStreamResult.getOutputStream().write(getSecondTestXMLAsString().getBytes("UTF-8"));
        w3cDocumentResult.getOutputStream().write(getSecondTestXMLAsString().getBytes("UTF-8"));
        doTestGetSAXSource(stringSQLXML, getSecondTestXMLAsString());
        doTestGetSAXSource(readerSQLXML, getSecondTestXMLAsString());
        doTestGetSAXSource(inputStreamSQLXML, getSecondTestXMLAsString());
        doTestGetSAXSource(w3cDocumentSQLXML, getSecondTestXMLAsString());
        assertFalse(stringSQLXML.isWriteable());
        assertFalse(readerSQLXML.isWriteable());
        assertFalse(inputStreamSQLXML.isWriteable());
        assertFalse(w3cDocumentSQLXML.isWriteable());
    }
    
    public void testSetDOMResultSetNode() throws Exception
    {
        prepareTestSQLXMLObjects();
        assertTrue(stringSQLXML.isWriteable());
        assertTrue(readerSQLXML.isWriteable());
        assertTrue(inputStreamSQLXML.isWriteable());
        assertTrue(w3cDocumentSQLXML.isWriteable());
        DOMResult stringResult = stringSQLXML.setResult(DOMResult.class);
        DOMResult readerResult = readerSQLXML.setResult(DOMResult.class);
        DOMResult inputStreamResult = inputStreamSQLXML.setResult(DOMResult.class);
        DOMResult w3cDocumentResult = w3cDocumentSQLXML.setResult(DOMResult.class);
        stringResult.setNode(getTestXMLAsW3CDocument(getSecondTestXMLAsString()));
        readerResult.setNode(getTestXMLAsW3CDocument(getSecondTestXMLAsString()).getFirstChild());
        inputStreamResult.setNode(getTestXMLAsW3CDocument(getSecondTestXMLAsString()));
        w3cDocumentResult.setNode(getTestXMLAsW3CDocument(getSecondTestXMLAsString()).getFirstChild());
        doTestGetStreamSource(stringSQLXML, getSecondTestXMLAsString());
        doTestGetStreamSource(readerSQLXML, getSecondTestXMLAsString());
        doTestGetStreamSource(inputStreamSQLXML, getSecondTestXMLAsString());
        doTestGetStreamSource(w3cDocumentSQLXML, getSecondTestXMLAsString());
        assertFalse(stringSQLXML.isWriteable());
        assertFalse(readerSQLXML.isWriteable());
        assertFalse(inputStreamSQLXML.isWriteable());
        assertFalse(w3cDocumentSQLXML.isWriteable());
    }
    
    public void testSetDOMResultGetNode() throws Exception
    {
        prepareTestSQLXMLObjects();
        assertTrue(stringSQLXML.isWriteable());
        assertTrue(readerSQLXML.isWriteable());
        assertTrue(inputStreamSQLXML.isWriteable());
        assertTrue(w3cDocumentSQLXML.isWriteable());
        DOMResult stringResult = stringSQLXML.setResult(DOMResult.class);
        DOMResult readerResult = readerSQLXML.setResult(DOMResult.class);
        DOMResult inputStreamResult = inputStreamSQLXML.setResult(DOMResult.class);
        DOMResult w3cDocumentResult = w3cDocumentSQLXML.setResult(DOMResult.class);
        org.w3c.dom.Document stringDocument = (org.w3c.dom.Document)stringResult.getNode();
        org.w3c.dom.Document readerDocument = (org.w3c.dom.Document)readerResult.getNode();
        org.w3c.dom.Document inputStreamDocument = (org.w3c.dom.Document)inputStreamResult.getNode();
        org.w3c.dom.Document w3cDocumentDocument = (org.w3c.dom.Document)w3cDocumentResult.getNode();
        stringDocument.appendChild(stringDocument.importNode(getTestXMLAsW3CDocument(getSecondTestXMLAsString()).getFirstChild(), true));
        readerDocument.appendChild(readerDocument.importNode(getTestXMLAsW3CDocument(getSecondTestXMLAsString()).getFirstChild(), true));
        inputStreamDocument.appendChild(inputStreamDocument.importNode(getTestXMLAsW3CDocument(getSecondTestXMLAsString()).getFirstChild(), true));
        w3cDocumentDocument.appendChild(w3cDocumentDocument.importNode(getTestXMLAsW3CDocument(getSecondTestXMLAsString()).getFirstChild(), true));
        doTestGetStAXSource(stringSQLXML, false);
        doTestGetStAXSource(readerSQLXML, false);
        doTestGetStAXSource(inputStreamSQLXML, false);
        doTestGetStAXSource(w3cDocumentSQLXML, false);
        assertFalse(stringSQLXML.isWriteable());
        assertFalse(readerSQLXML.isWriteable());
        assertFalse(inputStreamSQLXML.isWriteable());
        assertFalse(w3cDocumentSQLXML.isWriteable());
    }
    
    public void testSetSAXResult() throws Exception
    {
        prepareTestSQLXMLObjects();
        assertTrue(stringSQLXML.isWriteable());
        assertTrue(readerSQLXML.isWriteable());
        assertTrue(inputStreamSQLXML.isWriteable());
        assertTrue(w3cDocumentSQLXML.isWriteable());
        SAXResult stringResult = stringSQLXML.setResult(SAXResult.class);
        SAXResult readerResult = readerSQLXML.setResult(SAXResult.class);
        SAXResult inputStreamResult = inputStreamSQLXML.setResult(SAXResult.class);
        SAXResult w3cDocumentResult = w3cDocumentSQLXML.setResult(SAXResult.class);
        prepareSecondTestXMLForSAX(stringResult);
        prepareSecondTestXMLForSAX(readerResult);
        prepareSecondTestXMLForSAX(inputStreamResult);
        prepareSecondTestXMLForSAX(w3cDocumentResult);
        doTestGetCharacterStream(stringSQLXML, getSecondTestXMLAsString());
        doTestGetCharacterStream(readerSQLXML, getSecondTestXMLAsString());
        doTestGetCharacterStream(inputStreamSQLXML, getSecondTestXMLAsString());
        doTestGetCharacterStream(w3cDocumentSQLXML, getSecondTestXMLAsString());
        assertFalse(stringSQLXML.isWriteable());
        assertFalse(readerSQLXML.isWriteable());
        assertFalse(inputStreamSQLXML.isWriteable());
        assertFalse(w3cDocumentSQLXML.isWriteable());
    }
    
    public void testSetStAXResult() throws Exception
    {
        prepareTestSQLXMLObjects();
        assertTrue(stringSQLXML.isWriteable());
        assertTrue(readerSQLXML.isWriteable());
        assertTrue(inputStreamSQLXML.isWriteable());
        assertTrue(w3cDocumentSQLXML.isWriteable());
        StAXResult stringResult = stringSQLXML.setResult(StAXResult.class);
        StAXResult readerResult = readerSQLXML.setResult(StAXResult.class);
        StAXResult inputStreamResult = inputStreamSQLXML.setResult(StAXResult.class);
        StAXResult w3cDocumentResult = w3cDocumentSQLXML.setResult(StAXResult.class);
        prepareSecondTestXMLForStAX(stringResult);
        prepareSecondTestXMLForStAX(readerResult);
        prepareSecondTestXMLForStAX(inputStreamResult);
        prepareSecondTestXMLForStAX(w3cDocumentResult);
        doTestGetContentAsDocument(stringSQLXML, getSecondTestXMLAsString());
        doTestGetContentAsDocument(readerSQLXML, getSecondTestXMLAsString());
        doTestGetContentAsDocument(inputStreamSQLXML, getSecondTestXMLAsString());
        doTestGetContentAsDocument(w3cDocumentSQLXML, getSecondTestXMLAsString());
        assertFalse(stringSQLXML.isWriteable());
        assertFalse(readerSQLXML.isWriteable());
        assertFalse(inputStreamSQLXML.isWriteable());
        assertFalse(w3cDocumentSQLXML.isWriteable());
    }
    
    public void testEquals() throws Exception
    {
        prepareTestSQLXMLObjects();
        assertFalse(stringSQLXML.equals(null));
        assertTrue(stringSQLXML.equals(stringSQLXML));
        assertTrue(stringSQLXML.equals(w3cDocumentSQLXML));
        assertEquals(stringSQLXML.hashCode(), w3cDocumentSQLXML.hashCode());
        assertTrue(readerSQLXML.equals(inputStreamSQLXML));
        assertEquals(readerSQLXML.hashCode(), inputStreamSQLXML.hashCode());
        stringSQLXML.setString(getSecondTestXMLAsString());
        assertFalse(stringSQLXML.equals(inputStreamSQLXML));
        assertFalse(inputStreamSQLXML.equals(stringSQLXML));
        Writer writer = inputStreamSQLXML.setCharacterStream();
        writer.write(getSecondTestXMLAsString());
        assertTrue(stringSQLXML.equals(inputStreamSQLXML));
        assertTrue(inputStreamSQLXML.equals(stringSQLXML));
        assertEquals(stringSQLXML.hashCode(), inputStreamSQLXML.hashCode());
        stringSQLXML.getString();
        assertFalse(stringSQLXML.isReadable());
        assertTrue(inputStreamSQLXML.isReadable());
        assertTrue(stringSQLXML.equals(inputStreamSQLXML));
        assertTrue(inputStreamSQLXML.equals(stringSQLXML));
        assertEquals(stringSQLXML.hashCode(), inputStreamSQLXML.hashCode());
        stringSQLXML.free();
        assertFalse(stringSQLXML.equals(inputStreamSQLXML));
        assertFalse(inputStreamSQLXML.equals(stringSQLXML));
        inputStreamSQLXML.free();
        assertTrue(stringSQLXML.equals(inputStreamSQLXML));
        assertTrue(inputStreamSQLXML.equals(stringSQLXML));
        assertEquals(stringSQLXML.hashCode(), inputStreamSQLXML.hashCode());
    }
    
    public void testClone() throws Exception
    {
        prepareTestSQLXMLObjects();
        inputStreamSQLXML.getString();
        MockSQLXML clone = (MockSQLXML)inputStreamSQLXML.clone();
        assertFalse(clone.isReadable());
        assertTrue(clone.isWriteable());
        assertFalse(clone.wasFreeCalled());
        assertNotSame(inputStreamSQLXML, clone);
        assertTrue(inputStreamSQLXML.equals(clone));
    }
    
    public void testFree() throws Exception
    {
        prepareTestSQLXMLObjects();
        w3cDocumentSQLXML.free();
        try
        {
            w3cDocumentSQLXML.getString();
            fail();
        } 
        catch(SQLException exc)
        {
            //expected exception
        }
        try
        {
            w3cDocumentSQLXML.setString(getSecondTestXMLAsString());
            fail();
        } 
        catch(SQLException exc)
        {
            //expected exception
        }
        assertFalse(w3cDocumentSQLXML.isReadable());
        assertFalse(w3cDocumentSQLXML.isWriteable());
        assertTrue(w3cDocumentSQLXML.wasFreeCalled());
        assertXMLEqualsTestXML(w3cDocumentSQLXML.getContentAsString(), getFirstTestXMLAsString());
        String xml = new String(StreamUtil.getStreamAsByteArray(w3cDocumentSQLXML.getContentAsInputStream()), "UTF-8");
        assertXMLEqualsTestXML(xml, getFirstTestXMLAsString());
        xml = StreamUtil.getReaderAsString(w3cDocumentSQLXML.getContentAsReader());
        assertXMLEqualsTestXML(xml, getFirstTestXMLAsString());
        assertXMLEqualsTestXML(w3cDocumentSQLXML.getContentAsW3CDocument(), getFirstTestXMLAsString());
    }
    
    public void testNoContent() throws Exception
    {
        MockSQLXML emptySQLXML = new MockSQLXML();
        assertNull(emptySQLXML.getContentAsString());
        assertNull(emptySQLXML.getContentAsW3CDocument());
        assertNull(emptySQLXML.getContentAsInputStream());
        assertNull(emptySQLXML.getContentAsReader());
        try
        {
            emptySQLXML.getString();
            fail();
        } 
        catch(SQLException exc)
        {
            //expected exception
        }
        emptySQLXML.setString(getFirstTestXMLAsString());
        assertXMLEqualsTestXML(emptySQLXML.getString(), getFirstTestXMLAsString());
    }
    
    private void doTestGetString(MockSQLXML sqlXML, String testXML) throws Exception
    {
        assertTrue(sqlXML.isReadable());
        assertXMLEqualsTestXML(sqlXML.getContentAsString(), testXML);
        assertTrue(sqlXML.isReadable());
        assertXMLEqualsTestXML(sqlXML.getString(), testXML);
        assertFalse(sqlXML.isReadable());
        assertXMLEqualsTestXML(sqlXML.getContentAsString(), testXML);
        try
        {
            sqlXML.getString();
            fail();
        } 
        catch(SQLException exc)
        {
            //expected exception
        }
    }
    
    private void doTestGetCharacterStream(MockSQLXML sqlXML, String testXML) throws Exception
    {
        assertTrue(sqlXML.isReadable());
        String xml = StreamUtil.getReaderAsString(sqlXML.getContentAsReader());
        assertXMLEqualsTestXML(xml, testXML);
        assertTrue(sqlXML.isReadable());
        xml = StreamUtil.getReaderAsString(sqlXML.getCharacterStream());
        assertXMLEqualsTestXML(xml, testXML);
        assertFalse(sqlXML.isReadable());
        xml = StreamUtil.getReaderAsString(sqlXML.getContentAsReader());
        assertXMLEqualsTestXML(xml, testXML);
        try
        {
            sqlXML.getCharacterStream();
            fail();
        } 
        catch(SQLException exc)
        {
            //expected exception
        }
    }
    
    private void doTestGetBinaryStream(MockSQLXML sqlXML, String testXML) throws Exception
    {
        assertTrue(sqlXML.isReadable());
        String xml = new String(StreamUtil.getStreamAsByteArray(sqlXML.getContentAsInputStream()), "UTF-8");
        assertXMLEqualsTestXML(xml, testXML);
        assertTrue(sqlXML.isReadable());
        xml = new String(StreamUtil.getStreamAsByteArray(sqlXML.getBinaryStream()), "UTF-8");
        assertXMLEqualsTestXML(xml, testXML);
        assertFalse(sqlXML.isReadable());
        xml = new String(StreamUtil.getStreamAsByteArray(sqlXML.getContentAsInputStream()), "UTF-8");
        assertXMLEqualsTestXML(xml, testXML);
        try
        {
            sqlXML.getBinaryStream();
            fail();
        } 
        catch(SQLException exc)
        {
            //expected exception
        }
    }
    
    private void doTestGetContentAsDocument(MockSQLXML sqlXML, String testXML) throws Exception
    {
        assertTrue(sqlXML.isReadable());  
        assertXMLEqualsTestXML(sqlXML.getContentAsW3CDocument(), testXML);
        assertTrue(sqlXML.isReadable());
    }
    
    private void doTestGetStreamSource(MockSQLXML sqlXML, String testXML) throws Exception
    {
        assertTrue(sqlXML.isReadable());
        StreamSource source = sqlXML.getSource(StreamSource.class);
        String xml = new String(StreamUtil.getStreamAsByteArray(source.getInputStream()), "UTF-8");
        assertXMLEqualsTestXML(xml, testXML);
        assertFalse(sqlXML.isReadable());
        try
        {
            sqlXML.getSource(StreamSource.class);
            fail();
        } 
        catch(SQLException exc)
        {
            //expected exception
        }
    }
    
    private void doTestGetDOMSource(MockSQLXML sqlXML, String testXML) throws Exception
    {
        assertTrue(sqlXML.isReadable());
        DOMSource source = sqlXML.getSource(DOMSource.class);
        assertXMLEqualsTestXML((org.w3c.dom.Document)source.getNode(), testXML);
        assertFalse(sqlXML.isReadable());
        try
        {
            sqlXML.getSource(DOMSource.class);
            fail();
        } 
        catch(SQLException exc)
        {
            //expected exception
        }
    }
    
    private void doTestGetSAXSource(MockSQLXML sqlXML, String testXML) throws Exception
    {
        assertTrue(sqlXML.isReadable());
        SAXSource source = sqlXML.getSource(SAXSource.class);
        InputStream stream = source.getInputSource().getByteStream();
        String xml = new String(StreamUtil.getStreamAsByteArray(stream), "UTF-8");
        assertXMLEqualsTestXML(xml, testXML);
        assertFalse(sqlXML.isReadable());
        try
        {
            sqlXML.getSource(SAXSource.class);
            fail();
        } 
        catch(SQLException exc)
        {
            //expected exception
        }
    }
    
    private void doTestGetStAXSource(MockSQLXML sqlXML, boolean testFirst) throws Exception
    {
        assertTrue(sqlXML.isReadable());
        StAXSource source = sqlXML.getSource(StAXSource.class);
        if(testFirst)
        {
            assertXMLEqualsFirstTestXML(source.getXMLStreamReader());
        }
        else
        {
            assertXMLEqualsSecondTestXML(source.getXMLStreamReader());
        }
        assertFalse(sqlXML.isReadable());
        try
        {
            sqlXML.getSource(StAXSource.class);
            fail();
        } 
        catch(SQLException exc)
        {
            //expected exception
        }
    }
}
