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

import com.mockrunner.mock.MockBodyContent;

public class NestedBodyTag extends BodyTagSupport implements NestedTag
{
	private BodyTagSupport tag;
	private PageContext pageContext;
	private Map attributes;
	private List childs;
	private boolean doRelease;
	
	public NestedBodyTag(BodyTagSupport tag, PageContext pageContext)
	{
		this(tag, pageContext, new HashMap());
	}
	
	public NestedBodyTag(BodyTagSupport tag, PageContext pageContext, Map attributes)
	{
		this.tag = tag;
		this.pageContext = pageContext;
		tag.setPageContext(pageContext);
		childs = new ArrayList();
		this.attributes = attributes;
		doRelease = false;
	}
	
	public void setDoRelease(boolean doRelease)
	{
		this.doRelease = doRelease;
	}
    
    public void populateAttributes()
    {
        TagUtil.populateTag(tag, attributes, doRelease);
    }
	
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
	
	public TagSupport getTag()
	{
		return tag;
	}
	
	public void removeChilds()
	{
		childs.clear();
	}
	
	public List getChilds()
	{
		return childs;
	}
	
	public Object getChild(int index)
	{
		return childs.get(index);
	}
	
	public void addTextChild(String text)
	{
		if(null == text) text = "";
		childs.add(text);
	}
	
	public void addTagChild(Class tag)
	{
		addTagChild(tag, new HashMap());
	}
	
	public void addTagChild(Class tag, Map attributeMap)
	{
		TagSupport tagSupport = TagUtil.createNestedTagInstance(tag, this.pageContext, attributeMap);	
		tagSupport.setParent(this.tag);
		childs.add(tagSupport);
	}
	
	public int doAfterBody() throws JspException
	{
		return tag.doAfterBody();
	}

	public int doEndTag() throws JspException
	{
		return tag.doEndTag();
	}

	public int doStartTag() throws JspException
	{
		return tag.doStartTag();
	}

	public String getId()
	{
		return tag.getId();
	}

	public Tag getParent()
	{
		return tag.getParent();
	}

	public Object getValue(String arg0)
	{
		return tag.getValue(arg0);
	}

	public Enumeration getValues()
	{
		return tag.getValues();
	}

	public void release()
	{
		tag.release();
	}

	public void removeValue(String arg0)
	{
		tag.removeValue(arg0);
	}

	public void setId(String arg0)
	{
		tag.setId(arg0);
	}

	public void setPageContext(PageContext pageContext)
	{
		this.pageContext = pageContext;
		tag.setPageContext(pageContext);
		for(int ii = 0; ii < childs.size(); ii++)
		{
			((TagSupport)childs.get(ii)).setPageContext(pageContext);
		}
	}

	public void setParent(Tag arg0)
	{
		tag.setParent(arg0);
	}

	public void setValue(String arg0, Object arg1)
	{
		tag.setValue(arg0, arg1);
	}
	
	public void doInitBody() throws JspException
	{
		tag.doInitBody();
	}

	public BodyContent getBodyContent()
	{
		return tag.getBodyContent();
	}

	public JspWriter getPreviousOut()
	{
		return tag.getPreviousOut();
	}

	public void setBodyContent(BodyContent arg0)
	{
		tag.setBodyContent(arg0);
	}
}
