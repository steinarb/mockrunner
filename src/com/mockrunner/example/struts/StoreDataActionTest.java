package com.mockrunner.example.struts;

import com.mockrunner.mock.web.ActionMockObjectFactory;
import com.mockrunner.struts.ActionTestModule;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

/**
 * Example test for {@link StoreDataAction}. 
 * Demonstrates multithread testing. The use of an inner thread 
 * class and  a single test method is a standard pattern that can 
 * be used in any multithreading test for Servlets and actions.
 * Please note that each thread has to call the action with
 * different sessions and requests but with one 
 * <code>ServletContext</code> in order to simulate the real
 * container behaviour. See the constructor of the thread how
 * to deal with the test module and factory.
 * Remove the <code>synchronized</code> keyword in
 * {@link StoreDataAction} and the test
 * will fail.
 * {@link com.mockrunner.base.MultiThreadTestSuite} is meant to
 * make multithread testing much easier, but it is not working
 * properly yet, so we do not use it here.
 */
public class StoreDataActionTest extends BasicActionTestCaseAdapter
{ 
    private volatile int numberSuccess;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        numberSuccess = 0;
    }
    
    public void testStoreDataAction() throws Exception
    {
        StoreTestThread thread1 = new StoreTestThread("thread1");
        StoreTestThread thread2 = new StoreTestThread("thread2");
        StoreTestThread thread3 = new StoreTestThread("thread3");
        StoreTestThread thread4 = new StoreTestThread("thread4");
        StoreTestThread thread5 = new StoreTestThread("thread5");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        thread5.join();
        assertTrue(numberSuccess == 1);  
    }

    public class StoreTestThread extends Thread
    {
        private ActionMockObjectFactory mockFactory;
        private ActionTestModule module;
        
        public StoreTestThread(String name)
        {
            super(name);
            mockFactory = createActionMockObjectFactory(getActionMockObjectFactory());
            module = createActionTestModule(mockFactory);
        }

        public void run()
        {
            module.addRequestParameter("id", "id");
            module.addRequestParameter("data", getName());
            module.actionPerform(StoreDataAction.class);
            if(module.getActionForward().getPath().equals("success"))
            {
                numberSuccess++;
            }
        }
    }
}
