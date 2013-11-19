package com.mockrunner.util.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.net.URLDecoder;
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
     * Throws a <code>FileNotFoundException</code> if the file cannot be found.
     * @param fileName the file name
     * @return the file as reader
     * @throws FileNotFoundException if the cannot be found
     */
    public static File findFile(String fileName) throws FileNotFoundException
    {
        File file = new File(fileName);
        if(isExistingFile(file)) return file;
        fileName = fileName.replace('\\', '/');
        file = new File(fileName);
        if(isExistingFile(file)) return file;
        URL fileURL = FileUtil.class.getClassLoader().getResource(fileName);
        file = decodeFileURL(fileURL);
        if(null != file) return file;
        fileURL = FileUtil.class.getResource(fileName);
        file = decodeFileURL(fileURL);
        if(null != file) return file;
        throw new FileNotFoundException("Could not find file: " + fileName);
    }
    
    private static File decodeFileURL(URL fileURL)
    {
        if(fileURL != null)
        {
            File file = new File(fileURL.getFile());
            if(isExistingFile(file)) return file;
            file = new File(URLDecoder.decode(fileURL.getFile()));
            if(isExistingFile(file)) return file;
        }
        return null;
    }
    
    private static boolean isExistingFile(File file)
    {
        return (file.exists() && file.isFile());
    }
}
