package com.mockrunner.test.web;

import junit.framework.TestCase;

import com.mockrunner.mock.web.MockJspConfigDescriptor;

public class MockJspConfigDescriptorTest extends TestCase
{
    private MockJspConfigDescriptor descriptor;
    
    protected void setUp() throws Exception
    {
        descriptor = new MockJspConfigDescriptor();
    }

    protected void tearDown() throws Exception
    {
        descriptor = null;
    }
    
    /*public void testReset()
    {
        descriptor.addJspPropertyGroup(new MockJspPropertyGroupDescriptor());
        descriptor.addTaglib(new MockTaglibDescriptor());
        descriptor.reset();
        assertTrue(descriptor.getJspPropertyGroups().isEmpty());
        assertTrue(descriptor.getTaglibs().isEmpty());
    }
    
    public void testAddJspPropertyGroupsAndTaglibs()
    {
        assertTrue(descriptor.getJspPropertyGroups().isEmpty());
        assertTrue(descriptor.getTaglibs().isEmpty());
        MockJspPropertyGroupDescriptor propertyGroup1 = new MockJspPropertyGroupDescriptor();
        MockJspPropertyGroupDescriptor propertyGroup2 = new MockJspPropertyGroupDescriptor();
        MockTaglibDescriptor taglib1 = new MockTaglibDescriptor();
        MockTaglibDescriptor taglib2 = new MockTaglibDescriptor();
        MockTaglibDescriptor taglib3 = new MockTaglibDescriptor();
        descriptor.addJspPropertyGroup(propertyGroup1);
        descriptor.addJspPropertyGroup(propertyGroup2);
        descriptor.addTaglib(taglib1);
        descriptor.addTaglib(taglib2);
        descriptor.addTaglib(taglib3);
        assertEquals(2, descriptor.getJspPropertyGroups().size());
        assertEquals(3, descriptor.getTaglibs().size());
        Iterator propertyGroups = descriptor.getJspPropertyGroups().iterator();
        Iterator taglibs = descriptor.getTaglibs().iterator();
        assertSame(propertyGroup1, propertyGroups.next());
        assertSame(propertyGroup2, propertyGroups.next());
        assertSame(taglib1, taglibs.next());
        assertSame(taglib2, taglibs.next());
        assertSame(taglib3, taglibs.next());
        descriptor.clearJspPropertyGroups();
        descriptor.clearTaglibs();
        assertTrue(descriptor.getJspPropertyGroups().isEmpty());
        assertTrue(descriptor.getTaglibs().isEmpty());
    }
    
    public void testJspPropertyGroupsAndTaglibsListChange()
    {
        MockJspPropertyGroupDescriptor propertyGroup = new MockJspPropertyGroupDescriptor();
        MockTaglibDescriptor taglib = new MockTaglibDescriptor();
        descriptor.addJspPropertyGroup(propertyGroup);
        descriptor.addTaglib(taglib);
        assertEquals(1, descriptor.getJspPropertyGroups().size());
        assertEquals(1, descriptor.getTaglibs().size());
        descriptor.getJspPropertyGroups().add(new MockJspPropertyGroupDescriptor());
        descriptor.getJspPropertyGroups().add(new MockTaglibDescriptor());
        assertEquals(1, descriptor.getJspPropertyGroups().size());
        assertEquals(1, descriptor.getTaglibs().size());
        Iterator propertyGroups = descriptor.getJspPropertyGroups().iterator();
        Iterator taglibs = descriptor.getTaglibs().iterator();
        assertSame(propertyGroup, propertyGroups.next());
        assertSame(taglib, taglibs.next());
    }*/
}
