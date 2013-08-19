package com.mockrunner.test.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.mockrunner.util.common.CollectionUtil;

public class CollectionUtilTest extends TestCase
{
    public void testFillList()
    {
        ArrayList testList = new ArrayList();
        testList.add("1");
        testList.add("2");
        testList.add("3");
        CollectionUtil.fillList(testList, 5, "4");
        assertTrue(testList.size() == 5);
        assertEquals("4", testList.get(3));
        assertEquals("4", testList.get(4));
        CollectionUtil.fillList(testList, 10);
        assertTrue(testList.size() == 10);
        for(int ii = 5; ii < 10; ii++)
        {
            assertNull(testList.get(ii));
        }
    }
    
    public void testTruncateList()
    {
        ArrayList list = new ArrayList();
        for(int ii = 0; ii < 100; ii++)
        {
            list.add("Test" + ii);
        }
        List truncatedList = CollectionUtil.truncateList(list, 50);
        assertTrue(truncatedList.size() == 50);
        for(int ii = 0; ii < 50; ii++)
        {
            assertEquals("Test" + ii, truncatedList.get(ii));
        }
    }
}
