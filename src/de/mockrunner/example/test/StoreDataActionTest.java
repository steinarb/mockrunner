package de.mockrunner.example.test;

import de.mockrunner.base.ActionTestCaseAdapter;
import de.mockrunner.base.ActionTestModule;
import de.mockrunner.base.MockObjectFactory;
import de.mockrunner.example.StoreDataAction;

/**
 * Example test for {@link de.mockrunner.example.StoreDataAction}. 
 * Demonstrates multithread testing. The use of an inner thread 
 * class and  a single test method is a standard pattern that can 
 * be used in any multithreading test for Servlets and actions.
 * Please note that each thread has to call the action with
 * different sessions and requests but with one 
 * <code>ServletContext</code> in order to simulate the real
 * container behaviour. See the constructor of the thread how
 * to deal with the test module and factory.
 * Remove the <code>synchronized</code> keyword in
 * {@link de.mockrunner.example.StoreDataAction} and the test
 * will fail.
 * {@link de.mockrunner.base.MultiThreadTestSuite} is meant to
 * make multithread testing much easier, but it is not working
 * properly yet.
 */
public class StoreDataActionTest extends ActionTestCaseAdapter
{ 
    private volatile int numberSuccess;
    
    public StoreDataActionTest(String arg0)
    {
        super(arg0);
    }
    
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
        private MockObjectFactory mockFactory;
        private ActionTestModule module;
        
        public StoreTestThread(String name)
        {
            super(name);
            mockFactory = createMockObjectFactory(getMockObjectFactory());
            module = createActionTestModule(mockFactory);
        }

        public void run()
        {
            mockFactory.getMockRequest().setupAddParameter("id", "id");
            mockFactory.getMockRequest().setupAddParameter("data", getName());
            module.actionPerform(StoreDataAction.class);
            if(module.getActionForward().getPath().equals("success"))
            {
                numberSuccess++;
            }
        }
    }
}
