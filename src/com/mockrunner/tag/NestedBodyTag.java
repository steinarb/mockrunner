package com.mockrunner.tag;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.mockrunner.mock.web.MockBodyContent;
import com.mockrunner.util.TagUtil;

/**
 * Implementation of {@link NestedTag} wrapping tags of
 * type <code>BodyTagSupport</code>. <code>NestedBodyTag</code> instances 
 * are created with the help of {@link TagTestModule#createNestedTag}. 
 * You do not need to create them on your own in the tests.
 */
public class NestedBodyTag extends BodyTagSupport implements NestedTag
{
	private BodyTagSupport tag;
	private PageContext pageContext;
	private Map attributes;
	private List childs;
	private boolean doRelease;
	
    /**
     * Constructor for a tag with an empty attribute map.
     * @param tag the tag
     * @param pageContext the corresponding <code>PageContext</code>
     */
	public NestedBodyTag(BodyTagSupport tag, PageContext pageContext)
	{
		this(tag, pageContext, new HashMap());
	}
	
    /**
     * Constructor for a tag with the specified attribute map.
     * @param tag the tag
     * @param pageContext the corresponding <code>PageContext</code>
     * @param attributes the attribute map
     */
	public NestedBodyTag(BodyTagSupport tag, PageContext pageContext, Map attributes)
	{
		this.tag = tag;
		this.pageContext = pageContext;
		tag.setPageContext(pageContext);
		childs = new ArrayList();
		this.attributes = attributes;
		doRelease = false;
	}
	
    /**
     * Implementation of {@link NestedTag#setDoRelease}.
     */
	public void setDoRelease(boolean doRelease)
	{
		this.doRelease = doRelease;
	}
	
	/**
     * Implementation of {@link NestedTag#setDoReleaseRecursive}.
     */
	public void setDoReleaseRecursive(boolean doRelease)
	{
		this.doRelease = doRelease;
		for(int ii = 0; ii < childs.size(); ii++)
		{
			Object child = childs.get(ii);
            if(child instanceof NestedTag)
            {
                ((NestedTag)child).setDoReleaseRecursive(doRelease);
            }
		}
	}
    
    /**
     * Implementation of {@link NestedTag#populateAttributes}.
     */
    public void populateAttributes()
    {
        TagUtil.populateTag(tag, attributes, doRelease);
    }
	
    /**
     * Implementation of {@link NestedTag#doLifecycle} for body
     * tags.
     */
	public int doLifecycle() throws JspException
	{
        populateAttributes();
		int result = tag.doStartTag();
		if(Tag.EVAL_BODY_INCLUDE == result)
		{
			TagUtil.evalBody(childs, pageContext);
			while(IterationTag.EVAL_BODY_AGAIN == doAfterBody())
			{
				TagUtil.evalBody(childs, pageContext);
			}
		}
		else if(BodyTag.EVAL_BODY_BUFFERED == result)
		{
			MockBodyContent bodyContent = (MockBodyContent)pageContext.pushBody();
			tag.setBodyContent(bodyContent);
			tag.doInitBody();
			TagUtil.evalBody(childs, pageContext);
			while(IterationTag.EVAL_BODY_AGAIN == doAfterBody())
			{
				TagUtil.evalBody(childs, pageContext);
			}
			pageContext.popBody();
		}
		return tag.doEndTag();
	}
	
    /**
     * Implementation of {@link NestedTag#getTag}.
     */
	public TagSupport getTag()
	{
		return tag;
	}
	
    /**
     * Implementation of {@link NestedTag#removeChilds}.
     */
	public void removeChilds()
	{
		childs.clear();
	}
	
    /**
     * Implementation of {@link NestedTag#getChilds}.
     */
	public List getChilds()
	{
		return childs;
	}
	
    /**
     * Implementation of {@link NestedTag#getChild}.
     */
	public Object getChild(int index)
	{
		return childs.get(index);
	}
	
    /**
     * Implementation of {@link NestedTag#addTextChild}.
     */
	public void addTextChild(String text)
	{
		if(null == text) text = "";
		childs.add(text);
	}
	
    /**
     * Implementation of {@link NestedTag#addTagChild(Class)}.
     */
	public NestedTag addTagChild(Class tag)
	{
		return addTagChild(tag, new HashMap());
	}
	
    /**
     * Implementation of {@link NestedTag#addTagChild(Class, Map)}.
     */
	public NestedTag addTagChild(Class tag, Map attributeMap)
	{
		TagSupport tagSupport = TagUtil.createNestedTagInstance(tag, this.pageContext, attributeMap);	
		tagSupport.setParent(this.tag);
		childs.add(tagSupport);
        return (NestedTag)tagSupport;
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
		TagSupport tagSupport = TagUtil.createNestedTagInstance(tag, this.pageContext, attributeMap);	
		tagSupport.setParent(this.tag);
		childs.add(tagSupport);
        return (NestedTag)tagSupport;
	}
	
    /**
     * Delegates to wrapped tag.
     */
	public int doAfterBody() throws JspException
	{
		return tag.doAfterBody();
	}
    
    /**
     * Delegates to wrapped tag.
     */
	public int doEndTag() throws JspException
	{
		return tag.doEndTag();
	}

    /**
     * Delegates to wrapped tag.
     */
	public int doStartTag() throws JspException
	{
		return tag.doStartTag();
	}

    /**
     * Delegates to wrapped tag.
     */
	public String getId()
	{
		return tag.getId();
	}

    /**
     * Delegates to wrapped tag.
     */
	public Tag getParent()
	{
		return tag.getParent();
	}

    /**
     * Delegates to wrapped tag.
     */
	public Object getValue(String arg0)
	{
		return tag.getValue(arg0);
	}

    /**
     * Delegates to wrapped tag.
     */
	public Enumeration getValues()
	{
		return tag.getValues();
	}

    /**
     * Delegates to wrapped tag.
     */
	public void release()
	{
		tag.release();
	}

    /**
     * Delegates to wrapped tag.
     */
	public void removeValue(String arg0)
	{
		tag.removeValue(arg0);
	}

    /**
     * Delegates to wrapped tag.
     */
	public void setId(String arg0)
	{
		tag.setId(arg0);
	}

    /**
     * Delegates to wrapped tag. Also calls <code>setPageContext</code>
     * for all child tags.
     */
	public void setPageContext(PageContext pageContext)
	{
        this.pageContext = pageContext;
        tag.setPageContext(pageContext);
        for(int ii = 0; ii < childs.size(); ii++)
        {
            Object child = childs.get(ii);
            if(child instanceof TagSupport)
            {
                ((TagSupport)child).setPageContext(pageContext);
            }
        }
	}

    /**
     * Delegates to wrapped tag.
     */
	public void setParent(Tag arg0)
	{
		tag.setParent(arg0);
	}

    /**
     * Delegates to wrapped tag.
     */
	public void setValue(String arg0, Object arg1)
	{
		tag.setValue(arg0, arg1);
	}

    /**
     * Delegates to wrapped tag.
     */	
	public void doInitBody() throws JspException
	{
		tag.doInitBody();
	}
    
    /**
     * Delegates to wrapped tag.
     */
	public BodyContent getBodyContent()
	{
		return tag.getBodyContent();
	}

    /**
     * Delegates to wrapped tag.
     */
	public JspWriter getPreviousOut()
	{
		return tag.getPreviousOut();
	}

    /**
     * Delegates to wrapped tag.
     */
	public void setBodyContent(BodyContent arg0)
	{
		tag.setBodyContent(arg0);
	}
    
    /**
     * Dumps the content of this and the nested tags.
     */
    public String toString()
    {
        return TagUtil.dumpTag(this, new StringBuffer(), 0);
    }
}
