package com.mockrunner.test.consistency;

import java.lang.reflect.Method;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class Launcher
{
    public void run(String testClassName) throws Exception
    {
        Class testClass = this.getClass().getClassLoader().loadClass(testClassName);
        Test test = null;
        if(!Test.class.isAssignableFrom(testClass))
        {
            Method method = testClass.getMethod("suite", null);
            test = (Test)method.invoke(null, null);
        }
        else
        {
            test = new TestSuite(testClass);
        }
        TestResult result = TestRunner.run(test);
        if(!result.wasSuccessful())
        {
            throw new RuntimeException("Test " + testClassName + " failed");
        }
    }
    
}
