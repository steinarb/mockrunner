package com.mockrunner.test.consistency;

import junit.framework.TestCase;

import com.mockrunner.gen.jar.MockrunnerJarTestConfiguration;
import com.mockrunner.gen.jar.TestConfigurationClassLoader;
import com.mockrunner.gen.jar.MockrunnerJarTestConfiguration.Mapping;
import com.mockrunner.util.common.MethodUtil;

public class JarReferenceTest extends TestCase
{
    public void testReferenceTests() throws Exception
    {
        MockrunnerJarTestConfiguration configuration = new MockrunnerJarTestConfiguration();
        Mapping[] mappings = configuration.createMappings();
        int numberErrors = 0;
        for(int ii = 0; ii < mappings.length; ii++)
        {
            LauncherThread thread = new LauncherThread(mappings[ii]);
            thread.start();
            thread.join();
            if(thread.hasError())
            {
                numberErrors++;
            }
        }
        assertTrue("There are errors.", numberErrors == 0);
    }
    
    private class LauncherThread extends Thread
    {
        private Mapping mapping;
        private boolean hasError;
        
        public LauncherThread(Mapping mapping)
        {
            this.mapping = mapping;
            hasError = false;
        }
        
        public synchronized boolean hasError()
        {
            return hasError;
        }
        
        private synchronized void setHasError()
        {
            hasError = true;
        }
        
        public void run()
        {
            try
            {
                TestConfigurationClassLoader classLoader = new TestConfigurationClassLoader(mapping.getUrls(), this.getClass().getClassLoader());
                Object launcher = classLoader.loadClass("com.mockrunner.test.consistency.Launcher").newInstance();
                setContextClassLoader(classLoader);
                System.out.println("Executing reference test for " + mapping.getUrls()[0] + ": " + mapping.getTestClass());
                MethodUtil.invoke(launcher, "run", mapping.getTestClass());
            } 
            catch(Exception exc)
            {
                setHasError();
            }
        }
    }
}
