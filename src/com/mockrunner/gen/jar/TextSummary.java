package com.mockrunner.gen.jar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.kirkk.analyzer.framework.Analyzer;
import com.kirkk.analyzer.framework.JarBundle;

/* This is a modified version of the original 
 * com.kirkk.analyzer.textui.XMLUISummary class
 * of the JarAnalyzer project at
 * http://www.kirkk.com/wiki/wiki.php/Main/JarAnalyzer
 * by Kirk Knoernschild
 * 
 * It does not read the input directory and output file
 * from System.in but takes them as command line parameters
 * or as method parameters. This makes the class usable from
 * automatic build scripts and ant tasks.
 * The output is in text format. Only dependend jar files
 * and unresolved dependencies are dumped.
 */
public class TextSummary
{
    private PrintWriter writer;

    public static void main(String args[]) throws Exception 
    {
        if(args.length < 2)
        {
            throw new RuntimeException("Usage: java com.mockrunner.gen.jar.TextSummary srcDir destFile");
        }
        new TextSummary().createSummary(new File(args[0]), new File(args[1]));
    }

    public void createSummary(File srcDir, File destFile) throws Exception 
    {
        Analyzer analyzer = new Analyzer();
        JarBundle jarBundle[] = analyzer.analyze(srcDir);
        outputAll(jarBundle, destFile);
    }

    private void outputAll(JarBundle[] jarBundle, File destFile) 
    {
        try 
        {
            FileWriter fileWriter = new FileWriter(destFile);
            writer = new PrintWriter(fileWriter);
        } 
        catch(IOException exc) 
        {
            throw new RuntimeException(exc);
        }
        output(jarBundle);
        writer.flush();
        writer.close();
    }

    private void output(JarBundle[] jarBundles) 
    {
        DateFormat timestampFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US);
        String timestamp = timestampFormat.format(new Date(System.currentTimeMillis()));
        writer.println("Created: " + timestamp);
        writer.println();
        for (int ii = 0; ii < jarBundles.length; ii++) 
        {
            String jar = jarBundles[ii].getJarFileName().substring(jarBundles[ii].getJarFileName().lastIndexOf("\\") + 1, jarBundles[ii].getJarFileName().length());
            writer.println("Jar file name: " + jar);
            boolean hasJars = externalJars(jarBundles[ii]);
            boolean hasPackages = unresolveableDependencies(jarBundles[ii]);
            if(!(hasJars || hasPackages))
            {
                writer.println();
                writer.println("No dependencies");
            }
            if(ii < jarBundles.length - 1)
            {
                writer.println();
                writer.println();
            }
        }
    }

    private boolean externalJars(JarBundle jarBundle) 
    {
        List dependencyList = jarBundle.getDependentJars();
        if(dependencyList.isEmpty()) return false;
        writer.println();
        writer.println("Depends on: ");
        writer.println();
        Iterator jarDependencies = dependencyList.iterator();
        while(jarDependencies.hasNext()) 
        {
            JarBundle dependentBundle = (JarBundle) jarDependencies.next();
            String jar = dependentBundle.getJarFileName();
            writer.println(jar);
        }
        return true;
    }

    private boolean unresolveableDependencies(JarBundle jarBundle) 
    {
        List packageList = jarBundle.getAllUnidentifiableExternallyReferencedPackages();
        if(packageList.isEmpty()) return false;;
        writer.println();
        writer.println("Unresolved dependencies: ");
        writer.println();
        Iterator unresolvedPackages = packageList.iterator();
        while(unresolvedPackages.hasNext()) 
        {
            String packageName = (String)unresolvedPackages.next();
            writer.println(packageName);
        }
        return true;
    }
}
