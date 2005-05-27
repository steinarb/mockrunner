package com.mockrunner.util.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of a <code>Map</code> that recognizes the case of the
 * keys, if the keys are strings. If <code>isCaseSensitive</code> is
 * <code>true</code> it behaves exactly like a <code>HashMap</code>.
 * If <code>isCaseSensitive</code> is <code>false</code> (which is the
 * default), it considers same strings with different case as equal.
 * I.e. if you do
 * <br>
 * <br>
 * <code>put("test", "1");</code>
 * <br>
 * <code>put("TEST", "2");</code>
 * <br>
 * <br>
 * the second <code>put</code> overwrites the value of the first one, 
 * because the keys are considered to be equal. With
 * <br>
 * <br>
 * <code>get("TesT");</code>
 * <br>
 * <br>
 * you'll get the result <code>"2"</code>.
 * If you iterate through the keys (using either <code>keySet</code> or
 * <code>entrySet</code>), you'll get the first added version of the key,
 * in the above case, you'll get <code>"test"</code>.
 * It is allowed to use non-strings as keys. In this case the <code>Map</code>
 * behaves like a usual <code>HashMap</code>.<br>
 * Note: This class is similar to a <code>TreeMap(String.CASE_INSENSITIVE_ORDER)</code>
 *       except that non-strings do not throw a <code>ClassCastException</code>
 *       and that keys are not sorted.
 */
public class CaseAwareMap implements Map
{
    private boolean isCaseSensitive;
    private Map caseInsensitiveMap;
    private Map actualMap;
     
    public CaseAwareMap()
    {
        this(false);
    }
    
    public CaseAwareMap(boolean isCaseSensitive)
    {
        this.isCaseSensitive = isCaseSensitive;
        caseInsensitiveMap = new HashMap();
        actualMap = new HashMap();
    }
    
    /**
     * Returns if keys are case sensitive. Defaults to <code>false</code>.
     * @return are keys case sensitive
     */ 
    public boolean isCaseSensitive()
    {
        return isCaseSensitive;
    }
    
    /**
     * Sets if keys are case sensitive.
     * If set to <code>true</code> this implementation behaves like
     * a <code>HashMap</code>. Please note, that all entries are cleared
     * when switching case sensitivity. It's not possible to switch
     * and keep the entries.
     * @param isCaseSensitive are keys case sensitive
     */ 
    public void setCaseSensitive(boolean isCaseSensitive)
    {
        clear();
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
        return doConsistentModify(key, new ConsistentPut(value));
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
        return doConsistentModify(key, new ConsistentRemove());
    }
    
    public int size()
    {
        return actualMap.size();
    }
    
    public Collection values()
    {
        return actualMap.values();
    }
    
    private boolean areKeysEquals(Object actualKey, Object compareKey)
    {
        if(null == actualKey && null == compareKey) return true;
        if(null == actualKey) return false;
        if(null == compareKey) return false;
        Object actualCompareKey = getCompareKey(actualKey);
        return compareKey.equals(actualCompareKey);
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
    
    private Object doConsistentModify(Object key, ConsistentModify modifier)
    {
        Object compareKey = getCompareKey(key);
        if(!caseInsensitiveMap.containsKey(compareKey))
        {
            return modifier.modify(key, compareKey);
        }
        Iterator iterator = actualMap.keySet().iterator();
        while(iterator.hasNext())
        {
            Object actualKey = iterator.next();
            if(areKeysEquals(actualKey, compareKey))
            {
                return modifier.modify(actualKey, compareKey);
            }
        }
        return null;
    }
    
    private interface ConsistentModify
    {
        public Object modify(Object key1, Object key2);
    }
    
    private class ConsistentRemove implements ConsistentModify
    {
        public Object modify(Object key1, Object key2)
        {
            actualMap.remove(key1);
            return caseInsensitiveMap.remove(key2);
        }
    }
    
    private class ConsistentPut implements ConsistentModify
    {
        private Object value;
        
        public ConsistentPut(Object value)
        {
            this.value = value;
        }
        
        public Object modify(Object key1, Object key2)
        {
            actualMap.put(key1, value);
            return caseInsensitiveMap.put(key2, value);
        }
    }
}