package com.mockrunner.gen.jar;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.mockrunner.example.connector.AccountDAOTest;
import com.mockrunner.example.ejb.UserLoginSessionTest;
import com.mockrunner.example.jdbc.BookstoreTest;
import com.mockrunner.example.jms.StockQuotePublisherTest;
import com.mockrunner.example.servlet.RedirectServletTest;
import com.mockrunner.example.struts.AuthenticationActionTest;
import com.mockrunner.example.tag.TableEnumTagTest;

public class MockrunnerJarTestConfiguration
{
    public final static String RELEASE_DIR = "bin";
    public final static String BUILD_DIR = "build";
    public final static String LIB_DIR = "lib";
    public final static String J2EE13_DIR = "j2ee1.3";
    public final static String THIRD_PARTY_DIR = "jar";
    
    public final static String ALL_REFERENCE_TEST = AllReferenceTests.class.getName();
    public final static String JDBC_REFERENCE_TEST = BookstoreTest.class.getName();
    public final static String EJB_REFERENCE_TEST = UserLoginSessionTest.class.getName();
    public final static String JMS_REFERENCE_TEST = StockQuotePublisherTest.class.getName();
    public final static String SERVLET_REFERENCE_TEST = RedirectServletTest.class.getName();
    public final static String STRUTS_REFERENCE_TEST = AuthenticationActionTest.class.getName();
    public final static String TAG_REFERENCE_TEST = TableEnumTagTest.class.getName();
    public final static String CONNECTOR_REFERENCE_TEST = AccountDAOTest.class.getName();
    
    public Mapping[] createMappings()
    {
        try
        {
            List j2ee14Jars = getJ2EE14Jars();
            List j2ee13Jars = getJ2EE13Jars();
            List j2ee14ThirdPartyJarURLs = getURLFromFileList(getThirdPartyJarsJ2EE14());
            List j2ee13ThirdPartyJarURLs = getURLFromFileList(getThirdPartyJarsJ2EE13());
            List j2ee14mappings = createMappings(j2ee14Jars, j2ee14ThirdPartyJarURLs);
            List j2ee13mappings = createMappings(j2ee13Jars, j2ee13ThirdPartyJarURLs);
            List resultList = new ArrayList();
            resultList.addAll(j2ee14mappings);
            resultList.addAll(j2ee13mappings);
            return (Mapping[])resultList.toArray(new Mapping[resultList.size()]);
        } 
        catch (Exception exc)
        {
            throw new RuntimeException(exc);
        }
    }
    
    private List createMappings(List jars, List thirdpartyJarURLs) throws Exception
    {
        List mappings = new ArrayList();
        for(int ii = 0; ii < jars.size(); ii++)
        {
            File currentFile = (File)jars.get(ii);
            String name = currentFile.getName();
            String referenceTest = getReferenceTest(name);
            List urls = new ArrayList();
            urls.add(currentFile.toURL());
            urls.add(new File(BUILD_DIR).toURL());
            urls.addAll(thirdpartyJarURLs);
            Mapping currentMapping = new Mapping(referenceTest, (URL[])urls.toArray(new URL[urls.size()]));
            mappings.add(currentMapping);
        }
        return mappings;
    }

    private String getReferenceTest(String jarName)
    {
        if(jarName.indexOf("servlet") > -1)
        {
            return SERVLET_REFERENCE_TEST;
        }
        else if(jarName.indexOf("tag") > -1)
        {
            return TAG_REFERENCE_TEST;
        }
        else if(jarName.indexOf("struts") > -1)
        {
            return STRUTS_REFERENCE_TEST;
        }
        else if(jarName.indexOf("jms") > -1)
        {
            return JMS_REFERENCE_TEST;
        }
        else if(jarName.indexOf("ejb") > -1)
        {
            return EJB_REFERENCE_TEST;
        }
        else if(jarName.indexOf("jdbc") > -1)
        {
            return JDBC_REFERENCE_TEST;
        }
        else if(jarName.indexOf("jca") > -1)
        {
            return CONNECTOR_REFERENCE_TEST;
        }
        return ALL_REFERENCE_TEST;
    }
    
    private List getURLFromFileList(List list) throws Exception
    {
        List urlList = new ArrayList();
        for(int ii = 0; ii < list.size(); ii++)
        {
            File currentFile = (File)list.get(ii);
            urlList.add(currentFile.toURL());
        }
        return urlList;
    }
    
    public List getThirdPartyStandardJarsJ2EE14()
    {
        String jarDirName = getBaseDir() + THIRD_PARTY_DIR;
        List resultList = new ArrayList();
        File[] files = new File(jarDirName).listFiles(new JarFileFilter());
        for(int ii = 0; ii < files.length; ii++)
        {
            File currentFile = files[ii];
            if(MockrunnerJars.getStandardInterfaceJ2EE14Jars().contains(currentFile.getName()))
            {
                resultList.add(currentFile);
            }
        }
        return resultList;
    }
    
    public List getThirdPartyStandardJarsJ2EE13()
    {
        List resultList = new ArrayList();
        String jarDirName = getBaseDir() + THIRD_PARTY_DIR + File.separator + J2EE13_DIR;
        resultList.addAll(Arrays.asList(new File(jarDirName).listFiles(new JarFileFilter())));
        return resultList;
    }
    
    public List getThirdPartyJarsJ2EE14()
    {
        String jarDirName = getBaseDir() + THIRD_PARTY_DIR;
        return new ArrayList(Arrays.asList(new File(jarDirName).listFiles(new JarFileFilter())));
    }
    
    public List getThirdPartyJarsJ2EE13()
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
    
    public List getReleasedJars()
    {
        List jarFiles = new ArrayList();
        jarFiles.addAll(getJ2EE13Jars());
        jarFiles.addAll(getJ2EE14Jars());
        return jarFiles;
    }
    
    public List getJ2EE13Jars()
    {
        String jarDirName = getBaseDir() + LIB_DIR + File.separator + J2EE13_DIR;
        return listFiles(jarDirName);
    }
    
    public List getJ2EE14Jars()
    {
        String jarDirName = getBaseDir() + LIB_DIR;
        return listFiles(jarDirName);
    }
    
    public List listFiles(String jarDirName)
    {
        File[] jarFiles = new File(jarDirName).listFiles(new JarFileFilter());
        return Arrays.asList(jarFiles);
    }

    public String getBaseDir()
    {
        File releaseDir = new File(RELEASE_DIR);
        String jarDirName = releaseDir.getAbsolutePath() + File.separator;
        return jarDirName + releaseDir.list()[0] + File.separator;
    }
   
    private class JarFileFilter implements FilenameFilter
    {
        public boolean accept(File dir, String name)
        {
            if(name.endsWith(".jar")) return true;
            return false;
        }
    }
    
    public class Mapping
    {
        private String testClass;
        private URL[] urls;
        
        public Mapping(String testClass, URL[] urls)
        {
            this.testClass = testClass;
            this.urls = urls;
        }
        
        public String getTestClass()
        {
            return testClass;
        }
        
        public URL[] getUrls()
        {
            return urls;
        }
    }
    
    public static class AllReferenceTests
    {
        public static Test suite()
        {
            TestSuite suite = new TestSuite("AllReferenceTests");
            suite.addTest(new TestSuite(BookstoreTest.class));
            suite.addTest(new TestSuite(UserLoginSessionTest.class));
            suite.addTest(new TestSuite(StockQuotePublisherTest.class));
            suite.addTest(new TestSuite(RedirectServletTest.class));
            suite.addTest(new TestSuite(AuthenticationActionTest.class));
            suite.addTest(new TestSuite(TableEnumTagTest.class));
            suite.addTest(new TestSuite(AccountDAOTest.class));
            return suite;
        }
    }
}