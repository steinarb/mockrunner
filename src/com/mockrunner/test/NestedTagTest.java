package com.mockrunner.test;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.MockPageContext;
import com.mockrunner.tag.NestedBodyTag;
import com.mockrunner.tag.NestedStandardTag;
import com.mockrunner.tag.NestedTag;

public class NestedTagTest extends BaseTestCase
{
    private NestedTag nestedTagRoot;
    private MockPageContext context;
    private TagSupport testTag;
    private TagSupport testTag1;
    private TagSupport testTag11;
    private NestedTag testTagChild1;
    private NestedTag testTagChild11;
    private Map testMap;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        testMap = new HashMap();
        testMap.put("testString", "test");
        context = getMockObjectFactory().getMockPageContext();
    }

    private void prepareStandardTagTest()
    {
        testTag = new TestTag();
        nestedTagRoot = new NestedStandardTag(testTag, context, testMap);
        testTagChild1 = nestedTagRoot.addTagChild(TestTag.class, testMap);
        nestedTagRoot.addTextChild("test");
        testTagChild11 = testTagChild1.addTagChild(TestTag.class, testMap);
        testTag1 = (TestTag)testTagChild1.getTag();
        testTag11 = (TestTag)testTagChild11.getTag();
    }
    
    private void prepareBodyTagTest()
    {
        testTag = new TestBodyTag();
        nestedTagRoot = new NestedBodyTag((BodyTagSupport)testTag, context, testMap);
        testTagChild1 = nestedTagRoot.addTagChild(TestTag.class, testMap);
        testTagChild11 = testTagChild1.addTagChild(TestBodyTag.class, testMap);
        testTagChild1.addTextChild("bodytest");
        testTag1 = (TestTag)testTagChild1.getTag();
        testTag11 = (TestBodyTag)testTagChild11.getTag();
    }
    
    public void testPopulateAttributesStandard()
    {
        prepareStandardTagTest();
        nestedTagRoot.populateAttributes();
        assertEquals("test", ((TestTag)testTag).getTestString());
        assertNull(((TestTag)testTag1).getTestString());
        assertNull(((TestTag)testTag11).getTestString());
    }
    
    public void testPopulateAttributesBody()
    {
        prepareBodyTagTest();
        nestedTagRoot.populateAttributes();
        assertEquals("test", ((TestBodyTag)testTag).getTestString());
        assertNull(((TestTag)testTag1).getTestString());
        assertNull(((TestBodyTag)testTag11).getTestString());
    }

    public void testSetPageContextStandard()
    {
        prepareStandardTagTest();
        MockPageContext newContext = new MockPageContext(null, null, null);
        ((NestedStandardTag)nestedTagRoot).setPageContext(newContext);
        assertTrue(((TestTag)testTag).getPageContext() == newContext);
        assertTrue(((TestTag)testTag1).getPageContext() == newContext);
        assertTrue(((TestTag)testTag11).getPageContext() == newContext);
    }
    
    public void testSetPageContextBody()
    {
        prepareBodyTagTest();
        MockPageContext newContext = new MockPageContext(null, null, null);
        ((NestedBodyTag)nestedTagRoot).setPageContext(newContext);
        assertTrue(((TestBodyTag)testTag).getPageContext() == newContext);
        assertTrue(((TestTag)testTag1).getPageContext() == newContext);
        assertTrue(((TestBodyTag)testTag11).getPageContext() == newContext);
    }
}
