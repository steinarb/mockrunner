package com.mockrunner.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CaseInsensitiveMap implements Map
{
    private boolean isCaseSensitive;
    private Map caseInsensitiveMap;
    private Map actualMap;
     
    public CaseInsensitiveMap()
    {
        this(false);
    }
    
    public CaseInsensitiveMap(boolean isCaseSensitive)
    {
        this.isCaseSensitive = isCaseSensitive;
        caseInsensitiveMap = new HashMap();
        actualMap = new HashMap();
    }
    
    public boolean isCaseSensitive()
    {
        return isCaseSensitive;
    }
    
    public void setCaseSensitive(boolean isCaseSensitive)
    {
        this.isCaseSensitive = isCaseSensitive;
    }
    
    public void clear()
    {
        caseInsensitiveMap.clear();
        actualMap.clear();
    }
    
    public boolean containsKey(Object key)
    {
        Object compareKey = getCompareKey(key);
        return getCompareMap().containsKey(compareKey);
    }
    
    public boolean containsValue(Object value)
    {
        return actualMap.containsValue(value);
    }
    
    public Set entrySet()
    {
        return actualMap.entrySet();
    }
    
    public Object get(Object key)
    {
        Object compareKey = getCompareKey(key);
        return getCompareMap().get(compareKey);
    }
    
    public boolean isEmpty()
    {
        return size() <= 0;
    }
    
    public Set keySet()
    {
        return actualMap.keySet();
    }
    
    public Object put(Object key, Object value)
    {
        actualMap.put(key, value);
        if(!isStringKey(key))
        {
            return caseInsensitiveMap.put(key, value);
        }
        String stringKey = ((String)key).toUpperCase();
        return caseInsensitiveMap.put(stringKey, value);
    }
    
    public void putAll(Map map)
    {
        Iterator keys = map.keySet().iterator();
        while(keys.hasNext())
        {
            Object nextKey = keys.next();
            Object nextValue = map.get(nextKey);
            put(nextKey, nextValue);
        }
    }
    
    public Object remove(Object key)
    {
        actualMap.remove(key);
        if(!isStringKey(key))
        {
            return caseInsensitiveMap.remove(key);
        }
        String stringKey = ((String)key).toUpperCase();
        return caseInsensitiveMap.remove(stringKey);
    }
    
    public int size()
    {
        return actualMap.size();
    }
    
    public Collection values()
    {
        return actualMap.values();
    }
    
    private boolean isStringKey(Object key)
    {
        return (null != key) && (key instanceof String);
    }
    
    private Object getCompareKey(Object key)
    {
        if(isCaseSensitive || !isStringKey(key))
        {
            return key;
        }
        return ((String)key).toUpperCase();
    }
    
    private Map getCompareMap()
    {
        if(isCaseSensitive)
        {
            return actualMap;
        }
        return caseInsensitiveMap;
    }
}
