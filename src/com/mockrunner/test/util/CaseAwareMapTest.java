package com.mockrunner.test.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mockrunner.util.common.CaseAwareMap;

import junit.framework.TestCase;

public class CaseAwareMapTest extends TestCase
{
    private CaseAwareMap map;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        map = new CaseAwareMap();
    }
    
    public void testSetCaseSensitive()
    {
        assertFalse(map.isCaseSensitive());
        map.setCaseSensitive(true);
        assertTrue(map.isCaseSensitive());
        map.put("test", "abc");
        map.put(new Integer(3), "abc");
        map.setCaseSensitive(true);
        assertEquals(0, map.size());
    }
    
    public void testSize()
    {
        assertEquals(0, map.size());
        map.put("test", "abc");
        map.put(new Integer(3), "abc");
        assertEquals(2, map.size());
        map.setCaseSensitive(true);
        map.put(new Integer(3), "abc");
        assertEquals(1, map.size());
    }
    
    public void testIsEmptyAndClear()
    {
        assertTrue(map.isEmpty());
        map.clear();
        assertTrue(map.isEmpty());
        map.put(new Integer(3), "abc");
        assertFalse(map.isEmpty());
        map.clear();
        assertTrue(map.isEmpty());
    }
    
    public void testKeySet()
    {
        Set set = map.keySet();
        assertTrue(set.isEmpty());
        map.put("Test", "1");
        map.put("XyZ", "2");
        map.put("TEST", "3");
        map.put(new Double(4), "4");
        set = map.keySet();
        assertEquals(3, set.size());
        assertTrue(set.contains("Test"));
        assertTrue(set.contains("XyZ"));
        assertTrue(set.contains(new Double(4)));
        map.setCaseSensitive(true);
        map.put("Test", "1");
        map.put("XyZ", "2");
        map.put("TEST", "3");
        map.put(new Double(4), "4");
        assertEquals(4, set.size());
        set = map.keySet();
        assertTrue(set.contains("Test"));
        assertTrue(set.contains("XyZ"));
        assertTrue(set.contains(new Double(4)));
        assertTrue(set.contains("TEST"));
    }
    
    public void testPut()
    {
        assertNull(map.put("Test", "abc"));
        assertEquals("abc", map.put("TEST", "def"));
        assertEquals("def", map.put("TesT", "ghi"));
        assertEquals("ghi", map.put("test", "jkl"));
        assertEquals(1, map.size());
        Set set = map.keySet();
        assertEquals(1, set.size());
        assertTrue(set.contains("Test"));
        map.setCaseSensitive(true);
        assertNull(map.put("Test", "abc"));
        assertNull(map.put("TEST", "def"));
        assertNull(map.put("TesT", "ghi"));
        assertNull(map.put("test", "jkl"));
        assertEquals(4, map.size());
    }
    
    public void testPutAll()
    {
        Map otherMap = new HashMap();
        otherMap.put("hello", "1");
        otherMap.put("heLLo", "2");
        otherMap.put(new Integer(3), "3");
        map.putAll(otherMap);
        assertEquals(2, map.size());
        map.setCaseSensitive(true);
        map.putAll(otherMap);
        assertEquals(3, map.size());
    }
    
    public void testGet()
    {
        Object object = new Object();
        map.put("Test", object);
        map.put(new Integer(1), "123");
        assertSame(object, map.get("test"));
        assertSame(object, map.get("Test"));
        assertSame(object, map.get("TEST"));
        assertEquals("123", map.get(new Integer(1)));
        map.setCaseSensitive(true);
        map.put("Test", object);
        assertNull(map.get("test"));
        assertNull(map.get("TEST"));
        assertSame(object, map.get("Test"));
    }
    
    public void testValues()
    {
        map.put("Test", "1");
        map.put("TEST", "2");
        map.put("TeSt", "3");
        map.put(new Integer(1), "123");
        assertEquals(2, map.values().size());
        assertTrue(map.values().contains("3"));
        assertTrue(map.values().contains("123"));
        map.setCaseSensitive(true);
        map.put("Test", "1");
        map.put("TEST", "2");
        map.put("TeSt", "3");
        map.put(new Integer(1), "123");
        assertEquals(4, map.values().size());
        assertTrue(map.values().contains("1"));
        assertTrue(map.values().contains("2"));
        assertTrue(map.values().contains("3"));
        assertTrue(map.values().contains("123"));
    }
    
    public void testContainsKey()
    {
        Object key = new Object();
        map.put("Test", "1");
        map.put("TEST", "2");
        map.put("TeSt", "3");
        map.put(key, "4");
        assertTrue(map.containsKey("Test"));
        assertTrue(map.containsKey("TEST"));
        assertTrue(map.containsKey("TeSt"));
        assertTrue(map.containsKey("tEsT"));
        assertFalse(map.containsKey("Test1"));
        assertTrue(map.containsKey(key));
        map.setCaseSensitive(true);
        map.put("Test", "1");
        map.put("TEST", "2");
        map.put("TeSt", "3");
        map.put(key, "4");
        assertTrue(map.containsKey("Test"));
        assertTrue(map.containsKey("TEST"));
        assertTrue(map.containsKey("TeSt"));
        assertFalse(map.containsKey("tEsT"));
        assertFalse(map.containsKey("Test1"));
        assertTrue(map.containsKey(key));
    }
    
    public void testContainsValue()
    {
        Object key = new Object();
        map.put("Test", "1");
        map.put("TEST", "2");
        map.put("TeSt", "3");
        map.put(key, "4");
        assertFalse(map.containsValue("1"));
        assertFalse(map.containsValue("2"));
        assertTrue(map.containsValue("3"));
        assertTrue(map.containsValue("4"));
        map.setCaseSensitive(true);
        map.put("Test", "1");
        map.put("TEST", "2");
        map.put("TeSt", "3");
        map.put(key, "4");
        assertTrue(map.containsValue("1"));
        assertTrue(map.containsValue("2"));
        assertTrue(map.containsValue("3"));
        assertTrue(map.containsValue("4"));
    }
    
    public void testEntrySet()
    {
        assertEquals(0, map.entrySet().size());
        map.put("Test", "1");
        map.put("TEST", "2");
        map.put("TeSt", "3");
        assertEquals(1, map.entrySet().size());
        Map.Entry entry = (Map.Entry)map.entrySet().iterator().next();
        assertEquals("Test", entry.getKey());
        assertEquals("3", entry.getValue());
        map.setCaseSensitive(true);
        map.put("Test", "1");
        map.put("TEST", "2");
        map.put("TeSt", "3");
        assertEquals(3, map.entrySet().size());
    }
    
    public void testRemove()
    {
        map.put("Test", "0");
        map.put("test", "1");
        map.put(new Integer(2), "2");
        assertEquals(2, map.size());
        assertEquals("1", map.remove("TeST"));
        assertNull(map.remove("TEST"));
        assertEquals(1, map.size());
        assertEquals("2", map.remove(new Integer(2)));
        assertEquals(0, map.size());
        map.setCaseSensitive(true);
        map.put("Test", "1");
        map.put(new Integer(2), "2");
        assertNull(map.remove("TEST"));
        assertEquals("1", map.remove("Test"));
        assertNull(map.remove("Test"));
        assertEquals(1, map.size());
        assertEquals("2", map.remove(new Integer(2)));
        assertNull(map.remove(new Integer(2)));
    }
}