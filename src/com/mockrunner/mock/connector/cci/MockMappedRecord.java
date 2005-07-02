package com.mockrunner.mock.connector.cci;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.resource.cci.MappedRecord;

import com.mockrunner.base.NestedApplicationException;

/**
 * Mock implementation of <code>MappedRecord</code>.
 */
public class MockMappedRecord extends MockRecord implements MappedRecord
{
    private Map backingMap;
    
    public MockMappedRecord()
    {
        this("MockrunnerGenericMappedRecord");
    }
    
    public MockMappedRecord(String name)
    {
        this(name, name);
    }

    public MockMappedRecord(String name, String description)
    {
        super(name, description);
        backingMap = new HashMap();
    }
    
    public int size()
    {
        return backingMap.size();
    }

    public boolean isEmpty()
    {
        return backingMap.isEmpty();
    }

    public boolean containsKey(Object key)
    {
        return backingMap.containsKey(key);
    }

    public boolean containsValue(Object value)
    {
        return backingMap.containsValue(value);
    }

    public Object get(Object key)
    {
        return backingMap.get(key);
    }

    public Object put(Object key, Object value)
    {
        return backingMap.put(key, value);
    }

    public Object remove(Object key)
    {
        return backingMap.remove(key);
    }

    public void putAll(Map map)
    {
        backingMap.putAll(map);
    }

    public void clear()
    {
        backingMap.clear();
    }

    public Set keySet()
    {
        return backingMap.keySet();
    }

    public Collection values()
    {
        return backingMap.values();
    }

    public Set entrySet()
    {
        return backingMap.entrySet();
    }

    public boolean equals(Object object)
    {
        if(!super.equals(object)) return false;
        MockMappedRecord other = (MockMappedRecord)object;
        return backingMap.equals(other.backingMap);
    }

    public int hashCode()
    {
        return super.hashCode() + 31 * backingMap.hashCode();
    }

    public String toString()
    {
        return super.toString() + "\n" + backingMap.toString();
    }

    public Object clone()
    {
        try
        {
            MockMappedRecord clone = (MockMappedRecord)super.clone();
            clone.backingMap = new HashMap(this.backingMap);
            return clone;
        } 
        catch(Exception exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
}
