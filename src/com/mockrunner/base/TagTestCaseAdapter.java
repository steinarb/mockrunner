package com.mockrunner.base;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * Delegator for {@link TagTestModule}. You can
 * subclass this adapter or use {@link TagTestModule}
 * directly (so your test case can use another base
 * class).
 */
public class TagTestCaseAdapter extends BaseTestCase
{
    private TagTestModule tagTestModule;

    public TagTestCaseAdapter(String arg0)
    {
        super(arg0);
    }

    /**
     * Creates the <code>TagTestModule</code>. If you
     * overwrite this method, you must call 
     * <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        tagTestModule = createTagTestModule(getMockObjectFactory());
    }
    
    /**
     * Gets the <code>TagTestModule</code>. 
     * @return the <code>TagTestModule</code>
     */
    protected TagTestModule getTagTestModule()
    {
        return tagTestModule;
    }
    
    /**
     * Sets the <code>TagTestModule</code>. 
     * @param tagTestModule the <code>TagTestModule</code>
     */
    protected void setTagTestModule(TagTestModule tagTestModule)
    {
        this.tagTestModule = tagTestModule;
    }

    /**
     * Delegates to {@link TagTestModule#createTag}
     */
    public TagSupport createTag(Class tagClass)
    {
        return tagTestModule.createTag(tagClass);
    }
    
    /**
     * Delegates to {@link TagTestModule#getTag}
     */
    public TagSupport getTag()
    {
        return tagTestModule.getTag();
    }
    
    /**
     * Delegates to {@link TagTestModule#doStartTag}
     */
    public void doStartTag()
    {
        tagTestModule.doStartTag();
    }
    
    /**
     * Delegates to {@link TagTestModule#doEndTag}
     */
    public void doEndTag()
    {
        tagTestModule.doEndTag();
    }
    
    /**
     * Delegates to {@link TagTestModule#getOutput}
     */
    public String getOutput()
    {
        return tagTestModule.getOutput();
    }
    
    /**
     * Delegates to {@link TagTestModule#verifyOutput}
     */
    public void verifyOutput(String output)
    {
        tagTestModule.verifyOutput(output);
    }

    /**
     * Delegates to {@link TagTestModule#verifyOutputContains}
     */
    public void verifyOutputContains(String output)
    {
        tagTestModule.verifyOutputContains(output);
    }
}
