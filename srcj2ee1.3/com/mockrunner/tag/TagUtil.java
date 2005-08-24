package com.mockrunner.tag;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTag;
//import javax.servlet.jsp.tagext.DynamicAttributes;
//import javax.servlet.jsp.tagext.SimpleTag;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.util.common.MethodUtil;
import com.mockrunner.util.common.StringUtil;

/**
 * Util class for tag test framework.
 * Please note, that the methods of this class take
 * <code>Object</code> parameters where <code>JspTag</code>
 * or <code>JspContext</code> would be suitable. The reason is,
 * that these classes do not exist in J2EE 1.3. This class is
 * usable with J2EE 1.3 and J2EE 1.4.
 */
public class TagUtil
{
    private final static Log log = LogFactory.getLog(TagUtil.class);
    
    /**
     * Creates an {@link com.mockrunner.tag.NestedTag} instance wrapping the
     * specified tag. Returns an instance of {@link com.mockrunner.tag.NestedStandardTag}
     * or {@link com.mockrunner.tag.NestedBodyTag} depending on the
     * type of specified tag.
     * @param tag the tag class
     * @param pageContext the corresponding <code>PageContext</code> or <code>JspContext</code>
     * @param attributes the attribute map
     * @return the instance of {@link com.mockrunner.tag.NestedTag}
     * @throws IllegalArgumentException if <code>tag</code> is <code>null</code>
     */
    public static Object createNestedTagInstance(Class tag, Object pageContext, Map attributes)
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
     * or {@link com.mockrunner.tag.NestedBodyTag} depending on the
     * type of specified tag.
     * @param tag the tag
     * @param pageContext the corresponding <code>PageContext</code> or <code>JspContext</code>
     * @param attributes the attribute map
     * @return the instance of {@link com.mockrunner.tag.NestedTag}
     * @throws IllegalArgumentException if <code>tag</code> is <code>null</code>
     */
    public static Object createNestedTagInstance(Object tag, Object pageContext, Map attributes)
    {
        if(null == tag) throw new IllegalArgumentException("tag must not be null");
        Object nestedTag = null;
        if(tag instanceof BodyTag)
        {
            checkPageContext(pageContext);
            nestedTag = new NestedBodyTag((BodyTag)tag, (PageContext)pageContext, attributes);
        }
        else if(tag instanceof Tag)
        {
            checkPageContext(pageContext);
            nestedTag = new NestedStandardTag((Tag)tag, (PageContext)pageContext, attributes);
        }
        /*else if(tag instanceof SimpleTag)
        {
            checkJspContext(pageContext);
            nestedTag = new NestedSimpleTag((SimpleTag)tag, (JspContext)pageContext, attributes);
        }*/
        else
        {
            throw new IllegalArgumentException("tag must be an instance of Tag or SimpleTag");
        }
        return nestedTag;
    }
    
    private static void checkPageContext(Object pageContext)
    {
        if(pageContext instanceof PageContext) return;
        throw new IllegalArgumentException("pageContext must be an instance of PageContext");
    }
    
    /*private static void checkJspContext(Object pageContext)
    {
        if(pageContext instanceof JspContext) return;
        throw new IllegalArgumentException("pageContext must be an instance of JspContext");
    }*/
    
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
            Iterator names = attributes.keySet().iterator();
            while(names.hasNext()) 
            {
                String currentName = (String)names.next();
                /*Object currentValue = attributes.get(currentName);
                if(currentValue instanceof DynamicAttribute)
                {
                    populateDynamicAttribute(tag, currentName, (DynamicAttribute)currentValue);
                    return;
                }*/
                if(PropertyUtils.isWriteable(tag, currentName)) 
                {
                    BeanUtils.copyProperty(tag, currentName, attributes.get(currentName));
                }
                /*else if(tag instanceof DynamicAttributes)
                {
                    populateDynamicAttribute(tag, currentName, new DynamicAttribute(null, currentValue));
                }*/
            }
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
    
    /*private static void populateDynamicAttribute(Object tag, String name, DynamicAttribute attribute) throws JspException
    {
        if(!(tag instanceof DynamicAttributes))
        {
            String message = "Attribute " + name + " specified as dynamic attribute but tag ";
            message += "is not an instance of avax.servlet.jsp.tagext.DynamicAttributes.";
            throw new IllegalArgumentException(message);
        }
        ((DynamicAttributes)tag).setDynamicAttribute(attribute.getUri(), name, attribute.getValue());
    }*/
    
    /**
     * Handles body evaluation of a tag. Iterated through the childs.
     * If the child is an instance of {@link com.mockrunner.tag.NestedTag},
     * the {@link com.mockrunner.tag.NestedTag#doLifecycle} method of
     * this tag is called. If the child is an instance of 
     * {@link com.mockrunner.tag.DynamicChild}, the 
     * {@link com.mockrunner.tag.DynamicChild#evaluate} method is called
     * and the result is written to the out <code>JspWriter</code> as a
     * string. If the result is another object (usually a string) it is written
     * to the out <code>JspWriter</code> (the <code>toString</code> method will
     * be called).
     * @param bodyList the list of body entries
     * @param pageContext the corresponding <code>PageContext</code> or <code>JspContext</code>
     */
    public static void evalBody(List bodyList, Object pageContext) throws JspException
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
            /*else if(nextChild instanceof NestedSimpleTag)
            {
                ((NestedSimpleTag)nextChild).doLifecycle();
            }*/
            else
            {
                try
                {
                    if(pageContext instanceof PageContext)
                    {
                        ((PageContext)pageContext).getOut().print(getChildText(nextChild));
                    }
                    /*else if(pageContext instanceof JspContext)
                    {
                        ((JspContext)pageContext).getOut().print(getChildText(nextChild));
                    }*/
                    else
                    {
                        throw new IllegalArgumentException("pageContext must be an instance of JspContext");
                    }
                }
                catch(IOException exc)
                {
                    log.error(exc.getMessage(), exc);
                    throw new NestedApplicationException(exc);
                }	
            }
        }
    }
    
    private static String getChildText(Object child)
    {
        if(null == child) return "null";
        if(child instanceof DynamicChild)
        {
            Object result = ((DynamicChild)child).evaluate();
            if(null == result) return "null";
            return result.toString();
        }
        return child.toString();
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
