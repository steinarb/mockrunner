package com.mockrunner.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

public class JDKVersionConsistencyTest extends TestCase
{
    public void testJDK13Consistency() throws Exception
    {
        File jdk13dir = new File("src1.3");
        File jdkdir = new File("src");
        Map file13Map = new HashMap();
        Map fileMap = new HashMap();
        addJavaSrcFiles(jdk13dir, file13Map);
        addJavaSrcFiles(jdkdir, fileMap);
        boolean ok = true;
        Iterator file13Iterator = file13Map.keySet().iterator();
        while(file13Iterator.hasNext())
        {
            String currentFileName = (String)file13Iterator.next();
            System.out.println("---> " + currentFileName);
            File current13File = (File)file13Map.get(currentFileName);
            File currentFile = (File)fileMap.get(currentFileName);
            System.out.println("--->1 " + current13File.getName());
            System.out.println("--->2 " + currentFile.getName());
            if(null == currentFile)
            {
                System.out.println("File " + current13File.getPath() + " not found under src");
                ok = false;
            }
            else
            {
                if(!compareFiles(current13File, currentFile))
                {
                    System.out.println("Mismatch in file " + current13File.getPath());
                    System.out.println();
                    ok = false;
                }
            }
        }
        if(!ok) fail("There are errors");
    }
    
    private boolean compareFiles(File file1, File file2) throws Exception
    {
        BufferedReader reader1 = new BufferedReader(new FileReader(file1));
        BufferedReader reader2 = new BufferedReader(new FileReader(file2));
        boolean match = true;
        String line1 = null;
        String line2 = null;
        int lineNumber = 1;
        while(null != (line1 = reader1.readLine()))
        {
            line2 = reader2.readLine();
            line1 = filter(line1);
            line2 = filter(line2);
            if(!line1.equals(line2))
            {
                match = false;
                System.out.println("Mismatch in line " + lineNumber);
                System.out.println("Line1: " + line1);
                System.out.println("Line2: " + line2);
            }
            lineNumber++;
        }
        return match;
    }
    
    private String filter(String line)
    {
        if(null == line) return "";
        String filteredLine = line.replaceAll("//", "");
        filteredLine = filteredLine.replaceAll("/\\*", "");
        filteredLine = filteredLine.replaceAll("\\*/", "");
        return filteredLine.trim();
    }
    
    private void addJavaSrcFiles(File dir, Map resultMap)
    {
        File[] fileList = dir.listFiles();
        for(int ii = 0; ii < fileList.length; ii++)
        {
            File currentFile = fileList[ii];
            if(currentFile.isDirectory())
            {
                addJavaSrcFiles(currentFile, resultMap);
            }
            else if(currentFile.isFile() && currentFile.getName().endsWith(".java"))
            {
                resultMap.put(currentFile.getName(), currentFile);
            }
        }
    }
}
