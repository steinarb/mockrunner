package com.mockrunner.test.consistency;

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
//import com.mockrunner.example.jms.StockQuotePublisherTest;
import com.mockrunner.example.servlet.RedirectServletTest;
import com.mockrunner.example.struts.AuthenticationActionTest;
import com.mockrunner.example.tag.TableEnumTagTest;
import com.mockrunner.gen.jar.MockrunnerJars;

public class MockrunnerJarTestConfiguration
{
    public final static String RELEASE_DIR = "bin";
    public final static String BUILD_DIR = "buildjdk1.6jee5";
    public final static String LIB_DIR = "lib";
    public final static String JDK13_DIR = "jdk1.3";
    public final static String JDK14_DIR = "jdk1.4";
    public final static String JDK15_DIR = "jdk1.5";
    public final static String JDK16_DIR = "jdk1.6";
    public final static String JDK17_DIR = "jdk1.7";
    public final static String J2EE13_DIR = "j2ee1.3";
    public final static String J2EE14_DIR = "j2ee1.4";
    public final static String JEE5_DIR = "jee5";
    public final static String THIRD_PARTY_DIR = "jar";
    
    public final static String ALL_REFERENCE_TEST = AllReferenceTests.class.getName();
    public final static String JDBC_REFERENCE_TEST = BookstoreTest.class.getName();
    public final static String EJB_REFERENCE_TEST = UserLoginSessionTest.class.getName();
//    public final static String JMS_REFERENCE_TEST = StockQuotePublisherTest.class.getName();
    public final static String SERVLET_REFERENCE_TEST = RedirectServletTest.class.getName();
    public final static String STRUTS_REFERENCE_TEST = AuthenticationActionTest.class.getName();
    public final static String TAG_REFERENCE_TEST = TableEnumTagTest.class.getName();
    public final static String CONNECTOR_REFERENCE_TEST = AccountDAOTest.class.getName();
    
    public Mapping[] createMappings()
    {
        try
        {
            List jdk13j2ee13Jars = getReleasedJars(JDK13_DIR, J2EE13_DIR);
            List jdk14j2ee13Jars = getReleasedJars(JDK14_DIR, J2EE13_DIR);
            List jdk14j2ee14Jars = getReleasedJars(JDK14_DIR, J2EE14_DIR);
            List jdk15j2ee13Jars = getReleasedJars(JDK15_DIR, J2EE13_DIR);
            List jdk15j2ee14Jars = getReleasedJars(JDK15_DIR, J2EE14_DIR);
            List jdk15jee5Jars = getReleasedJars(JDK15_DIR, JEE5_DIR);
            List jdk16j2ee13Jars = getReleasedJars(JDK16_DIR, J2EE13_DIR);
            List jdk16j2ee14Jars = getReleasedJars(JDK16_DIR, J2EE14_DIR);
            List jdk16jee5Jars = getReleasedJars(JDK16_DIR, JEE5_DIR);
            List jdk17j2ee13Jars = getReleasedJars(JDK17_DIR, J2EE13_DIR);
            List jdk17j2ee14Jars = getReleasedJars(JDK17_DIR, J2EE14_DIR);
            List jdk17jee5Jars = getReleasedJars(JDK17_DIR, JEE5_DIR);
            List jee5ThirdPartyJarURLs = getURLFromFileList(getThirdPartyJarsJEE5());
            List j2ee14ThirdPartyJarURLs = getURLFromFileList(getThirdPartyJarsJ2EE14());
            List j2ee13ThirdPartyJarURLs = getURLFromFileList(getThirdPartyJarsJ2EE13());
            List jdk13j2ee13mappings = createMappings(jdk13j2ee13Jars, j2ee13ThirdPartyJarURLs);
            List jdk14j2ee13mappings = createMappings(jdk14j2ee13Jars, j2ee13ThirdPartyJarURLs);
            List jdk14j2ee14mappings = createMappings(jdk14j2ee14Jars, j2ee14ThirdPartyJarURLs);
            List jdk15j2ee13mappings = createMappings(jdk15j2ee13Jars, j2ee13ThirdPartyJarURLs);
            List jdk15j2ee14mappings = createMappings(jdk15j2ee14Jars, j2ee14ThirdPartyJarURLs);
            List jdk15jee5mappings = createMappings(jdk15jee5Jars, jee5ThirdPartyJarURLs);
            List jdk16j2ee13mappings = createMappings(jdk16j2ee13Jars, j2ee13ThirdPartyJarURLs);
            List jdk16j2ee14mappings = createMappings(jdk16j2ee14Jars, j2ee14ThirdPartyJarURLs);
            List jdk16jee5mappings = createMappings(jdk16jee5Jars, jee5ThirdPartyJarURLs);
            List jdk17j2ee13mappings = createMappings(jdk17j2ee13Jars, j2ee13ThirdPartyJarURLs);
            List jdk17j2ee14mappings = createMappings(jdk17j2ee14Jars, j2ee14ThirdPartyJarURLs);
            List jdk17jee5mappings = createMappings(jdk17jee5Jars, jee5ThirdPartyJarURLs);
            List resultList = new ArrayList();
            resultList.addAll(jdk13j2ee13mappings);
            resultList.addAll(jdk14j2ee13mappings);
            resultList.addAll(jdk14j2ee14mappings);
            resultList.addAll(jdk15j2ee13mappings);
            resultList.addAll(jdk15j2ee14mappings);
            resultList.addAll(jdk15jee5mappings);
            resultList.addAll(jdk16j2ee13mappings);
            resultList.addAll(jdk16j2ee14mappings);
            resultList.addAll(jdk16jee5mappings);
            resultList.addAll(jdk17j2ee13mappings);
            resultList.addAll(jdk17j2ee14mappings);
            resultList.addAll(jdk17jee5mappings);
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
            urls.add(currentFile.toURI().toURL());
            urls.add(new File(BUILD_DIR).toURI().toURL());
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
//        else if(jarName.indexOf("jms") > -1)
//        {
//            return JMS_REFERENCE_TEST;
//        }
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
            urlList.add(currentFile.toURI().toURL());
        }
        return urlList;
    }
    
    public List getThirdPartyJarsJEE5()
    {
        String jarDirName = getBaseDir() + THIRD_PARTY_DIR;
        return new ArrayList(Arrays.asList(new File(jarDirName).listFiles(new JarFileFilter())));
    }
    
    public List getThirdPartyJarsJ2EE14()
    {
        List resultList = getThirdPartyJarsWithoutJEE5Jars();
        String jarDirName = getBaseDir() + THIRD_PARTY_DIR + File.separator + J2EE14_DIR;
        resultList.addAll(Arrays.asList(new File(jarDirName).listFiles(new JarFileFilter())));
        return resultList;
    }
    
    public List getThirdPartyJarsJ2EE13()
    {
        List resultList = getThirdPartyJarsWithoutJEE5Jars();
        String jarDirName = getBaseDir() + THIRD_PARTY_DIR + File.separator + J2EE13_DIR;
        resultList.addAll(Arrays.asList(new File(jarDirName).listFiles(new JarFileFilter())));
        return resultList;
    }
    
    private List getThirdPartyJarsWithoutJEE5Jars()
    {
        List fileList = getThirdPartyJarsJEE5();
        List resultList = new ArrayList();
        for(int ii = 0; ii < fileList.size(); ii++)
        {
            File currentFile = (File)fileList.get(ii);
            if(!isJEE5StandardInterfaceOrJEE5OnlyJar(currentFile))
            {
                resultList.add(currentFile);
            }
        }
        return resultList;
    }
    
    private boolean isJEE5StandardInterfaceOrJEE5OnlyJar(File currentFile)
    {
        if(MockrunnerJars.getStandardInterfaceJars().contains(currentFile.getName())) return true;
        if(MockrunnerJars.getJEE5OnlyJars().contains(currentFile.getName())) return true;
        return false;
    }

    public List getReleasedJars(String jdkDir, String j2eeDir)
    {
        String jarDirName = getBaseDir() + LIB_DIR + File.separator + jdkDir + File.separator + j2eeDir;
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
//            suite.addTest(new TestSuite(StockQuotePublisherTest.class));
            suite.addTest(new TestSuite(RedirectServletTest.class));
            suite.addTest(new TestSuite(AuthenticationActionTest.class));
            suite.addTest(new TestSuite(TableEnumTagTest.class));
            suite.addTest(new TestSuite(AccountDAOTest.class));
            return suite;
        }
    }
}