package com.mockrunner.example.test;

import com.mockrunner.base.TagTestCaseAdapter;
import com.mockrunner.example.ConstrainedNumericTextTag;

/**
 * Example test for the {@link de.mockrunner.example.ConstrainedNumericTextTag}.
 * Demonstrates the usage of {@link de.mockrunner.base.TagTestModule} 
 * resp. {@link de.mockrunner.base.TagTestCaseAdapter}.
 */
public class ConstrainedNumericTextTagTest extends TagTestCaseAdapter
{
    private ConstrainedNumericTextTag tag;
    
    public ConstrainedNumericTextTagTest(String arg0)
    {
        super(arg0);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        tag = (ConstrainedNumericTextTag)createTag(ConstrainedNumericTextTag.class);
        prepareStrutsTag();
    }

    private void prepareStrutsTag()
    {
        tag.setName("test");
        getMockObjectFactory().getMockRequest().setAttribute("test", "test");
    }

    public void testOnBlurHandler() throws Exception
    {
        tag.setMinValue(3);
        tag.setMaxValue(10000);
        doStartTag();
        verifyOutputContains("checkConstraints(this,3,10000)");
    }
}
