package com.mockrunner.util.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
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
     * Tries to open the file from its absolute or relative path. If the file
     * doesn't exist, tries to load the file with <code>getResource</code>.
     * Returns <code>null</code> if the file cannot be found.
     * @param fileName the file name
     * @return the file as reader
     */
    public static File findFile(String fileName)
    {
        File file = new File(fileName);
        try
        {
            if(file.exists() && file.isFile()) 
            {
                return file;
            }
            URL fileURL = FileUtil.class.getClassLoader().getResource(fileName);
            if (fileURL != null) return new File(fileURL.getFile());
            fileURL = FileUtil.class.getResource(fileName);
            if (fileURL != null) return new File(fileURL.getFile());
            return null;
        } 
        catch(Exception exc)
        {
            throw new NestedApplicationException("Error while trying to find the file " + fileName, exc);
        } 
    }
}
