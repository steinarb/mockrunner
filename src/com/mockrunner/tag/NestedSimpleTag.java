package com.mockrunner.tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.mock.web.MockJspFragment;

/**
 * Implementation of {@link NestedTag} wrapping tags of
 * type <code>SimpleTag</code>. <code>NestedSimpleTag</code> instances 
 * are created with the help of {@link TagTestModule#createNestedTag}. 
 * You do not need to create them on your own in the tests.
 */
public class NestedSimpleTag extends SimpleTagSupport implements NestedTag
{
    private SimpleTag tag;
    private JspContext jspContext;
    private JspFragment jspBody;
    private Map attributes;
    
    /**
     * Constructor for a tag with an empty attribute map.
     * @param tag the tag
     * @param jspContext the corresponding <code>JspContext</code>
     */
    public NestedSimpleTag(SimpleTag tag, JspContext jspContext)
    {
        this(tag, jspContext, new HashMap());
    }
    
    /**
     * Constructor for a tag with the specified attribute map.
     * @param tag the tag
     * @param jspContext the corresponding <code>JspContext</code>
     * @param attributes the attribute map
     */
    public NestedSimpleTag(SimpleTag tag, JspContext jspContext, Map attributes)
    {
        this.tag = tag;
        this.jspContext = jspContext;
        jspBody = new MockJspFragment(jspContext, tag);
        tag.setJspContext(jspContext);
        tag.setJspBody(jspBody);
        this.attributes = attributes;
    }
    
    /**
     * Constructor for a tag with an empty attribute map.
     * @param tag the tag
     * @param jspContext the corresponding <code>JspContext</code>
     */
    public NestedSimpleTag(SimpleTagSupport tag, JspContext jspContext)
    {
        this(tag, jspContext, new HashMap());
    }
    
    /**
     * Constructor for a tag with the specified attribute map.
     * @param tag the tag
     * @param jspContext the corresponding <code>JspContext</code>
     * @param attributes the attribute map
     */
    public NestedSimpleTag(SimpleTagSupport tag, JspContext jspContext, Map attributes)
    {
        this((SimpleTag)tag, jspContext, attributes);
    }
    
    /**
     * Implementation of {@link NestedTag#setDoRelease}.
     * Does nothing in this case.
     */
    public void setDoRelease(boolean doRelease)
    {

    }
    
    /**
     * Implementation of {@link NestedTag#setDoReleaseRecursive}.
     * Does nothing in this case.
     */
    public void setDoReleaseRecursive(boolean doRelease)
    {
        
    }
    
    /**
     * Implementation of {@link NestedTag#populateAttributes}.
     */
    public void populateAttributes()
    {
        TagUtil.populateTag(tag, attributes, false);
    }
    
    /**
     * Implementation of {@link NestedTag#doLifecycle} for simple
     * tags. Returns <code>-1</code> in this case, because
     * <code>doTag()</code> does not have a return value.
     */
    public int doLifecycle() throws JspException
    {
        try
        {
            populateAttributes();
            tag.doTag();
        } 
        catch(IOException exc)
        {
            throw new NestedApplicationException(exc);
        }
        return -1;
    }
    
    /**
     * Implementation of {@link NestedTag#getTag}.
     * Should not be called and throws a <code>RuntimeException</code>,
     * because a simple tag is not an instance of <code>TagSupport</code>.
     */
    public TagSupport getTag()
    {
        throw new RuntimeException("getTag() method cannot be called for simple tags.");
    }
    
    /**
     * Implementation of {@link NestedTag#getWrappedTag}.
     */
    public JspTag getWrappedTag()
    {
        return tag;
    }
    
    /**
     * Implementation of {@link NestedTag#removeChilds}.
     */
    public void removeChilds()
    {
        if(null != jspBody && jspBody instanceof MockJspFragment)
        {
            ((MockJspFragment)jspBody).removeChilds();
        }
    }
    
    /**
     * Implementation of {@link NestedTag#getChilds}.
     */
    public List getChilds()
    {
        if(null != jspBody && jspBody instanceof MockJspFragment)
        {
            return ((MockJspFragment)jspBody).getChilds();
        }
        return null;
    }
    
    /**
     * Implementation of {@link NestedTag#getChild}.
     */
    public Object getChild(int index)
    {
        if(null != jspBody && jspBody instanceof MockJspFragment)
        {
            return ((MockJspFragment)jspBody).getChild(index);
        }
        return null;
    }
    
    /**
     * Implementation of {@link NestedTag#addTextChild}.
     */
    public void addTextChild(String text)
    {
        if(null != jspBody && jspBody instanceof MockJspFragment)
        {
            ((MockJspFragment)jspBody).addTextChild(text);
        }
    }
    
    /**
     * Implementation of {@link NestedTag#addDynamicChild}.
     */
    public void addDynamicChild(DynamicChild child)
    {
        if(null != jspBody && jspBody instanceof MockJspFragment)
        {
            ((MockJspFragment)jspBody).addDynamicChild(child);
        }
    }
    
    /**
     * Implementation of {@link NestedTag#addTagChild(Class)}.
     */
    public NestedTag addTagChild(Class tag)
    {
        if(null != jspBody && jspBody instanceof MockJspFragment)
        {
            return ((MockJspFragment)jspBody).addTagChild(tag);
        }
        return null;
    }
    
    /**
     * Implementation of {@link NestedTag#addTagChild(Class, Map)}.
     */
    public NestedTag addTagChild(Class tag, Map attributeMap)
    {
        if(null != jspBody && jspBody instanceof MockJspFragment)
        {
            return ((MockJspFragment)jspBody).addTagChild(tag, attributeMap);
        }
        return null;
    }
    
    /**
     * Implementation of {@link NestedTag#addTagChild(TagSupport)}.
     */
    public NestedTag addTagChild(TagSupport tag)
    {
        return addTagChild(tag, new HashMap());
    }
    
    /**
     * Implementation of {@link NestedTag#addTagChild(TagSupport, Map)}.
     */
    public NestedTag addTagChild(TagSupport tag, Map attributeMap)
    {
        return addTagChild((JspTag)tag, attributeMap);
    }
    
    /**
     * Implementation of {@link NestedTag#addTagChild(JspTag)}.
     */
    public NestedTag addTagChild(JspTag tag)
    {
        if(null != jspBody && jspBody instanceof MockJspFragment)
        {
            return ((MockJspFragment)jspBody).addTagChild(tag);
        }
        return null;
    }
    
    /**
     * Implementation of {@link NestedTag#addTagChild(JspTag, Map)}.
     */
    public NestedTag addTagChild(JspTag tag, Map attributeMap)
    {
        if(null != jspBody && jspBody instanceof MockJspFragment)
        {
            return ((MockJspFragment)jspBody).addTagChild(tag, attributeMap);
        }
        return null;
    }
    
    /**
     * Delegates to wrapped tag.
     */
    public void doTag() throws JspException, IOException
    {
        tag.doTag();
    }
    
    /**
     * Returns the body fragment.
     * @return the body fragment
     */
    public JspFragment getJspBody()
    {
        return jspBody;
    }
    
    /**
     * Returns the <code>JspContext</code>.
     * @return the <code>JspContext</code>
     */
    public JspContext getJspContext()
    {
        return jspContext;
    }
    
    /**
     * Delegates to wrapped tag.
     */
    public JspTag getParent()
    {
        return tag.getParent();
    }
    
    /**
     * Delegates to wrapped tag.
     */
    public void setJspBody(JspFragment jspBody)
    {
        this.jspBody = jspBody;
        tag.setJspBody(jspBody);
    }
    
    /**
     * Delegates to wrapped tag. Also calls <code>setJspContext</code>
     * on the body fragment, if the body fragment is an instance of
     * {@link com.mockrunner.mock.web.MockJspFragment}
     */
    public void setJspContext(JspContext jspContext)
    {
        this.jspContext = jspContext;
        tag.setJspContext(jspContext);
        if(null != jspBody && jspBody instanceof MockJspFragment)
        {
           ((MockJspFragment)jspBody).setJspContext(jspContext);
        }
    }
    
    /**
     * Delegates to wrapped tag.
     */
    public void setParent(JspTag parent)
    {
        tag.setParent(parent);
    }
    
    /**
     * Dumps the content of this and the nested tags.
     */
    public String toString()
    {
        return TagUtil.dumpTag(this, new StringBuffer(), 0);
    }
}
