package de.mockrunner.example.test;

import de.mockrunner.base.ActionTestCaseAdapter;
import de.mockrunner.base.ActionTestModule;
import de.mockrunner.base.MockObjectFactory;
import de.mockrunner.example.StoreDataAction;

/**
 * An example how to code a multithread test.
 * Please note that you have to use 
 * createMockObjectFactory(getMockObjectFactory())
 * so that all threads share one ServletContext.
 * Just remove the synchronized block
 * from StoreDataAction to see the test fail.
 * It is planned to implement a better and easier 
 * way for multithread testing in the next release.
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
