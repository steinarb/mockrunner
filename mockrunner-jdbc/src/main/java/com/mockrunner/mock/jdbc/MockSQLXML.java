package com.mockrunner.mock.jdbc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.SQLXML;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jdom.Document;
import org.jdom.input.DOMBuilder;
import org.jdom.input.SAXBuilder;
import org.jdom.input.SAXHandler;
import org.jdom.output.DOMOutputter;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.util.common.StreamUtil;

/**
 * Mock implementation of <code>MockSQLXML</code>.
 * Uses JDOM for XML handling.
 */
public class MockSQLXML implements SQLXML, Cloneable
{
    private DocumentBuilder domParser;
    private SAXParser saxParser;
    private XMLOutputFactory outputFactory;
    private XMLInputFactory inputFactory;
    private SAXBuilder jdomParser;
    private DOMBuilder jdomDOMBuilder;
    private XMLOutputter xmlPrintOutputter;
    private DOMOutputter domOutputter;
    private XMLOutputter xmlCompareOutputter;
    private Object content;
    private boolean wasFreeCalled;
    private boolean wasWriteMethodCalled;
    private boolean wasReadMethodCalled;

    public MockSQLXML()
    {
        createXMLObjects();
        content = null;
        wasFreeCalled = false;
        wasWriteMethodCalled = false;
        wasReadMethodCalled = false;
    }
    
    public MockSQLXML(String stringContent)
    {
        createXMLObjects();
        content = stringContent;
        wasFreeCalled = false;
        wasWriteMethodCalled = false;
        wasReadMethodCalled = false;
    }
    
    public MockSQLXML(Reader readerContent)
    {
        createXMLObjects();
        content = StreamUtil.getReaderAsString(readerContent);
        wasFreeCalled = false;
        wasWriteMethodCalled = false;
        wasReadMethodCalled = false;
    }
    
    public MockSQLXML(InputStream inputStreamContent)
    {
        createXMLObjects();
        content = StreamUtil.getStreamAsByteArray(inputStreamContent);
        wasFreeCalled = false;
        wasWriteMethodCalled = false;
        wasReadMethodCalled = false;
    }
    
    public MockSQLXML(org.w3c.dom.Document documentContent)
    {
        createXMLObjects();
        content = documentContent;
        wasFreeCalled = false;
        wasWriteMethodCalled = false;
        wasReadMethodCalled = false;
    }
    
    protected DocumentBuilder createDocumentBuilder()
    {
        try
        {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } 
        catch(ParserConfigurationException exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    protected SAXParser createSAXParser()
    {
        try
        {
            return SAXParserFactory.newInstance().newSAXParser();
        } 
        catch(Exception exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    protected XMLOutputFactory createXMLOutputFactory()
    {
        return XMLOutputFactory.newInstance();
    }
    
    protected XMLInputFactory createXMLInputFactory()
    {
        return XMLInputFactory.newInstance();
    }
    
    protected SAXBuilder createJDOMSAXBuilder()
    {
        SAXBuilder builder = new SAXBuilder();
        builder.setValidation(false);
        return builder;
    }
    
    protected DOMBuilder createJDOMDOMBuilder()
    {
        return new DOMBuilder();
    }
    
    protected XMLOutputter createJDOMXMLPrintOutputter()
    {
        return new XMLOutputter(Format.getPrettyFormat());
    }
    
    protected XMLOutputter createJDOMXMLCompareOutputter()
    {
        Format format = Format.getCompactFormat();
        format.setOmitDeclaration(true);
        format.setOmitEncoding(true);
        return new XMLOutputter(format);
    }
    
    protected DOMOutputter createJDOMDOMOutputter()
    {
        return new DOMOutputter();
    }
    
    /**
     * Returns the XML content as a string without affecting the state of
     * the object. This method can be called multiple times unlike
     * the <code>get</code> methods of <code>java.sql.SQLXML</code>.
     * @return the XML content as a string
     */
    public String getContentAsString()
    {
        try
        {
            return contentToString();
        } 
        catch(Exception exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Returns the XML content as an <code>InputStream</code> without affecting the 
     * state of the object. This method can be called multiple times unlike
     * the <code>get</code> methods of <code>java.sql.SQLXML</code>.
     * @return the XML content as an <code>InputStream</code>
     */
    public InputStream getContentAsInputStream() throws SQLException
    {
        try
        {
            return contentToInputStream();
        } 
        catch(Exception exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Returns the XML content as a <code>Reader</code> without affecting the 
     * state of the object. This method can be called multiple times unlike
     * the <code>get</code> methods of <code>java.sql.SQLXML</code>.
     * @return the XML content as a <code>Reader</code>
     */
    public Reader getContentAsReader() throws SQLException
    {
        try
        {
            return contentToReader();
        } 
        catch(Exception exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Returns the XML content as a W3C <code>Document</code> without affecting 
     * the state of the object. This method can be called multiple times unlike
     * the <code>get</code> methods of <code>java.sql.SQLXML</code>.
     * @return the XML content as a W3C <code>Document</code>
     */
    public org.w3c.dom.Document getContentAsW3CDocument()
    {
        try
        {
            return contentToW3CDocument();
        } 
        catch(Exception exc)
        {
            throw new NestedApplicationException(exc);
        }
    }

    public InputStream getBinaryStream() throws SQLException
    {
        verifyRead();
        wasReadMethodCalled = true;
        try
        {
            return contentToInputStream();
        } 
        catch(Exception exc)
        {
            throw new SQLException(exc);
        }
    }

    public Reader getCharacterStream() throws SQLException
    {
        verifyRead();
        wasReadMethodCalled = true;
        try
        {
            return contentToReader();
        } 
        catch(Exception exc)
        {
            throw new SQLException(exc);
        }
    }

    public <T extends Source> T getSource(Class<T> sourceClass) throws SQLException
    {
        verifyRead();
        wasReadMethodCalled = true;
        try
        {
            if(null == sourceClass || StreamSource.class.equals(sourceClass))
            {
                return (T) new StreamSource(contentToInputStream());
            }
            if(DOMSource.class.equals(sourceClass))
            {
                return (T) new DOMSource(contentToW3CDocument());
            }
            if(SAXSource.class.equals(sourceClass))
            {
                return (T) new SAXSource(saxParser.getXMLReader(), new InputSource(contentToInputStream()));
            }
            if(StAXSource.class.equals(sourceClass))
            {
                return (T) new StAXSource(contentToXMLStreamReader());
            }
        } 
        catch(Exception exc)
        {
            throw new SQLException(exc);
        }
        throw new SQLException(sourceClass.getName() + " not supported as Source");
    }

    public String getString() throws SQLException
    {
        verifyRead();
        wasReadMethodCalled = true;
        try
        {
            return contentToString();
        } 
        catch(Exception exc)
        {
            throw new SQLException(exc);
        }
    }

    public OutputStream setBinaryStream() throws SQLException
    {
        verifyWrite();
        wasWriteMethodCalled = true;
        content = new ByteArrayOutputStream();
        return (OutputStream)content;
    }

    public Writer setCharacterStream() throws SQLException
    {
        verifyWrite();
        wasWriteMethodCalled = true;
        content = new StringWriter();
        return (Writer)content;
    }

    public <T extends Result> T setResult(Class<T> resultClass) throws SQLException
    {
        verifyWrite();
        wasWriteMethodCalled = true;
        if(null == resultClass || StreamResult.class.equals(resultClass))
        {
            content = new ByteArrayOutputStream();
            return (T)new StreamResult((OutputStream)content);
        }
        if(DOMResult.class.equals(resultClass))
        {
            org.w3c.dom.Document document = domParser.newDocument();
            content = new DOMResult(document);
            return (T)content;
        }
        if(SAXResult.class.equals(resultClass))
        {
            content = new SAXHandler();
            return (T)new SAXResult((SAXHandler)content);
        }
        if(StAXResult.class.equals(resultClass))
        {
            XMLStreamWriter xmlWriter;
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            try
            {
                xmlWriter = outputFactory.createXMLStreamWriter(outStream);
            } 
            catch(XMLStreamException exc)
            {
                throw new SQLException(exc);
            }
            content = new StreamWriterOutputStreamMapping(xmlWriter, outStream);
            return (T)new StAXResult(xmlWriter);
        }
        throw new SQLException(resultClass.getName() + " not supported as Result");
    }

    public void setString(String value) throws SQLException
    {
        verifyWrite();
        wasWriteMethodCalled = true;
        content = value;
    }
    
    public void free() throws SQLException
    {
        wasFreeCalled = true;
    }

    /**
     * Returns if {@link #free} has been called.
     * @return <code>true</code> if {@link #free} has been called,
     *         <code>false</code> otherwise
     */
    public boolean wasFreeCalled()
    {
        return wasFreeCalled;
    }
    
    /**
     * Returns if this object is readable.
     * @return <code>true</code> this object is readable,
     *         <code>false</code> otherwise
     */
    public boolean isReadable()
    {
        return !(wasFreeCalled || wasReadMethodCalled);
    }
    
    /**
     * Returns if this object is writeable.
     * @return <code>true</code> this object is writeable,
     *         <code>false</code> otherwise
     */
    public boolean isWriteable()
    {
        return !(wasFreeCalled || wasWriteMethodCalled);
    }
    
    @Override
    public boolean equals(Object otherObject)
    {
        if(null == otherObject) return false;
        if(this == otherObject) return true;
        if(!otherObject.getClass().equals(this.getClass())) return false;
        MockSQLXML otherSQLXML = (MockSQLXML)otherObject;
        if(wasFreeCalled != otherSQLXML.wasFreeCalled()) return false;
        if(null == content && null == otherSQLXML.content) return true;
        if(null == content || null == otherSQLXML.content) return false;
        try
        {
            Document thisContent = contentToJDOMDocument();
            Document otherContent = otherSQLXML.contentToJDOMDocument();
            if(null == thisContent || null == otherContent) return false;
            String thisContentAsString = xmlCompareOutputter.outputString(thisContent);
            String otherContentAsString = xmlCompareOutputter.outputString(otherContent);
            return thisContentAsString.equals(otherContentAsString);
        } 
        catch(Exception exc)
        {
            return false;
        }   
    }

    @Override
    public int hashCode()
    {
        int hashCode = 17;
        if(null != content)
        {
            try
            {
                Document document = contentToJDOMDocument();
                if(null == document) return hashCode;
                String documentAsString = xmlCompareOutputter.outputString(document);
                if(null != documentAsString) hashCode = (31 * hashCode) + documentAsString.hashCode();
            } 
            catch(Exception ignored)
            {
                
            }
        }
        hashCode = (31 * hashCode) + (wasFreeCalled ? 31 : 62);
        return hashCode;
    }
    
    @Override
    public Object clone()
    {
        try
        {
            MockSQLXML other = (MockSQLXML)super.clone();
            other.domParser = createDocumentBuilder();
            other.saxParser = createSAXParser();
            other.outputFactory = createXMLOutputFactory();
            other.inputFactory = createXMLInputFactory();
            other.jdomParser = createJDOMSAXBuilder();
            other.jdomDOMBuilder = createJDOMDOMBuilder();
            other.xmlPrintOutputter = createJDOMXMLPrintOutputter();
            other.domOutputter = createJDOMDOMOutputter();
            other.xmlCompareOutputter = createJDOMXMLCompareOutputter();
            if(null != content)
            {
                try
                {
                    Document document = contentToJDOMDocument();
                    other.content = document.clone();
                } 
                catch(Exception exc)
                {
                    other.content = null;
                }
            }
            return other;
        }
        catch(CloneNotSupportedException exc)
        {
            throw new NestedApplicationException(exc);
        }
    }

    @Override
    public String toString()
    {
        StringBuilder buffer = new StringBuilder("XML data:\n");
        if(null == content)
        {
            buffer.append("null");
        }
        else
        {
            try
            {
                Document document = contentToJDOMDocument();
                if(null != document) buffer.append(document.toString());
            } 
            catch(Exception exc)
            {
                buffer.append(exc.getMessage());
            }
        }
        return buffer.toString();
    }
    
    private void createXMLObjects()
    {
        domParser = createDocumentBuilder();
        saxParser = createSAXParser();
        outputFactory = createXMLOutputFactory();
        inputFactory = createXMLInputFactory();
        jdomParser = createJDOMSAXBuilder();
        jdomDOMBuilder = createJDOMDOMBuilder();
        xmlPrintOutputter = createJDOMXMLPrintOutputter();
        domOutputter = createJDOMDOMOutputter();
        xmlCompareOutputter = createJDOMXMLCompareOutputter();
    }

    private void verifyWrite() throws SQLException
    {
        if(!isWriteable())
        {
            throw new SQLException("not writeable");
        }
    }
    
    private void verifyRead() throws SQLException
    {
        if(!isReadable())
        {
            throw new SQLException("not readable");
        }
        if(null == content)
        {
            throw new SQLException("no content");
        }
    }
    
    private Document contentToJDOMDocument() throws Exception
    {
        Document jdomDocument = null;
        if(content instanceof Document)
        {
            jdomDocument = (Document)content;
        }
        else if(content instanceof String)
        {
            jdomDocument = jdomParser.build(new StringReader((String)content));
        }
        else if(content instanceof StringWriter)
        {
            jdomDocument = jdomParser.build(new StringReader(content.toString()));
        }
        else if(content instanceof ByteArrayOutputStream)
        {
            jdomDocument = jdomParser.build(new ByteArrayInputStream(((ByteArrayOutputStream)content).toByteArray()));
        }
        else if(content instanceof byte[])
        {
            jdomDocument = jdomParser.build(new ByteArrayInputStream((byte[])content));
        }
        else if(content instanceof org.w3c.dom.Document)
        {
            jdomDocument = jdomDOMBuilder.build((org.w3c.dom.Document)content);
        }
        else if(content instanceof DOMResult)
        {
            Node node = ((DOMResult)content).getNode();
            org.w3c.dom.Document document = null;
            if(node instanceof org.w3c.dom.Document)
            {
                document = (org.w3c.dom.Document)node; 
            }
            else
            {
                document = domParser.newDocument(); 
                document.appendChild(document.importNode(node, true));
            }
            jdomDocument = jdomDOMBuilder.build(document);
        }
        else if(content instanceof SAXHandler)
        {
            jdomDocument = ((SAXHandler)content).getDocument();
        }
        else if(content instanceof StreamWriterOutputStreamMapping)
        {
            XMLStreamWriter xmlWriter = ((StreamWriterOutputStreamMapping)content).getStreamWriter();
            xmlWriter.flush();
            xmlWriter.close();
            ByteArrayOutputStream outStream = ((StreamWriterOutputStreamMapping)content).getOutputStream();
            jdomDocument = jdomParser.build(new ByteArrayInputStream(outStream.toByteArray()));
        }
        return jdomDocument;
    }
    
    private String contentToString() throws Exception
    {
        Document jdomDocument = contentToJDOMDocument();
        if(null != jdomDocument)
        {
            return xmlPrintOutputter.outputString(jdomDocument);
        }
        return null;
    }
    
    private Reader contentToReader() throws Exception
    {
        Document jdomDocument = contentToJDOMDocument();
        if(null != jdomDocument)
        {
            return new StringReader(xmlPrintOutputter.outputString(jdomDocument));
        }
        return null;
    }
    
    private InputStream contentToInputStream() throws Exception
    {
        Document jdomDocument = contentToJDOMDocument();
        if(null != jdomDocument)
        {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            xmlPrintOutputter.output(jdomDocument, outStream);
            outStream.flush();
            return new ByteArrayInputStream(outStream.toByteArray());
        }
        return null;
    }
    
    private org.w3c.dom.Document contentToW3CDocument() throws Exception
    {
        Document jdomDocument = contentToJDOMDocument();
        if(null != jdomDocument)
        {
            return domOutputter.output(jdomDocument);
        }
        return null;
    }
    
    private XMLStreamReader contentToXMLStreamReader() throws Exception
    {
        Document jdomDocument = contentToJDOMDocument();
        if(null != jdomDocument)
        {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            xmlPrintOutputter.output(jdomDocument, outStream);
            outStream.flush();
            InputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
            return inputFactory.createXMLStreamReader(inStream);
        }
        return null;
    }
    
    private class StreamWriterOutputStreamMapping
    {
        private XMLStreamWriter streamWriter;
        private ByteArrayOutputStream outputStream;
        
        public StreamWriterOutputStreamMapping(XMLStreamWriter streamWriter, ByteArrayOutputStream outputStream)
        {
            this.streamWriter = streamWriter;
            this.outputStream = outputStream;
        }
        
        public XMLStreamWriter getStreamWriter()
        {
            return streamWriter;
        }

        public ByteArrayOutputStream getOutputStream()
        {
            return outputStream;
        } 
    }
}
