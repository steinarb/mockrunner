package com.mockrunner.test.web;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.base.VerifyFailedException;
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
}
