package com.mockrunner.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mockrunner.util.SearchUtil;

import junit.framework.TestCase;

public class SearchUtilTest extends TestCase
{
    public void testGetMatchingObjects()
    {
        Map testMap = new HashMap();
        testMap.put("Test1", "TestObject1");
        testMap.put("Test2", "TestObject2");
        testMap.put("Test3", "TestObject3");
        testMap.put("Test", "TestObject");
        List resultList = SearchUtil.getMatchingObjects(testMap, "Test", true, true);
        assertTrue(resultList.contains("TestObject"));
        assertFalse(resultList.contains("TestObject1"));
        assertFalse(resultList.contains("TestObject2"));
        assertFalse(resultList.contains("TestObject3"));
        resultList = SearchUtil.getMatchingObjects(testMap, "Test", true, false);
        assertTrue(resultList.contains("TestObject"));
        assertTrue(resultList.contains("TestObject1"));
        assertTrue(resultList.contains("TestObject2"));
        assertTrue(resultList.contains("TestObject3"));
        resultList = SearchUtil.getMatchingObjects(testMap, "test", true, false);
        assertTrue(resultList.isEmpty());
        resultList = SearchUtil.getMatchingObjects(testMap, "test", false, false);
        assertTrue(resultList.contains("TestObject"));
        assertTrue(resultList.contains("TestObject1"));
        assertTrue(resultList.contains("TestObject2"));
        assertTrue(resultList.contains("TestObject3"));
        List testList = new ArrayList();
        testList.add("TestList1");
        testList.add("TestList2");
        testList.add("TestList3");
        testMap.put("Test4", testList);
        resultList = SearchUtil.getMatchingObjects(testMap, "", true, false);
        assertTrue(resultList.contains("TestObject"));
        assertTrue(resultList.contains("TestObject1"));
        assertTrue(resultList.contains("TestObject2"));
        assertTrue(resultList.contains("TestObject3"));
        assertTrue(resultList.contains("TestList1"));
        assertTrue(resultList.contains("TestList2"));
        assertTrue(resultList.contains("TestList3"));
    }
    
    public void testDoesStringMatch()
    {
        assertFalse(SearchUtil.doesStringMatch("X", "x", true, false));
        assertTrue(SearchUtil.doesStringMatch("X", "x", false, true));
        assertTrue(SearchUtil.doesStringMatch("Test", "tes", false, false));
        assertFalse(SearchUtil.doesStringMatch("Test", "tes", true, false));
        assertTrue(SearchUtil.doesStringMatch("Test", "", true, false));
        assertTrue(SearchUtil.doesStringMatch("Test", null, true, false));
        assertFalse(SearchUtil.doesStringMatch("Test", null, false, true));
        assertTrue(SearchUtil.doesStringMatch(null, null, true, true));
        assertTrue(SearchUtil.doesStringMatch("ThisIsATest", "ThisIsATest", true, true));
    }
}
