package com.mockrunner.tag;

import java.util.Map;

import javax.servlet.jsp.tagext.TagSupport;

import com.mockrunner.base.HTMLOutputModule;
import com.mockrunner.base.HTMLOutputTestCase;
import com.mockrunner.base.WebTestModule;
import com.mockrunner.mock.web.MockPageContext;

/**
 * Delegator for {@link TagTestModule}. You can
 * subclass this adapter or use {@link TagTestModule}
 * directly (so your test case can use another base
 * class).
 */
public class TagTestCaseAdapter extends HTMLOutputTestCase
{
    private TagTestModule tagTestModule;
    
    public TagTestCaseAdapter()
    {
        
    }

    public TagTestCaseAdapter(String arg0)
    {
        super(arg0);
    }
    
    protected void tearDown() throws Exception
    {
        super.tearDown();
        tagTestModule = null;
    }

    /**
     * Creates the {@link com.mockrunner.tag.TagTestModule}. If you
     * overwrite this method, you must call 
     * <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        tagTestModule = createTagTestModule(getWebMockObjectFactory());
    }
    
    /**
     * Returns the {@link com.mockrunner.tag.TagTestModule} as 
     * {@link com.mockrunner.base.WebTestModule}.
     * @return the {@link com.mockrunner.base.WebTestModule}
     */
    protected WebTestModule getWebTestModule()
    {
        return tagTestModule;
    }
    
    /**
     * Returns the {@link com.mockrunner.tag.TagTestModule} as 
     * {@link com.mockrunner.base.HTMLOutputModule}.
     * @return the {@link com.mockrunner.base.HTMLOutputModule}
     */
    protected HTMLOutputModule getHTMLOutputModule()
    {
        return tagTestModule;
    }
    
    /**
     * Gets the {@link com.mockrunner.tag.TagTestModule}. 
     * @return the {@link com.mockrunner.tag.TagTestModule}
     */
    protected TagTestModule getTagTestModule()
    {
        return tagTestModule;
    }
    
    /**
     * Sets the {@link com.mockrunner.tag.TagTestModule}. 
     * @param tagTestModule the {@link com.mockrunner.tag.TagTestModule}
     */
    protected void setTagTestModule(TagTestModule tagTestModule)
    {
        this.tagTestModule = tagTestModule;
    }
    
    /**
     * Delegates to {@link TagTestModule#setCaseSensitive}
     */
    public void setCaseSensitive(boolean caseSensitive)
    {
        tagTestModule.setCaseSensitive(caseSensitive);
    }

    /**
     * Delegates to {@link TagTestModule#createTag(Class)}
     */
    protected TagSupport createTag(Class tagClass)
    {
        return tagTestModule.createTag(tagClass);
    }
    
    /**
     * Delegates to {@link TagTestModule#createTag(Class, Map)}
     */
    protected TagSupport createTag(Class tagClass, Map attributes)
    {
        return tagTestModule.createTag(tagClass, attributes);
    }
    
    /**
     * Delegates to {@link TagTestModule#createNestedTag(Class)}
     */
    protected NestedTag createNestedTag(Class tagClass)
    {
        return tagTestModule.createNestedTag(tagClass);
    }
    
    /**
     * Delegates to {@link TagTestModule#createNestedTag(Class, Map)}
     */
    protected NestedTag createNestedTag(Class tagClass, Map attributes)
    {
        return tagTestModule.createNestedTag(tagClass, attributes);
    }
    
    /**
     * Delegates to {@link TagTestModule#setTag(TagSupport)}
     */
    protected NestedTag setTag(TagSupport tag)
    {
        return tagTestModule.setTag(tag);
    }
    
    /**
     * Delegates to {@link TagTestModule#setTag(TagSupport, Map)}
     */
    protected NestedTag setTag(TagSupport tag, Map attributes)
    {
        return tagTestModule.setTag(tag, attributes);
    }
    
    /**
     * Delegates to {@link TagTestModule#setDoRelease}
     */
    protected void setDoRelease(boolean doRelease)
    {
        tagTestModule.setDoRelease(doRelease);
    }
    
    /**
     * Delegates to {@link TagTestModule#setDoReleaseRecursive}
     */
    protected void setDoReleaseRecursive(boolean doRelease)
    {
        tagTestModule.setDoReleaseRecursive(doRelease);
    }
    
    /**
     * Delegates to {@link TagTestModule#populateAttributes}
     */
    protected void populateAttributes()
    {
        tagTestModule.populateAttributes();
    }
    
    /**
     * Delegates to {@link TagTestModule#setBody}
     */
    protected void setBody(String body)
    {
        tagTestModule.setBody(body);
    }
    
    /**
     * Delegates to {@link TagTestModule#getTag}
     */
    protected TagSupport getTag()
    {
        return tagTestModule.getTag();
    }
    
    /**
     * Delegates to {@link TagTestModule#getNestedTag}
     */
    protected NestedTag getNestedTag()
    {
        return tagTestModule.getNestedTag();
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
     * Delegates to {@link TagTestModule#processTagLifecycle}
     */
    protected int processTagLifecycle()
    {
        return tagTestModule.processTagLifecycle();
    }
    
    /**
     * Delegates to {@link TagTestModule#clearOutput}
     */
    protected void clearOutput()
    {
        tagTestModule.clearOutput();
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
    
    /**
     * Delegates to {@link TagTestModule#verifyOutputRegularExpression}
     */
    protected void verifyOutputRegularExpression(String output)
    {
        tagTestModule.verifyOutputRegularExpression(output);
    }
}
