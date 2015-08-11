package com.mockrunner.test.jdbc;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.mockrunner.mock.jdbc.MockStruct;

public class MockStructTest extends TestCase
{
    private MockStruct prepareTestStruct()
    {
        MockStruct struct = new MockStruct("teststruct", new Object[] {1L, "anAttribute2"});
        struct.addAttribute("myAttribute3");
        struct.addAttributes(new Object[] {4, "anAttribute5"});
        List<Object> list = new ArrayList<Object>();
        list.add("myAttribute6");
        list.add("myAttribute7");
        struct.addAttributes(list);
        return struct;
    }
    
    public void testEquals() throws Exception
    {
        MockStruct nullStruct = new MockStruct((String)null);
        assertFalse(nullStruct.equals(null));
        assertTrue(nullStruct.equals(nullStruct));
        MockStruct struct = new MockStruct("test");
        assertFalse(struct.equals(nullStruct));
        assertFalse(nullStruct.equals(struct));
        MockStruct other = new MockStruct("test");
        assertTrue(struct.equals(other));
        assertTrue(other.equals(struct));
        assertEquals(struct.hashCode(), other.hashCode());
        other = new MockStruct("test") {};
        assertFalse(other.equals(struct));
        assertFalse(struct.equals(other));
        other = new MockStruct("test");
        other.addAttribute("myAttribute1");
        assertFalse(other.equals(struct));
        assertFalse(struct.equals(other));
        struct.addAttribute("myAttribute1");
        assertTrue(struct.equals(other));
        assertTrue(other.equals(struct));
        assertEquals(struct.hashCode(), other.hashCode());
        other.addAttributes(new String[] {"1", "2"});
        assertFalse(other.equals(struct));
        assertFalse(struct.equals(other));
        struct.addAttributes(new String[] {"1", "2"});
        assertTrue(struct.equals(other));
        assertTrue(other.equals(struct));
        assertEquals(struct.hashCode(), other.hashCode());
    }
    
    public void testAttributes() throws Exception
    {
        doTestAttributes(prepareTestStruct());
    }
    
    public void testToString() throws Exception
    {
        MockStruct struct = prepareTestStruct();
        assertEquals("Struct data: [1, anAttribute2, myAttribute3, 4, anAttribute5, myAttribute6, myAttribute7]", struct.toString());
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
        assertEquals(1L, attributes[0]);
        assertEquals("anAttribute2", attributes[1]);
        assertEquals("myAttribute3", attributes[2]);
        assertEquals(4, attributes[3]);
        assertEquals("anAttribute5", attributes[4]);
        assertEquals("myAttribute6", attributes[5]);
        assertEquals("myAttribute7", attributes[6]);
    }
}
