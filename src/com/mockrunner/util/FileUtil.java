package com.mockrunner.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil
{
    /**
     * Reads all lines from a text file and adds them to a <code>List</code>.
     * @param file the input file
     * @return the <code>List</code> with the file lines
     */
    public static List getLinesFromFile(File file)
    {
        List resultList = new ArrayList();
        FileReader reader = null;
        try
        {
            reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while(line != null)
            {
                resultList.add(line);
                line = bufferedReader.readLine();
            }
        }
        catch(FileNotFoundException exc)
        {
            throw new RuntimeException("File not found. " + exc.getMessage());

        }
        catch(IOException exc)
        {
            throw new RuntimeException(exc.getMessage());
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
                    throw new RuntimeException(exc.getMessage());
                }
            }
        }
        return resultList;
    }
}
