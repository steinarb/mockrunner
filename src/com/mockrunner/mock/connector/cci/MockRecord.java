package com.mockrunner.mock.connector.cci;

import javax.resource.cci.Record;

/**
 * Mock implementation of <code>Record</code>.
 */
public class MockRecord implements Record
{
    private String recordName;
    private String recordDescription;
 
    public MockRecord()
    {
        this("MockrunnerGenericRecord");
    }

    public MockRecord(String name)
    {
        this(name, name);
    }

    public MockRecord(String name, String description)
    {
        recordName = name;
        recordDescription = description;
    }

    public String getRecordName()
    {
        return recordName;
    }

    public String getRecordShortDescription()
    {
        return recordDescription;
    }

    public void setRecordName(String recordName)
    {
        this.recordName = recordName;
    }

    public void setRecordShortDescription(String recordDescription)
    {
        this.recordDescription = recordDescription;
    }

    public boolean equals(Object object)
    {
        if(null == object) return false;
        if(!object.getClass().equals(this.getClass())) return false;
        MockRecord other = (MockRecord)object;
        if(null != recordName && !recordName.equals(other.recordName)) return false;
        if(null != recordDescription && !recordDescription.equals(other.recordDescription)) return false;
        if(null == recordName && null != other.recordName) return false;
        if(null == recordDescription && null != other.recordDescription) return false;
        return true;
    }

    public int hashCode()
    {
        int hashCode = 0;
        if(null != recordName) hashCode += 31 * recordName.hashCode();
        if(null != recordDescription) hashCode += 31 * recordDescription.hashCode();
        return hashCode;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.getClass().getName() + ", ");
        buffer.append("name: " + recordName + ", ");
        buffer.append("description: " + recordDescription);
        return buffer.toString();
    }

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
