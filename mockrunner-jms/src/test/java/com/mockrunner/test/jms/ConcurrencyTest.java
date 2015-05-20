package com.mockrunner.test.jms;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockConnectionFactory;
import com.mockrunner.mock.jms.MockQueue;
import org.junit.Assert;
import org.junit.Test;

import javax.jms.Message;
import javax.jms.Session;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by et2448 on 5/13/15.
 */
public class ConcurrencyTest
    extends Assert
{
    @Test
    public void testAlotOfIterations()
        throws Exception
    {
        ConfigurationManager configurationManager = new ConfigurationManager();
        DestinationManager destinationManager = new DestinationManager();
        final MockQueue mockQueue = destinationManager.createQueue( "someQueue" );
        final MockConnectionFactory mockConnectionFactory = new MockConnectionFactory( destinationManager, configurationManager );
        int numThreads = Runtime.getRuntime().availableProcessors() > 1 ? Runtime.getRuntime().availableProcessors() : 10;
        ExecutorService executorService = Executors.newFixedThreadPool( numThreads );

        Callable<Object> callable = new Callable<Object>()
        {
            public Object call()
                throws Exception
            {
                Session session = mockConnectionFactory.createConnection().createSession( false,
                                                                                          Session.AUTO_ACKNOWLEDGE );
                Message message = session.createTextMessage( "someText" );
                session.createProducer( mockQueue ).send( message );

                return message;
            }
        };

        Collection<Callable<Object>> callables = Collections.nCopies( 10000, callable );
        executorService.invokeAll( callables );
        executorService.shutdown();
        executorService.awaitTermination( 1, TimeUnit.SECONDS );
        assertEquals( callables.size(), mockQueue.getReceivedMessageList().size() );
    }
}
