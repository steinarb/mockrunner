package com.mockrunner.test.consistency;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mockrunner.util.StreamUtil;

public class SynchronizeVersionUtil
{
    private final static String src14Dir = "src";
    private final static String src13Dir = "src1.3test";
    
    public static void main(String[] args) throws Exception
    {
        SynchronizeVersionUtil synchVersionUtil = new SynchronizeVersionUtil();
        synchVersionUtil.doSynchronize();
    }
    
    public void doSynchronize() throws Exception
    {
        doSynchronizeJDBCJDK13();
    }

    private void doSynchronizeJDBCJDK13() throws Exception
    {
        Map jdbcFiles = new HashMap();
        ConsistencyUtil util = new ConsistencyUtil();
        Map jdbcMap = new HashMap();
        File jdbc = new File(src14Dir + "/com/mockrunner/jdbc");
        File jdbcMock = new File(src14Dir + "/com/mockrunner/mock/jdbc");
        util.addJavaSrcFiles(src14Dir, jdbc, jdbcMap);
        util.addJavaSrcFiles(src14Dir, jdbcMock, jdbcMap);
        Iterator sourceIterator = jdbcMap.keySet().iterator();
        while(sourceIterator.hasNext())
        {
            String currentFileName = (String)sourceIterator.next();
            File currentSourceFile = (File)jdbcMap.get(currentFileName);
            File currentDestFile = new File(src13Dir + currentFileName);
            String sourceFileContent = StreamUtil.getReaderAsString(new FileReader(currentSourceFile));
            System.out.println("Processing file " + currentSourceFile);
            String processedFileContent = processFile(sourceFileContent);
            writeFileContent(processedFileContent, currentDestFile);
        }
        System.out.println("Sucessfully finished");
    }
    
    private String processFile(String fileContent)
    {
        return fileContent;
    }

    private void writeFileContent(String fileContent, File currentDestFile) throws FileNotFoundException, IOException
    {
        if(!currentDestFile.getParentFile().exists())
        {
            currentDestFile.getParentFile().mkdirs();
        }
        System.out.println("Writing file " + currentDestFile);
        FileWriter currentDestFileWriter = new FileWriter(currentDestFile); 
        currentDestFileWriter.write(fileContent);
        currentDestFileWriter.flush();
        currentDestFileWriter.close();
    }
}
