package com.mockrunner.gen.jar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.kirkk.analyzer.framework.Analyzer;
import com.kirkk.analyzer.framework.JarBundle;

public class MockrunnerTextSummary implements Summary
{
    private List mockrunnerJars;

    public MockrunnerTextSummary()
    {
        mockrunnerJars = new ArrayList();
        mockrunnerJars.add("mockrunner.jar");
        mockrunnerJars.add("mockrunner-jdk1.3.jar");
    }
    
    public void createSummary(File srcDir, File destFile) throws Exception 
    {
        Analyzer analyzer = new Analyzer();
        JarBundle jarBundle[] = analyzer.analyze(srcDir);
        JarFileExtractor extractor = new JarFileExtractor(mockrunnerJars);
        Map dependencyMap = extractor.createDependencies(jarBundle);
        output(dependencyMap, destFile);
    }

    private void output(Map dependencyMap, File destFile) 
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

    private void output(Map dependencyMap, PrintWriter writer) 
    {
        DateFormat timestampFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US);
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
}
