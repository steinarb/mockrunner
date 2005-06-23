package com.mockrunner.tag;

import java.util.Map;

//import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.TagSupport;

import com.mockrunner.base.BasicHTMLOutputTestCase;
import com.mockrunner.base.HTMLOutputModule;
import com.mockrunner.mock.web.MockPageContext;
import com.mockrunner.mock.web.WebMockObjectFactory;

/**
 * Delegator for {@link com.mockrunner.tag.TagTestModule}. You can
 * subclass this adapter or use {@link com.mockrunner.tag.TagTestModule}
 * directly (so your test case can use another base class).
 * This basic adapter can be used if you don't need any other modules. It
 * does not extend {@link com.mockrunner.base.BaseTestCase}. If you want
 * to use several modules in conjunction, consider subclassing
 * {@link com.mockrunner.tag.TagTestCaseAdapter}.
 * <b>This class is generated from the {@link com.mockrunner.tag.TagTestModule}
 * and should not be edited directly</b>.
 */
public class BasicTagTestCaseAdapter extends BasicHTMLOutputTestCase
{
    private TagTestModule tagTestModule;
    private WebMockObjectFactory webMockObjectFactory;

    public BasicTagTestCaseAdapter()
    {

    }

    public BasicTagTestCaseAdapter(String name)
    {
        super(name);
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        tagTestModule = null;
        webMockObjectFactory = null;
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
     * Creates a {@link com.mockrunner.mock.web.WebMockObjectFactory}.
     * @return the created {@link com.mockrunner.mock.web.WebMockObjectFactory}
     */
    protected WebMockObjectFactory createWebMockObjectFactory()
    {
        return new WebMockObjectFactory();
    }

    /**
     * Same as <code>createWebMockObjectFactory(otherFactory, true)</code>.
     */
    protected WebMockObjectFactory createWebMockObjectFactory(WebMockObjectFactory otherFactory)
    {
        return new WebMockObjectFactory(otherFactory);
    }

    /**
     * Creates a {@link com.mockrunner.mock.web.WebMockObjectFactory} based on another 
     * {@link com.mockrunner.mock.web.WebMockObjectFactory}.
     * The created {@link com.mockrunner.mock.web.WebMockObjectFactory} will have its own
     * request and response objects. If you set <i>createNewSession</i>
     * to <code>true</code> it will also have its own session object.
     * The two factories will share one <code>ServletContext</code>.
     * Especially important for multithreading tests.
     * If you set <i>createNewSession</i> to false, the two factories
     * will share one session. This setting simulates multiple requests
     * from the same client.
     * @param otherFactory the other factory
     * @param createNewSession create a new session for the new factory
     * @return the created {@link com.mockrunner.mock.web.WebMockObjectFactory}
     */
    protected WebMockObjectFactory createWebMockObjectFactory(WebMockObjectFactory otherFactory, boolean createNewSession)
    {
        return new WebMockObjectFactory(otherFactory, createNewSession);
    }

    /**
     * Gets the {@link com.mockrunner.mock.web.WebMockObjectFactory}.
     * @return the {@link com.mockrunner.mock.web.WebMockObjectFactory}
     */
    protected WebMockObjectFactory getWebMockObjectFactory()
    {
        synchronized(WebMockObjectFactory.class)
        {
            if(webMockObjectFactory == null)
            {
                webMockObjectFactory = createWebMockObjectFactory();
            }
        }
        return webMockObjectFactory;
    }

    /**
     * Sets the {@link com.mockrunner.mock.web.WebMockObjectFactory}.
     * @param webMockObjectFactory the {@link com.mockrunner.mock.web.WebMockObjectFactory}
     */
    protected void setWebMockObjectFactory(WebMockObjectFactory webMockObjectFactory)
    {
        this.webMockObjectFactory = webMockObjectFactory;
    }

    /**
     * Creates a {@link com.mockrunner.tag.TagTestModule} based on the current
     * {@link com.mockrunner.mock.web.WebMockObjectFactory}.
     * Same as <code>createTagTestModule(getWebMockObjectFactory())</code>.
     * @return the created {@link com.mockrunner.tag.TagTestModule}
     */
    protected TagTestModule createTagTestModule()
    {
        return new TagTestModule(getWebMockObjectFactory());
    }

    /**
     * Creates a {@link com.mockrunner.tag.TagTestModule} with the specified
     * {@link com.mockrunner.mock.web.WebMockObjectFactory}.
     * @return the created {@link com.mockrunner.tag.TagTestModule}
     */
    protected TagTestModule createTagTestModule(WebMockObjectFactory mockFactory)
    {
        return new TagTestModule(mockFactory);
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
