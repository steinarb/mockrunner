package com.mockrunner.tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class NestedSimpleTag //extends SimpleTagSupport implements NestedTag
{
    //private SimpleTag tag;
    //private JspContext jspContext;
    //private Map attributes;
    private List childs;
    
    /**
     * Constructor for a tag with an empty attribute map.
     * @param tag the tag
     * @param pageContext the corresponding <code>PageContext</code>
     */
    /*public NestedSimpleTag(SimpleTag tag, JspContext jspContext)
    {
        this(tag, jspContext, new HashMap());
    }*/
    
    /**
     * Constructor for a tag with the specified attribute map.
     * @param tag the tag
     * @param pageContext the corresponding <code>PageContext</code>
     * @param attributes the attribute map
     */
    /*public NestedSimpleTag(SimpleTag tag, JspContext jspContext, Map attributes)
    {
        this.tag = tag;
        this.jspContext = jspContext;
        childs = new ArrayList();
        this.attributes = attributes;
    }*/
    
    /**
     * Constructor for a tag with an empty attribute map.
     * @param tag the tag
     * @param pageContext the corresponding <code>PageContext</code>
     */
    /*public NestedSimpleTag(SimpleTagSupport tag, JspContext jspContext)
    {
        this(tag, jspContext, new HashMap());
    }*/
    
    /**
     * Constructor for a tag with the specified attribute map.
     * @param tag the tag
     * @param pageContext the corresponding <code>PageContext</code>
     * @param attributes the attribute map
     */
    /*public NestedSimpleTag(SimpleTagSupport tag, JspContext jspContext, Map attributes)
    {
        this((SimpleTag)tag, jspContext, attributes);
    }*/
    
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
        //TagUtil.populateTag(tag, attributes, false);
    }
    
    /**
     * Implementation of {@link NestedTag#doLifecycle} for simple
     * tags.
     */
    public int doLifecycle() throws JspException
    {
        return 0;
    }
    
    /**
     * Implementation of {@link NestedTag#getTag}.
     * Returns <code>null</code> because simple tag
     * cannot be an instance of <code>TagSupport</code>.
     */
    public TagSupport getTag()
    {
        return null;
    }
    
    /**
     * Implementation of {@link NestedTag#getWrappedTag}.
     */
    /*public JspTag getWrappedTag()
    {
        return tag;
    }*/
    
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
        return null;
    }
    
    /**
     * Implementation of {@link NestedTag#addTagChild(JspTag)}.
     */
    /*public NestedTag addTagChild(JspTag tag)
    {
        return addTagChild((TagSupport)tag);
    }*/
    
    /**
     * Implementation of {@link NestedTag#addTagChild(JspTag, Map)}.
     */
    /*public NestedTag addTagChild(JspTag tag, Map attributeMap)
    {
        return addTagChild((TagSupport)tag, attributeMap);
    }*/
    
    public void doTag() throws JspException, IOException
    {
        //super.doTag();
    }
    
    /*public JspFragment getJspBody()
    {
        return super.getJspBody();
    }
    
    public JspContext getJspContext()
    {
        return jspContext;
    }
    
    public JspTag getParent()
    {
        return super.getParent();
    }
    
    public void setJspBody(JspFragment arg0)
    {
        super.setJspBody(arg0);
    }
    
    public void setJspContext(JspContext jspContext)
    {
        this.jspContext = jspContext;
    }
    
    public void setParent(JspTag arg0)
    {
        super.setParent(arg0);
    }*/
    
    /**
     * Dumps the content of this and the nested tags.
     */
    /*public String toString()
    {
        return TagUtil.dumpTag(this, new StringBuffer(), 0);
    }*/
}
