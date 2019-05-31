package com.mockrunner.test.gen;

import java.lang.reflect.Method;
import java.net.URL;

import com.mockrunner.gen.proc.BCELClassAnalyzer;

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
        assertDeprecated("method3", true);
        assertDeprecated("method4", false);
        checkMethod2();
        theClass = Thread.class;
        analyzer = new BCELClassAnalyzer(theClass);
        assertDeprecated("resume", true);
        assertDeprecated("run", false);
    }
    
    private void checkMethod2()
    {
        Method[] methods = theClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("method2")) {
                boolean isDeprecated = analyzer.isMethodDeprecated(method);
                if (method.getParameterTypes()[0].equals(String.class)) {
                    assertTrue(isDeprecated);
                } else {
                    assertFalse(isDeprecated);
                }
            }
        }
    }
    
    private void assertDeprecated(String methodName, boolean shouldBeDeprecated)
    {
        Method[] methods = theClass.getDeclaredMethods();
        for (Method method : methods) {
            if (methodName.equals(method.getName())) {
                boolean isDeprecated = analyzer.isMethodDeprecated(method);
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
        
        protected String method2(String[] aStringArray)
        {
            return null;
        }
        
        /**
         * @deprecated
         */
        public BCELClassAnalyzerTest method3(int[][] anIntArray, double aDouble) throws Exception
        {
            method4(null);
            return null;
        }
        
        private void method4(URL[] urls) throws Exception
        {
            
        }  
    }
}
