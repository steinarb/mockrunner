package com.mockrunner.test.consistency;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class Launcher
{
    public void run(String testClassName) throws Exception
    {
        Class testClass = this.getClass().getClassLoader().loadClass(testClassName);
        TestResult result = TestRunner.run(new TestSuite(testClass));
        if(!result.wasSuccessful())
        {
            throw new RuntimeException("Test " + testClassName + " failed");
        }
    }
    
}
