package com.mockrunner.test.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.el.ValueExpression;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.junit.Before;
import org.junit.Test;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.web.JasperJspFactory;
import com.mockrunner.mock.web.MockPageContext;
import com.mockrunner.tag.DynamicAttribute;
import com.mockrunner.tag.NestedBodyTag;
import com.mockrunner.tag.NestedSimpleTag;
import com.mockrunner.tag.NestedStandardTag;
import com.mockrunner.tag.NestedTag;
import com.mockrunner.tag.RuntimeAttribute;

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
    
    @Before
    public void setUp() throws Exception
    {
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
        testTag1 = testTagChild1.getTag();
        testTag11 = testTagChild11.getTag();
    }
    
    private void prepareBodyTagTest()
    {
        testTag = new TestBodyTag();
        nestedTagRoot = new NestedBodyTag((BodyTagSupport)testTag, context, testMap);
        testTagChild1 = nestedTagRoot.addTagChild(TestTag.class, testMap);
        testTagChild11 = testTagChild1.addTagChild(new TestBodyTag(), testMap);
        testTagChild1.addTextChild("bodytest");
        testTag1 = testTagChild1.getTag();
        testTag11 = testTagChild11.getTag();
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
        testTag1 = testTagChild1.getTag();
        testTag11 = testTagChild11.getTag();
        testTagChild111 = testTagChild11.addTagChild(TestSimpleTag.class);
        testTag111 = (SimpleTag)testTagChild111.getWrappedTag();
    }
    
    @Test
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
    
    @Test
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
        NestedStandardTag standardtag = (NestedStandardTag)nestedTagRoot.getChild(0);
        standardtag.addTagChild(new TestSimpleTag());
        assertEquals(1, standardtag.getChilds().size());
        assertTrue(((NestedTag)standardtag.getChild(0)).getWrappedTag() instanceof TestSimpleTag);
    }
    
    @Test
    public void testFindTag()
    {
        AnotherTestTag anotherTestTag = new AnotherTestTag();
        NestedStandardTag root = new NestedStandardTag(anotherTestTag, context);
        NestedTag child1 = root.addTagChild(TestTag.class, testMap);
        NestedTag child11 = child1.addTagChild(TestTag.class, testMap);
        Tag foundTag = TagSupport.findAncestorWithClass((Tag)child11.getWrappedTag(), AnotherTestTag.class);
        assertNotNull(foundTag);
    }
    
    @Test
    public void testPopulateAttributesStandard()
    {
        prepareStandardTagTest();
        nestedTagRoot.populateAttributes();
        assertEquals("test", ((TestTag)testTag).getTestString());
        assertNull(((TestTag)testTag1).getTestString());
        assertNull(((TestTag)testTag11).getTestString());
    }
    
    @Test
    public void testPopulateAttributesBody()
    {
        prepareBodyTagTest();
        nestedTagRoot.populateAttributes();
        assertEquals("test", ((TestBodyTag)testTag).getTestString());
        assertNull(((TestTag)testTag1).getTestString());
        assertNull(((TestBodyTag)testTag11).getTestString());
    }
    
    @Test
    public void testPopulateAttributesSimple()
    {
        prepareSimpleTagTest();
        nestedTagRoot.populateAttributes();
        assertEquals("test", ((TestSimpleTag)rootSimpleTag).getStringProperty());
        assertEquals(1, ((TestSimpleTag)rootSimpleTag).getFloatProperty(), 0.0);
    }
    
    @Test
    public void testPopulateDynamicAndRuntimeAttributes()
    {
        Map map = new HashMap();
        map.put("dynamicAttribute1", "test");
        map.put("dynamicAttribute2", 1);
        map.put("dynamicAttribute3", new TestRuntimeAttribute(3L));
        map.put("dynamicAttribute4", new DynamicAttribute(null, new TestRuntimeAttribute((byte) 4)));
        TestSimpleTag simpleTag = new TestSimpleTag();
        NestedTag nestedTag = new NestedSimpleTag(simpleTag, context, map);
        nestedTag.populateAttributes();
        assertEquals(4, simpleTag.getDynamicAttributesMap().size());
        DynamicAttribute attribute1 = (DynamicAttribute)simpleTag.getDynamicAttributesMap().get("dynamicAttribute1");
        DynamicAttribute attribute2 = (DynamicAttribute)simpleTag.getDynamicAttributesMap().get("dynamicAttribute2");
        DynamicAttribute attribute3 = (DynamicAttribute)simpleTag.getDynamicAttributesMap().get("dynamicAttribute3");
        DynamicAttribute attribute4 = (DynamicAttribute)simpleTag.getDynamicAttributesMap().get("dynamicAttribute4");
        assertNull(attribute1.getUri());
        assertEquals("test", attribute1.getValue());
        assertNull(attribute2.getUri());
        assertEquals(1, attribute2.getValue());
        assertNull(attribute3.getUri());
        assertEquals(3L, attribute3.getValue());
        assertNull(attribute4.getUri());
        assertEquals((byte) 4, attribute4.getValue());
        map = new HashMap();
        map.put("dynamicAttribute1", "test");
        map.put("dynamicAttribute2", 1);
        map.put("dynamicAttribute3", new TestRuntimeAttribute(3L));
        TestBodyTag bodyTag = new TestBodyTag();
        nestedTag = new NestedBodyTag(bodyTag, context, map);
        nestedTag.populateAttributes();
        map.put("dynamicAttribute4", new DynamicAttribute("test", "test"));
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
    
    @Test
    public void testPopulateValueExpressionAttribute() throws Exception
    {
        getWebMockObjectFactory().setDefaultJspFactory(new JasperJspFactory().configure(getWebMockObjectFactory()));
        JspApplicationContext applicationContext = JspFactory.getDefaultFactory().getJspApplicationContext(getWebMockObjectFactory().getMockPageContext().getServletContext());
        ValueExpression valueExpression = applicationContext.getExpressionFactory().createValueExpression(getWebMockObjectFactory().getMockPageContext().getELContext(), "#{sessionScope.test}", String.class);
        Map map = new HashMap();
        map.put("valueExpressionProperty", valueExpression);
        TestSimpleTag simpleTag = new TestSimpleTag();
        NestedTag nestedTag = new NestedSimpleTag(simpleTag, context, map);
        nestedTag.populateAttributes();
        assertSame(valueExpression, simpleTag.getValueExpressionProperty());
        getWebMockObjectFactory().getMockSession().setAttribute("test", "testValue");
        nestedTag.doLifecycle();
        assertEquals("testValue", simpleTag.getValueExpressionResult());
    }

    @Test
    public void testSetPageContextStandard()
    {
        prepareStandardTagTest();
        MockPageContext newContext = new MockPageContext(null, null, null);
        ((NestedStandardTag)nestedTagRoot).setPageContext(newContext);
        assertTrue(((TestTag)testTag).getPageContext() == newContext);
        assertTrue(((TestTag)testTag1).getPageContext() == newContext);
        assertTrue(((TestTag)testTag11).getPageContext() == newContext);
    }
    
    @Test
    public void testSetPageContextBody()
    {
        prepareBodyTagTest();
        MockPageContext newContext = new MockPageContext(null, null, null);
        ((NestedBodyTag)nestedTagRoot).setPageContext(newContext);
        assertTrue(((TestBodyTag)testTag).getPageContext() == newContext);
        assertTrue(((TestTag)testTag1).getPageContext() == newContext);
        assertTrue(((TestBodyTag)testTag11).getPageContext() == newContext);
    }
    
    @Test
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
    
    @Test
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
    
    @Test
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
    
    @Test
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
    
    @Test
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
    
    @Test
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
    
    @Test
    public void testNoIterationTag() throws Exception
    {
        NestedStandardTag tag = new NestedStandardTag(new NoIterationTag(), context);
        assertEquals(Tag.EVAL_PAGE, tag.doLifecycle());
    }
    
    @Test
    public void testTryCatchFinallyHandlingStandardTag() throws Exception
    {
        ExceptionTestTag testTag = new ExceptionTestTag();
        NestedStandardTag tag = new NestedStandardTag(testTag, context);
        RuntimeException excToBeThrown = new RuntimeException();
        tag.addTagChild(new FailureTestTag(excToBeThrown));
        assertEquals(-1, tag.doLifecycle());
        assertTrue(testTag.wasDoCatchCalled());
        assertTrue(testTag.wasDoFinallyCalled());
        assertSame(excToBeThrown, testTag.getCaughtException());
        RuntimeException excToBeRethrown = new RuntimeException();
        testTag = new ExceptionTestTag(excToBeRethrown);
        tag = new NestedStandardTag(testTag, context);
        excToBeThrown = new RuntimeException();
        tag.addTagChild(new FailureTestTag(excToBeThrown));
        try
        {
            tag.doLifecycle();
        } 
        catch(Exception exc)
        {
            assertTrue(testTag.wasDoCatchCalled());
            assertTrue(testTag.wasDoFinallyCalled());
            assertSame(excToBeThrown, testTag.getCaughtException());
            assertSame(excToBeRethrown, exc);
        }
    }
    
    @Test
    public void testTryCatchFinallyHandlingBodyTag() throws Exception
    {
        ExceptionTestTag testTag = new ExceptionTestTag();
        NestedBodyTag tag = new NestedBodyTag(testTag, context);
        RuntimeException excToBeThrown = new RuntimeException();
        tag.addTagChild(new FailureTestTag(excToBeThrown));
        assertEquals(-1, tag.doLifecycle());
        assertTrue(testTag.wasDoCatchCalled());
        assertTrue(testTag.wasDoFinallyCalled());
        assertSame(excToBeThrown, testTag.getCaughtException());
        RuntimeException excToBeRethrown = new RuntimeException();
        testTag = new ExceptionTestTag(excToBeRethrown);
        tag = new NestedBodyTag(testTag, context);
        excToBeThrown = new RuntimeException();
        tag.addTagChild(new FailureTestTag(excToBeThrown));
        try
        {
            tag.doLifecycle();
        } 
        catch(Exception exc)
        {
            assertTrue(testTag.wasDoCatchCalled());
            assertTrue(testTag.wasDoFinallyCalled());
            assertSame(excToBeThrown, testTag.getCaughtException());
            assertSame(excToBeRethrown, exc);
        }
    }
    
    private class TestRuntimeAttribute implements RuntimeAttribute
    {
        private Object value;

        public TestRuntimeAttribute(Object value)
        {
            this.value = value;
        }

        public Object evaluate()
        {
            return value;
        }
    }
    
    private class FailureTestTag extends TagSupport
    {
        private RuntimeException exc;
        
        public FailureTestTag(RuntimeException exc)
        {
            this.exc = exc;
        }
        
        public int doStartTag() throws JspException
        {
            throw exc;
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
    
    private class NoIterationTag implements Tag
    {
        public int doStartTag() throws JspException
        {
            return Tag.EVAL_BODY_INCLUDE;
        }
        
        public int doEndTag() throws JspException
        {
            return Tag.EVAL_PAGE;
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
    }
}
