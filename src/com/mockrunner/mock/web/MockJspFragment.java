package com.mockrunner.mock.web;

public class MockJspFragment //extends JspFragment
{
    /*private JspContext jspContext;
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
    
    public JspTag getParent()
    {
        return parent;
    }
    
    public void setParent(JspTag parent)
    {
        this.parent = parent;
    }
    
    public JspContext getJspContext()
    {
        return jspContext;
    }
    
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
    
    public void invoke(Writer writer) throws JspException, IOException
    {
        if(null == jspContext) return;
        jspContext.pushBody(writer);
        TagUtil.evalBody(childs, jspContext);
        jspContext.getOut().flush();
        jspContext.popBody();
    }
    
    *//**
     * Removes all childs.
     *//*
    public void removeChilds()
    {
        childs.clear();
    }
     
    *//**
     * Returns the <code>List</code> of childs.
     * @return the <code>List</code> of childs
     *//*
    public List getChilds()
    {
        return childs;
    }
     
    *//**
     * Returns a child specified by its index.
     * @param index the index
     * @return the child
     *//*
    public Object getChild(int index)
    {
        return childs.get(index);
    }
      
    *//**
     * Adds a text child simulating static body content.
     * @param text the static text
     *//*
    public void addTextChild(String text)
    {
        if(null == text) text = "";
        childs.add(text);
    }
     
    *//**
     * Adds a tag child simulating nested tags.
     * The corresponding <code>NestedTag</code> will be created 
     * automatically wrapping the specified tag. An empty attribute 
     * <code>Map</code> will be used for the tag.
     * @param tag the tag class
     *//*  
    public NestedTag addTagChild(Class tag)
    {
        return addTagChild(tag, new HashMap());
    }
     
    *//**
     * Adds a tag child simulating nested tags.
     * The corresponding <code>NestedTag</code> will be created 
     * automatically wrapping the specified tag. The attributes 
     * <code>Map</code> contains the attributes of this tag 
     * (<i>propertyname</i> maps to <i>propertyvalue</i>).
     * @param tag the tag class
     * @param attributeMap the attribute map
     *//*     
    public NestedTag addTagChild(Class tag, Map attributeMap)
    {
        Object childTag = TagUtil.createNestedTagInstance(tag, jspContext, attributeMap);   
        return addChild(childTag);
    }
    
    *//**
     * Adds a tag child simulating nested tags.
     * <code>NestedTag</code> will be created automatically
     * wrapping the specified tag. An empty attribute <code>Map</code> 
     * will be used for the tag.
     * @param tag the tag
     *//*  
    public NestedTag addTagChild(JspTag tag)
    {
        return addTagChild(tag, new HashMap());
    }
     
    *//**
     * Adds a tag child simulating nested tags.
     * The corresponding <code>NestedTag</code> will be created 
     * automatically wrapping the specified tag. The attributes 
     * <code>Map</code>  contains the attributes of this tag 
     * (<i>propertyname</i> maps to <i>propertyvalue</i>).
     * @param tag the tag
     * @param attributeMap the attribute map
     *//*     
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
    }*/
}
