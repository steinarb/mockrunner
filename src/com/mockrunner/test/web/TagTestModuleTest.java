package com.mockrunner.test.web;

import java.io.StringWriter;
import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.mock.web.MockJspFragment;
import com.mockrunner.tag.DynamicAttribute;
import com.mockrunner.tag.NestedBodyTag;
import com.mockrunner.tag.NestedSimpleTag;
import com.mockrunner.tag.NestedStandardTag;
import com.mockrunner.tag.NestedTag;
import com.mockrunner.tag.TagTestModule;

public class TagTestModuleTest extends BaseTestCase
{
    private TagTestModule module;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        module = new TagTestModule(getWebMockObjectFactory());
    }
    
    public void testCreateAndSetTag()
    {
        module.createTag(TestTag.class);
        assertTrue(module.getTag() instanceof TestTag);
        assertTrue(module.getNestedTag() instanceof NestedStandardTag);
        module.createWrappedTag(TestBodyTag.class);
        assertTrue(module.getTag() instanceof TestBodyTag);
        assertTrue(module.getNestedTag() instanceof NestedBodyTag);
        module.createWrappedTag(TestSimpleTag.class);
        assertTrue(module.getWrappedTag() instanceof TestSimpleTag);
        assertTrue(module.getNestedTag() instanceof NestedSimpleTag);
        TestTag testTag = new TestTag();
        module.setTag(testTag);
        assertSame(testTag, module.getTag());
        TestBodyTag testBodyTag = new TestBodyTag();
        module.setTag(testBodyTag);
        assertSame(testBodyTag, module.getTag());
        TestSimpleTag testSimpleTag = new TestSimpleTag();
        module.setTag(testSimpleTag);
        assertSame(testSimpleTag, module.getWrappedTag());
    }
    
    public void testCreateTagWithAttributes()
    {
        HashMap testMap = new HashMap();
        testMap.put("testString", "test");
        testMap.put("dynamicAttribute", "dynamicAttributeValue");
        module.createWrappedTag(TestSimpleTag.class, testMap);
        TestSimpleTag simpleTag = (TestSimpleTag)module.getWrappedTag();
        assertNull(simpleTag.getTestString());
        assertEquals(0, simpleTag.getDynamicAttributesMap().size());
        module.populateAttributes();
        assertEquals("test", simpleTag.getTestString());
        assertEquals(1, simpleTag.getDynamicAttributesMap().size());
        assertEquals("dynamicAttributeValue", ((DynamicAttribute)simpleTag.getDynamicAttributesMap().get("dynamicAttribute")).getValue());
        assertNull(((DynamicAttribute)simpleTag.getDynamicAttributesMap().get("dynamicAttribute")).getUri());
        module.createTag(TestTag.class, testMap);
        module.populateAttributes();
        TestTag tag = (TestTag)module.getWrappedTag();
        assertEquals("test", tag.getTestString());
        module.createTag(TestBodyTag.class, testMap);
        module.populateAttributes();
        TestBodyTag bodyTag = (TestBodyTag)module.getWrappedTag();
        assertEquals("test", bodyTag.getTestString());
    }
    
    public void testSetTagWithAttributes()
    {
        HashMap testMap = new HashMap();
        testMap.put("testString", "test");
        TestTag testTag = new TestTag();
        testTag.setTestInteger(new Integer(3));
        module.setTag(testTag, testMap);
        module.populateAttributes();
        assertEquals(new Integer(3), testTag.getTestInteger());
        assertEquals("test", testTag.getTestString());
        testMap.put("testInteger", new Integer(5));
        module.setTag(testTag, testMap);
        module.populateAttributes();
        assertEquals(new Integer(5), testTag.getTestInteger());
        assertEquals("test", testTag.getTestString());
        TestSimpleTag simpleTag = new TestSimpleTag();
        module.setTag(simpleTag, testMap);
        module.populateAttributes();
        assertEquals("test", simpleTag.getTestString());
        assertEquals(1, simpleTag.getDynamicAttributesMap().size());
        assertEquals(new Integer(5), ((DynamicAttribute)simpleTag.getDynamicAttributesMap().get("testInteger")).getValue());
    }

    public void testVerifyOutput()
    {
        module.createNestedTag(TestTag.class);
        module.processTagLifecycle();
        try
        {
            module.verifyOutput("testtag");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setCaseSensitive(false);
        module.verifyOutput("testtag");
        module.verifyOutputContains("ES");
        module.verifyOutputRegularExpression("[abT].*");
        module.setCaseSensitive(true);
        try
        {
            module.verifyOutputContains("ES");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyOutputRegularExpression("tesT.*");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }
    
    public void testSetDoReleaseCalled()
    {
        try
        {
            module.setDoRelease(true);
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            module.setDoReleaseRecursive(true);
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        TestTag tag = (TestTag)module.createTag(TestTag.class);
        module.setDoRelease(true);
        module.populateAttributes();
        assertTrue(tag.wasReleaseCalled());
        tag = (TestTag)module.createTag(TestTag.class);
        module.setDoReleaseRecursive(true);
        module.populateAttributes();
        assertTrue(tag.wasReleaseCalled());
    }
    
    public void testSetBody() throws Exception
    {
        try
        {
            module.setBody("body");
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        NestedTag tag = module.createNestedTag(TestTag.class);
        tag.addTagChild(TestTag.class);
        assertTrue(tag.getChild(0) instanceof NestedTag);
        module.setBody("body");
        assertTrue(tag.getChild(0) instanceof String);
        TestSimpleTag simpleTag = new TestSimpleTag();
        tag = module.setTag(simpleTag);
        module.setBody("body");
        assertTrue(tag.getChild(0) instanceof String);
        MockJspFragment fragment = (MockJspFragment)((NestedSimpleTag)tag).getJspBody();
        assertTrue(fragment.getChild(0) instanceof String);
        StringWriter writer = new StringWriter();
        fragment.invoke(writer);
        assertEquals("body", writer.toString());
    }
    
    public void testNullTag()
    {
        try
        {
            module.populateAttributes();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            module.doInitBody();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            module.doStartTag();
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            module.doEndTag();
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            module.doAfterBody();
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            module.processTagLifecycle();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
    }
    
    public void testDoMethodsCalled()
    {
        TestBodyTag bodyTag = (TestBodyTag)module.createTag(TestBodyTag.class);
        module.doInitBody();
        assertTrue(bodyTag.wasDoInitBodyCalled());
        module.doStartTag();
        assertTrue(bodyTag.wasDoStartTagCalled());
        module.doEndTag();
        assertTrue(bodyTag.wasDoEndTagCalled());
        module.doAfterBody();
        assertTrue(bodyTag.wasDoAfterBodyCalled());
        bodyTag = (TestBodyTag)module.createTag(TestBodyTag.class);
        module.processTagLifecycle();
        assertTrue(bodyTag.wasDoStartTagCalled());
        TestSimpleTag simpleTag = (TestSimpleTag)module.createWrappedTag(TestSimpleTag.class);
        module.doTag();
        assertTrue(simpleTag.wasDoTagCalled());
        simpleTag = (TestSimpleTag)module.createWrappedTag(TestSimpleTag.class);
        module.processTagLifecycle();
        assertTrue(simpleTag.wasDoTagCalled());
    }
    
    public void testCreateInvalidParameter()
    {
        try
        {
            module.createTag(String.class);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        try
        {
            module.createTag(TestSimpleTag.class, new HashMap());
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        try
        {
            module.createTag(AnotherTag.class);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        module.createWrappedTag(AnotherTag.class, new HashMap());
        module.createWrappedTag(TestSimpleTag.class);
        try
        {
            module.createWrappedTag(String.class);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        try
        {
            module.createNestedTag(String.class);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        module.createNestedTag(TestSimpleTag.class, new HashMap());
    }
    
    public void testGetTag()
    {
        assertNull(module.getTag());
        assertNull(module.getWrappedTag());
        module.createWrappedTag(TestSimpleTag.class, new HashMap());
        try
        {
            module.getTag();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        assertNotNull(module.getWrappedTag());
    }
    
    public void testDoTagInvalidParameter()
    {
        module.createWrappedTag(TestSimpleTag.class);
        module.doTag();
        try
        {
            module.doStartTag();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            module.doEndTag();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            module.doInitBody();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            module.doAfterBody();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        module.createWrappedTag(TestTag.class);
        try
        {
            module.doTag();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            module.doInitBody();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        module.doStartTag();
        module.doEndTag();
        module.doAfterBody();
        module.createWrappedTag(TestBodyTag.class);
        try
        {
            module.doTag();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        module.doStartTag();
        module.doEndTag();
        module.doAfterBody();
        module.doInitBody();
        module.createWrappedTag(AnotherTag.class);
        try
        {
            module.doTag();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            module.doInitBody();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            module.doAfterBody();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        module.doStartTag();
        module.doEndTag();
    }
    
    public void testDoTagThrowsException()
    {
        module.createWrappedTag(ErrorTag.class);
        try
        {
            module.doStartTag();
            fail();
        } 
        catch(NestedApplicationException exc)
        {
            //should throw exception
        }
        try
        {
            module.doEndTag();
            fail();
        } 
        catch(NestedApplicationException exc)
        {
            //should throw exception
        }
        try
        {
            module.doInitBody();
            fail();
        } 
        catch(NestedApplicationException exc)
        {
            //should throw exception
        }
        try
        {
            module.doAfterBody();
            fail();
        } 
        catch(NestedApplicationException exc)
        {
            //should throw exception
        }
        try
        {
            module.processTagLifecycle();
            fail();
        } 
        catch(NestedApplicationException exc)
        {
            //should throw exception
        }
    }
    
    public static class AnotherTag implements Tag
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
        
        public void setPageContext(PageContext pageContext)
        {

        }
        
        public void setParent(Tag parent)
        {

        }
    }
    
    public static class ErrorTag implements BodyTag
    {
        public void doInitBody() throws JspException
        {
            throw new JspException("ErrorTag doInitBody");
        }
        
        public void setBodyContent(BodyContent arg0)
        {
            
        }
        
        public int doAfterBody() throws JspException
        {
            throw new JspException("ErrorTag doAfterBody");
        }
        
        public int doEndTag() throws JspException
        {
            throw new JspException("ErrorTag doEndTag");
        }
        
        public int doStartTag() throws JspException
        {
            throw new JspException("ErrorTag doStartTag");
        }
        
        public Tag getParent()
        {
            return null;
        }
        
        public void release()
        {

        }
        
        public void setPageContext(PageContext pageContext)
        {

        }
        
        public void setParent(Tag tag)
        {

        }
    }
}
