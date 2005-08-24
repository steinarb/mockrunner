package com.mockrunner.example.tag;

import com.mockrunner.tag.BasicTagTestCaseAdapter;

/**
 * Example test for the {@link ConstrainedNumericTextTag}.
 * Demonstrates the usage of {@link com.mockrunner.tag.TagTestModule} 
 * and {@link com.mockrunner.tag.BasicTagTestCaseAdapter}.
 */
public class ConstrainedNumericTextTagTest extends BasicTagTestCaseAdapter
{
    private ConstrainedNumericTextTag tag;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        tag = (ConstrainedNumericTextTag)createTag(ConstrainedNumericTextTag.class);
        prepareStrutsTag();
    }

    private void prepareStrutsTag()
    {
        tag.setName("test");
        setRequestAttribute("test", "test");
    }

    public void testOnBlurHandler() throws Exception
    {
        tag.setMinValue(3);
        tag.setMaxValue(10000);
        doStartTag();
        verifyOutputContains("checkConstraints(this,3,10000)");
    }
}
