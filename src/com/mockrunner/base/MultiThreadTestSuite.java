package com.mockrunner.base;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This TestSuite starts all test methods in
 * a number of seperate threads. <b>Doesn't
 * work properly yet. Do not use it :-)</b>
 */
public class MultiThreadTestSuite extends TestSuite
{
	private final static Log log = LogFactory.getLog(MultiThreadTestSuite.class);
    private int numberThreads;
	private boolean doClone;
	
	public MultiThreadTestSuite(Class theClass, String name)
	{
		this(theClass, name, 5, true);
	}
	
	public MultiThreadTestSuite(Class theClass)
	{
		this(theClass, 5, true);
	}
	
	public MultiThreadTestSuite(Class theClass, String name, int numberThreads, boolean doClone)
	{
		super(theClass, name);
		this.numberThreads = numberThreads;
		this.doClone = doClone;
	}

	public MultiThreadTestSuite(Class theClass, int numberThreads, boolean doClone)
	{
		super(theClass);
		this.numberThreads = numberThreads;
		this.doClone = doClone;
	}
	
	public void run(TestResult result)
	{
		Enumeration tests = tests();
		while(tests.hasMoreElements()) 
		{
			if(result.shouldStop()) return;
			TestCase currentTest = (TestCase)tests.nextElement();	
			List threads = createThreadListForTest(currentTest, result);
			runAllThreadsForTest(threads);
		}
	}
	
	private Test createNewTestInstanceBasedOn(TestCase test)
	{
		TestCase newTest = null;
		try
		{	
			Constructor constructor = getTestConstructor(test.getClass());
			if (constructor.getParameterTypes().length == 0) 
			{
				newTest = (TestCase)constructor.newInstance(new Object[0]);
				newTest.setName(test.getName());
								
			} 
			else 
			{
				newTest = (TestCase)constructor.newInstance(new Object[]{test.getName()});
			}
		}
		catch(Exception exc)
		{
			log.error(exc.getMessage(), exc);
		}
		return newTest;
	}

	private List createThreadListForTest(TestCase currentTest, TestResult result)
	{
		ArrayList threads = new ArrayList(numberThreads);
		for(int ii = 0; ii < numberThreads; ii++)
		{
			Test newTest = currentTest;
			if(doClone) newTest = createNewTestInstanceBasedOn(currentTest);
			TestThread thread = new TestThread("TestThread " + ii, newTest, result);
			threads.add(thread);
		}
		return threads;
	}

	private void runAllThreadsForTest(List threads)
	{
		for(int ii = 0; ii < threads.size(); ii++)
		{
			Thread thread = (Thread)threads.get(ii);
			thread.start();
		}
		for(int ii = 0; ii < threads.size(); ii++)
		{
			Thread thread = (Thread)threads.get(ii);
			try
			{
				thread.join();
			}
			catch(InterruptedException exc)
			{
				log.error("Interrupted", exc);
			}
		}
	}
	
	private static class TestThread extends Thread
	{
		private Test test;
		private TestResult result;
		
		public TestThread(String name, Test test, TestResult result)
		{
			super(name);
			this.test = test;
			this.result = result;
		}
		
		public void run()
		{
			test.run(result);
		}
	}
}
