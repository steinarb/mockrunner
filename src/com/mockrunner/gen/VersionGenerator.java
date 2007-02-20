package com.mockrunner.gen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mockrunner.gen.proc.GeneratorUtil;
import com.mockrunner.gen.proc.JavaLineProcessor;
import com.mockrunner.util.common.StreamUtil;

public class VersionGenerator
{
    private Map processorMap;
    private String generatorName;
    private String rootTargetDir;
    private String rootSourceDir;
    private String[] processedPackages;
    
    public VersionGenerator(Map processorMap, String generatorName, String rootTargetDir, String rootSourceDir, String[] processedPackages)
    {
        this.processorMap = processorMap;
        this.generatorName = generatorName;
        this.rootTargetDir = rootTargetDir;
        this.rootSourceDir = rootSourceDir;
        this.processedPackages = processedPackages;
    }

    public void doSynchronize() throws Exception
    {
        System.out.println("Start processing for " + generatorName);
        GeneratorUtil util = new GeneratorUtil();
        deleteContent(new File(rootTargetDir));
        Map srcMap = new HashMap();
        for(int ii = 0; ii < processedPackages.length; ii++)
        {
            File currentFile = new File(rootSourceDir + "/" + processedPackages[ii]);
            util.addJavaSrcFiles(rootSourceDir, currentFile, srcMap);
        }
        processFiles(processorMap, srcMap, rootTargetDir);
        System.out.println("Sucessfully finished processing for " + generatorName);
    }
    
    private void deleteContent(File file)
    {
        if(!file.isDirectory())
        {
            file.delete();
        }
        else
        {
            File[] files = file.listFiles();
            for(int ii = 0; ii < files.length; ii++)
            {
                deleteContent(files[ii]);
            }
            file.delete();
        }
    }
    
    private void processFiles(Map procMap, Map map, String targetDir) throws FileNotFoundException, IOException
    {
        Iterator sourceIterator = map.keySet().iterator();
        while(sourceIterator.hasNext())
        {
            String currentFileName = (String)sourceIterator.next();
            File currentSourceFile = (File)map.get(currentFileName);
            File currentDestFile = new File(targetDir + currentFileName);
            String sourceFileContent = StreamUtil.getReaderAsString(new FileReader(currentSourceFile));
            System.out.println("Processing file " + currentSourceFile);
            String processedFileContent = processFile(currentFileName, sourceFileContent, procMap);
            if(null != processedFileContent)
            {
                writeFileContent(processedFileContent, currentDestFile);
            }
        }
    }

    private String processFile(String currentFileName, String fileContent, Map jdbcProcMap)
    {
        currentFileName = currentFileName.replace('\\', '.');
        currentFileName = currentFileName.replace('/', '.');
        currentFileName = currentFileName.substring(1);
        currentFileName = currentFileName.substring(0 , currentFileName.length() - 5);
        Object currentObject = (Object)jdbcProcMap.get(currentFileName);
        if(null == currentObject)
        {
            return fileContent;
        }
        else if(currentObject instanceof JavaLineProcessor)
        {
            return ((JavaLineProcessor)currentObject).process(fileContent);
        }
        else if(currentObject instanceof Boolean)
        {
            if(!((Boolean)currentObject).booleanValue())
            {
                return null;
            }
            else
            {
                return fileContent;
            }
        }
        return null;
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
