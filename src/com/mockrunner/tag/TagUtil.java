package com.mockrunner.tag;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.BeanUtils;


public class TagUtil
{
	public static TagSupport createNestedTagInstance(Class tag, PageContext pageContext, Map attributes)
	{
		if(null == tag) throw new RuntimeException("tag must not be null");
		TagSupport tagSupport;
		try
		{
			tagSupport = (TagSupport)tag.newInstance();
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
			throw new RuntimeException(exc.getMessage());
		}
		TagSupport nestedTag;
		if(tagSupport instanceof BodyTagSupport)
		{
			nestedTag = new NestedBodyTag((BodyTagSupport)tagSupport, pageContext, attributes);
		}
		else
		{
			nestedTag = new NestedStandardTag(tagSupport, pageContext, attributes);
		}
		return nestedTag;
	}
	
	public static void populateTag(TagSupport tag, Map attributes, boolean doRelease)
	{
		if(doRelease) tag.release();
		try
		{
			BeanUtils.copyProperties(tag, attributes);
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
			throw new RuntimeException(exc.getMessage());
		}
	}
	
	public static void evalBody(List bodyList, PageContext pageContext) throws JspException
	{
		for(int ii = 0; ii < bodyList.size(); ii++)
		{
			Object nextChild = bodyList.get(ii);
            if(nextChild instanceof NestedBodyTag)
			{
                int result = ((NestedBodyTag)nextChild).doLifecycle();
                if(Tag.SKIP_PAGE == result) return;
			}
			else if(nextChild instanceof NestedStandardTag)
			{
                int result = ((NestedStandardTag)nextChild).doLifecycle();
                if(Tag.SKIP_PAGE == result) return;
			}
			else
			{
				try
				{
					pageContext.getOut().print(nextChild.toString());
				}
				catch(IOException exc)
				{
					exc.printStackTrace();
					throw new RuntimeException(exc.getMessage());
				}	
			}
		}
	}
}
