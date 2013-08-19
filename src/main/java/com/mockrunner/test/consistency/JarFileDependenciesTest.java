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

import com.kirkk.analyzer.Analyzer;
import com.kirkk.analyzer.framework.Jar;
import com.mockrunner.gen.jar.JarFileExtractor;
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
        List jdk13j2ee13Jars = configuration.getReleasedJars(MockrunnerJarTestConfiguration.JDK13_DIR, MockrunnerJarTestConfiguration.J2EE13_DIR);
        List jdk14j2ee13Jars = configuration.getReleasedJars(MockrunnerJarTestConfiguration.JDK14_DIR, MockrunnerJarTestConfiguration.J2EE13_DIR);
        List jdk14j2ee14Jars = configuration.getReleasedJars(MockrunnerJarTestConfiguration.JDK14_DIR, MockrunnerJarTestConfiguration.J2EE14_DIR);
        List jdk15j2ee13Jars = configuration.getReleasedJars(MockrunnerJarTestConfiguration.JDK15_DIR, MockrunnerJarTestConfiguration.J2EE13_DIR);
        List jdk15j2ee14Jars = configuration.getReleasedJars(MockrunnerJarTestConfiguration.JDK15_DIR, MockrunnerJarTestConfiguration.J2EE14_DIR);
        List jdk15jee5Jars = configuration.getReleasedJars(MockrunnerJarTestConfiguration.JDK15_DIR, MockrunnerJarTestConfiguration.JEE5_DIR);
        List jdk16j2ee13Jars = configuration.getReleasedJars(MockrunnerJarTestConfiguration.JDK16_DIR, MockrunnerJarTestConfiguration.J2EE13_DIR);
        List jdk16j2ee14Jars = configuration.getReleasedJars(MockrunnerJarTestConfiguration.JDK16_DIR, MockrunnerJarTestConfiguration.J2EE14_DIR);
        List jdk16jee5Jars = configuration.getReleasedJars(MockrunnerJarTestConfiguration.JDK16_DIR, MockrunnerJarTestConfiguration.JEE5_DIR);
        doTestAllJarsReleased(jdk13j2ee13Jars, "JDK1.3, J2EE1.3");
        doTestAllJarsReleased(jdk14j2ee13Jars, "JDK1.4, J2EE1.3");
        doTestAllJarsReleased(jdk14j2ee14Jars, "JDK1.4, J2EE1.4");
        doTestAllJarsReleased(jdk15j2ee13Jars, "JDK1.5, J2EE1.3");
        doTestAllJarsReleased(jdk15j2ee14Jars, "JDK1.5, J2EE1.4");
        doTestAllJarsReleased(jdk15jee5Jars, "JDK1.5, JEE5");
        doTestAllJarsReleased(jdk16j2ee13Jars, "JDK1.6, J2EE1.3");
        doTestAllJarsReleased(jdk16j2ee14Jars, "JDK1.6, J2EE1.4");
        doTestAllJarsReleased(jdk16jee5Jars, "JDK1.6, JEE5");
    }
    
    private void doTestAllJarsReleased(List jarFiles, String message)
    {
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
                System.out.println("Missing jar in release " + message + ": " + mockrunnerJars.get(ii));
                ok = false;
            }
        }
        assertTrue("There are missing jars in the release", ok);
    }
    
    public void testJarFileDependencies() throws Exception
    {
        List jdk13j2ee13Jars = configuration.getReleasedJars(MockrunnerJarTestConfiguration.JDK13_DIR, MockrunnerJarTestConfiguration.J2EE13_DIR);
        List jdk14j2ee13Jars = configuration.getReleasedJars(MockrunnerJarTestConfiguration.JDK14_DIR, MockrunnerJarTestConfiguration.J2EE13_DIR);
        List jdk14j2ee14Jars = configuration.getReleasedJars(MockrunnerJarTestConfiguration.JDK14_DIR, MockrunnerJarTestConfiguration.J2EE14_DIR);
        List jdk15j2ee13Jars = configuration.getReleasedJars(MockrunnerJarTestConfiguration.JDK15_DIR, MockrunnerJarTestConfiguration.J2EE13_DIR);
        List jdk15j2ee14Jars = configuration.getReleasedJars(MockrunnerJarTestConfiguration.JDK15_DIR, MockrunnerJarTestConfiguration.J2EE14_DIR);
        List jdk15jee5Jars = configuration.getReleasedJars(MockrunnerJarTestConfiguration.JDK15_DIR, MockrunnerJarTestConfiguration.JEE5_DIR);
        List jdk16j2ee13Jars = configuration.getReleasedJars(MockrunnerJarTestConfiguration.JDK16_DIR, MockrunnerJarTestConfiguration.J2EE13_DIR);
        List jdk16j2ee14Jars = configuration.getReleasedJars(MockrunnerJarTestConfiguration.JDK16_DIR, MockrunnerJarTestConfiguration.J2EE14_DIR);
        List jdk16jee5Jars = configuration.getReleasedJars(MockrunnerJarTestConfiguration.JDK16_DIR, MockrunnerJarTestConfiguration.JEE5_DIR);
        List thirdPartyJ2EE13Jars = configuration.getThirdPartyJarsJ2EE13();
        List thirdPartyJ2EE14Jars = configuration.getThirdPartyJarsJ2EE14();
        List thirdPartyJEE5Jars = configuration.getThirdPartyJarsJEE5();
        doTestJarFileDependencies(jdk13j2ee13Jars, thirdPartyJ2EE13Jars, "JDK1.3, J2EE1.3");
        doTestJarFileDependencies(jdk14j2ee13Jars, thirdPartyJ2EE13Jars, "JDK1.4, J2EE1.3");
        doTestJarFileDependencies(jdk14j2ee14Jars, thirdPartyJ2EE14Jars, "JDK1.4, J2EE1.4");
        doTestJarFileDependencies(jdk15j2ee13Jars, thirdPartyJ2EE13Jars, "JDK1.5, J2EE1.3");
        doTestJarFileDependencies(jdk15j2ee14Jars, thirdPartyJ2EE14Jars, "JDK1.5, J2EE1.4");
        doTestJarFileDependencies(jdk15jee5Jars, thirdPartyJEE5Jars, "JDK1.5, JEE5");
        doTestJarFileDependencies(jdk16j2ee13Jars, thirdPartyJ2EE13Jars, "JDK1.6, J2EE1.3");
        doTestJarFileDependencies(jdk16j2ee14Jars, thirdPartyJ2EE14Jars, "JDK1.6, J2EE1.4");
        doTestJarFileDependencies(jdk16jee5Jars, thirdPartyJEE5Jars, "JDK1.6, JEE5");
    }
    
    private void doTestJarFileDependencies(List releasedJars, List thirdpartyJars, String message) throws Exception
    {  
        File tempDir = new File("temp");
        delete(tempDir);
        tempDir.mkdir();
        List allJars = new ArrayList();
        allJars.addAll(releasedJars);
        allJars.addAll(thirdpartyJars);
        copyFiles(tempDir, allJars);
        boolean failure = doFilesContainIllegalDependencies(tempDir);
        delete(tempDir);
        if(failure)
        {
            System.out.println("There are illegal dependencies in release " + message);
            fail("There are illegal dependencies");
        }
        else
        {
            System.out.println("Dependencies for " + message + " ok");
        }
    }
    
    private boolean doFilesContainIllegalDependencies(File srcDir) throws Exception
    {
        Analyzer analyzer = new Analyzer();
        Jar jars[] = analyzer.analyze(srcDir);
        JarFileExtractor extractor = new JarFileExtractor(MockrunnerJars.getMockrunnerJars(), MockrunnerJars.getStandardInterfaceJars());
        Map dependencyMap = extractor.createDependencies(jars);
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
        Jar filteredJars[] = extractor.filter(jars);
        for(int ii = 0; ii < filteredJars.length; ii++)
        {
            Jar currentJar = filteredJars[ii];
            List extRefList = currentJar.getAllUnidentifiableExternallyReferencedPackages();
            if(null != extRefList && !extRefList.isEmpty())
            {
                System.out.println("Unidentifiable dependencies for " + currentJar.getJarFileName());
                for(int yy = 0; yy < extRefList.size(); yy++)
                {
                    System.out.println("Unidentifiable dependency: " + extRefList.get(yy));
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
                System.out.println("Illegal dependency: " + iterator.next());
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
