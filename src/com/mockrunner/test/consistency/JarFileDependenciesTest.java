package com.mockrunner.test.consistency;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import com.kirkk.analyzer.framework.Analyzer;
import com.kirkk.analyzer.framework.JarBundle;
import com.mockrunner.gen.jar.JarFileExtractor;
import com.mockrunner.gen.jar.MockrunnerJars;
import com.mockrunner.gen.jar.MockrunnerJars.Permission;

public class JarFileDependenciesTest extends TestCase
{
    private final static String RELEASE_DIR = "bin";
    private final static String LIB_DIR = "lib";
    private final static String J2EE13_DIR = "j2ee1.3";
    private final static String THIRD_PARTY_DIR = "jar";
    
    private List getThirdPartyJarsJ2EE14()
    {
        String jarDirName = getBaseDir() + THIRD_PARTY_DIR;
        return new ArrayList(Arrays.asList(new File(jarDirName).listFiles(new JarFileFilter())));
    }
    
    private List getThirdPartyJarsJ2EE13()
    {
        List fileList = getThirdPartyJarsJ2EE14();
        List resultList = new ArrayList();
        for(int ii = 0; ii < fileList.size(); ii++)
        {
            File currentFile = (File)fileList.get(ii);
            if(!MockrunnerJars.getStandardInterfaceJ2EE14Jars().contains(currentFile.getName()))
            {
                resultList.add(currentFile);
            }
        }
        String jarDirName = getBaseDir() + THIRD_PARTY_DIR + File.separator + J2EE13_DIR;
        resultList.addAll(Arrays.asList(new File(jarDirName).listFiles(new JarFileFilter())));
        return resultList;
    }
    
    private List getReleasedJars()
    {
        List jarFiles = new ArrayList();
        jarFiles.addAll(getJ2EE13Jars());
        jarFiles.addAll(getJ2EE14Jars());
        return jarFiles;
    }
    
    private List getJ2EE13Jars()
    {
        String jarDirName = getBaseDir() + LIB_DIR + File.separator + J2EE13_DIR;
        return listFiles(jarDirName);
    }
    
    private List getJ2EE14Jars()
    {
        String jarDirName = getBaseDir() + LIB_DIR;
        return listFiles(jarDirName);
    }
    
    private List listFiles(String jarDirName)
    {
        File[] jarFiles = new File(jarDirName).listFiles(new JarFileFilter());
        return Arrays.asList(jarFiles);
    }

    private String getBaseDir()
    {
        File releaseDir = new File(RELEASE_DIR);
        String jarDirName = releaseDir.getAbsolutePath() + File.separator;
        return jarDirName + releaseDir.list()[0] + File.separator;
    }
    
    public void testAllJarsReleased()
    {
        List jarFiles = getReleasedJars();
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
        allJars.addAll(getJ2EE14Jars());
        allJars.addAll(getThirdPartyJarsJ2EE14());
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
        allJars.addAll(getJ2EE13Jars());
        allJars.addAll(getThirdPartyJarsJ2EE13());
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
    
    private class JarFileFilter implements FilenameFilter
    {
        public boolean accept(File dir, String name)
        {
            if(name.endsWith(".jar")) return true;
            return false;
        }
    }
}
