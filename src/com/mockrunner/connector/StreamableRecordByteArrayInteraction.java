package com.mockrunner.connector;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;

import javax.resource.ResourceException;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;
import javax.resource.cci.Streamable;

import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.mock.connector.cci.MockStreamableByteArrayRecord;
import com.mockrunner.util.common.ArrayUtil;
import com.mockrunner.util.common.StreamUtil;

/**
 * This interaction implementor works with bytes arrays and streamable
 * records. It takes a byte array for the request and a byte array or
 * a <code>Record</code> instance for the response. If the request byte array 
 * is <code>null</code>, which is the default, the implementor accepts any 
 * request and returns the specified result. If a request byte array is specified, 
 * this implementor accepts only requests that are equal to the specified request 
 * byte array. If a request is accepted, this implementor replies with the specified
 * response. You can use the various constructors and <code>set</code> methods
 * to configure the expected request data and the response.<br>
 * Please check out the documentation of the various methods for details.
 */
public class StreamableRecordByteArrayInteraction implements InteractionImplementor
{
    private boolean enabled;
    private byte[] expectedRequest;
    private byte[] responseData;
    private Class responseClass;
    private Record responseRecord;
    
    /**
     * Sets the expected request and the response to <code>null</code>,
     * i.e. an empty response is returned for every request.
     */
    public StreamableRecordByteArrayInteraction()
    {
        this(null, null, MockStreamableByteArrayRecord.class);
    }
    
    /**
     * Sets the expected request to <code>null</code> and prepares
     * the specified response data. The response class for the
     * {@link #execute(InteractionSpec,Record)} method is set
     * to the default {@link com.mockrunner.mock.connector.cci.MockStreamableByteArrayRecord}.
     * It is allowed to pass <code>null</code> for the response data which is equivalent
     * to an empty response.
     * The specified response is returned for every request.
     * @param responseData the response data
     */
    public StreamableRecordByteArrayInteraction(byte[] responseData)
    {
        this(null, responseData, MockStreamableByteArrayRecord.class);
    }
    
    /**
     * Sets the specified expected request data and prepares
     * the specified response data. The response class for the
     * {@link #execute(InteractionSpec,Record)} method is set
     * to the default {@link com.mockrunner.mock.connector.cci.MockStreamableByteArrayRecord}.
     * It is allowed to pass <code>null</code> for the request and response data 
     * which is equivalent to an empty expected request (i.e. every request is accepted)
     * or to an empty response respectively.
     * The specified response is returned, if the actual request matches the specified expected 
     * request data.
     * @param expectedRequest the expected request data
     * @param responseData the response data
     */
    public StreamableRecordByteArrayInteraction(byte[] expectedRequest, byte[] responseData)
    {
        this(expectedRequest, responseData, MockStreamableByteArrayRecord.class);
    }
    
    /**
     * Sets the expected request to <code>null</code> and prepares
     * the specified response data. The response class for the
     * {@link #execute(InteractionSpec,Record)} method is set
     * to the specified <code>responseClass</code>. The specified 
     * <code>responseClass</code> must implement <code>Record</code>
     * and <code>Streamable</code>, otherwise an 
     * <code>IllegalArgumentException</code> will be thrown.
     * It is allowed to pass <code>null</code> for the response data which is 
     * equivalent to an empty response.
     * The specified response is returned for every request.
     * @param responseData the response data
     * @param responseClass the response <code>Record</code> class
     * @throws IllegalArgumentException if the <code>responseClass</code>
     *         is not valid
     */
    public StreamableRecordByteArrayInteraction(byte[] responseData, Class responseClass)
    {
        this(null, responseData, responseClass);
    }
    
    /**
     * Sets the specified expected request data and prepares
     * the specified response data. The response class for the
     * {@link #execute(InteractionSpec,Record)} method is set
     * to the specified <code>responseClass</code>. The specified 
     * <code>responseClass</code> must implement <code>Record</code>
     * and <code>Streamable</code>, otherwise an 
     * <code>IllegalArgumentException</code> will be thrown.
     * It is allowed to pass <code>null</code> for the request and response data 
     * which is equivalent to an empty expected request (i.e. every request is accepted)
     * or to an empty response respectively.
     * The specified response is returned, if the actual request matches the specified expected 
     * request data.
     * @param expectedRequest the expected request data
     * @param responseData the response data
     * @param responseClass the response <code>Record</code> class
     * @throws IllegalArgumentException if the <code>responseClass</code>
     *         is not valid
     */
    public StreamableRecordByteArrayInteraction(byte[] expectedRequest, byte[] responseData, Class responseClass)
    {
        setExpectedRequest(expectedRequest);
        setResponse(responseData, responseClass);
        this.enabled = true;
    }
    
    /**
     * Sets the specified expected request data and the response
     * <code>Record</code> for the {@link #execute(InteractionSpec, Record)}
     * method. The response <code>Record</code> is ignored for 
     * {@link #execute(InteractionSpec,Record,Record)} but takes precedence
     * over the specified response byte data for {@link #execute(InteractionSpec, Record)}.
     * It is allowed to pass <code>null</code> for the request and response <code>Record</code> 
     * which is equivalent to an empty expected request (i.e. every request is accepted)
     * or to no specified response <code>Record</code>, i.e. the specified response
     * byte data is taken.
     * The specified response is returned, if the actual request matches the specified expected 
     * request data.
     * @param expectedRequest the expected request data
     * @param responseRecord the response <code>Record</code>
     */
    public StreamableRecordByteArrayInteraction(byte[] expectedRequest, Record responseRecord)
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
     * over the specified response byte data for {@link #execute(InteractionSpec, Record)}.
     * It is allowed to pass <code>null</code> for the response <code>Record</code> 
     * which is equivalent to no specified response <code>Record</code>, i.e. the specified response
     * byte data is taken.
     * The specified response is returned for every request.
     * @param responseRecord the response <code>Record</code>
     */
    public StreamableRecordByteArrayInteraction(Record responseRecord)
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
     * Sets the specified expected request data. The response is returned, 
     * if the actual request matches the specified expected request data.
     * It is allowed to pass <code>null</code> for the request data 
     * which is equivalent to an empty expected request (i.e. every request 
     * is accepted).
     * @param expectedRequest the expected request data
     */
    public void setExpectedRequest(byte[] expectedRequest)
    {
        if(null == expectedRequest)
        {
            this.expectedRequest = null;
        }
        else
        {
            this.expectedRequest = (byte[])ArrayUtil.copyArray(expectedRequest);
        }
    }
    
    /**
     * Reads the expected request data from the specified <code>InputStream</code>.
     * The response is returned, if the actual request matches the  expected request data.
     * It is allowed to pass <code>null</code> for the <code>InputStream</code>
     * which is equivalent to an empty expected request (i.e. every request
     * is accepted).
     * @param expectedRequest the expected request
     */
    public void setExpectedRequest(InputStream expectedRequest)
    {
        if(null == expectedRequest)
        {
            this.expectedRequest = null;
        }
        else
        {
            this.expectedRequest = StreamUtil.getStreamAsByteArray(expectedRequest);
        }
    }
    
    /**
     * Prepares the specified response data. The response class for the
     * {@link #execute(InteractionSpec,Record)} method is set
     * to the default {@link com.mockrunner.mock.connector.cci.MockStreamableByteArrayRecord}.
     * It is allowed to pass <code>null</code> for the response data
     * which is equivalent to an empty response.
     * @param responseData the response data
     */
    public void setResponse(byte[] responseData)
    {
        setResponse(responseData, MockStreamableByteArrayRecord.class);
    }
    
    /**
     * Prepares the specified response data. The response class for the
     * {@link #execute(InteractionSpec,Record)} method is set
     * to the specified <code>responseClass</code>. The specified 
     * <code>responseClass</code> must implement <code>Record</code>
     * and <code>Streamable</code>, otherwise an 
     * <code>IllegalArgumentException</code> will be thrown.
     * It is allowed to pass <code>null</code> for the response data 
     * which is equivalent to an empty response.
     * @param responseData the response data
     * @param responseClass the response <code>Record</code> class
     * @throws IllegalArgumentException if the <code>responseClass</code>
     *         is not valid
     */
    public void setResponse(byte[] responseData, Class responseClass)
    {
        if(!isResponseClassAcceptable(responseClass))
        {
            throw new IllegalArgumentException("responseClass must implement " + Streamable.class.getName() + " and " + Record.class.getName());
        }
        if(null == responseData)
        {
            this.responseData = null;
        }
        else
        {
            this.responseData = (byte[])ArrayUtil.copyArray(responseData);
        }
        this.responseClass = responseClass;
    }

    /**
     * Reads the response data from the specified <code>InputStream</code>. 
     * The response class for the {@link #execute(InteractionSpec,Record)} method 
     * is set to the default 
     * {@link com.mockrunner.mock.connector.cci.MockStreamableByteArrayRecord}.
     * It is allowed to pass <code>null</code> for the <code>InputStream</code>
     * which is equivalent to an empty response.
     * @param responseData the response data
     */
    public void setResponse(InputStream responseData)
    {
        setResponse(responseData, MockStreamableByteArrayRecord.class);
    }
    
    /**
     * Reads the response data from the specified <code>InputStream</code>. 
     * The response class for the
     * {@link #execute(InteractionSpec,Record)} method is set
     * to the specified <code>responseClass</code>. The specified 
     * <code>responseClass</code> must implement <code>Record</code>
     * and <code>Streamable</code>, otherwise an 
     * <code>IllegalArgumentException</code> will be thrown.
     * It is allowed to pass <code>null</code> for the <code>InputStream</code>
     * which is equivalent to an empty response.
     * @param responseData the response data
     * @param responseClass the response <code>Record</code> class
     * @throws IllegalArgumentException if the <code>responseClass</code>
     *         is not valid
     */
    public void setResponse(InputStream responseData, Class responseClass)
    {
        if(!isResponseClassAcceptable(responseClass))
        {
            throw new IllegalArgumentException("responseClass must implement " + Streamable.class.getName() + " and " + Record.class.getName());
        }
        if(null == responseData)
        {
            this.responseData = null;
        }
        else
        {
            this.responseData = StreamUtil.getStreamAsByteArray(responseData);
        }
        this.responseClass = responseClass;
    }
    
    /**
     * Prepares the response <code>Record</code> for the 
     * {@link #execute(InteractionSpec, Record)} method. The response 
     * <code>Record</code> is ignored for {@link #execute(InteractionSpec,Record,Record)} 
     * but takes precedence over the specified response byte data for 
     * {@link #execute(InteractionSpec, Record)}.
     * It is allowed to pass <code>null</code> for the response <code>Record</code> 
     * which is equivalent to no specified response <code>Record</code>, i.e. the specified response
     * byte data is taken.
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
     * The response <code>Record</code> must implement <code>Streamable</code>
     * or it must be <code>null</code> (which is the case, if the actual request 
     * targets the {@link #execute(InteractionSpec,Record)} method instead of 
     * {@link #execute(InteractionSpec,Record,Record)}).<br><br>
     * The expected request must be <code>null</code> (use the various
     * <code>setExpectedRequest</code> methods) or the actual request <code>Record</code>
     * must implement <code>Streamable</code> and must contain the same data as
     * the specified expected request.<br><br>
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
        if(request instanceof Streamable)
        {
            try
            {
                Streamable streamableRequest = (Streamable)request;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                streamableRequest.write(stream);
                stream.flush();
                return Arrays.equals(expectedRequest, stream.toByteArray());
            } 
            catch(Exception exc)
            {
                throw new NestedApplicationException(exc);
            }
        }
        return false;
    }
    
    private boolean isResponseAcceptable(Record response)
    {
        return (null == response) || (response instanceof Streamable);
    }
    
    private boolean isResponseClassAcceptable(Class responseClass)
    {
        return (null == responseClass) || ((Streamable.class.isAssignableFrom(responseClass)) && (Record.class.isAssignableFrom(responseClass)));
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
     * it always takes precedence, i.e. the byte array response will be ignored.
     * If no <code>Record</code> object is specified, a <code>Record</code> object
     * is created and filled with the specified byte response data. Use the 
     * <code>setResponse</code> methods that take a byte array or an <code>InputStream</code>
     * to prepare response data. The created <code>Record</code> is of the the
     * specified type (the <code>setResponse</code> methods that take a second
     * <code>Class</code> parameter allows for specifying a type). If no type
     * is specified, a {@link com.mockrunner.mock.connector.cci.MockStreamableByteArrayRecord}
     * is created. If no response data is specified at all, an empty
     * {@link com.mockrunner.mock.connector.cci.MockStreamableByteArrayRecord}
     * will be returned.
     * @param interactionSpec the interaction spec
     * @param actualRequest the actual request
     * @return the response according to the current request
     */
    public Record execute(InteractionSpec interactionSpec, Record actualRequest) throws ResourceException
    {
        if(!canHandle(interactionSpec, actualRequest, null)) return null;
        if(null != responseRecord) return responseRecord;
        Streamable response = null;
        try
        {
            if(null == responseClass)
            {
                response = new MockStreamableByteArrayRecord();
            }
            else
            {
                response = (Streamable)responseClass.newInstance();
            }
            if(null != responseData)
            {
                response.read(new ByteArrayInputStream(responseData));
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
     * specified byte response data. Use the <code>setResponse</code> methods that 
     * take a byte array or an <code>InputStream</code> to prepare response data. 
     * The response <code>Record</code> must implement <code>Streamable</code>
     * (it does, otherwise the request would have been rejected by
     * {@link #canHandle}). If no response data is specified at all,
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
                ((Streamable)actualResponse).read(new ByteArrayInputStream(responseData));
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
