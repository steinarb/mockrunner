package com.mockrunner.test.consistency;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import com.kirkk.analyzer.textui.XMLUISummary;
import com.mockrunner.gen.jar.MockrunnerJars;

public class JarFileConsistencyTest extends TestCase
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
    
    public void testJarFileConsistency() throws Exception
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
        doTestJarFileConsistency(tempDir, new File(tempDir, "output.txt"));
        delete(tempDir);
    }
    
    private void doTestJarFileConsistency(File srcDir, File destFile) throws Exception
    {
        XMLUISummary summary = new XMLUISummary();
        summary.createSummary(srcDir, destFile);
        summary = null;
        System.gc();
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
