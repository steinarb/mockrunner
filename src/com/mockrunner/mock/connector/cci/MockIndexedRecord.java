package com.mockrunner.mock.connector.cci;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.resource.cci.IndexedRecord;

import com.mockrunner.base.NestedApplicationException;

/**
 * Mock implementation of <code>IndexedRecord</code>.
 */
public class MockIndexedRecord extends MockRecord implements IndexedRecord
{
    private List backingList;
    
    public MockIndexedRecord()
    {
        this("MockrunnerGenericIndexedRecord");
    }
    
    public MockIndexedRecord(String name)
    {
        this(name, name);
    }

    public MockIndexedRecord(String name, String description)
    {
        super(name, description);
        backingList = new ArrayList();
    }

    public int size()
    {
        return backingList.size();
    }

    public boolean isEmpty()
    {
        return backingList.isEmpty();
    }

    public boolean contains(Object object)
    {
        return backingList.contains(object);
    }

    public Iterator iterator()
    {
        return backingList.iterator();
    }

    public Object[] toArray()
    {
        return backingList.toArray();
    }

    public Object[] toArray(Object[] object)
    {
        return backingList.toArray(object);
    }

    public boolean add(Object object)
    {
        return backingList.add(object);
    }

    public boolean remove(Object object)
    {
        return backingList.remove(object);
    }

    public boolean containsAll(Collection collection)
    {
        return backingList.containsAll(collection);
    }

    public boolean addAll(Collection collection)
    {
        return backingList.addAll(collection);
    }

    public boolean addAll(int index, Collection collection)
    {
        return backingList.addAll(index, collection);
    }

    public boolean removeAll(Collection collection)
    {
        return backingList.removeAll(collection);
    }

    public boolean retainAll(Collection collection)
    {
        return backingList.retainAll(collection);
    }

    public void clear()
    {
        backingList.clear();
    }

    public Object get(int index)
    {
        return backingList.get(index);
    }

    public Object set(int index, Object object)
    {
        return backingList.set(index, object);
    }

    public void add(int index, Object object)
    {
        backingList.add(index, object);
    }

    public Object remove(int index)
    {
        return backingList.remove(index);
    }

    public int indexOf(Object object)
    {
        return backingList.indexOf(object);
    }

    public int lastIndexOf(Object object)
    {
        return backingList.lastIndexOf(object);
    }

    public ListIterator listIterator()
    {
        return backingList.listIterator();
    }

    public ListIterator listIterator(int index)
    {
        return backingList.listIterator(index);
    }

    public List subList(int fromIndex, int toIndex)
    {
        return backingList.subList(fromIndex, toIndex);
    }

    public boolean equals(Object object)
    {
        if(!super.equals(object)) return false;
        MockIndexedRecord other = (MockIndexedRecord)object;
        return backingList.equals(other.backingList);
    }

    public int hashCode()
    {
        return super.hashCode() + 31 * backingList.hashCode();
    }

    public String toString()
    {
        return super.toString() + "\n" + backingList.toString();
    }

    public Object clone()
    {
        try
        {
            MockIndexedRecord clone = (MockIndexedRecord)super.clone();
            clone.backingList = new ArrayList(this.backingList);
            return clone;
        } 
        catch(Exception exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
}
