package com.mockrunner.tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mockrunner.base.HTMLOutputModule;
import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.mock.web.MockJspWriter;
import com.mockrunner.mock.web.MockPageContext;
import com.mockrunner.mock.web.WebMockObjectFactory;

/**
 * Module for custom tag tests. Simulates the container by
 * performing the tag lifecycle.
 */
public class TagTestModule extends HTMLOutputModule
{
    private final static Log log = LogFactory.getLog(TagTestModule.class);
    private WebMockObjectFactory mockFactory;
    private NestedTag tag;

    public TagTestModule(WebMockObjectFactory mockFactory)
    {
        super(mockFactory);
        this.mockFactory = mockFactory;
    }
    
    /**
     * Creates a tag. Internally a {@link NestedTag}
     * is created but the wrapped tag is returned. If you
     * simply want to test the output of the tag without 
     * nesting other tags, you do not have to care about the
     * {@link NestedTag}, just use the returned instance.
     * An empty attribute <code>Map</code> will be used for
     * the tag.
     * @param tagClass the class of the tag
     * @return instance of <code>TagSupport</code> or <code>BodyTagSupport</code>
     * @throws <code>RuntimeException</code>, if the created tag
     *         is not an instance of <code>TagSupport</code>
     */
    public TagSupport createTag(Class tagClass)
    {
        if(!TagSupport.class.isAssignableFrom(tagClass))
        {
            throw new IllegalArgumentException("specified class is not an instance of TagSupport. Please use createWrappedTag");
        }
        return createTag(tagClass, new HashMap());
    }
    
    /**
     * Creates a tag. Internally a {@link NestedTag}
     * is created but the wrapped tag is returned. If you
     * simply want to test the output of the tag without 
     * nesting other tags, you do not have to care about the
     * {@link NestedTag}, just use the returned instance.
     * The attributes <code>Map</code> contains the attributes
     * of this tag (<i>propertyname</i> maps to <i>propertyvalue</i>).
     * The attributes are populated (i.e. the tags setters are called)
     * during the lifecycle or with an explicit call of
     * {@link #populateAttributes}.
     * @param tagClass the class of the tag
     * @param attributes the attribute map
     * @return instance of <code>TagSupport</code> or <code>BodyTagSupport</code>
     * @throws <code>RuntimeException</code>, if the created tag
     *         is not an instance of <code>TagSupport</code>
     */
    public TagSupport createTag(Class tagClass, Map attributes)
    {
        if(!TagSupport.class.isAssignableFrom(tagClass))
        {
            throw new IllegalArgumentException("specified class is not an instance of TagSupport. Please use createWrappedTag");
        }
        return createNestedTag(tagClass, attributes).getTag();
    }
    
    /**
     * Creates a tag. Internally a {@link NestedTag}
     * is created but the wrapped tag is returned. If you
     * simply want to test the output of the tag without 
     * nesting other tags, you do not have to care about the
     * {@link NestedTag}, just use the returned instance.
     * An empty attribute <code>Map</code> will be used for
     * the tag.
     * This method can be used for all kind of tags. The tag
     * class does not need to be a subclass of <code>TagSupport</code>.
     * @param tagClass the class of the tag
     * @return instance of <code>JspTag</code>
     */
    public JspTag createWrappedTag(Class tagClass)
    {
        return createWrappedTag(tagClass, new HashMap());
    }
    
    /**
     * Creates a tag. Internally a {@link NestedTag}
     * is created but the wrapped tag is returned. If you
     * simply want to test the output of the tag without 
     * nesting other tags, you do not have to care about the
     * {@link NestedTag}, just use the returned instance.
     * The attributes <code>Map</code> contains the attributes
     * of this tag (<i>propertyname</i> maps to <i>propertyvalue</i>).
     * The attributes are populated (i.e. the tags setters are called)
     * during the lifecycle or with an explicit call of
     * {@link #populateAttributes}.
     * This method can be used for all kind of tags. The tag
     * class does not need to be a subclass of <code>TagSupport</code>.
     * @param tagClass the class of the tag
     * @param attributes the attribute map
     * @return instance of <code>JspTag</code>
     */
    public JspTag createWrappedTag(Class tagClass, Map attributes)
    {
        return createNestedTag(tagClass, attributes).getWrappedTag();
    }
    
    /**
     * Creates a {@link NestedTag} and returns it. You can
     * add child tags or body blocks to the {@link NestedTag}.
     * Use {@link #getTag} to get the wrapped tag.
     * An empty attribute <code>Map</code> will be used for
     * the tag.
     * @param tagClass the class of the tag
     * @return instance of {@link NestedStandardTag}, {@link NestedBodyTag} or 
     *                     {@link NestedSimpleTag}
     */
    public NestedTag createNestedTag(Class tagClass)
    {
        return createNestedTag(tagClass, new HashMap());
    }
    
    /**
     * Creates a {@link NestedTag} and returns it. You can
     * add child tags or body blocks to the {@link NestedTag}.
     * Use {@link #getTag} to get the wrapped tag.
     * The attributes <code>Map</code> contains the attributes
     * of this tag (<i>propertyname</i> maps to <i>propertyvalue</i>).
     * The attributes are populated (i.e. the tags setters are called)
     * during the lifecycle or with an explicit call of
     * {@link #populateAttributes}.
     * @param tagClass the class of the tag
     * @param attributes the attribute map
     * @return instance of {@link NestedStandardTag}, {@link NestedBodyTag} or 
     *                     {@link NestedSimpleTag}
     */
    public NestedTag createNestedTag(Class tagClass, Map attributes)
    {
        try
        {
            this.tag = (NestedTag)TagUtil.createNestedTagInstance(tagClass, getMockPageContext(), attributes);
            return this.tag;
        }
        catch(IllegalArgumentException exc)
        {
            throw exc;
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Creates a {@link NestedTag} and returns it. You can
     * add child tags or body blocks to the {@link NestedTag}.
     * Use {@link #getTag} to get the wrapped tag.
     * An empty attribute <code>Map</code> will be used for
     * the tag.
     * @param tag the tag
     * @return instance of {@link NestedStandardTag} or {@link NestedBodyTag}
     */
    public NestedTag setTag(TagSupport tag)
    {
        return setTag(tag, new HashMap());
    }
    
    /**
     * Creates a {@link NestedTag} and returns it. You can
     * add child tags or body blocks to the {@link NestedTag}.
     * Use {@link #getTag} to get the wrapped tag.
     * The attributes <code>Map</code> contains the attributes
     * of this tag (<i>propertyname</i> maps to <i>propertyvalue</i>).
     * The attributes are populated (i.e. the tags setters are called)
     * during the lifecycle or with an explicit call of
     * {@link #populateAttributes}.
     * @param tag the tag
     * @param attributes the attribute map
     * @return instance of {@link NestedStandardTag} or {@link NestedBodyTag}
     */
    public NestedTag setTag(TagSupport tag, Map attributes)
    {
        try
        {
            this.tag = (NestedTag)TagUtil.createNestedTagInstance(tag, getMockPageContext(), attributes);
            return this.tag;
        }
        catch(IllegalArgumentException exc)
        {
            throw exc;
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Creates a {@link NestedTag} and returns it. You can
     * add child tags or body blocks to the {@link NestedTag}.
     * Use {@link #getTag} to get the wrapped tag.
     * An empty attribute <code>Map</code> will be used for
     * the tag.
     * This method can be used for all kind of tags. The tag
     * class does not need to be a subclass of <code>TagSupport</code>.
     * @param tag the tag
     * @return instance of {@link NestedStandardTag}, {@link NestedBodyTag} or 
     *                     {@link NestedSimpleTag}
     */
    public NestedTag setTag(JspTag tag)
    {
        return setTag(tag, new HashMap());
    }
    
    /**
     * Creates a {@link NestedTag} and returns it. You can
     * add child tags or body blocks to the {@link NestedTag}.
     * Use {@link #getTag} to get the wrapped tag.
     * The attributes <code>Map</code> contains the attributes
     * of this tag (<i>propertyname</i> maps to <i>propertyvalue</i>).
     * The attributes are populated (i.e. the tags setters are called)
     * during the lifecycle or with an explicit call of
     * {@link #populateAttributes}.
     * This method can be used for all kind of tags. The tag
     * class does not need to be a subclass of <code>TagSupport</code>.
     * @param tag the tag
     * @param attributes the attribute map
     * @return instance of {@link NestedStandardTag}, {@link NestedBodyTag} or 
     *                     {@link NestedSimpleTag}
     */
    public NestedTag setTag(JspTag tag, Map attributes)
    {
        try
        {
            this.tag = (NestedTag)TagUtil.createNestedTagInstance(tag, getMockPageContext(), attributes);
            return this.tag;
        }
        catch(IllegalArgumentException exc)
        {
            throw exc;
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Specify if the <code>release</code> method should be called
     * before populating a tag. Delegates to {@link NestedTag#setDoRelease}
     * Defaults to <code>false</code>. It's the container behaviour to call 
     * <code>release</code>, but it's usually not necessary in the tests, 
     * because the tag instances are not reused during a test run.
     * @param doRelease should release be called
     */
    public void setDoRelease(boolean doRelease)
    {
        if(null == tag)
        {
            throw new RuntimeException("Not current tag set");
        }
        tag.setDoRelease(doRelease);
    }
    
    /**
     * Specify if the <code>release</code> method should be called
     * before populating a tag. Delegates to {@link NestedTag#setDoReleaseRecursive}
     * Defaults to <code>false</code>. It's the container behaviour to call 
     * <code>release</code>, but it's usually not necessary in the tests, 
     * because the tag instances are not reused during a test run.
     * @param doRelease should release be called
     */
    public void setDoReleaseRecursive(boolean doRelease)
    {
        if(null == tag)
        {
            throw new RuntimeException("Not current tag set");
        }
        tag.setDoReleaseRecursive(doRelease);
    }
    
    /**
     * Populates the attributes of the underlying tag by
     * calling {@link NestedTag#populateAttributes}. The setters
     * of the tag are called. Please note that child tags are not
     * populated. This is done during the lifecycle.
     */
    public void populateAttributes()
    {
        if(null == tag)
        {
            throw new RuntimeException("Not current tag set");
        }
        tag.populateAttributes();
    }
    
    /**
     * Sets the body of the tag as a static string. Please
     * note that all childs of the underlying {@link NestedTag}
     * are deleted and the static content is set. If you want
     * to use nested tags, please use the method {@link NestedTag#addTextChild}
     * to set static content.
     * @param body the static body content
     */
    public void setBody(String body)
    {
        if(null == tag)
        {
            throw new RuntimeException("Not current tag set");
        }
        tag.removeChilds();
        tag.addTextChild(body);
    }
    
    /**
     * Returns the current wrapped tag.
     * @return instance of <code>TagSupport</code> or <code>BodyTagSupport</code>
     * @throws <code>RuntimeException</code>, if the wrapped tag
     *         is not an instance of <code>TagSupport</code>
     */
    public TagSupport getTag()
    {
        if(null == tag) return null;
        return tag.getTag();
    }
    
    /**
     * Returns the current wrapped tag.
     * This method can be used for all kind of tags. The tag
     * class does not need to be a subclass of <code>TagSupport</code>.
     * @return instance of <code>JspTag</code>
     */
    public JspTag getWrappedTag()
    {
        if(null == tag) return null;
        return tag.getWrappedTag();
    }
    
    /**
     * Returns the current nested tag. You can
     * add child tags or body blocks to the {@link NestedTag}.
     * Use {@link #getTag} to get the wrapped tag.
     * @return instance of {@link NestedStandardTag} or {@link NestedBodyTag}
     */
    public NestedTag getNestedTag()
    {
        return tag;
    }
    
    /**
     * Returns the <code>MockPageContext</code> object.
     * Delegates to {@link com.mockrunner.mock.web.WebMockObjectFactory#getMockPageContext}.
     * @return the MockPageContext
     */
    public MockPageContext getMockPageContext()
    {
        return mockFactory.getMockPageContext();
    }
    
    /**
     * Calls the <code>doStartTag</code> method of the current tag.
     * @throws <code>RuntimeException</code>, if the tag
     *         is not a simple tag
     */
    public void doTag()
    {
        if(null == tag)
        {
            throw new RuntimeException("No current tag set");
        }
        if(!isSimpleTag()) 
        {
            throw new RuntimeException("Tag is no simple tag");
        }
        try
        {
            ((NestedSimpleTag)tag).doTag();
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Calls the <code>doStartTag</code> method of the current tag.
     * @return the result of <code>doStartTag</code>
     * @throws <code>RuntimeException</code>, if the tag
     *         is a simple tag
     */
    public int doStartTag()
    {
        if(null == tag)
        {
            throw new RuntimeException("No current tag set");
        }
        if(isSimpleTag()) 
        {
            throw new RuntimeException("Cannot call doStartTag() on simple tags");
        }
        try
        {
            return ((Tag)tag).doStartTag();
        }
        catch(JspException exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Calls the <code>doEndTag</code> method of the current tag.
     * @return the result of <code>doEndTag</code>
     * @throws <code>RuntimeException</code>, if the tag
     *         is a simple tag
     */
    public int doEndTag()
    {
        if(null == tag)
        {
            throw new RuntimeException("No current tag set");
        }
        if(isSimpleTag()) 
        {
            throw new RuntimeException("Cannot call doEndTag() on simple tags");
        }
        try
        {
            return ((Tag)tag).doEndTag();
        }
        catch(JspException exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Calls the <code>doInitBody</code> method of the current tag.
     * @throws RuntimeException if the current tag is no body tag
     * @throws <code>RuntimeException</code>, if the tag
     *         is a simple tag
     */
    public void doInitBody()
    {
        if(null == tag)
        {
            throw new RuntimeException("No current tag set");
        }
        if(!isBodyTag()) 
        {
            throw new RuntimeException("Tag is no body tag");
        }
        try
        {
            NestedBodyTag bodyTag = (NestedBodyTag)tag;
            bodyTag.doInitBody();
        }
        catch(JspException exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }

    /**
     * Calls the <code>doAfterBody</code> method of the current tag.
     * @return the result of <code>doAfterBody</code>
     * @throws <code>RuntimeException</code>, if the tag
     *         is a simple tag
     */
    public int doAfterBody()
    {
        if(null == tag)
        {
            throw new RuntimeException("No current tag set");
        }
        if(isSimpleTag()) 
        {
            throw new RuntimeException("Cannot call doAfterBody() on simple tags");
        }
        try
        {
            return ((TagSupport)tag).doAfterBody();
        }
        catch(JspException exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }

    /**
     * Calls the <code>release</code> method of the current tag.
     * @throws <code>RuntimeException</code>, if the tag
     *         is a simple tag
     */
    public void release()
    {
        if(isSimpleTag())
        {
            throw new RuntimeException("Cannot call release() on simple tags");
        }
        ((Tag)tag).release();
    }
    
    /**
     * Performs the tags lifecycle by calling {@link NestedTag#doLifecycle}.
     * All <code>doBody</code> and <code>doTag</code> methods are called as 
     * in the real web container. The evaluation of the body is simulated 
     * by performing the lifecycle recursively for all childs of the 
     * {@link NestedTag}.
     * @return the result of the final <code>doEndTag</code> call or -1 in
     *         the case of a simple tag
     */
    public int processTagLifecycle()
    {
        if(null == tag)
        {
            throw new RuntimeException("No current tag set");
        }
        try
        {
            return ((NestedTag)tag).doLifecycle();
        }
        catch(JspException exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Resets the output buffer.
     */
    public void clearOutput()
    {
        MockJspWriter writer = (MockJspWriter)mockFactory.getMockPageContext().getOut();
        try
        {
            writer.clearBuffer();
        }
        catch(IOException exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Gets the output data the current tag has rendered. Makes only sense
     * after calling at least {@link #doStartTag} or {@link #processTagLifecycle}
     * @return the output data
     */
    public String getOutput()
    {
        MockJspWriter writer = (MockJspWriter)mockFactory.getMockPageContext().getOut();
        return writer.getOutputAsString();
    }

    private boolean isBodyTag()
    {
        return (tag instanceof NestedBodyTag);
    }
    
    private boolean isSimpleTag()
    {
        return (tag instanceof NestedSimpleTag);
    }
}
