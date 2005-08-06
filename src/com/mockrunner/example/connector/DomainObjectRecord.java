package com.mockrunner.example.connector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.resource.cci.Record;
import javax.resource.cci.Streamable;

/**
 * Base class for domain objects that can be directly used in 
 * <code>Interaction.execute</code> calls.
 */
public abstract class DomainObjectRecord implements Record, Streamable
{
    private String name = this.getClass().getName() + "Record";
    private String description = name;
    
    public String getRecordName()
    {
        return name;
    }

    public String getRecordShortDescription()
    {
        return description;
    }

    public void setRecordName(String name)
    {
        this.name = name;
    }

    public void setRecordShortDescription(String description)
    {
        this.description = description;
    }

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public void read(InputStream stream) throws IOException
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while(-1 <(bytesRead = stream.read(buffer))) 
        {
            byteStream.write(buffer, 0, bytesRead);
        }
        byteStream.flush();
        unmarshal(byteStream.toByteArray());
    }

    public void write(OutputStream stream) throws IOException
    {
        stream.write(marshal());
    }
    
    public abstract byte[] marshal();
    
    public abstract void unmarshal(byte[] data);
}
