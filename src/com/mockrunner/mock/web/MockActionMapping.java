package com.mockrunner.mock.web;

import java.util.ArrayList;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Mock implementation of <code>ActionMapping</code>.
 */
public class MockActionMapping extends ActionMapping
{
    private ArrayList forwards;

    public MockActionMapping()
    {
        super();
        forwards = new ArrayList();
    }
    
    /**
     * Always return a valid <code>ActionForward</code>
     * since we do not care if it exists in the
     * struts-config. The action want to have it, so
     * return it. We even do not care about forward name
     * and path. It's the same here since we do not have
     * a struts-config for an approriate mapping name ->
     * path.
     */
    public ActionForward findForward(String name)
    {
        return new MockActionForward(name, name, false);
    }
    
    /**
     * If the action wants to see special forwards,
     * specify them here.
     */
    public void setupForwards(String[] forwards)
    {
        if(null == forwards) return;
        for(int ii = 0; ii < forwards.length; ii++)
        {
            this.forwards.add(forwards[ii]);
        }
    }

    public String[] findForwards()
    {
        return (String[])forwards.toArray(new String[forwards.size()]);
    }
    
    /**
     * Always return a valid <code>ActionForward</code>
     * since we do not care if it exists in the
     * struts-config. The action wants to have it, so
     * return it. We even do not care about forward name
     * and path. It's the same here since we do not have
     * a struts-config for an approriate mapping name ->
     * path.
     */
    public ActionForward getInputForward()
    {
        return new MockActionForward(input, input, false);
    }

    public String toString()
    {
        return "ActionConfig[path=" + getPath() + ",name=" + getName() + ",scope=" + getScope() + ",type=" + getType();
    }
}
