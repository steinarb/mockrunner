package com.mockrunner.jdbc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.util.common.FileUtil;

/**
 * Can be used to create a <code>ResultSet</code> based on
 * a an XML <code>Document</code> of the proper format. You can specify 
 * the dialect, for proper parsing of the document. Furthermore you can 
 * specify the dialect of the <code>ResultSet</code>, which determines 
 * the expected format of the XML <code>Document</code> and whether or not  
 * the column entries should be trimmed (default is <code>true</code>).
 * The file can be specified directly or by its name. The class
 * tries to find the file in the absolut or relative path and
 * (if not found) by calling <code>getResource</code>. Note that the
 * file must exist in the local file system and cannot be loaded from
 * inside a jar archive.
 */
public class XMLResultSetFactory implements ResultSetFactory 
{
    public final static int SYBASE_DIALECT = 0;
    public final static int SQUIRREL_DIALECT = 1;
    
    private File file = null;
    private String fileName = null;
    private boolean trim = true;
    private int dialect = SYBASE_DIALECT;
    
    public XMLResultSetFactory(String fileName)
    {
        this.file = new File(fileName);
        this.fileName = fileName;
    }
    
    public XMLResultSetFactory(File file)
    {
        this.file = file;
        this.fileName = file.getAbsolutePath();
    }
    
    /**
     * Makes and returns a MockResultSet created from 
     * an existing and valid XML <code>Document</code>.
     * 
     * @return a new MockResultSet
     */
    public MockResultSet create(String id) 
    {
        MockResultSet resultSet;
        
        switch (dialect) 
        {
        	case SYBASE_DIALECT:
        	    resultSet = createSybaseResultSet(id);
        	    break;
        	case SQUIRREL_DIALECT:
        	     resultSet = createSquirrelResultSet(id);
        	     break;
        	default:
        	    resultSet = createSybaseResultSet(id);
        	    break;
        }
        
        return resultSet;
    }
    
    /**
     * Get the <code>File</code> being used to read in the 
     * <code>ResultSet</code>. Returns <code>null</code> if
     * the file does not exist.
     * @return the file 
     */
    public File getXMLFile()
    {
        if(file.exists() && file.isFile())
        {
            return file;
        }
        else
        {
            try 
            {
                file = FileUtil.findFile(file.getPath());
                return file;
            } 
            catch(FileNotFoundException exc) 
            {
                throw new RuntimeException("Could not find: " + file.getPath());
            }
        }
    }

    /**
     * Set if the column entries should be trimmed.
     * Default is <code>true</code>.
     * 
     * @param trim
     */
    public void setTrim(boolean trim)
    {
        this.trim = trim;
    }
    
    /**
     * Get whether or not trim is true or false.
     */
    public boolean getTrim() 
    {
        return trim;
    }
    
    /**
     * Set the dialect of the XML <code>Document</code>.  Can be 
     * different for different database systems.  
     * Will determine the expected XML format for 
     * the <code>ResultSet</code>.  <code>SYBASE_DIALECT</code> 
     * is the <b>only</b> accepted dialect for now.
     * @param dialect int specifying which createXXXResultSet 
     * method to call.
     */
    public void setDialect(int dialect) 
    {
        this.dialect = dialect;
    }
    
    /**
     * Get the dialect of the XML <code>Document</code.
     * 
     * @return dialect
     */
    public int getDialect() 
    {
        return dialect;
    }
    
    /**
     * Return a MockResultSet with proper column names and 
     * rows based on the XML <code>Document</code>.
     * @return MockResultSet Results read from XML 
     * <code>Document</code>.
     */
    public MockResultSet createSybaseResultSet(String id) 
    {
       MockResultSet resultSet = new MockResultSet(id);
       SAXBuilder builder = new SAXBuilder();
       Document doc = null;
       File fileToParse = getXMLFile();
       if(null == fileToParse)
       {
           throw new RuntimeException("File " + fileName + " not found.");
       }
       try 
       {
           doc = builder.build(fileToParse);
           Element root = doc.getRootElement();
           List rows = root.getChildren("row");
           Iterator ri = rows.iterator();
           boolean firstIteration = true;
           int colNum = 0;
           while (ri.hasNext()) 
           {
               Element cRow = (Element)ri.next();
               List cRowChildren = cRow.getChildren();
               Iterator cri = cRowChildren.iterator();   
               if (firstIteration)
               {
                   List columns = cRowChildren;

                   for (Object column : columns) {
                       Element ccRow = (Element) column;
                       resultSet.addColumn(ccRow.getName());
                       colNum++;
                   }
                   firstIteration = false;
               }
               String[] cRowValues = new String[colNum];
               int curCol = 0;
               while (cri.hasNext())
               {
                   Element crValue = (Element)cri.next();
                   String value = trim ? crValue.getTextTrim() : crValue.getText();
                   cRowValues[curCol] = value;
                   curCol++;
               }
               resultSet.addRow(cRowValues);
           }
       } 
       catch(Exception exc) 
       {
           throw new NestedApplicationException("Failure while reading from XML file", exc);
       }
       return resultSet;
    }
    
    /**
     * Return a MockResultSet with proper column names and 
     * rows based on the XML <code>Document</code>.
     * @return MockResultSet Results read from XML 
     * <code>Document</code>.
     */
    public MockResultSet createSquirrelResultSet(String id) 
    {
       MockResultSet resultSet = new MockResultSet(id);
       SAXBuilder builder = new SAXBuilder();
       Document doc = null;
       File fileToParse = getXMLFile();
       if(null == fileToParse)
       {
           throw new RuntimeException("File " + fileName + " not found.");
       }
       try 
       {
           doc = builder.build(fileToParse);
           int offset;
           int maxColumnNumber; // first column is 0 (unlike JDBC)
           
           Map columnNameMap = new HashMap();
           Element root = doc.getRootElement();
           List columns = root.getChild("columns").getChildren("column");
           Iterator ci = columns.iterator();
           maxColumnNumber = 0;
           while (ci.hasNext()) 
           {
               Element cColumn = (Element)ci.next();
               int columnNumber = cColumn.getAttribute("number").getIntValue();
               if (columnNumber>maxColumnNumber)
               {
               	    maxColumnNumber = columnNumber;
               }
               String columnName = cColumn.getChildText("name");
               columnNameMap.put(columnNumber, columnName);
           }
           offset = 0;
           for (int ii=0; ii<columnNameMap.size(); ii++)
           {
               while (true) {
                   String name = (String)columnNameMap.get(ii + offset);
                   if (name==null) {
                       offset++;
                       resultSet.addColumn("unknown."+offset);
                       continue;
                   }
                   else 
                   {
                       resultSet.addColumn(name);
                       break;
                   }
               }
               
           }
           
           List rows = root.getChild("rows").getChildren("row");
           for (Object row : rows) {
               Element cRow = (Element) row;
               List cRowChildren = cRow.getChildren();
               Iterator cri = cRowChildren.iterator();
               String[] cRowValues = new String[maxColumnNumber + 1];
               while (cri.hasNext()) {
                   Element crValue = (Element) cri.next();
                   String value = trim ? crValue.getTextTrim() : crValue.getText();
                   int curCol = crValue.getAttribute("columnNumber").getIntValue();
                   cRowValues[curCol] = value;
               }
               resultSet.addRow(cRowValues);
           }
       } 
       catch(Exception exc) 
       {
           throw new NestedApplicationException("Failure while reading from XML file", exc);
       }
       return resultSet;
    }
}
