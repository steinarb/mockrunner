package de.mockrunner.base;

import javax.servlet.jsp.tagext.TagSupport;

public class TagTestCaseAdapter extends BaseTestCase
{
    private TagTestModule tagTestModule;

    public TagTestCaseAdapter(String arg0)
    {
        super(arg0);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        tagTestModule = createTagTestModule(getMockObjectFactory());
    }

    protected TagTestModule createTagTestModule(MockObjectFactory mockFactory)
    {
        return new TagTestModule(mockFactory);
    }
    
    public TagSupport createTag(Class tagClass)
    {
        return tagTestModule.createTag(tagClass);
    }
    
    public TagSupport getTag()
    {
        return tagTestModule.getTag();
    }
    
    public void doStartTag()
    {
        tagTestModule.doStartTag();
    }
    
    public void doEndTag()
    {
        tagTestModule.doEndTag();
    }
    
    public String getOutput()
    {
        return tagTestModule.getOutput();
    }
    
    public void verifyOutput(String output)
    {
        tagTestModule.verifyOutput(output);
    }

    public void verifyOutputContains(String output)
    {
        tagTestModule.verifyOutputContains(output);
    }
}
