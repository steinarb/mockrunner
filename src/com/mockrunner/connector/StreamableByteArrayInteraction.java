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
 * a record instance as a response. If the request byte array is <code>null</code>,
 * which is the default, the implementor accepts any request and returns the specified
 * result. If a request byte array is specified, this implementor accepts only 
 * requests that are equal to the specified request byte array. In this case 
 * the actual request <code>Record</code> must implement <code>Streamable</code>.
 * If a response record is specified, it always takes precedence, i.e.
 * the {@link #execute(InteractionSpec , Record)} method returns this
 * response record regardless of any byte array response. Please check
 * out the documentation of the various methods for details.
 */
public class StreamableByteArrayInteraction implements InteractionImplementor
{
    private byte[] expectedRequest;
    private byte[] responseData;
    private Class responseClass;
    private Record responseRecord;
    
    public StreamableByteArrayInteraction()
    {
        this(null, null, MockStreamableByteArrayRecord.class);
    }
    
    public StreamableByteArrayInteraction(byte[] responseData)
    {
        this(null, responseData, MockStreamableByteArrayRecord.class);
    }
    
    public StreamableByteArrayInteraction(byte[] expectedRequest, byte[] responseData)
    {
        this(expectedRequest, responseData, MockStreamableByteArrayRecord.class);
    }
    
    public StreamableByteArrayInteraction(byte[] responseData, Class responseClass)
    {
        this(null, responseData, responseClass);
    }
    
    public StreamableByteArrayInteraction(byte[] expectedRequest, byte[] responseData, Class responseClass)
    {
        setExpectedRequest(expectedRequest);
        setResponse(responseData, responseClass);
    }
    
    public StreamableByteArrayInteraction(byte[] expectedRequest, Record responseRecord)
    {
        setExpectedRequest(expectedRequest);
        setResponse(responseRecord);
    }
    
    public StreamableByteArrayInteraction(Record responseRecord)
    {
        this(null, responseRecord);
    }
    
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
    
    public void setResponse(byte[] responseData)
    {
        setResponse(responseData, MockStreamableByteArrayRecord.class);
    }
    
    public void setResponse(byte[] responseData, Class responseClass)
    {
        if(!isReponseClassAcceptable(responseClass))
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

    public void setResponse(InputStream responseData)
    {
        setResponse(responseData, MockStreamableByteArrayRecord.class);
    }
    
    public void setResponse(InputStream responseData, Class responseClass)
    {
        if(!isReponseClassAcceptable(responseClass))
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
    
    public void setResponse(Record responseRecord)
    {
        this.responseRecord = responseRecord;
    }
    
    /**
     * Returns <code>true</code> if this implementor will handle the request.
     * This method returns <code>true</code> in the following cases:<br><br>
     * The response <code>Record</code> must implement <code>Streamable</code> 
     * or it must be <code>null</code> (which is the case, if the actual request 
     * targets the {@link #execute(InteractionSpec,Record)} method instead of 
     * {@link #execute(InteractionSpec,Record,Record)})<br><br>
     * The expected request must be <code>null</code> (use the various
     * <code>setExpectedRequest</code> methods) or the actual request <code>Record</code>
     * must implement <code>Streamable</code> and must contain the same data as
     * the specified expected request.<br><br>
     * Otherwise, <code>false</code> is returned.
     * @param interactionSpec the <code>InteractionSpec</code> for the actual call
     * @param request the request for the actual call
     * @param response the response for the actual call, may be <code>null</code>
     * @return <code>true</code> if this implementor will handle the request and
     *         will return the specified response, <code>false</code> otherwise
     */
    public boolean canHandle(InteractionSpec interactionSpec, Record request, Record response)
    {
        if(!isReponseAcceptable(response)) return false;
        return doesRequestMatch(request);
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
    
    private boolean isReponseAcceptable(Record response)
    {
        return (null == response) || (response instanceof Streamable);
    }
    
    private boolean isReponseClassAcceptable(Class responseClass)
    {
        return (null == responseClass) || ((Streamable.class.isAssignableFrom(responseClass)) && (Record.class.isAssignableFrom(responseClass)));
    }
    
    public Record execute(InteractionSpec interactionSpec, Record request) throws ResourceException
    {
        if(!canHandle(interactionSpec, request, null)) return null;
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

    public boolean execute(InteractionSpec interactionSpec, Record request, Record response) throws ResourceException
    {
        if(!canHandle(interactionSpec, request, response)) return false;
        try
        {
            if(null != responseData && null != response)
            {
                ((Streamable)response).read(new ByteArrayInputStream(responseData));
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
