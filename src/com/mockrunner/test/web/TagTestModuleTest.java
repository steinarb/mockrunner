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
