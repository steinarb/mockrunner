package com.mockrunner.tag;

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public interface NestedTag
{
    public void populateAttributes();
    
    public int doLifecycle() throws JspException;
    
    public TagSupport getTag();
    
    public void removeChilds();
      
    public List getChilds();
        
    public Object getChild(int index);
      
    public void addTextChild(String text);
       
    public void addTagChild(Class tag);
        
    public void addTagChild(Class tag, Map attributeMap);     
}
