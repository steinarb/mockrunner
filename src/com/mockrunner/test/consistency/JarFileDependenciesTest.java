package com.mockrunner.test.consistency;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import com.kirkk.analyzer.framework.Analyzer;
import com.kirkk.analyzer.framework.JarBundle;
import com.mockrunner.gen.jar.JarFileExtractor;
import com.mockrunner.gen.jar.MockrunnerJarTestConfiguration;
import com.mockrunner.gen.jar.MockrunnerJars;
import com.mockrunner.gen.jar.MockrunnerJars.Permission;

public class JarFileDependenciesTest extends TestCase
{
    private MockrunnerJarTestConfiguration configuration;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        configuration = new MockrunnerJarTestConfiguration();
    }
    
    protected void tearDown() throws Exception
    {   
        super.tearDown();
        configuration = null;
    }
    
    public void testAllJarsReleased()
    {
        List jarFiles = configuration.getReleasedJars();
        List mockrunnerJars = MockrunnerJars.getMockrunnerJars();
        List jarFileNames = new ArrayList();
        for(int ii = 0; ii < jarFiles.size(); ii++)
        {
            jarFileNames.add(((File)jarFiles.get(ii)).getName());
        }
        boolean ok = true;
        for(int ii = 0; ii < mockrunnerJars.size(); ii++)
        {
            if(!jarFileNames.contains(mockrunnerJars.get(ii)))
            {
                System.out.println("Missing jar in release: " + mockrunnerJars.get(ii));
                ok = false;
            }
        }
        assertEquals(jarFileNames.size(), mockrunnerJars.size());
        assertTrue("There are missing jars in the release", ok);
    }
    
    public void testJarFileDependenciesJ2EE14() throws Exception
    {  
        File tempDir = new File("temp");
        delete(tempDir);
        tempDir.mkdir();
        List allJars = new ArrayList();
        allJars.addAll(configuration.getJ2EE14Jars());
        allJars.addAll(configuration.getThirdPartyJarsJ2EE14());
        copyFiles(tempDir, allJars);
        boolean failure = doFilesContainIllegalDependencies(tempDir);
        delete(tempDir);
        assertFalse("There are illegal dependencies", failure);
    }
    
    public void testJarFileDependenciesJ2EE13() throws Exception
    {  
        File tempDir = new File("temp");
        delete(tempDir);
        tempDir.mkdir();
        List allJars = new ArrayList();
        allJars.addAll(configuration.getJ2EE13Jars());
        allJars.addAll(configuration.getThirdPartyJarsJ2EE13());
        copyFiles(tempDir, allJars);
        boolean failure = doFilesContainIllegalDependencies(tempDir);
        delete(tempDir);
        assertFalse("There are illegal dependencies", failure);
    }
    
    private boolean doFilesContainIllegalDependencies(File srcDir) throws Exception
    {
        Analyzer analyzer = new Analyzer();
        JarBundle jarBundle[] = analyzer.analyze(srcDir);
        JarFileExtractor extractor = new JarFileExtractor(MockrunnerJars.getMockrunnerJars(), MockrunnerJars.getStandardInterfaceJars());
        Map dependencyMap = extractor.createDependencies(jarBundle);
        Iterator jarNames = dependencyMap.keySet().iterator();
        boolean failure = false;
        while(jarNames.hasNext())
        {
            String nextJarName = (String)jarNames.next();
            if(!isJarOk(dependencyMap, nextJarName))
            {
                failure = true;
            }
        }
        JarBundle filteredBundle[] = extractor.filterBundles(jarBundle);
        for(int ii = 0; ii < filteredBundle.length; ii++)
        {
            JarBundle currentBundle = filteredBundle[ii];
            List extRefList = currentBundle.getAllUnidentifiableExternallyReferencedPackages();
            if(null != extRefList && !extRefList.isEmpty())
            {
                System.out.println("Unidentifiable dependencies for " + currentBundle.getJarFileName());
                for(int yy = 0; yy < extRefList.size(); yy++)
                {
                    System.out.println(extRefList.get(yy));
                }
                System.out.println();
                failure = true;
            }
        }
        return failure;
    }
    
    private boolean isJarOk(Map dependencyMap, String nextJarName)
    {
        Set nextJarSet = (Set)dependencyMap.get(nextJarName);
        Permission permission = MockrunnerJars.getPermission(nextJarName);
        Set prohibited = permission.getProhibited(nextJarSet);
        if(!prohibited.isEmpty())
        {
            System.out.println("Illegal dependencies for " + nextJarName);
            Iterator iterator = prohibited.iterator();
            while(iterator.hasNext())
            {
                System.out.println(iterator.next());
            }
            System.out.println();
            return false;
        }
        return true;
    }
    
    private void copyFiles(File tempDir, List allJars) throws IOException
    {
        for(int ii = 0; ii < allJars.size(); ii++)
        {
            File nextFile = (File)allJars.get(ii);
            File nextCopyFile = new File(tempDir, nextFile.getName());   
            copyFile(nextFile, nextCopyFile);
        }
    }

    private void copyFile(File source, File destination) throws IOException
    {
        FileChannel sourceChannel = new FileInputStream(source).getChannel();
        FileChannel destinationChannel = new FileOutputStream(destination).getChannel();
        destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        sourceChannel.close();
        destinationChannel.close();
    }

    private void delete(File file)
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
                delete(files[ii]);
            }
            file.delete();
        }
    }
}
