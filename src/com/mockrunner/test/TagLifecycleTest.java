package com.mockrunner.test;

import java.util.HashMap;
import java.util.Map;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.tag.NestedStandardTag;
import com.mockrunner.tag.NestedTag;

public class TagLifecycleTest extends BaseTestCase
{
    private NestedTag root;
    private NestedTag level1child1;
    private NestedTag level1child2;
    private NestedTag level1child3;
    private NestedTag level2child1;
    private NestedTag level2child2;
    private NestedTag level3child1;
    private NestedTag level3child2;
    
    public TagLifecycleTest(String arg0)
    {
        super(arg0);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        Map testMap = new HashMap();
        testMap.put("testString", "test");
        root = new NestedStandardTag(new TestTag(), getMockObjectFactory().getMockPageContext(), testMap);
        level1child1 = root.addTagChild(TestBodyTag.class, testMap);
        root.addTextChild("level1text");
        level1child2 = root.addTagChild(TestTag.class, testMap);
        level1child3 = root.addTagChild(TestBodyTag.class, testMap);
        level2child1 = level1child2.addTagChild(TestTag.class, testMap);
        level2child2 = level1child2.addTagChild(TestBodyTag.class, testMap);
        level1child2.addTextChild("level2text");
        level3child1 = level2child1.addTagChild(TestTag.class, testMap);
        level3child2 = level2child2.addTagChild(TestTag.class, testMap);
    }
}
