package com.mockrunner.test.web;

import java.util.HashMap;
import java.util.Map;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.base.VerifyFailedException;
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
        module.createTag(TestBodyTag.class);
        assertTrue(module.getTag() instanceof TestBodyTag);
        TestTag testTag = new TestTag();
        module.setTag(testTag);
        assertSame(testTag, module.getTag());
        TestBodyTag testBodyTag = new TestBodyTag();
        module.setTag(testBodyTag);
        assertSame(testBodyTag, module.getTag());
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
    
    public void testSetBody()
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
    
    public void testPopulateAttributes()
    {
        Map testMap = new HashMap();
        testMap.put("testString", "test");
        TestTag tag = (TestTag)module.createTag(TestTag.class, testMap);
        module.populateAttributes();
        assertEquals("test", tag.getTestString());
    }
    
    public void testDoMethodsCalled()
    {
        TestBodyTag tag = (TestBodyTag)module.createTag(TestBodyTag.class);
        module.doInitBody();
        assertTrue(tag.wasDoInitBodyCalled());
        module.doStartTag();
        assertTrue(tag.wasDoStartTagCalled());
        module.doEndTag();
        assertTrue(tag.wasDoEndTagCalled());
        module.doAfterBody();
        assertTrue(tag.wasDoAfterBodyCalled());
        tag = (TestBodyTag)module.createTag(TestBodyTag.class);
        module.processTagLifecycle();
        assertTrue(tag.wasDoStartTagCalled());
    }
}
