package com.mockrunner.jdbc;

import java.io.File;
import java.util.List;

import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.util.FileUtil;
import com.mockrunner.util.StringUtil;

/**
 * Can be used to create a <code>ResultSet</code> based on
 * a table specified in a CSV file. You can specify the delimiter
 * of the columns (default is <i>";"</i>). Furthermore you can specify if the first line
 * contains the column names (default is <code>false</code>) and if
 * the column entries should be trimmed (default is <code>true</code>).
 */
public class FileResultSetFactory implements ResultSetFactory
{
    private File file;
    private String delimiter;
    private boolean firstLineContainsColumnNames;
    private boolean trim;
    
    public FileResultSetFactory(String fileName)
    {
        this(new File(fileName));
    }
    
    public FileResultSetFactory(File file)
    {
        this.file = file;
        delimiter = ";";
        firstLineContainsColumnNames = false;
        trim = true;
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

    public MockResultSet create()
    {
        MockResultSet resultSet = new MockResultSet();
        List lines = FileUtil.getLinesFromFile(file);
        int firstLineNumber = 0;
        if(firstLineContainsColumnNames)
        {
            String firstLine = (String)lines.get(firstLineNumber);
            firstLineNumber++;
            String[] names = StringUtil.split(firstLine, delimiter, trim);
            for(int ii = 0; ii < names.length; ii++)
            {
                String nextName = names[ii];
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
