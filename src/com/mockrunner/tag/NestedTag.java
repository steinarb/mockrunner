package com.mockrunner.tag;

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * <code>NestedTag</code> is used to simulate tags with static body
 * content and child tags. It can be used to test the interaction
 * of different tags. A <code>NestedTag</code> always wraps a real tag
 * class (the actual testee).
 * {@link TagTestModule} works with <code>NestedTag</code> instances 
 * internally. If you only want to test the ouptput of one single tag
 * without interaction with other tags, you do not have to care about
 * <code>NestedTag</code>. Use it, if you want to write sophisticated
 * tests of body tags. <code>NestedTag</code> instances are created with
 * the help of {@link TagTestModule#createNestedTag}. You do not need to
 * create them on your own in the tests.
 */
public interface NestedTag
{
    /**
     * Specify if the <code>release</code> method should be called
     * before populating a tag. Defaults to <code>false</code>. It's
     * the container behaviour to call <code>release</code>, but it's
     * usually not necessary in the tests, because the tag instances
     * are not reused during a test run.
     * @param doRelease should release be called
     */
    public void setDoRelease(boolean doRelease);
    
    /**
     * Populates the attributes of the underlying tag. The setters
     * of the tag are called. Please note that child tags are not
     * populated.
     */
    public void populateAttributes();
    
    /**
     * Performs the tags lifecycle. All <code>doBody</code> and <code>doTag</code> 
     * methods are called as  in the real web container. The evaluation of the body 
     * is simulated by performing the lifecycle recursively for all childs of the 
     * <code>NestedTag</code>.
     * @return the result of the final <code>doEndTag</code> call
     */
    public int doLifecycle() throws JspException;
    
    /**
     * Returns the wrapped tag (the testee).
     * @return the wrapped tag
     */
    public TagSupport getTag();
    
    /**
     * Removes all childs.
     */
    public void removeChilds();
     
    /**
     * Returns the <code>List</code> of childs.
     * @return the <code>List</code> of childs
     */
    public List getChilds();
     
    /**
     * Returns a child specified by its index.
     * @param index the index
     * @return the child
     */
    public Object getChild(int index);
      
    /**
     * Adds a text child simulating static body content.
     * @param text the static text
     */
    public void addTextChild(String text);
     
    /**
     * Adds a tag child simulating nested tags.
     * Note that <i>tag</i> must be of type <code>TagSupport</code>
     * or <code>BodyTagSupport</code>. The corresponding
     * <code>NestedTag</code> will be created automatically
     * wrapping the specified tag. An empty attribute <code>Map</code> 
     * will be used for the tag.
     * @param tag the tag class
     */  
    public NestedTag addTagChild(Class tag);
     
    /**
     * Adds a tag child simulating nested tags.
     * Note that <i>tag</i> must be of type <code>TagSupport</code>
     * or <code>BodyTagSupport</code>. The corresponding
     * <code>NestedTag</code> will be created automatically
     * wrapping the specified tag. The attributes <code>Map</code> 
     * contains the attributes of this tag (<i>propertyname</i> maps
     * to <i>propertyvalue</i>).
     * @param tag the tag class
     * @param attributeMap the attribute map
     */     
    public NestedTag addTagChild(Class tag, Map attributeMap);     
}
