package com.mockrunner.test.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mockrunner.jdbc.SQLStatementMatcher;

import junit.framework.TestCase;

public class SQLStatementMatcherTest extends TestCase
{
	public void testGetMatchingObjects()
	{
		Map<String, String> testMap = new HashMap<String, String>();
		testMap.put("Test1", "TestObject1");
		testMap.put("Test2", "TestObject2");
//		List testList = new ArrayList();
//		testList.add("TestList1");
//		testList.add("TestList2");
//		testList.add("TestList3");
		SQLStatementMatcher<String> matcher = new SQLStatementMatcher<String>(true, false);
		List<String> resultList = matcher.getMatchingObjects(testMap, "Test", false);
		assertEquals(2, resultList.size());
		assertTrue(resultList.contains("TestObject1"));
		assertTrue(resultList.contains("TestObject2"));
	}
	
	public void testGetMatchingObjectsRegEx()
	{
		Map<String, String> testMap = new HashMap<String, String>();
		testMap.put("Test1", "TestObject1");
		testMap.put("Test2", "TestObject2");
		SQLStatementMatcher<String> matcher = new SQLStatementMatcher<String>(true, false, true);
		List<String> resultList = matcher.getMatchingObjects(testMap, "Test.*", false);
		assertEquals(2, resultList.size());
		assertTrue(resultList.contains("TestObject1"));
		assertTrue(resultList.contains("TestObject2"));
		resultList = matcher.getMatchingObjects(testMap, "test.*", false);
		assertEquals(0, resultList.size());
	}
    
    public void testGetMatchingObjectsResolveCollection()
    {
        Map<String, List<String>> testMap = new HashMap<String, List<String>>();
//        testMap.put("Test1", "TestObject1");
//        testMap.put("Test2", "TestObject2");
//        testMap.put("Test3", "TestObject3");
//        testMap.put("Test", "TestObject");
        SQLStatementMatcher<String> matcher;
        List<String> resultList;
//        SQLStatementMatcher<String> matcher = new SQLStatementMatcher<String>(true, true);
//        List<String> resultList = matcher.getMatchingObjectsFromCollections(testMap, "Test", false);
//        assertTrue(resultList.size() == 1);
//        assertTrue(resultList.contains("TestObject"));
//        assertFalse(resultList.contains("TestObject1"));
//        assertFalse(resultList.contains("TestObject2"));
//        assertFalse(resultList.contains("TestObject3"));
//        matcher = new SQLStatementMatcher<String>(true, false);
//        resultList = matcher.getMatchingObjectsFromCollections(testMap, "Test", false);
//        assertTrue(resultList.size() == 4);
//        assertTrue(resultList.contains("TestObject"));
//        assertTrue(resultList.contains("TestObject1"));
//        assertTrue(resultList.contains("TestObject2"));
//        assertTrue(resultList.contains("TestObject3"));
//        matcher = new SQLStatementMatcher<String>(true, false);
//        resultList = matcher.getMatchingObjectsFromCollections(testMap, "test", false);
//        assertTrue(resultList.isEmpty());
//        matcher = new SQLStatementMatcher<String>(false, false);
//        resultList = matcher.getMatchingObjectsFromCollections(testMap, "test", false);
//        assertTrue(resultList.size() == 4);
//        assertTrue(resultList.contains("TestObject"));
//        assertTrue(resultList.contains("TestObject1"));
//        assertTrue(resultList.contains("TestObject2"));
//        assertTrue(resultList.contains("TestObject3"));
        List<String> testList = new ArrayList<String>();
        testList.add("TestList1");
        testList.add("TestList2");
        testList.add("TestList3");
        testMap.put("Test4", testList);
        matcher = new SQLStatementMatcher<String>(true, false);
        resultList = matcher.getMatchingObjectsFromCollections(testMap, "", false);
        assertTrue(resultList.size() == 3);
//        assertTrue(resultList.contains("TestObject"));
//        assertTrue(resultList.contains("TestObject1"));
//        assertTrue(resultList.contains("TestObject2"));
//        assertTrue(resultList.contains("TestObject3"));
        assertTrue(resultList.contains("TestList1"));
        assertTrue(resultList.contains("TestList2"));
        assertTrue(resultList.contains("TestList3"));
        matcher = new SQLStatementMatcher<String>(true, false);
        resultList = matcher.getMatchingObjectsFromCollections(testMap, "", true);
        assertTrue(resultList.isEmpty());
        matcher = new SQLStatementMatcher<String>(true, false);
        resultList = matcher.getMatchingObjectsFromCollections(testMap, "TestObjectObject", true);
        assertTrue(resultList.isEmpty());
//        assertTrue(resultList.contains("TestObject"));
    }
    
    public void testGetMatchingObjectsResolveCollectionRegEx()
    {
        Map<String, List<String>> testMap = new HashMap<String, List<String>>();
		List<String> testList = new ArrayList<String>();
		testList.add("TestList1");
		testList.add("TestList2");
		testList.add("TestList3");
		testMap.put("Test3", testList);
		SQLStatementMatcher<String> matcher = new SQLStatementMatcher<String>(true, false, true);
		List<String> resultList = matcher.getMatchingObjectsFromCollections(testMap, "[T].*", false);
		assertEquals(3, resultList.size());
    }
    
    public void testContains()
    {
        ArrayList<String> list = new ArrayList<String>();
        list.add("TestString1");
        list.add("TestString2");
        list.add("TestString3");
        SQLStatementMatcher<String> matcher = new SQLStatementMatcher<String>(false, false);
        assertTrue(matcher.contains(list, "TESTSTRING", false));
        matcher = new SQLStatementMatcher<String>(true, false);
        assertFalse(matcher.contains(list, "TESTSTRING", false));
        matcher = new SQLStatementMatcher<String>(false, true);
        assertFalse(matcher.contains(list, "TESTSTRING", false));
        matcher = new SQLStatementMatcher<String>(true, true);
        assertTrue(matcher.contains(list, "TestString3", false));
        matcher = new SQLStatementMatcher<String>(true, true);
        assertFalse(matcher.contains(list, "TestString4", false));
        matcher = new SQLStatementMatcher<String>(false, false);
        assertFalse(matcher.contains(list, "TestString4", false));
        matcher = new SQLStatementMatcher<String>(false, false);
        assertFalse(matcher.contains(list, "TESTSTRING1XYZ", false));
        matcher = new SQLStatementMatcher<String>(false, false);
        assertTrue(matcher.contains(list, "TESTSTRING1XYZ", true));
        matcher = new SQLStatementMatcher<String>(true, false);
        assertFalse(matcher.contains(list, "TEstString3Test", true));
        matcher = new SQLStatementMatcher<String>(false, false);
        assertTrue(matcher.contains(list, "TEstString3Test", true));
    }
    
    public void testContainsRegEx()
    {
        ArrayList<String> list = new ArrayList<String>();
        list.add("TestString1");
        list.add("TestString2");
        list.add("TestString3");
        SQLStatementMatcher<String> matcher = new SQLStatementMatcher<String>(false, false, true);
        assertTrue(matcher.contains(list, "TESTSTRING.", false));
        assertTrue(matcher.contains(list, ".*", false));
        assertFalse(matcher.contains(list, "...", false));
        list = new ArrayList<String>();
        list.add("TestString..");
        assertTrue(matcher.contains(list, "TESTSTRING11", true));
        assertFalse(matcher.contains(list, "TESTSTRING1", true));
    }
    
    public void testDoStringsMatch()
    {
        SQLStatementMatcher<String> matcher = new SQLStatementMatcher<String>(true, false);
        assertFalse(matcher.doStringsMatch("X", "x"));
        matcher = new SQLStatementMatcher<String>(false, true);
        assertTrue(matcher.doStringsMatch("X", "x"));
        matcher = new SQLStatementMatcher<String>(false, false);
        assertTrue(matcher.doStringsMatch("Test", "tes"));
        matcher = new SQLStatementMatcher<String>(true, false);
        assertFalse(matcher.doStringsMatch("Test", "tes"));
        matcher = new SQLStatementMatcher<String>(true, false);
        assertTrue(matcher.doStringsMatch("Test", ""));
        matcher = new SQLStatementMatcher<String>(true, false);
        assertTrue(matcher.doStringsMatch("Test", null));
        matcher = new SQLStatementMatcher<String>(false, true);
        assertFalse(matcher.doStringsMatch("Test", null));
        matcher = new SQLStatementMatcher<String>(true, true);
        assertTrue(matcher.doStringsMatch(null, null));
        matcher = new SQLStatementMatcher<String>(true, true);
        assertTrue(matcher.doStringsMatch("ThisIsATest", "ThisIsATest"));
    }
    
    public void testDoStringsMatchRegEx()
    {
        SQLStatementMatcher<String> matcher = new SQLStatementMatcher<String>(true, false, true);
        assertFalse(matcher.doStringsMatch("X", "x"));
        assertTrue(matcher.doStringsMatch("AbcDef", ".*"));
        assertFalse(matcher.doStringsMatch("AbcDef", "a.*"));
        matcher = new SQLStatementMatcher<String>(false, false, true);
        assertTrue(matcher.doStringsMatch("AbcDef", "a.*"));
        assertTrue(matcher.doStringsMatch("myTest", "[nmg]ytest"));
        assertFalse(matcher.doStringsMatch("dyTest", "[nmg]ytest"));
        matcher = new SQLStatementMatcher<String>(true, true, true);
        assertFalse(matcher.doStringsMatch("myTest", "[nmg]ytest"));
    }
}
