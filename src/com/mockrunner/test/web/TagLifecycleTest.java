package com.mockrunner.test.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.web.MockBodyContent;
import com.mockrunner.mock.web.MockJspWriter;
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
    
    private TestTag rootTag;
    private TestBodyTag level1child1Tag;
    private TestTag level1child2Tag;
    private TestBodyTag level1child3Tag;
    private TestTag level2child1Tag;
    private TestBodyTag level2child2Tag;
    private TestTag level3child1Tag;
    private TestTag level3child2Tag;

    protected void setUp() throws Exception
    {
        super.setUp();
        Map testMap = new HashMap();
        testMap.put("testString", "test");
        root = new NestedStandardTag(new TestTag(), getMockObjectFactory().getMockPageContext(), testMap);
        level1child1 = root.addTagChild(TestBodyTag.class, testMap);
        root.addTextChild("roottext");
        level1child2 = root.addTagChild(TestTag.class, testMap);
        level1child3 = root.addTagChild(TestBodyTag.class, testMap);
        level1child3.addTextChild("level1textchild3");
        level2child1 = level1child2.addTagChild(TestTag.class, testMap);
        level2child2 = level1child2.addTagChild(TestBodyTag.class, testMap);
        level1child2.addTextChild("level2textchild2");
        level3child1 = level2child1.addTagChild(TestTag.class, testMap);
        level3child2 = level2child2.addTagChild(TestTag.class, testMap);
        
        rootTag = (TestTag)root.getTag();
        level1child1Tag = (TestBodyTag)level1child1.getTag();
        level1child2Tag = (TestTag)level1child2.getTag();
        level1child3Tag = (TestBodyTag)level1child3.getTag();
        level2child1Tag = (TestTag)level2child1.getTag();
        level2child2Tag = (TestBodyTag)level2child2.getTag();
        level3child1Tag = (TestTag)level3child1.getTag();
        level3child2Tag = (TestTag)level3child2.getTag();
    }
    
    public void testMethodsCalled() throws Exception
    {
        level1child1Tag.setDoStartTagReturnValue(TagSupport.EVAL_BODY_INCLUDE);
        level1child2Tag.setDoStartTagReturnValue(TagSupport.SKIP_BODY);
        level1child3Tag.setDoStartTagReturnValue(BodyTagSupport.EVAL_BODY_BUFFERED);
        root.doLifecycle();
        assertTrue(rootTag.wasDoStartTagCalled());
        assertTrue(rootTag.wasDoAfterBodyCalled());
        assertTrue(rootTag.wasDoEndTagCalled());
  
        assertTrue(level1child1Tag.wasDoStartTagCalled());
        assertFalse(level1child1Tag.wasDoInitBodyCalled());
        assertTrue(level1child1Tag.wasDoAfterBodyCalled());
        assertTrue(level1child1Tag.wasDoEndTagCalled());
        
        assertTrue(level1child2Tag.wasDoStartTagCalled());
        assertFalse(level1child2Tag.wasDoAfterBodyCalled());
        assertTrue(level1child2Tag.wasDoEndTagCalled());
        
        assertTrue(level1child3Tag.wasDoStartTagCalled());
        assertTrue(level1child3Tag.wasDoInitBodyCalled());
        assertTrue(level1child3Tag.wasDoAfterBodyCalled());
        assertTrue(level1child3Tag.wasDoEndTagCalled());
        
        assertFalse(level2child1Tag.wasDoStartTagCalled());
        assertFalse(level2child1Tag.wasDoAfterBodyCalled());
        assertFalse(level2child1Tag.wasDoEndTagCalled());
        
        assertFalse(level2child2Tag.wasDoStartTagCalled());
        assertFalse(level2child2Tag.wasDoInitBodyCalled());
        assertFalse(level2child2Tag.wasDoAfterBodyCalled());
        assertFalse(level2child2Tag.wasDoEndTagCalled());
        
        assertFalse(level3child1Tag.wasDoStartTagCalled());
        assertFalse(level3child1Tag.wasDoAfterBodyCalled());
        assertFalse(level3child1Tag.wasDoEndTagCalled());
        
        assertFalse(level3child2Tag.wasDoStartTagCalled());
        assertFalse(level3child2Tag.wasDoAfterBodyCalled());
        assertFalse(level3child2Tag.wasDoEndTagCalled());
    }
    
    public void testPopulated() throws Exception
    {
        level1child1Tag.setDoStartTagReturnValue(TagSupport.EVAL_BODY_INCLUDE);
        level1child2Tag.setDoStartTagReturnValue(TagSupport.SKIP_BODY);
        level1child3Tag.setDoStartTagReturnValue(BodyTagSupport.EVAL_BODY_BUFFERED);
        root.doLifecycle();
        assertEquals("test", rootTag.getTestString());
        assertEquals("test", level1child1Tag.getTestString());
        assertEquals("test", level1child2Tag.getTestString());
        assertEquals("test", level1child2Tag.getTestString());
        assertNull(level2child1Tag.getTestString());
        assertNull(level2child2Tag.getTestString());
        assertNull(level3child1Tag.getTestString());
        assertNull(level3child2Tag.getTestString());
    }
    
    public void testOutput() throws Exception
    {
        level1child1Tag.setDoStartTagReturnValue(TagSupport.SKIP_BODY);
        level1child2Tag.setDoStartTagReturnValue(TagSupport.SKIP_BODY);
        level1child3Tag.setDoStartTagReturnValue(BodyTagSupport.EVAL_BODY_BUFFERED);
        root.doLifecycle();
        assertEquals("TestTagTestBodyTagroottextTestTagTestBodyTag", getTagOutput());
        assertEquals("level1textchild3", ((MockBodyContent)level1child3Tag.getBufferedOut()).getOutputAsString());
        clearOutput();
        level1child3Tag.setDoStartTagReturnValue(BodyTagSupport.EVAL_BODY_INCLUDE);
        root.doLifecycle();
        assertEquals("TestTagTestBodyTagroottextTestTagTestBodyTaglevel1textchild3", getTagOutput());
        clearOutput();
        level1child3Tag.setDoAfterBodyReturnValue(BodyTagSupport.EVAL_BODY_AGAIN);
        root.doLifecycle();
        assertEquals("TestTagTestBodyTagroottextTestTagTestBodyTaglevel1textchild3level1textchild3", getTagOutput());
    }
    
    private String getTagOutput()
    {
        return ((MockJspWriter)(getMockObjectFactory().getMockPageContext().getOut())).getOutputAsString();
    }
    
    private void clearOutput() throws Exception
    {
        ((MockJspWriter)(getMockObjectFactory().getMockPageContext().getOut())).clearBuffer();
    }
}
