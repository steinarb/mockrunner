package com.mockrunner.base;

import javax.servlet.jsp.tagext.TagSupport;

import com.mockrunner.mock.MockPageContext;

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
    protected TagSupport createTag(Class tagClass)
    {
        return tagTestModule.createTag(tagClass);
    }
    
    /**
     * Delegates to {@link TagTestModule#setBody}
     */
    protected void setBody(String body)
    {
        tagTestModule.setBody(body);
    }
    
    /**
     * Delegates to {@link TagTestModule#getBody}
     */
    protected String getBody()
    {
        return tagTestModule.getBody();
    }
    
    /**
     * Delegates to {@link TagTestModule#getTag}
     */
    protected TagSupport getTag()
    {
        return tagTestModule.getTag();
    }
    
    /**
     * Delegates to {@link TagTestModule#getMockPageContext}
     */
    protected MockPageContext getMockPageContext()
    {
        return tagTestModule.getMockPageContext();
    }
        
    /**
     * Delegates to {@link TagTestModule#doStartTag}
     */
    protected int doStartTag()
    {
        return tagTestModule.doStartTag();
    }
    
    /**
     * Delegates to {@link TagTestModule#doEndTag}
     */
    protected int doEndTag()
    {
        return tagTestModule.doEndTag();
    }
    
    /**
     * Delegates to {@link TagTestModule#doInitBody}
     */
    protected void doInitBody()
    {
        tagTestModule.doInitBody();
    }

    /**
     * Delegates to {@link TagTestModule#doAfterBody}
     */
    protected int doAfterBody()
    {
        return tagTestModule.doAfterBody();
    }

    /**
     * Delegates to {@link TagTestModule#release}
     */
    protected void release()
    {
        tagTestModule.release();
    }

    /**
     * Delegates to {@link TagTestModule#getOutput}
     */
    protected String getOutput()
    {
        return tagTestModule.getOutput();
    }
    
    /**
     * Delegates to {@link TagTestModule#verifyOutput}
     */
    protected void verifyOutput(String output)
    {
        tagTestModule.verifyOutput(output);
    }

    /**
     * Delegates to {@link TagTestModule#verifyOutputContains}
     */
    protected void verifyOutputContains(String output)
    {
        tagTestModule.verifyOutputContains(output);
    }
}
