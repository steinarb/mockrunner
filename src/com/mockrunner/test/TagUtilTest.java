package com.mockrunner.test;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.tagext.TagSupport;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.web.MockPageContext;
import com.mockrunner.tag.NestedBodyTag;
import com.mockrunner.tag.NestedStandardTag;
import com.mockrunner.tag.NestedTag;
import com.mockrunner.util.TagUtil;

public class TagUtilTest extends BaseTestCase
{
    private MockPageContext pageContext;
    private Map testMap;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        pageContext = getMockObjectFactory().getMockPageContext();
        testMap = new HashMap();
    }

    public void testCreateNestedTagInstance()
    {
        TagSupport tag = TagUtil.createNestedTagInstance(TestTag.class, pageContext, testMap);
        assertTrue(tag instanceof NestedStandardTag);
        assertTrue(((NestedTag)tag).getTag() instanceof TestTag);
        tag = TagUtil.createNestedTagInstance(TestBodyTag.class, pageContext, testMap);
        assertTrue(tag instanceof NestedBodyTag);
        assertTrue(((NestedTag)tag).getTag() instanceof TestBodyTag);
    }
    
    public void testPopulateTag()
    {
        testMap.put("testString", "test");
        testMap.put("testDouble", new Double(12.3));
        testMap.put("testInteger", new Integer(100));
        TestTag tag = new TestTag();
        TagUtil.populateTag(tag, testMap, false);
        assertEquals("test", tag.getTestString());
        assertEquals(12.3, tag.getTestDouble(), 0.00);
        assertEquals(new Integer(100), tag.getTestInteger());
        assertFalse(tag.wasReleaseCalled());
        testMap.put("testInteger", "3");
        testMap.put("noValidProperty", "123");
        testMap.put("testDouble", "notValid");
        TagUtil.populateTag(tag, testMap, true);
        assertTrue(tag.wasReleaseCalled());
        assertEquals(new Integer(3), tag.getTestInteger());
        assertEquals(0.0, tag.getTestDouble(), 0.00);
    }
}
