package com.mockrunner.gen.jar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kirkk.analyzer.framework.Analyzer;
import com.kirkk.analyzer.framework.JarBundle;
import com.kirkk.analyzer.textui.Summary;
import com.mockrunner.util.common.StreamUtil;

public class MockrunnerTextSummary implements Summary
{
    private final static String TEMPLATE_FILE = "dependtemplate.txt";
    
    private List mockrunnerJars;
    private List standardJars;

    public MockrunnerTextSummary()
    {
        mockrunnerJars = MockrunnerJars.getMockrunnerJars();
        standardJars = MockrunnerJars.getStandardInterfaceJars();
    }
    
    public void createSummary(File srcDir, File destFile) throws Exception 
    {
        Analyzer analyzer = new Analyzer();
        JarBundle jarBundle[] = analyzer.analyze(srcDir);
        JarFileExtractor extractor = new JarFileExtractor(mockrunnerJars, standardJars);
        Map dependencyMap = extractor.createDependencies(jarBundle);
        try
        {
            output(dependencyMap, destFile);
        } 
        catch(Exception exc)
        {
            exc.printStackTrace();
        }
    }

    private void output(Map dependencyMap, File destFile) throws Exception 
    {
        try 
        {
            FileWriter fileWriter = new FileWriter(destFile);
            PrintWriter writer = new PrintWriter(fileWriter);
            output(dependencyMap, writer);
            writer.flush();
            writer.close();
        } 
        catch(IOException exc) 
        {
            throw new RuntimeException(exc);
        }
    }

    private void output(Map dependencyMap, PrintWriter writer) throws Exception 
    {
        dumpTemplate(writer);
        DateFormat timestampFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
        String timestamp = timestampFormat.format(new Date(System.currentTimeMillis()));
        writer.println("Created: " + timestamp);
        writer.println();
        Iterator jars = dependencyMap.keySet().iterator();
        while(jars.hasNext())
        {
            String jar = (String)jars.next();
            writer.println("Jar file name: " + jar);
            Set dependendJars = (Set)dependencyMap.get(jar);
            if(null == dependendJars || dependendJars.isEmpty())
            {
                writer.println();
                writer.println("No dependencies");
            }
            else
            {
                outputDependendJars(dependendJars, writer);
            }
            if(jars.hasNext())
            {
                writer.println();
                writer.println();
            }
        }
    }

    private void outputDependendJars(Set dependendJars, PrintWriter writer) 
    {
        writer.println();
        writer.println("Depends on: ");
        writer.println();
        Iterator jarDependencies = dependendJars.iterator();
        while(jarDependencies.hasNext()) 
        {
            String jar = (String)jarDependencies.next();
            writer.println(jar);
        }
    }
    
    private void dumpTemplate(PrintWriter writer) throws Exception
    {
        InputStream templateStream = MockrunnerTextSummary.class.getClassLoader().getResourceAsStream("/" + TEMPLATE_FILE);
        String templateText = StreamUtil.getReaderAsString(new InputStreamReader(templateStream, "ISO-8859-1"));
        writer.println(templateText);
        writer.println();
        writer.flush();
    }
}
