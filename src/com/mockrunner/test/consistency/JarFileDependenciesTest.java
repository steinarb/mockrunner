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
    private final static String DIV_DIR = "div";
    private final static String THIRD_PARTY_DIR = "jar";
    
    private List getThirdPartyJars()
    {
        String jarDirName = getBaseDir() + THIRD_PARTY_DIR;
        return new ArrayList(Arrays.asList(new File(jarDirName).listFiles()));
    }
    
    private List getReleasedJars()
    {
        String jarDirName = getBaseDir() + LIB_DIR;
        File[] jarFiles1 = new File(jarDirName).listFiles(new JarFileFiler());
        jarDirName += File.separator + DIV_DIR;
        File[] jarFiles2 = new File(jarDirName).listFiles(new JarFileFiler());
        List jarFiles = new ArrayList();
        jarFiles.addAll(Arrays.asList(jarFiles1));
        jarFiles.addAll(Arrays.asList(jarFiles2));
        return jarFiles;
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
        assertEquals(jarFileNames.size(), mockrunnerJars.size());
        assertTrue(jarFileNames.containsAll(mockrunnerJars));
    }
    
    public void testJarFileDependencies() throws Exception
    {  
        File tempDir = new File("temp");
        tempDir.mkdir();
        List allJars = new ArrayList();
        allJars.addAll(getReleasedJars());
        allJars.addAll(getThirdPartyJars());
        for(int ii = 0; ii < allJars.size(); ii++)
        {
            File nextFile = (File)allJars.get(ii);
            File nextCopyFile = new File(tempDir, nextFile.getName());   
            copyFile(nextFile, nextCopyFile);
        }
        boolean failure = doFilesContainIllegalDependencies(tempDir);
        delete(tempDir);
        assertFalse("There are illegal dependencies", failure);
    }
    
    private boolean doFilesContainIllegalDependencies(File srcDir) throws Exception
    {
        Analyzer analyzer = new Analyzer();
        JarBundle jarBundle[] = analyzer.analyze(srcDir);
        JarFileExtractor extractor = new JarFileExtractor(MockrunnerJars.getMockrunnerJars());
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
    
    private class JarFileFiler implements FilenameFilter
    {
        public boolean accept(File dir, String name)
        {
            if(name.endsWith(".jar")) return true;
            return false;
        }
    }
}
