package com.mockrunner.tag;

import java.util.Map;

//import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.TagSupport;

import com.mockrunner.base.HTMLOutputModule;
import com.mockrunner.base.HTMLOutputTestCase;
import com.mockrunner.mock.web.MockPageContext;

/**
 * Delegator for {@link com.mockrunner.tag.TagTestModule}. You can
 * subclass this adapter or use {@link com.mockrunner.tag.TagTestModule}
 * directly (so your test case can use another base class).
 * This adapter extends {@link com.mockrunner.base.BaseTestCase}.
 * It can be used if you want to use several modules in conjunction.
 * <b>This class is generated from the {@link com.mockrunner.tag.TagTestModule}
 * and should not be edited directly</b>.
 */
public class TagTestCaseAdapter extends HTMLOutputTestCase
{
    private TagTestModule tagTestModule;

    public TagTestCaseAdapter()
    {

    }

    public TagTestCaseAdapter(String name)
    {
        super(name);
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        tagTestModule = null;
    }

    /**
     * Creates the {@link com.mockrunner.tag.TagTestModule}. If you
     * overwrite this method, you must call <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        tagTestModule = createTagTestModule(getWebMockObjectFactory());
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
     * Delegates to {@link com.mockrunner.tag.TagTestModule#getMockPageContext}
     */
    protected MockPageContext getMockPageContext()
    {
        return tagTestModule.getMockPageContext();
    }

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#clearOutput}
     */
    protected void clearOutput()
    {
        tagTestModule.clearOutput();
    }

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#createNestedTag(Class, Map)}
     */
    protected NestedTag createNestedTag(Class tagClass, Map attributes)
    {
        return tagTestModule.createNestedTag(tagClass, attributes);
    }

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#createNestedTag(Class)}
     */
    protected NestedTag createNestedTag(Class tagClass)
    {
        return tagTestModule.createNestedTag(tagClass);
    }

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#createWrappedTag(Class, Map)}
     */
    /*protected JspTag createWrappedTag(Class tagClass, Map attributes)
    {
        return tagTestModule.createWrappedTag(tagClass, attributes);
    }*/

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#createWrappedTag(Class)}
     */
    /*protected JspTag createWrappedTag(Class tagClass)
    {
        return tagTestModule.createWrappedTag(tagClass);
    }*/

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#getWrappedTag}
     */
    /*protected JspTag getWrappedTag()
    {
        return tagTestModule.getWrappedTag();
    }*/

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#setTag(TagSupport)}
     */
    protected NestedTag setTag(TagSupport tag)
    {
        return tagTestModule.setTag(tag);
    }

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#setTag(TagSupport, Map)}
     */
    protected NestedTag setTag(TagSupport tag, Map attributes)
    {
        return tagTestModule.setTag(tag, attributes);
    }

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#setTag(JspTag)}
     */
    /*protected NestedTag setTag(JspTag tag)
    {
        return tagTestModule.setTag(tag);
    }*/

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#setTag(JspTag, Map)}
     */
    /*protected NestedTag setTag(JspTag tag, Map attributes)
    {
        return tagTestModule.setTag(tag, attributes);
    }*/

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#setDoRelease(boolean)}
     */
    protected void setDoRelease(boolean doRelease)
    {
        tagTestModule.setDoRelease(doRelease);
    }

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#setDoReleaseRecursive(boolean)}
     */
    protected void setDoReleaseRecursive(boolean doRelease)
    {
        tagTestModule.setDoReleaseRecursive(doRelease);
    }

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#populateAttributes}
     */
    protected void populateAttributes()
    {
        tagTestModule.populateAttributes();
    }

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#setBody(String)}
     */
    protected void setBody(String body)
    {
        tagTestModule.setBody(body);
    }

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#getNestedTag}
     */
    protected NestedTag getNestedTag()
    {
        return tagTestModule.getNestedTag();
    }

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#doTag}
     */
    /*protected void doTag()
    {
        tagTestModule.doTag();
    }*/

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#doStartTag}
     */
    protected int doStartTag()
    {
        return tagTestModule.doStartTag();
    }

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#doEndTag}
     */
    protected int doEndTag()
    {
        return tagTestModule.doEndTag();
    }

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#doInitBody}
     */
    protected void doInitBody()
    {
        tagTestModule.doInitBody();
    }

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#doAfterBody}
     */
    protected int doAfterBody()
    {
        return tagTestModule.doAfterBody();
    }

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#processTagLifecycle}
     */
    protected int processTagLifecycle()
    {
        return tagTestModule.processTagLifecycle();
    }

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#release}
     */
    protected void release()
    {
        tagTestModule.release();
    }

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#createTag(Class, Map)}
     */
    protected TagSupport createTag(Class tagClass, Map attributes)
    {
        return tagTestModule.createTag(tagClass, attributes);
    }

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#createTag(Class)}
     */
    protected TagSupport createTag(Class tagClass)
    {
        return tagTestModule.createTag(tagClass);
    }

    /**
     * Delegates to {@link com.mockrunner.tag.TagTestModule#getTag}
     */
    protected TagSupport getTag()
    {
        return tagTestModule.getTag();
    }
}
