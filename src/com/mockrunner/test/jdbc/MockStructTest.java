package com.mockrunner.test.jdbc;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.mockrunner.mock.jdbc.MockStruct;

public class MockStructTest extends TestCase
{
    private MockStruct prepareTestStruct()
    {
        MockStruct struct = new MockStruct("teststruct");
        struct.addAttribute("myAttribute1");
        struct.addAttributes(new Object[] {new Integer(2), "anAttribute3"});
        List list = new ArrayList();
        list.add("myAttribute4");
        struct.addAttributes(list);
        return struct;
    }
    
    public void testAttributes() throws Exception
    {
        doTestAttributes(prepareTestStruct());
    }
    
    public void testToString() throws Exception
    {
        MockStruct struct = prepareTestStruct();
        String structString = struct.toString();
        assertContains(structString, "myAttribute1");
        assertContains(structString, "2");
        assertContains(structString, "anAttribute3");
        assertContains(structString, "myAttribute4");
    }
    
    public void testClone() throws Exception
    {
        MockStruct struct = prepareTestStruct();
        MockStruct copyStruct = (MockStruct)struct.clone();
        assertNotSame(struct, copyStruct);
        doTestAttributes(copyStruct);
    }
    
    public void doTestAttributes(MockStruct struct) throws Exception
    {
        assertEquals("teststruct", struct.getSQLTypeName());
        Object[] attributes = struct.getAttributes();
        assertEquals("myAttribute1", attributes[0]);
        assertEquals(new Integer(2), attributes[1]);
        assertEquals("anAttribute3", attributes[2]);
        assertEquals("myAttribute4", attributes[3]);
    }
    
    private void assertContains(String structString, String expected)
    {
        assertTrue(-1 != structString.indexOf(expected));
    }
}
