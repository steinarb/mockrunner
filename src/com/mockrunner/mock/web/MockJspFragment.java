package com.mockrunner.mock.web;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagAdapter;

import com.mockrunner.tag.DynamicChild;
import com.mockrunner.tag.NestedTag;
import com.mockrunner.tag.TagUtil;


/**
 * Mock implementation of <code>JspFragment</code>.
 * The body of a simple tag is a <code>JspFragment</code>.
 * All child handling methods of {@link com.mockrunner.tag.NestedSimpleTag}
 * delegate to an underlying instance of this class.
 */
public class MockJspFragment extends JspFragment
{
    private JspContext jspContext;
    private List childs;
    private JspTag parent;
    
    public MockJspFragment(JspContext jspContext)
    {
        this(jspContext, null);
    }
    
    public MockJspFragment(JspContext jspContext, JspTag parent)
    {
        this.jspContext = jspContext;
        this.parent = parent;
        childs = new ArrayList();
    }
    
    /**
     * Returns the parent tag.
     * @return the parent tag
     */
    public JspTag getParent()
    {
        return parent;
    }
    
    /**
     * Sets the parent tag.
     * @param parent the parent tag
     */
    public void setParent(JspTag parent)
    {
        this.parent = parent;
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
     * Sets the <code>JspContext</code>. Also calls <code>setJspContext</code>
     * (or <code>setPageContext</code>) for all child tags.
     * <code>setPageContext</code> is only called if the specified <code>JspContext</code>
     * is an instance of <code>PageContext</code>.
     * @param jspContext the <code>JspContext</code>
     */
    public void setJspContext(JspContext jspContext)
    {
        this.jspContext = jspContext;
        for(int ii = 0; ii < childs.size(); ii++)
        {
            Object child = childs.get(ii);
            if(child instanceof Tag && jspContext instanceof PageContext)
            {
                ((Tag)child).setPageContext((PageContext)jspContext);
            }
            else if(child instanceof SimpleTag)
            {
                ((SimpleTag)child).setJspContext(jspContext);
            }
        }
    }
    
    /**
     * Executes the fragment and directs all output to the given Writer, or the JspWriter 
     * returned by the getOut() method of the JspContext associated with the fragment 
     * if out is null (copied from <code>JspFragment</code> JavaDoc).
     * @param writer the Writer to output the fragment to, or null if output should be 
     *               sent to JspContext.getOut().
     */
    public void invoke(Writer writer) throws JspException, IOException
    {
        if(null == jspContext) return;
        if(null != writer)
        {
            jspContext.pushBody(writer);
        }
        TagUtil.evalBody(childs, jspContext);
        jspContext.getOut().flush();
        if(null != writer)
        {
            jspContext.popBody();
        }
    }
    
    /**
     * Removes all childs.
     */
    public void removeChilds()
    {
        childs.clear();
    }
     
    /**
     * Returns the <code>List</code> of childs.
     * @return the <code>List</code> of childs
     */
    public List getChilds()
    {
        return childs;
    }
     
    /**
     * Returns a child specified by its index.
     * @param index the index
     * @return the child
     */
    public Object getChild(int index)
    {
        return childs.get(index);
    }
      
    /**
     * Adds a text child simulating static body content.
     * @param text the static text
     */
    public void addTextChild(String text)
    {
        if(null == text) text = "";
        childs.add(text);
    }
    
    /**
     * Adds a dynamic child simulating scriptlets and
     * EL expressions. Check out
     * {@link com.mockrunner.tag.TagUtil#evalBody(List, Object)}
     * for details about child handling.
     * @param child the dynamic child instance
     */
    public void addDynamicChild(DynamicChild child)
    {
        if(null == child) return;
        childs.add(child);
    }
     
    /**
     * Adds a tag child simulating nested tags.
     * The corresponding <code>NestedTag</code> will be created 
     * automatically wrapping the specified tag. An empty attribute 
     * <code>Map</code> will be used for the tag.
     * @param tag the tag class
     */  
    public NestedTag addTagChild(Class tag)
    {
        return addTagChild(tag, new HashMap());
    }
     
    /**
     * Adds a tag child simulating nested tags.
     * The corresponding <code>NestedTag</code> will be created 
     * automatically wrapping the specified tag. The attributes 
     * <code>Map</code> contains the attributes of this tag 
     * (<i>propertyname</i> maps to <i>propertyvalue</i>).
     * @param tag the tag class
     * @param attributeMap the attribute map
     */     
    public NestedTag addTagChild(Class tag, Map attributeMap)
    {
        Object childTag = TagUtil.createNestedTagInstance(tag, jspContext, attributeMap);   
        return addChild(childTag);
    }
    
    /**
     * Adds a tag child simulating nested tags.
     * <code>NestedTag</code> will be created automatically
     * wrapping the specified tag. An empty attribute <code>Map</code> 
     * will be used for the tag.
     * @param tag the tag
     */  
    public NestedTag addTagChild(JspTag tag)
    {
        return addTagChild(tag, new HashMap());
    }
     
    /**
     * Adds a tag child simulating nested tags.
     * The corresponding <code>NestedTag</code> will be created 
     * automatically wrapping the specified tag. The attributes 
     * <code>Map</code>  contains the attributes of this tag 
     * (<i>propertyname</i> maps to <i>propertyvalue</i>).
     * @param tag the tag
     * @param attributeMap the attribute map
     */     
    public NestedTag addTagChild(JspTag tag, Map attributeMap)
    {
        Object childTag = TagUtil.createNestedTagInstance(tag, jspContext, attributeMap);   
        return addChild(childTag);
    }
    
    private NestedTag addChild(Object childTag)
    {
        if(childTag instanceof SimpleTag)
        {
            ((SimpleTag)childTag).setParent(parent);
        }
        else if(parent instanceof Tag)
        {
            if(childTag instanceof Tag)
            {
                ((Tag)childTag).setParent((Tag)parent);
            }
        }
        else if(parent instanceof SimpleTag)
        {
            if(childTag instanceof Tag)
            {
                ((Tag)childTag).setParent(new TagAdapter((SimpleTag)parent));
            }
        }
        childs.add(childTag);
        return (NestedTag)childTag;
    }
}
