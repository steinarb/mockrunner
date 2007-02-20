package com.mockrunner.jdbc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.util.common.FileUtil;
import com.mockrunner.util.common.StringUtil;

/**
 * Can be used to create a <code>ResultSet</code> based on
 * a table specified in a CSV file. You can specify the delimiter
 * of the columns (default is <code>;</code>). Furthermore you can specify if the first line
 * contains the column names (default is <code>false</code>) and if
 * the column entries should be trimmed (default is <code>true</code>).
 * With {@link #setUseTemplates} you can enable template replacement in the
 * files (default is <code>false</code>, i.e. templates are disabled).
 * The file can be specified directly or by its name. The class
 * tries to find the file in the absolut or relative path and
 * (if not found) by calling <code>getResource</code>. Note that the
 * file must exist in the local file system and cannot be loaded from
 * inside a jar archive.
 */
public class FileResultSetFactory implements ResultSetFactory
{
    private File file = null;
    private String delimiter = ";";
    private boolean firstLineContainsColumnNames = false;
    private boolean trim = true;
    private boolean useTemplates = false;
    private String templateMarker = null;
    private Map templates = null;

    public FileResultSetFactory(String fileName)
    {
        this(new File(fileName));
    }
    
    public FileResultSetFactory(File file)
    {
        this.file = file;
        setDefaultTemplateConfiguration();
    }
    
    /**
     * Get the <code>File</code> being used to read in the 
     * <code>ResultSet</code>. Throws a <code>RuntimeException</code>
     * if the file does not exist.
     * @return the file 
     */
    public File getFile()
    {
        if (file.exists() && file.isFile())
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
            catch (FileNotFoundException exc)
            {
                throw new RuntimeException("Could not find: " + file.getPath());
            }
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

    /**
     * Set this to <code>true</code> to allow the use of templates
     * in data files. A template is identified by a marker followed
     * by a label. The template is replaced by a predefined string in
     * the corresponding data file. E.g. with the default configuration,
     * <code>$defaultString</code> is replaced by an empty string
     * in the file.
     * The default configuration which is automatically set uses
     * <code>$</code> as a marker. See {@link #setDefaultTemplateConfiguration}
     * for details. You can also set a custom template configuration using
     * {@link #setTemplateConfiguration(String, Map)}.
     * Default is <code>false</code>, i.e. templates are disabled.
     * @param useTemplates set <code>true</code> to enable templates.
     */
    public void setUseTemplates(boolean useTemplates) 
    {
        this.useTemplates = useTemplates;    
    }
    
    /**
     * This method sets a custom template configuration. See 
     * {@link #setUseTemplates} for an explanation how templates work.
     * <code>marker + map key</code> is replaced by the corresponding <code>map
     * value</code> in the data files.
     * Please use {@link #setDefaultTemplateConfiguration} to set a
     * default configuration.
     * @param marker the custom marker replacing the default <code>$</code>
     * @param templates the custom template map
     */
    public void setTemplateConfiguration(String marker, Map templates)
    {
        this.templates = templates;
        this.templateMarker = marker;
    }
    
    /**
     * This method sets the default template configuration. See 
     * {@link #setUseTemplates} for an explanation how templates work.
     * The default marker is <code>$</code> and the default templates are:<br><br>
     * <code>$defaultString</code> is replaced by an empty string<br>
     * <code>$defaultDate</code> is replaced by <code>1970-01-01</code><br>
     * <code>$defaultInteger</code> is replaced by <code>0</code><br><br>
     * Please use {@link #setTemplateConfiguration(String, Map)} to set a
     * custom marker and custom templates.
     */
    public void setDefaultTemplateConfiguration()
    {
        Map templates = new HashMap();
        templates.put("defaultString", "");
        templates.put("defaultDate", "1970-01-01");
        templates.put("defaultInteger", "0");
        setTemplateConfiguration("$", templates);
    }

    public MockResultSet create(String id)
    {
        MockResultSet resultSet = new MockResultSet(id);
        File fileToRead = getFile();
        List lines = FileUtil.getLinesFromFile(fileToRead);

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
            if(useTemplates)
            {
            	for(int yy = 0; yy < values.length; yy++)
            	{
            		if(null != values[yy])
            		{
                		if(values[yy].startsWith(templateMarker) && templates.containsKey(values[yy].substring(1)))
                		{
                			values[yy] = (String)templates.get(values[yy].substring(1));
                		}
            		}
            	}
            }
            resultSet.addRow(values);
        }
        return resultSet;
    }
}
