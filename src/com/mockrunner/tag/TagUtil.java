package com.mockrunner.tag;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.util.common.MethodUtil;
import com.mockrunner.util.common.StringUtil;

/**
 * Util class for tag test framework.
 */
public class TagUtil
{
    private final static Log log = LogFactory.getLog(TagUtil.class);
    
    /**
     * Creates an {@link com.mockrunner.tag.NestedTag} instance wrapping the
     * specified tag. Returns an instance of {@link com.mockrunner.tag.NestedStandardTag}
     * resp. {@link com.mockrunner.tag.NestedBodyTag} depending on the
     * type of specified tag.
     * @param tag the tag class
     * @param pageContext the corresponding <code>PageContext</code>
     * @param attributes the attribute map
     * @return the instance of {@link com.mockrunner.tag.NestedTag}
     * @throws IllegalArgumentException if <code>tag</code> is <code>null</code>
     */
    public static Object createNestedTagInstance(Class tag, PageContext pageContext, Map attributes)
    {
        if(null == tag) throw new IllegalArgumentException("tag must not be null");
        Object tagObject;
        try
        {
            tagObject = tag.newInstance();
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
        return createNestedTagInstance(tagObject, pageContext, attributes);
    }
    
    /**
     * Creates an {@link com.mockrunner.tag.NestedTag} instance wrapping the
     * specified tag. Returns an instance of {@link com.mockrunner.tag.NestedStandardTag}
     * resp. {@link com.mockrunner.tag.NestedBodyTag} depending on the
     * type of specified tag.
     * @param tag the tag
     * @param pageContext the corresponding <code>PageContext</code>
     * @param attributes the attribute map
     * @return the instance of {@link com.mockrunner.tag.NestedTag}
     * @throws IllegalArgumentException if <code>tag</code> is <code>null</code>
     */
    public static Object createNestedTagInstance(Object tag, PageContext pageContext, Map attributes)
    {
        if(null == tag) throw new IllegalArgumentException("tag must not be null");
        Object nestedTag = null;
        if(tag instanceof BodyTag)
        {
            nestedTag = new NestedBodyTag((BodyTag)tag, pageContext, attributes);
        }
        else if(tag instanceof Tag)
        {
            nestedTag = new NestedStandardTag((Tag)tag, pageContext, attributes);
        }
        /*else if(tag instanceof SimpleTag)
        {
            nestedTag = new NestedSimpleTag((SimpleTag)tag, pageContext, attributes);
        }*/
        return nestedTag;
    }
    
    /**
     * Populates the specified attributes to the specified tag. Calls the
     * <code>release</code> method before populating, if <i>doRelease</i> is set to
     * <code>true</code>.
     * @param tag the tag
     * @param attributes the attribute map
     * @param doRelease should release be called
     */
    public static void populateTag(Object tag, Map attributes, boolean doRelease)
    {
        if(doRelease) MethodUtil.invoke(tag, "release");
        if(null == attributes || attributes.isEmpty()) return;
        try
        {
            BeanUtils.copyProperties(tag, attributes);
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Handles body evaluation of a tag.
     */
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
                    log.error(exc.getMessage(), exc);
                    throw new NestedApplicationException(exc);
                }	
            }
        }
    }
    
    /**
     * Helper method to dump tags incl. child tags.
     */
    public static String dumpTag(NestedTag tag, StringBuffer buffer, int level)
    {
        StringUtil.appendTabs(buffer, level);
        buffer.append("<" + tag.getClass().getName() + ">\n");
        TagUtil.dumpTagTree(tag.getChilds(), buffer, level);
        StringUtil.appendTabs(buffer, level);
        buffer.append("</" + tag.getClass().getName() + ">");
        return buffer.toString();
    }
    
    /**
     * Helper method to dump tags incl. child tags.
     */
    public static void dumpTagTree(List bodyList, StringBuffer buffer, int level)
    {
        for(int ii = 0; ii < bodyList.size(); ii++)
        {
            Object nextChild = bodyList.get(ii);
            if(nextChild instanceof NestedTag)
            {
                dumpTag((NestedTag)nextChild, buffer, level + 1);
            }
            else
            {
                StringUtil.appendTabs(buffer, level + 1);
                buffer.append(bodyList.get(ii).toString());
            }
            buffer.append("\n");
        }
    }
}
