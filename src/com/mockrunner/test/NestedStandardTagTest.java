package com.mockrunner.test;

import java.util.HashMap;
import java.util.Map;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.MockPageContext;
import com.mockrunner.tag.NestedStandardTag;
import com.mockrunner.tag.NestedTag;

public class NestedStandardTagTest extends BaseTestCase
{
    private NestedStandardTag nestedTagRoot;
    private MockPageContext context;
    private TestTag testTag;
    private TestTag testTag1;
    private TestTag testTag11;
    private NestedTag testTagChild1;
    private NestedTag testTagChild11;
    private Map testMap;
    
    public NestedStandardTagTest(String arg0)
    {
        super(arg0);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        testMap = new HashMap();
        testMap.put("testString", "test");
        context = getMockObjectFactory().getMockPageContext();
        testTag = new TestTag();
        nestedTagRoot = new NestedStandardTag(testTag, context, testMap);
        testTagChild1 = nestedTagRoot.addTagChild(TestTag.class, testMap);
        nestedTagRoot.addTextChild("test");
        testTagChild11 = testTagChild1.addTagChild(TestTag.class, testMap);
        testTag1 = (TestTag)testTagChild1.getTag();
        testTag11 = (TestTag)testTagChild11.getTag();
    }
    
    public void testPopulateAttributes()
    {
        nestedTagRoot.populateAttributes();
        assertEquals("test", testTag.getTestString());
        assertNull(testTag1.getTestString());
        assertNull(testTag11.getTestString());
    }

    public void testSetPageContext()
    {
        MockPageContext newContext = new MockPageContext(null, null, null);
        nestedTagRoot.setPageContext(newContext);
        assertTrue(testTag.getPageContext() == newContext);
        assertTrue(testTag1.getPageContext() == newContext);
        assertTrue(testTag11.getPageContext() == newContext);
    }
}
