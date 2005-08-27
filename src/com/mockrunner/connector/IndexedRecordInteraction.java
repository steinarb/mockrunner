package com.mockrunner.connector;

import java.util.ArrayList;
import java.util.List;

import javax.resource.ResourceException;
import javax.resource.cci.IndexedRecord;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;

import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.mock.connector.cci.MockIndexedRecord;

/**
 * This interaction implementor works with indexed records. It takes a <code>List</code> for 
 * the request and a <code>List</code> or a <code>Record</code> instance for the response. 
 * If the request <code>List</code> is <code>null</code>, which is the default, 
 * the implementor accepts any  request and returns the specified result. If a request 
 * <code>List</code> is specified, this implementor accepts only requests that contain the same
 * data as the specified expected request <code>List</code>. The underlying lists are compared
 * as described in the <code>List.equals</code> method.
 * If a request is accepted, this implementor replies with the specified
 * response. You can use the various constructors and <code>set</code> methods
 * to configure the expected request and the response.<br>
 * Please check out the documentation of the various methods for details.
 */
public class IndexedRecordInteraction implements InteractionImplementor
{
    private boolean enabled;
    private List expectedRequest;
    private List responseData;
    private Class responseClass;
    private Record responseRecord;
    
    /**
     * Sets the expected request and the response to <code>null</code>,
     * i.e. an empty response is returned for every request.
     */
    public IndexedRecordInteraction()
    {
        this(null, null, MockIndexedRecord.class);
    }
    
    /**
     * Sets the expected request to <code>null</code> and prepares
     * the specified response <code>List</code>. The response class for the
     * {@link #execute(InteractionSpec,Record)} method is set
     * to the default {@link com.mockrunner.mock.connector.cci.MockIndexedRecord}.
     * It is allowed to pass <code>null</code> for the response <code>List</code> 
     * which is equivalent to an empty response.
     * The specified response is returned for every request.
     * @param responseList the response <code>List</code>
     */
    public IndexedRecordInteraction(List responseList)
    {
        this(null, responseList, MockIndexedRecord.class);
    }
    
    /**
     * Sets the specified expected request <code>List</code> and prepares
     * the specified response <code>List</code>. The response class for the
     * {@link #execute(InteractionSpec,Record)} method is set
     * to the default {@link com.mockrunner.mock.connector.cci.MockIndexedRecord}.
     * It is allowed to pass <code>null</code> for the request and response <code>List</code> 
     * which is equivalent to an empty expected request (i.e. every request is accepted)
     * or to an empty response respectively.
     * The specified response is returned, if the actual request matches the specified expected 
     * request.
     * @param expectedRequest the expected request <code>List</code
     * @param responseList the response <code>List</code
     */
    public IndexedRecordInteraction(List expectedRequest, List responseList)
    {
        this(expectedRequest, responseList, MockIndexedRecord.class);
    }
    
    /**
     * Sets the expected request to <code>null</code> and prepares
     * the specified response <code>List</code>. The response class for the
     * {@link #execute(InteractionSpec,Record)} method is set
     * to the specified <code>responseClass</code>. The specified 
     * <code>responseClass</code> must implement <code>IndexedRecord</code>,
     * otherwise an <code>IllegalArgumentException</code> will be thrown.
     * It is allowed to pass <code>null</code> for the response <code>List</code>
     * which is  equivalent to an empty response.
     * The specified response is returned for every request.
     * @param responseList the response <code>List</code
     * @param responseClass the response <code>Record</code> class
     * @throws IllegalArgumentException if the <code>responseClass</code>
     *         is not valid
     */
    public IndexedRecordInteraction(List responseList, Class responseClass)
    {
        this(null, responseList, responseClass);
    }
    
    /**
     * Sets the specified expected request <code>List</code> and prepares
     * the specified response <code>List</code>. The response class for the
     * {@link #execute(InteractionSpec,Record)} method is set
     * to the specified <code>responseClass</code>. The specified 
     * <code>responseClass</code> must implement <code>IndexedRecord</code>,
     * otherwise an <code>IllegalArgumentException</code> will be thrown.
     * It is allowed to pass <code>null</code> for the request and response <code>List</code> 
     * which is equivalent to an empty expected request (i.e. every request is accepted)
     * or to an empty response respectively.
     * The specified response is returned, if the actual request matches the specified expected 
     * request.
     * @param expectedRequest the expected request <code>List</code>
     * @param responseList the response <code>List</code>
     * @param responseClass the response <code>Record</code> class
     * @throws IllegalArgumentException if the <code>responseClass</code>
     *         is not valid
     */
    public IndexedRecordInteraction(List expectedRequest, List responseList, Class responseClass)
    {
        setExpectedRequest(expectedRequest);
        setResponse(responseList, responseClass);
        this.enabled = true;
    }
    
    /**
     * Sets the specified expected request <code>List</code> and the response
     * <code>Record</code> for the {@link #execute(InteractionSpec, Record)}
     * method. The response <code>Record</code> is ignored for 
     * {@link #execute(InteractionSpec,Record,Record)} but takes precedence
     * over the specified response <code>List</code> for {@link #execute(InteractionSpec, Record)}.
     * It is allowed to pass <code>null</code> for the request and response <code>Record</code> 
     * which is equivalent to an empty expected request (i.e. every request is accepted)
     * or to no specified response <code>Record</code>, i.e. the specified response
     * <code>List</code> is taken.
     * The specified response is returned, if the actual request matches the specified expected 
     * request.
     * @param expectedRequest the expected request <code>List</code>
     * @param responseRecord the response <code>Record</code>
     */
    public IndexedRecordInteraction(List expectedRequest, Record responseRecord)
    {
        setExpectedRequest(expectedRequest);
        setResponse(responseRecord);
        this.enabled = true;
    }
    
    /**
     * Sets the expected request to <code>null</code> and prepares the response
     * <code>Record</code> for the {@link #execute(InteractionSpec, Record)}
     * method. The response <code>Record</code> is ignored for 
     * {@link #execute(InteractionSpec,Record,Record)} but takes precedence
     * over the specified response <code>List</code> for {@link #execute(InteractionSpec, Record)}.
     * It is allowed to pass <code>null</code> for the response <code>Record</code> 
     * which is equivalent to no specified response <code>Record</code>, i.e. the specified response
     * <code>List</code> is taken.
     * The specified response is returned for every request.
     * @param responseRecord the response <code>Record</code>
     */
    public IndexedRecordInteraction(Record responseRecord)
    {
        this(null, responseRecord);
    }
    
    /**
     * Enables this implementor.
     */
    public void enable()
    {
        this.enabled = true;
    }
    
    /**
     * Disables this implementor. {@link #canHandle(InteractionSpec, Record, Record)}
     * always returns <code>false</code>, if this implementor is disabled.
     */
    public void disable()
    {
        this.enabled = false;
    }
    
    /**
     * Sets the specified expected request <code>List</code>. The response is returned, 
     * if the actual request matches the specified expected request <code>List</code>
     * according to <code>List.equals</code>.
     * It is allowed to pass <code>null</code> for the request <code>List</code> 
     * which is equivalent to an empty expected request (i.e. every request 
     * is accepted).
     * @param expectedRequest the expected request <code>List</code>
     */
    public void setExpectedRequest(List expectedRequest)
    {
        if(null == expectedRequest)
        {
            this.expectedRequest = null;
        }
        else
        {
            this.expectedRequest = new ArrayList(expectedRequest);
        }
    }
    
    /**
     * Prepares the specified response <code>List</code>. The response class for the
     * {@link #execute(InteractionSpec,Record)} method is set
     * to the default {@link com.mockrunner.mock.connector.cci.MockIndexedRecord}.
     * It is allowed to pass <code>null</code> for the response <code>List</code>
     * which is equivalent to an empty response.
     * @param responseList the response <code>List</code
     */
    public void setResponse(List responseList)
    {
        setResponse(responseList, MockIndexedRecord.class);
    }
    
    /**
     * Prepares the specified response <code>List</code>. The response class for the
     * {@link #execute(InteractionSpec,Record)} method is set
     * to the specified <code>responseClass</code>. The specified 
     * <code>responseClass</code> must implement <code>IndexedRecord</code>,
     * otherwise an <code>IllegalArgumentException</code> will be thrown.
     * It is allowed to pass <code>null</code> for the response <code>List</code> 
     * which is equivalent to an empty response.
     * @param responseList the response <code>List</code>
     * @param responseClass the response <code>Record</code> class
     * @throws IllegalArgumentException if the <code>responseClass</code>
     *         is not valid
     */
    public void setResponse(List responseList, Class responseClass)
    {
        if(!isResponseClassAcceptable(responseClass))
        {
            throw new IllegalArgumentException("responseClass must implement " + IndexedRecord.class.getName());
        }
        if(null == responseList)
        {
            this.responseData = null;
        }
        else
        {
            this.responseData = new ArrayList(responseList);
        }
        this.responseClass = responseClass;
    }

    /**
     * Prepares the response <code>Record</code> for the 
     * {@link #execute(InteractionSpec, Record)} method. The response 
     * <code>Record</code> is ignored for {@link #execute(InteractionSpec,Record,Record)} 
     * but takes precedence over the specified response <code>List</code> for 
     * {@link #execute(InteractionSpec, Record)}.
     * It is allowed to pass <code>null</code> for the response <code>Record</code> 
     * which is equivalent to no specified response <code>Record</code>, i.e. the specified response
     * <code>List</code> is taken.
     * @param responseRecord the response <code>Record</code>
     */
    public void setResponse(Record responseRecord)
    {
        this.responseRecord = responseRecord;
    }
    
    /**
     * Returns <code>true</code> if this implementor is enabled and will handle the request.
     * This method returns <code>true</code> if the following prerequisites are fulfilled:<br><br>
     * It is enabled.<br><br>
     * The response <code>Record</code> must implement <code>IndexedRecord</code>
     * or it must be <code>null</code> (which is the case, if the actual request 
     * targets the {@link #execute(InteractionSpec,Record)} method instead of 
     * {@link #execute(InteractionSpec,Record,Record)}).<br><br>
     * The expected request must be <code>null</code> (use the various
     * <code>setExpectedRequest</code> methods) or the actual request <code>Record</code>
     * must implement <code>IndexedRecord</code> and must contain the same data as
     * the specified expected request <code>List</code> according to <code>List.equals</code>.<br><br>
     * Otherwise, <code>false</code> is returned.
     * @param interactionSpec the <code>InteractionSpec</code> for the actual call
     * @param actualRequest the request for the actual call
     * @param actualResponse the response for the actual call, may be <code>null</code>
     * @return <code>true</code> if this implementor will handle the request and
     *         will return the specified response, <code>false</code> otherwise
     */
    public boolean canHandle(InteractionSpec interactionSpec, Record actualRequest, Record actualResponse)
    {
        if(!enabled) return false;
        if(!isResponseAcceptable(actualResponse)) return false;
        return doesRequestMatch(actualRequest);
    }
    
    private boolean doesRequestMatch(Record request)
    {
        if(null == expectedRequest) return true;
        if(null == request) return false;
        if(request instanceof IndexedRecord)
        {
            try
            {
                IndexedRecord indexedRequest = (IndexedRecord)request;
                if(indexedRequest.size() != expectedRequest.size()) return false;
                for(int ii = 0; ii < indexedRequest.size(); ii++)
                {
                    Object actualValue = indexedRequest.get(ii);
                    Object expectedValue = expectedRequest.get(ii);
                    if(!areObjectsEquals(actualValue, expectedValue))
                    {
                        return false;
                    }
                }
                return true;
            } 
            catch(Exception exc)
            {
                throw new NestedApplicationException(exc);
            }
        }
        return false;
    }
    
    private boolean areObjectsEquals(Object object1, Object object2)
    {
        if(null == object1 && null == object2) return true;
        if(null == object1) return false;
        return object1.equals(object2);
    }
    
    private boolean isResponseAcceptable(Record response)
    {
        return (null == response) || (response instanceof IndexedRecord);
    }
    
    private boolean isResponseClassAcceptable(Class responseClass)
    {
        return (null == responseClass) || (IndexedRecord.class.isAssignableFrom(responseClass));
    }
    
    /**
     * First version of the <code>execute</code> methods.<br><br>
     * This method returns <code>null</code>, if the request does not match 
     * according to the contract of {@link #canHandle}. This never happens under 
     * normal conditions since the {@link InteractionHandler} does not call 
     * <code>execute</code>, if {@link #canHandle} returns <code>false</code>.
     * <br><br>
     * Otherwise, this method returns the specified response. If a response 
     * <code>Record</code> object is specified (use {@link #setResponse(Record)}), 
     * it always takes precedence, i.e. the response <code>List</code> will be ignored.
     * If no <code>Record</code> object is specified, a <code>Record</code> object
     * is created and filled with the specified response <code>List</code> data. Use the 
     * <code>setResponse</code> methods that take a <code>List</code>
     * to prepare the response <code>List</code>. The created <code>Record</code> is of the the
     * specified type (the <code>setResponse</code> method that takes a second
     * <code>Class</code> parameter allows for specifying a type). If no type
     * is specified, a {@link com.mockrunner.mock.connector.cci.MockIndexedRecord}
     * is created. If no response <code>List</code> is specified at all, an empty
     * {@link com.mockrunner.mock.connector.cci.MockIndexedRecord}
     * will be returned.
     * @param interactionSpec the interaction spec
     * @param actualRequest the actual request
     * @return the response according to the current request
     */
    public Record execute(InteractionSpec interactionSpec, Record actualRequest) throws ResourceException
    {
        if(!canHandle(interactionSpec, actualRequest, null)) return null;
        if(null != responseRecord) return responseRecord;
        IndexedRecord response = null;
        try
        {
            if(null == responseClass)
            {
                response = new MockIndexedRecord();
            }
            else
            {
                response = (IndexedRecord)responseClass.newInstance();
            }
            if(null != responseData)
            {
                response.addAll(responseData);
            }
        } 
        catch(Exception exc)
        {
            ResourceException resExc = new ResourceException("execute() failed");
            resExc.setLinkedException(exc);
            throw resExc;
        }
        return (Record)response;
    }

    /**
     * Second version of the <code>execute</code> methods.<br><br>
     * This method returns <code>false</code>, if the request does not match 
     * according to the contract of {@link #canHandle}. This never happens under 
     * normal conditions since the {@link InteractionHandler} does not call 
     * <code>execute</code>, if {@link #canHandle} returns <code>false</code>.
     * <br><br>
     * Otherwise, this method fills the response <code>Record</code> with the
     * specified response <code>List</code> data. Use the <code>setResponse</code> methods that 
     * take a <code>List</code> to prepare the response <code>List</code>. 
     * The response <code>Record</code> must implement <code>IndexedRecord</code>
     * (it does, otherwise the request would have been rejected by
     * {@link #canHandle}). If no response <code>List</code> is specified at all,
     * the response <code>Record</code> is not touched but <code>true</code>
     * is returned anyway
     * @param interactionSpec the interaction spec
     * @param actualRequest the actual request
     * @param actualResponse the actual response
     * @return <code>true</code> under normal conditions
     */
    public boolean execute(InteractionSpec interactionSpec, Record actualRequest, Record actualResponse) throws ResourceException
    {
        if(!canHandle(interactionSpec, actualRequest, actualResponse)) return false;
        try
        {
            if(null != responseData && null != actualResponse)
            {
                ((IndexedRecord)actualResponse).clear();
                ((IndexedRecord)actualResponse).addAll(responseData);
            }
        } 
        catch(Exception exc)
        {
            ResourceException resExc = new ResourceException("execute() failed");
            resExc.setLinkedException(exc);
            throw resExc;
        }
        return true;
    }
}
