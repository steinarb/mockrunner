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
        this.responseRecord = null;
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
        this.responseRecord = null;
    }
    
    public void setResponse(Record responseRecord)
    {
        this.responseRecord = responseRecord;
        this.responseData = null;
        this.responseClass = MockStreamableByteArrayRecord.class;
    }
    
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
