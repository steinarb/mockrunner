package com.mockrunner.jdbc;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.util.common.FileUtil;
import com.mockrunner.util.common.StreamUtil;
import com.mockrunner.util.common.StringUtil;

/**
 * Can be used to create a <code>ResultSet</code> based on
 * a table specified in a CSV file. You can specify the delimiter
 * of the columns (default is <i>";"</i>). Furthermore you can specify if the first line
 * contains the column names (default is <code>false</code>) and if
 * the column entries should be trimmed (default is <code>true</code>).
 * If a <code>File</code> is specified, this file is used. If a file name
 * is specified, this class tries to find the file in the local
 * file system and (if not found) to load it with <code>getResourceAsStream</code>.
 */
public class FileResultSetFactory implements ResultSetFactory
{
    private final static Log log = LogFactory.getLog(XMLResultSetFactory.class);
    
    private String fileName = null;
    private File file = null;
    private String delimiter = ";";
    private boolean firstLineContainsColumnNames = false;
    private boolean trim = true;
    
    public FileResultSetFactory(String fileName)
    {
        this.fileName = fileName;
    }
    
    public FileResultSetFactory(File file)
    {
        if (file.exists() && file.isFile()) 
        {
            this.file = file;
        } 
        else 
        {
            this.file = null;
        }
    }
    
    /**
     * Set the delimiter. Default is <i>";"</i>.
     * @param delimiter the delimiter
     */
    public void setDelimiter(String delimiter)
    {
        this.delimiter = delimiter;
    }

    /**
     * Set if the first line contains the column names.
     * Default is <code>false</code>.
     */
    public void setFirstLineContainsColumnNames(boolean firstLineContainsColumnNames)
    {
        this.firstLineContainsColumnNames = firstLineContainsColumnNames;
    }

    /**
     * Set if the coumn entries should be trimmed.
     * Default is <code>true</code>.
     */
    public void setTrim(boolean trim)
    {
        this.trim = trim;
    }

    public MockResultSet create(String id)
    {
        MockResultSet resultSet = new MockResultSet(id);
        List lines = null;
        if(null != file)
        {
            lines = FileUtil.getLinesFromFile(file);
        }
        else
        {
            Reader reader = FileUtil.findFile(fileName);
            lines = StreamUtil.getLinesFromReader(reader);
            tryClose(reader);
        }
        int firstLineNumber = 0;
        if(firstLineContainsColumnNames)
        {
            String firstLine = (String)lines.get(firstLineNumber);
            firstLineNumber++;
            String[] names = StringUtil.split(firstLine, delimiter, trim);
            for(int ii = 0; ii < names.length; ii++)
            {
                resultSet.addColumn(names[ii]);
            }
        }
        for(int ii = firstLineNumber; ii < lines.size(); ii++)
        {
            String line = (String)lines.get(ii);
            String[] values = StringUtil.split(line, delimiter, trim);
            resultSet.addRow(values);
        }
        return resultSet;
    }
    
    private void tryClose(Reader reader)
    {
        try
        {
            reader.close();
        } 
        catch(IOException exc)
        {
            log.error(exc.getMessage(), exc);
        }
    }
}
