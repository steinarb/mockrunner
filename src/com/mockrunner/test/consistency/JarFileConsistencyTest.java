package com.mockrunner.test.consistency;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mockrunner.gen.jar.MockrunnerJars;

import junit.framework.TestCase;

public class JarFileConsistencyTest extends TestCase
{
    private final static String RELEASE_DIR = "bin";
    private final static String LIB_DIR = "lib";
    private final static String DIV_DIR = "div";
    
    private List getReleasedJars()
    {
        File releaseDir = new File(RELEASE_DIR);
        String jarDirName = releaseDir.getAbsolutePath() + File.separator;
        jarDirName += releaseDir.list()[0] + File.separator + LIB_DIR;
        File[] jarFiles1 = new File(jarDirName).listFiles(new JarFileFiler());
        jarDirName += File.separator + DIV_DIR;
        File[] jarFiles2 = new File(jarDirName).listFiles(new JarFileFiler());
        List jarFiles = new ArrayList();
        jarFiles.addAll(Arrays.asList(jarFiles1));
        jarFiles.addAll(Arrays.asList(jarFiles2));
        return jarFiles;
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
    
    public void testJarFileConsistency()
    {
        File tempDir = new File("temp");
        tempDir.mkdir();
        List jarFiles = getReleasedJars();
        for(int ii = 0; ii < jarFiles.size(); ii++)
        {
            File nextFile = (File)jarFiles.get(ii);
            File nextCopyFile = new File(tempDir.getAbsolutePath() + File.separator + nextFile.getName());   
            copyFile(nextFile, nextCopyFile);
            
            nextCopyFile.delete();
        }
        tempDir.delete();
    }
    
    private void copyFile(File source, File destination)
    {
        
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
