package de.mockrunner.example.test;

import de.mockrunner.base.TagTestCaseAdapter;
import de.mockrunner.example.ConstrainedNumericTextTag;

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
