package com.mockrunner.test.web;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.web.MockPageContext;
import com.mockrunner.tag.DynamicAttribute;
import com.mockrunner.tag.NestedBodyTag;
import com.mockrunner.tag.NestedSimpleTag;
import com.mockrunner.tag.NestedStandardTag;
import com.mockrunner.tag.NestedTag;

public class NestedTagTest extends BaseTestCase
{
    private NestedTag nestedTagRoot;
    private MockPageContext context;
    private Tag testTag;
    private Tag testTag1;
    private Tag testTag11;
    private SimpleTag testTag111;
    private NestedTag testTagChild1;
    private NestedTag testTagChild11;
    private NestedTag testTagChild111;
    private SimpleTag rootSimpleTag;
    private Map testMap;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        testMap = new HashMap();
        testMap.put("testString", "test");
        context = getWebMockObjectFactory().getMockPageContext();
    }

    private void prepareStandardTagTest()
    {
        testTag = new TestTag();
        nestedTagRoot = new NestedStandardTag(testTag, context, testMap);
        testTagChild1 = nestedTagRoot.addTagChild(new TestTag(), testMap);
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
        testTagChild11 = testTagChild1.addTagChild(new TestBodyTag(), testMap);
        testTagChild1.addTextChild("bodytest");
        testTag1 = (TestTag)testTagChild1.getTag();
        testTag11 = (TestBodyTag)testTagChild11.getTag();
    }
    
    private void prepareSimpleTagTest()
    {
        Map map = new HashMap();
        map.put("stringProperty", "test");
        map.put("floatProperty", "1");
        rootSimpleTag = new TestSimpleTag();
        nestedTagRoot = new NestedSimpleTag(rootSimpleTag, context, map);
        testTagChild1 = nestedTagRoot.addTagChild(TestTag.class, testMap);
        testTagChild11 = testTagChild1.addTagChild(new TestBodyTag(), testMap);
        testTagChild1.addTextChild("simpletest");
        testTag1 = (TestTag)testTagChild1.getTag();
        testTag11 = (TestBodyTag)testTagChild11.getTag();
        testTagChild111 = testTagChild11.addTagChild(TestSimpleTag.class);
        testTag111 = (SimpleTag)testTagChild111.getWrappedTag();
    }
    
    public void testGetWrappedTag()
    {
        BodyTag testBodyTag = new TestBodyTag();
        Tag testStandardTag = new TestTag();
        SimpleTag testSimpleTag = new TestSimpleTag();
        NestedBodyTag nestedBodyTag = new NestedBodyTag(testBodyTag, context);
        NestedStandardTag nestedStandardTag = new NestedStandardTag(testStandardTag, context);
        NestedSimpleTag nestedSimpleTag = new NestedSimpleTag(testSimpleTag, context);
        assertSame(testBodyTag, nestedBodyTag.getTag());
        assertSame(testBodyTag, nestedBodyTag.getWrappedTag());
        assertSame(testStandardTag, nestedStandardTag.getTag());
        assertSame(testStandardTag, nestedStandardTag.getWrappedTag());
        assertSame(testSimpleTag, nestedSimpleTag.getWrappedTag());
        try
        {
            nestedSimpleTag.getTag();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
    }
    
    public void testAddTagChild()
    {
        testTag = new TestBodyTag();
        nestedTagRoot = new NestedBodyTag((BodyTagSupport)testTag, context, testMap);
        TestTag childTestTag = new TestTag();
        nestedTagRoot.addTagChild(childTestTag);
        nestedTagRoot.addTagChild(childTestTag);
        nestedTagRoot.addTagChild(TestSimpleTag.class);
        nestedTagRoot.addTagChild(TestBodyTag.class);
        assertEquals(4, nestedTagRoot.getChilds().size());
        assertSame(childTestTag, ((NestedTag)nestedTagRoot.getChild(0)).getTag());
        assertSame(childTestTag, ((NestedTag)nestedTagRoot.getChild(1)).getTag());
        assertNotSame(childTestTag, ((NestedTag)nestedTagRoot.getChild(2)).getWrappedTag());
        assertNotSame(childTestTag, ((NestedBodyTag)nestedTagRoot.getChild(3)).getTag());
        NestedBodyTag bodyTag = (NestedBodyTag)nestedTagRoot.getChild(3);
        bodyTag.addTagChild(TestTag.class);
        bodyTag.addTagChild(new TestTag());
        bodyTag.addTagChild(TestBodyTag.class);
        bodyTag.addTagChild(new TestBodyTag());
        assertEquals(4, bodyTag.getChilds().size());
        assertTrue(((NestedTag)bodyTag.getChild(0)).getTag() instanceof TestTag);
        assertTrue(((NestedTag)bodyTag.getChild(1)).getTag() instanceof TestTag);
        assertTrue(((NestedTag)bodyTag.getChild(2)).getTag() instanceof TestBodyTag);
        assertTrue(((NestedTag)bodyTag.getChild(3)).getTag() instanceof TestBodyTag);
        NestedSimpleTag simpleTag = (NestedSimpleTag)nestedTagRoot.getChild(2);
        simpleTag.addTagChild(TestBodyTag.class);
        simpleTag.addTagChild(new TestSimpleTag());
        assertEquals(2, simpleTag.getChilds().size());
        assertTrue(((NestedTag)simpleTag.getChild(0)).getWrappedTag() instanceof TestBodyTag);
        assertTrue(((NestedTag)simpleTag.getChild(1)).getWrappedTag() instanceof TestSimpleTag);
    }
    
    public void testFindTag()
    {
        AnotherTestTag anotherTestTag = new AnotherTestTag();
        NestedStandardTag root = new NestedStandardTag(anotherTestTag, context);
        NestedTag child1 = root.addTagChild(TestTag.class, testMap);
        NestedTag child11 = child1.addTagChild(TestTag.class, testMap);
        Tag foundTag = TagSupport.findAncestorWithClass((Tag)child11.getWrappedTag(), AnotherTestTag.class);
        assertNotNull(foundTag);
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
    
    public void testPopulateAttributesSimple()
    {
        prepareSimpleTagTest();
        nestedTagRoot.populateAttributes();
        assertEquals("test", ((TestSimpleTag)rootSimpleTag).getStringProperty());
        assertEquals(1, ((TestSimpleTag)rootSimpleTag).getFloatProperty(), 0.0);
    }
    
    public void testPopulateDynamicAttributes()
    {
        Map map = new HashMap();
        map.put("dynamicAttribute1", "test");
        map.put("dynamicAttribute2", new Integer(1));
        TestSimpleTag simpleTag = new TestSimpleTag();
        NestedTag nestedTag = new NestedSimpleTag(simpleTag, context, map);
        nestedTag.populateAttributes();
        assertEquals(2, simpleTag.getDynamicAttributesMap().size());
        DynamicAttribute attribute1 = (DynamicAttribute)simpleTag.getDynamicAttributesMap().get("dynamicAttribute1");
        DynamicAttribute attribute2 = (DynamicAttribute)simpleTag.getDynamicAttributesMap().get("dynamicAttribute2");
        assertNull(attribute1.getUri());
        assertEquals("test", attribute1.getValue());
        assertNull(attribute2.getUri());
        assertEquals(new Integer(1), attribute2.getValue());
        TestBodyTag bodyTag = new TestBodyTag();
        nestedTag = new NestedBodyTag(bodyTag, context, map);
        nestedTag.populateAttributes();
        map.put("dynamicAttribute3", new DynamicAttribute("test", "test"));
        try
        {
            nestedTag.populateAttributes();
            fail();
        }
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
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
    
    public void testSetJspContextSimple()
    {
        prepareSimpleTagTest();
        MockPageContext newContext = new MockPageContext(null, null, null);
        ((NestedSimpleTag)nestedTagRoot).setJspContext(newContext);
        assertTrue(((TestSimpleTag)rootSimpleTag).getJspContext() == newContext);
        assertTrue(((TestTag)testTag1).getPageContext() == newContext);
        assertTrue(((TestBodyTag)testTag11).getPageContext() == newContext);
        assertTrue(((TestSimpleTag)testTag111).getJspContext() == newContext);
    }
    
    public void testSetDoReleaseStandard() throws Exception
    {
        prepareStandardTagTest();
        nestedTagRoot.setDoRelease(true);
        nestedTagRoot.doLifecycle();
        assertTrue(((TestTag)testTag).wasReleaseCalled());
        assertFalse(((TestTag)testTag1).wasReleaseCalled());
        assertFalse(((TestTag)testTag11).wasReleaseCalled());
        nestedTagRoot.setDoReleaseRecursive(true);
        nestedTagRoot.doLifecycle();
        assertTrue(((TestTag)testTag).wasReleaseCalled());
        assertTrue(((TestTag)testTag1).wasReleaseCalled());
        assertTrue(((TestTag)testTag11).wasReleaseCalled());
    }
    
    public void testSetDoReleaseBody() throws Exception
    {
        prepareBodyTagTest();
        nestedTagRoot.setDoRelease(true);
        nestedTagRoot.doLifecycle();
        assertTrue(((TestBodyTag)testTag).wasReleaseCalled());
        assertFalse(((TestTag)testTag1).wasReleaseCalled());
        assertFalse(((TestBodyTag)testTag11).wasReleaseCalled());
        nestedTagRoot.setDoReleaseRecursive(true);
        nestedTagRoot.doLifecycle();
        assertTrue(((TestBodyTag)testTag).wasReleaseCalled());
        assertTrue(((TestTag)testTag1).wasReleaseCalled());
        assertTrue(((TestBodyTag)testTag11).wasReleaseCalled());
    }
    
    public void testChildsWithCustomFragmentSimpleTag() throws Exception
    {
        TestSimpleTag testSimpleTag = new TestSimpleTag();
        NestedSimpleTag nestedSimpleTag = new NestedSimpleTag(testSimpleTag, context);
        nestedSimpleTag.setJspBody(new TestJspFragment());
        nestedSimpleTag.addTagChild(TestTag.class);
        nestedSimpleTag.addTextChild("text");
        assertNull(nestedSimpleTag.getChilds());
        assertNull(nestedSimpleTag.getChild(0));
        nestedSimpleTag.removeChilds();
        assertNull(nestedSimpleTag.getChilds());
        assertNull(nestedSimpleTag.getChild(0));
    }
    
    public void testNotTagSupportInstanceStandard() throws Exception
    {
        MyTestTag myTag = new MyTestTag();
        NestedStandardTag tag = new NestedStandardTag(myTag, context);
        assertSame(myTag, tag.getWrappedTag());
        try
        {
            tag.getTag();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            tag.getId();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            tag.getValue("");
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            tag.removeValue("");
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
    }
    
    public void testNotTagSupportInstanceBody() throws Exception
    {
        MyTestTag myTag = new MyTestTag();
        NestedBodyTag tag = new NestedBodyTag(myTag, context);
        assertSame(myTag, tag.getWrappedTag());
        try
        {
            tag.getTag();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            tag.getId();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            tag.setValue("", "");
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            tag.getPreviousOut();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            tag.getBodyContent();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
    }
    
    private class TestJspFragment extends JspFragment
    {
        public JspContext getJspContext()
        {
            return null;
        }
        
        public void invoke(Writer writer) throws JspException, IOException
        {

        }
    }
    
    private class AnotherTestTag extends TagSupport
    {
        
    }
    
    private class MyTestTag implements BodyTag
    {
        public int doEndTag() throws JspException
        {
            return 0;
        }
        
        public int doStartTag() throws JspException
        {
            return 0;
        }
        
        public Tag getParent()
        {
            return null;
        }
        
        public void release()
        {

        }
        
        public void setPageContext(PageContext context)
        {

        }
        
        public void setParent(Tag parent)
        {

        }
        
        public void doInitBody() throws JspException
        {

        }
        
        public void setBodyContent(BodyContent content)
        {

        }
        
        public int doAfterBody() throws JspException
        {
            return 0;
        }
    }
}
