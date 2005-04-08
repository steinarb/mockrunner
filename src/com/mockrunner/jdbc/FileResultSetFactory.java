package com.mockrunner.jdbc;

import java.io.File;
import java.util.List;

import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.util.common.FileUtil;
import com.mockrunner.util.common.StringUtil;

/**
 * Can be used to create a <code>ResultSet</code> based on
 * a table specified in a CSV file. You can specify the delimiter
 * of the columns (default is <i>";"</i>). Furthermore you can specify if the first line
 * contains the column names (default is <code>false</code>) and if
 * the column entries should be trimmed (default is <code>true</code>).
 * The file can be specified directly or by its name. The class
 * tries to find the file in the absolut or relative path and
 * (if not found) by calling <code>getResource</code>. Note that the
 * file must exist in the local file system and cannot be loaded from
 * inside a jar archive.
 */
public class FileResultSetFactory implements ResultSetFactory
{
    private File file = null;
    private String fileName = null;
    private String delimiter = ";";
    private boolean firstLineContainsColumnNames = false;
    private boolean trim = true;
    
    public FileResultSetFactory(String fileName)
    {
        this.file = new File(fileName);
    	this.fileName = fileName;
    }
    
    public FileResultSetFactory(File file)
    {
    	this.file = file;
    	this.fileName = file.getAbsolutePath();
    }
    
    /**
     * Get the <code>File</code> being used to read in the 
     * <code>ResultSet</code>. Returns <code>null</code> if
     * the file does not exist.
     * @return the file 
     */
    public File getFile()
    {
        if(file.exists() && file.isFile())
        {
            return file;
        }
        else
        {
            return FileUtil.findFile(fileName);
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
     * Set if the column entries should be trimmed.
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
        File fileToRead = getFile();
        if(null == fileToRead)
        {
            throw new RuntimeException("File " + fileName + " not found.");
        }
        lines = FileUtil.getLinesFromFile(fileToRead);
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
}
