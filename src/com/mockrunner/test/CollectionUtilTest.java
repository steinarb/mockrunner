package com.mockrunner.test;

import java.util.ArrayList;

import com.mockrunner.util.CollectionUtil;

import junit.framework.TestCase;

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
}
