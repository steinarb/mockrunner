package com.mockrunner.test.consistency;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mockrunner.gen.proc.GeneratorUtil;

import junit.framework.TestCase;

public class VersionConsistencyTest extends TestCase
{
    public void testFileConsistency() throws Exception
    {
        compareDirTrees("srcjdk1.3", "src");
        compareDirTrees("srcj2ee1.3", "src");
    }
    
    private void compareDirTrees(String sourceDir, String destinationDir) throws Exception
    {
        GeneratorUtil util = new GeneratorUtil();
        File sourceFile = new File(sourceDir);
        File destFile = new File(destinationDir);
        Map sourceMap = new HashMap();
        Map destMap = new HashMap();
        util.addJavaSrcFiles(sourceDir, sourceFile, sourceMap);
        util.addJavaSrcFiles(destinationDir, destFile, destMap);
        boolean ok = true;
        Iterator sourceIterator = sourceMap.keySet().iterator();
        while(sourceIterator.hasNext())
        {
            String currentFileName = (String)sourceIterator.next();
            File currentSourceFile = (File)sourceMap.get(currentFileName);
            File currentDestFile = (File)destMap.get(currentFileName);
            if(null == currentDestFile)
            {
                System.out.println("File " + currentSourceFile.getPath() + " not found under src");
                ok = false;
            }
            else
            {
                if(!compareFiles(currentSourceFile, currentDestFile))
                {
                    System.out.println("Mismatch in file " + currentSourceFile.getPath());
                    System.out.println();
                    ok = false;
                }
            }
        }
        assertTrue("There are errors", ok);
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
            if(!line1.equals(line2))
            {
                line1 = filter(line1);
                line2 = filter(line2);
                if(!line1.equals(line2))
                {
                    match = false;
                    System.out.println("Mismatch in line " + lineNumber);
                    System.out.println("Line1: " + line1);
                    System.out.println("Line2: " + line2);
                }
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
}
