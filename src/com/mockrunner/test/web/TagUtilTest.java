package com.mockrunner.test.web;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.SimpleTag;
import javax.servlet.jsp.tagext.TagSupport;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.web.MockJspWriter;
import com.mockrunner.mock.web.MockPageContext;
import com.mockrunner.tag.DynamicAttribute;
import com.mockrunner.tag.DynamicChild;
import com.mockrunner.tag.NestedBodyTag;
import com.mockrunner.tag.NestedSimpleTag;
import com.mockrunner.tag.NestedStandardTag;
import com.mockrunner.tag.NestedTag;
import com.mockrunner.tag.TagUtil;

public class TagUtilTest extends BaseTestCase
{
    private MockPageContext pageContext;
    private Map testMap;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        pageContext = getWebMockObjectFactory().getMockPageContext();
        testMap = new HashMap();
    }
    
    public void testCreateNestedTagInstanceParameters()
    {
        try
        {
            TagUtil.createNestedTagInstance(null, pageContext, testMap);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        try
        {
            TagUtil.createNestedTagInstance((Object)null, pageContext, testMap);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        try
        {
            TagUtil.createNestedTagInstance("abc", pageContext, testMap);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        try
        {
            TagUtil.createNestedTagInstance(TestSimpleTag.class, "abc", testMap);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        try
        {
            TagUtil.createNestedTagInstance(TestBodyTag.class, new TestJspContext(), testMap);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        TagUtil.createNestedTagInstance(TestSimpleTag.class, new TestJspContext(), testMap);
    }

    public void testCreateNestedTagInstance()
    {
        TagSupport tag = (TagSupport)TagUtil.createNestedTagInstance(TestTag.class, pageContext, testMap);
        assertTrue(tag instanceof NestedStandardTag);
        assertTrue(((NestedTag)tag).getTag() instanceof TestTag);
        tag = (TagSupport)TagUtil.createNestedTagInstance(TestBodyTag.class, pageContext, testMap);
        assertTrue(tag instanceof NestedBodyTag);
        assertTrue(((NestedTag)tag).getTag() instanceof TestBodyTag);
        SimpleTag simpleTag = (SimpleTag)TagUtil.createNestedTagInstance(TestSimpleTag.class, pageContext, testMap);
        assertTrue(simpleTag instanceof NestedSimpleTag);
        assertTrue(((NestedTag)simpleTag).getWrappedTag() instanceof TestSimpleTag);
        
        TestTag testTag = new TestTag();
        tag = (TagSupport)TagUtil.createNestedTagInstance(testTag, pageContext, testMap);
        assertTrue(tag instanceof NestedStandardTag);
        assertSame(testTag, ((NestedTag)tag).getTag());
        TestBodyTag testBodyTag = new TestBodyTag();
        tag = (TagSupport)TagUtil.createNestedTagInstance(testBodyTag, pageContext, testMap);
        assertTrue(tag instanceof NestedBodyTag);
        assertSame(testBodyTag, ((NestedTag)tag).getTag());
        TestSimpleTag testSimpleTag = new TestSimpleTag();
        simpleTag = (SimpleTag)TagUtil.createNestedTagInstance(testSimpleTag, pageContext, testMap);
        assertTrue(simpleTag instanceof NestedSimpleTag);
        assertSame(testSimpleTag, ((NestedTag)simpleTag).getWrappedTag());
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
        tag = new TestTag();
        testMap.clear();
        TagUtil.populateTag(tag, testMap, true);
        assertTrue(tag.wasReleaseCalled());
        assertNull(tag.getTestInteger());
        assertNull(tag.getTestString());
        assertEquals(0, tag.getTestDouble(), 0.0);
        TestSimpleTag testSimpleTag = new TestSimpleTag();
        testMap.put("booleanProperty", "true");
        testMap.put("floatProperty", "123.x");
        testMap.put("stringProperty", "aString");
        TagUtil.populateTag(testSimpleTag, testMap, false);
        assertEquals(0.0, testSimpleTag.getFloatProperty(), 0.0);
        assertTrue(testSimpleTag.getBooleanProperty());
        assertEquals("aString", testSimpleTag.getStringProperty());
    }
    
    public void testPopulateTagWithArbitraryBeans()
    {
        testMap.put("object", this);
        testMap.put("tagUtilTest", this);
        ArbitraryTag tag = new ArbitraryTag();
        TagUtil.populateTag(tag, testMap, false);
        assertSame(this, tag.getObject());
        assertSame(this, tag.getTagUtilTest());
        testMap.put("object", "abc");
        TagUtil.populateTag(tag, testMap, false);
        assertEquals("abc", tag.getObject());
        assertSame(this, tag.getTagUtilTest());
    }
    
    public void testPopulateDynamicAttributesWithNonDynamicBean()
    {
        Object object = new Object(); 
        testMap.put("object", new DynamicAttribute("uri", object));
        ArbitraryTag tag = new ArbitraryTag();
        try
        {
            TagUtil.populateTag(tag, testMap, false);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        testMap.clear();
        testMap.put("testobject", object);
        TagUtil.populateTag(tag, testMap, false);
    }
    
    public void testPopulateDynamicAttributesWithDynamicBean()
    {
        Object object = new Object(); 
        testMap.put("object", new DynamicAttribute("uri", object));
        DynamicTag tag = new DynamicTag();
        TagUtil.populateTag(tag, testMap, false);
        assertNull(tag.getObject());
        assertEquals(1, tag.getDynamicAttributesMap().size());
        DynamicAttribute attribute = (DynamicAttribute)tag.getDynamicAttributesMap().get("object");
        assertEquals("uri", attribute.getUri());
        assertSame(object, attribute.getValue());
        tag.clearDynamicAttributes();
        testMap.put("object", object);
        testMap.put("testobject", object);
        TagUtil.populateTag(tag, testMap, false);
        assertSame(object, tag.getObject());
        assertEquals(1, tag.getDynamicAttributesMap().size());
        attribute = (DynamicAttribute)tag.getDynamicAttributesMap().get("testobject");
        assertNull(attribute.getUri());
        assertSame(object, attribute.getValue());
    }
    
    public void testEvalBody() throws Exception
    {
        TestTag testStandardTag = new TestTag();
        TestBodyTag testBodyTag = new TestBodyTag();
        TestSimpleTag testSimpleTag = new TestSimpleTag();
        NestedStandardTag standardTag = (NestedStandardTag)TagUtil.createNestedTagInstance(testStandardTag, pageContext, new HashMap());
        NestedBodyTag bodyTag = (NestedBodyTag)TagUtil.createNestedTagInstance(testBodyTag, pageContext, new HashMap());
        NestedSimpleTag simpleTag = (NestedSimpleTag)TagUtil.createNestedTagInstance(testSimpleTag, pageContext, new HashMap());
        List bodyList = new ArrayList();
        bodyList.add(standardTag);
        bodyList.add(bodyTag);
        bodyList.add(simpleTag);
        bodyList.add(new TestDynamicChild("thisisa"));
        bodyList.add("test");
        bodyList.add(new TestDynamicChild(null));
        bodyList.add(new Integer(123));
        bodyList.add(null);
        TagUtil.evalBody(bodyList, pageContext);
        assertTrue(testStandardTag.wasDoStartTagCalled());
        assertTrue(testBodyTag.wasDoStartTagCalled());
        assertTrue(testSimpleTag.wasDoTagCalled());
        assertEquals("TestTagTestBodyTagTestSimpleTagthisisatestnull123null", ((MockJspWriter)pageContext.getOut()).getOutputAsString());
        try
        {
            TagUtil.evalBody(bodyList, "WrongContext");
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
    }
    
    public class ArbitraryTag extends TagSupport
    {
        private TagUtilTest tagUtilTest;
        private Object object;
        
        public Object getObject()
        {
            return object;
        }
        
        public TagUtilTest getTagUtilTest()
        {
            return tagUtilTest;
        }
        
        public void setObject(Object object)
        {
            this.object = object;
        }
        
        public void setTagUtilTest(TagUtilTest tagUtilTest)
        {
            this.tagUtilTest = tagUtilTest;
        }
    }
    
    public class DynamicTag extends TagSupport implements DynamicAttributes
    {
        private Map dynamicAttributes = new HashMap();
        private Object object;
        
        public void setDynamicAttribute(String uri, String localName, Object value) throws JspException
        {
            dynamicAttributes.put(localName, new DynamicAttribute(uri, value));
        }
        
        public void setObject(Object object)
        {
            this.object = object;
        }
        
        public Object getObject()
        {
            return object;
        }
        
        public void clearDynamicAttributes()
        {
            dynamicAttributes.clear();
        }
        
        public Map getDynamicAttributesMap()
        {
            return dynamicAttributes;
        }
    }
    
    private class TestJspContext extends JspContext
    {
        public Object findAttribute(String name)
        {
            return null;
        }
        
        public Object getAttribute(String name, int scope)
        {
            return null;
        }
        
        public Object getAttribute(String name)
        {
            return null;
        }
        
        public Enumeration getAttributeNamesInScope(int scope)
        {
            return null;
        }
        
        public int getAttributesScope(String name)
        {
            return 0;
        }
        
        public ExpressionEvaluator getExpressionEvaluator()
        {
            return null;
        }
        
        public JspWriter getOut()
        {
            return null;
        }
        
        public VariableResolver getVariableResolver()
        {
            return null;
        }
        
        public void removeAttribute(String name, int scope)
        {

        }
        
        public void removeAttribute(String name)
        {

        }
        
        public void setAttribute(String name, Object value, int scope)
        {

        }
        
        public void setAttribute(String name, Object value)
        {

        }
    }
    
    private class TestDynamicChild implements DynamicChild
    {
        private String value;
        
        public TestDynamicChild(String value)
        {
            this.value = value;
        }
        
        public Object evaluate()
        {
            return value;
        }
    }
}
