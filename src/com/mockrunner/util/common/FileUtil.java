package com.mockrunner.util.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import com.mockrunner.base.NestedApplicationException;

public class FileUtil
{
    /**
     * Reads all lines from a text file and adds them to a <code>List</code>.
     * @param file the input file
     * @return the <code>List</code> with the file lines
     */
    public static List getLinesFromFile(File file)
    {
        List resultList = null;
        FileReader reader = null;
        try
        {
            reader = new FileReader(file);
            resultList = StreamUtil.getLinesFromReader(reader);
        }
        catch(FileNotFoundException exc)
        {
            throw new NestedApplicationException(exc);

        }
        finally
        {
            if(null != reader)
            {
                try
                {
                    reader.close();
                }
                catch(Exception exc)
                {
                    throw new NestedApplicationException(exc);
                }
            }
        }
        return resultList;
    }
    
    /**
     * Tries to open the file from the file system. If the file
     * doesn't exist, tries to load the file with <code>getResourceAsStream</code>.
     * Throws a {@link com.mockrunner.base.NestedApplicationException}, if
     * the file cannot be found.
     * @param filePath the file path
     * @return the file as reader
     */
    public static Reader findFile(String filePath)
    {
        File file = new File(filePath);
        try
        {
            if(file.exists() && file.isFile()) 
            {
                return new FileReader(file);
            }
            InputStream stream = FileUtil.class.getResourceAsStream(filePath);
            return new InputStreamReader(stream);
        } 
        catch(Exception exc)
        {
            throw new NestedApplicationException("Error while trying to find the file " + filePath, exc);
        } 
    }
}
