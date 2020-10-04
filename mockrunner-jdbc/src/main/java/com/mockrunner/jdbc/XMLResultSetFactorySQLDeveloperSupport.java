package com.mockrunner.jdbc;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.mock.jdbc.MockResultSet;

/**
 * Adds support for the XML export format that Oracle SQLDeveloper (at least version 17.2.0.188) creates.
 * See https://www.oracle.com/tools/downloads/sqldev-downloads.html 
 * <p>
 * <b>Example format:</b>
 * 
 * <pre>
 * <RESULTS>
 *   <ROW>
 *     <COLUMN NAME="CONNECTION_NO"><![CDATA[2517308732]]></COLUMN>
 *     <COLUMN NAME="VAR"><![CDATA[chatservice.platformid]]></COLUMN>
 *     <COLUMN NAME="VALUE"><![CDATA[QDE]]></COLUMN>
 *     <COLUMN NAME="VALID_FROM"><![CDATA[08-NOV-18 05.00.34.298260000 PM]]></COLUMN>
 *     <COLUMN NAME="EXPERT_NO"><![CDATA[758750]]></COLUMN>
 *     <COLUMN NAME="MANDATOR_NO"><![CDATA[100]]></COLUMN>
 *   </ROW>
 *   <ROW>
 *     <COLUMN NAME="CONNECTION_NO"><![CDATA[2517308732]]></COLUMN>
 *     <COLUMN NAME="VAR"><![CDATA[member_login_time]]></COLUMN>
 *     <COLUMN NAME="VALUE"><![CDATA[20181108164224]]></COLUMN>
 *     <COLUMN NAME="VALID_FROM"><![CDATA[08-NOV-18 05.00.34.352730000 PM]]></COLUMN>
 *     <COLUMN NAME="EXPERT_NO"><![CDATA[758750]]></COLUMN>
 *     <COLUMN NAME="MANDATOR_NO"><![CDATA[100]]></COLUMN>
 *   </ROW>
 * </pre>
 * <p>
 * 
 * Obtain data files via Oracle SQLDeveloper:
 * 
 * <ol>
 * <li>Execute Query, e.g.: "SELECT * FROM PERSON PARAM WHERE ROWNUM < 4;"</li>
 * <li>Right click "Query Result" Window.</li>
 * <li>Select "Export"</li>
 * <li>Choose "Format -> XML"</li>
 * <li>Choose "Save As -> Clipboard"</li>
 * <li>Create a file somewhere in your (resources) classpath.</li>
 * <li>Paste the clipboard (<STRG + V>) into the file.</li>
 * <li>In case Oracle SQL Developer created a header with " encoding='UTF8'" correct that to "encoding='UTF-8'" (else you get an exception).</li>
 * </ol>
 * <p>
 * 
 * @author Achim.Westermann@gmx.de
 */
public class XMLResultSetFactorySQLDeveloperSupport extends XMLResultSetFactory
{
    /** ID of the "dialect" the XML export feature of Oracle SQLDeveloper creates. */
	public final static int ORACLE_SQLDEVELOPER_DIALECT = 2;
    
	/** A copy from superclass. Field is needed for output, but private and I did not want to touch original. */
	protected String copyFileName; 
	
	public XMLResultSetFactorySQLDeveloperSupport(final File file)
	{
		super(file);
		this.copyFileName = file.getAbsolutePath();
	}

	public XMLResultSetFactorySQLDeveloperSupport(final String fileName)
	{
		super(fileName);
		this.copyFileName = fileName;
	}

	/**
	 * Compared to {@link XMLResultSetFactory#create(String)} this adds support for 
	 * {@link #ORACLE_SQLDEVELOPER_DIALECT} and dispatches to <code>super</code> when 
	 * that {@link #getDialect()} is not matched. 
	 * 
	 * @see #setDialect(int)
	 */
	@Override
	public MockResultSet create(String id)
	{
		MockResultSet mockResultSet;
		switch(this.getDialect())
		{
			case ORACLE_SQLDEVELOPER_DIALECT :{
				mockResultSet = createSQLDeveloperResultSet(id);
				break;
			}
			default :
			{
				mockResultSet = super.create(id);
			}
		}
		return mockResultSet;
	}
	
	   
    /**
	 * Return a MockResultSet with proper column names and rows based on the XML <code>Document</code>.
	 * <p>
	 * Basically this is the same "dialect" as {@link XMLResultSetFactory#SYBASE_DIALECT} with the following changes: 
	 * <ul>
	 * <li>Nodes for rows are not "row" but "ROW" (uppercase). </li>
	 * <li>Column nodes have the column name in the attribute value of attribute "NAME" (v.s. the column node name is the database column name): <FIRSTNAME ...> --> <COLUMN NAME="FIRSTNAME"..> </li>
	 * </ul>
	 * <p>
	 * <b>Example format:</b>
	 * <RESULTS>
	 *   <ROW>
	 *     <COLUMN NAME="CONNECTION_NO"><![CDATA[2517308732]]></COLUMN>
	 *     <COLUMN NAME="VAR"><![CDATA[chatservice.platformid]]></COLUMN>
	 *     <COLUMN NAME="VALUE"><![CDATA[QDE]]></COLUMN>
	 *     <COLUMN NAME="VALID_FROM"><![CDATA[08-NOV-18 05.00.34.298260000 PM]]></COLUMN>
	 *     <COLUMN NAME="EXPERT_NO"><![CDATA[758750]]></COLUMN>
	 *     <COLUMN NAME="MANDATOR_NO"><![CDATA[100]]></COLUMN>
	 *   </ROW>
	 *   <ROW>
	 *     <COLUMN NAME="CONNECTION_NO"><![CDATA[2517308732]]></COLUMN>
	 *     <COLUMN NAME="VAR"><![CDATA[member_login_time]]></COLUMN>
	 *     <COLUMN NAME="VALUE"><![CDATA[20181108164224]]></COLUMN>
	 *     <COLUMN NAME="VALID_FROM"><![CDATA[08-NOV-18 05.00.34.352730000 PM]]></COLUMN>
	 *     <COLUMN NAME="EXPERT_NO"><![CDATA[758750]]></COLUMN>
	 *     <COLUMN NAME="MANDATOR_NO"><![CDATA[100]]></COLUMN>
	 *   </ROW> 
	 * <p>
	 * 
	 * @return MockResultSet Results read from XML <code>Document</code> in the format Oracle SQL Developer creates.
	 */
	public MockResultSet createSQLDeveloperResultSet(String id)
	{
		MockResultSet resultSet = new MockResultSet(id);
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		File fileToParse = getXMLFile();
		if (null == fileToParse)
		{
			throw new RuntimeException("File " + this.copyFileName + " not found.");
		}
		try
		{
			doc = builder.build(fileToParse);
			Element root = doc.getRootElement();
			List<Element> rows = root.getChildren("ROW");
			Iterator<Element> ri = rows.iterator();
			boolean firstIteration = true;
			// <ROW> - iteration START
			while (ri.hasNext())
			{
				// 1 ROW START
				Element cRow = (Element) ri.next();
				List<Element> cRowChildren = cRow.getChildren();
				Iterator<Element> cri = cRowChildren.iterator();
				String[] cRowValues = new String[cRowChildren.size()];
				int curCol = 0;
				
				while (cri.hasNext())
				{
					// 1 CELL START
					Element crValue = cri.next();
					// 1st row: Get column names: 
					if (firstIteration)
					{
						resultSet.addColumn(crValue.getAttributeValue("NAME"));
					}
					
					String value = this.getTrim() ? crValue.getTextTrim() : crValue.getText();
					cRowValues[curCol] = value;
					curCol++;
					// 1 CELL END
				}
				resultSet.addRow(cRowValues);
				firstIteration = false;
				// 1 ROW END
				
			}
			// <ROW> - iteration END
			
		}
		catch (Exception exc)
		{
			throw new NestedApplicationException("Failure while reading from XML file", exc);
		}
		return resultSet;
	}
	
}
