package com.mockrunner.test.gen.util;

import java.lang.reflect.Method;
import java.net.URL;

import com.mockrunner.gen.util.BCELClassAnalyzer;

import junit.framework.TestCase;

public class BCELClassAnalyzerTest extends TestCase
{
    private BCELClassAnalyzer analyzer;
    private Class theClass;
    
    public void testIsMethodDeprecated()
    {
        theClass = MyTestClass.class;
        analyzer = new BCELClassAnalyzer(theClass);
        assertDeprecated("method1", false);
        assertDeprecated("method2", true);
        assertDeprecated("method3", true);
        assertDeprecated("method4", false);
        theClass = Thread.class;
        analyzer = new BCELClassAnalyzer(theClass);
        assertDeprecated("resume", true);
        assertDeprecated("run", false);
    }
    
    private void assertDeprecated(String methodName, boolean shouldBeDeprecated)
    {
        Method[] methods = theClass.getDeclaredMethods();
        for(int ii = 0; ii < methods.length; ii++)
        {
            if(methodName.equals(methods[ii].getName()))
            {
                boolean isDeprecated = analyzer.isMethodDeprecated(methods[ii]);
                assertTrue(isDeprecated == shouldBeDeprecated);
            }
        }
    }
    
    private class MyTestClass
    {
        public void method1()
        {
            
        }
        
        /**
         * @deprecated
         */
        protected String method2(String aString)
        {
            return null;
        }
        
        /**
         * @deprecated
         */
        public BCELClassAnalyzerTest method3(int anInt, double aDouble) throws Exception
        {
            method4(null);
            return null;
        }
        
        private void method4(URL[] urls) throws Exception
        {
            
        }
    }
}
