package com.mockrunner.mock.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MockJspConfigDescriptor // implements JspConfigDescriptor
{
    private List jspPropertyGroups;
    private List taglibs;
    
    public MockJspConfigDescriptor()
    {
        reset();
    }
    
    public void reset()
    {
        jspPropertyGroups = new ArrayList();
        taglibs = new ArrayList();
    }
    
    public Collection getJspPropertyGroups()
    {
        return new ArrayList(jspPropertyGroups);
    }

    public Collection getTaglibs()
    {
        return new ArrayList(taglibs);
    }

    /**
     * Adds a <code>JspPropertyGroupDescriptor</code> to the list of JSP property groups.
     * 
     * @param jspPropertyGroup the <code>JspPropertyGroupDescriptor</code> to add
     */
//    public void addJspPropertyGroup(JspPropertyGroupDescriptor jspPropertyGroup)
//    {
//        this.jspPropertyGroups.add(jspPropertyGroup);
//    }
    
    /**
     * Clears the list of JSP property groups.
     */
    public void clearJspPropertyGroups()
    {
        this.jspPropertyGroups.clear();
    }
    
    /**
     * Adds a <code>TaglibDescriptor</code> to the list of taglibs.
     * 
     * @param taglib the <code>TaglibDescriptor</code> to add
     */
//    public void addTaglib(TaglibDescriptor taglib)
//    {
//        this.taglibs.add(taglib);
//    }
    
    /**
     * Clears the list of taglibs.
     */
    public void clearTaglibs()
    {
        this.taglibs.clear();
    }
}
