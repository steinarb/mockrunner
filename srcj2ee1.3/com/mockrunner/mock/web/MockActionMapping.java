package com.mockrunner.mock.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Mock implementation of <code>ActionMapping</code>.
 */
public class MockActionMapping extends ActionMapping
{
    private Map forwards;

    public MockActionMapping()
    {
        super();
        forwards = new HashMap();
    }
    
    /**
     * Clears all specified forwards.
     */
    public void clearForwards()
    {
        forwards.clear();
    }
    
    /**
     * Always return a valid <code>ActionForward</code>
     * since we do not care if it exists in the
     * struts-config. If an <code>ActionForward</code>
     * was defined using {@link #addForward},
     * this <code>ActionForward</code> will be returned.
     * Otherwise a new <code>ActionForward</code>
     * (with equal name and path) will be returned.
     * @param name the name
     * @return the corresponding <code>ActionForward</code>
     */
    public ActionForward findForward(String name)
    {
        Iterator iterator = forwards.keySet().iterator();
        while(iterator.hasNext())
        { 
            String key = (String)iterator.next();
            if(key.equals(name))
            {
                return (ActionForward)forwards.get(key);
            }
        }
        return new MockActionForward(name, name, false); 
    }
    
    /**
     * Adds an <code>ActionForward</code>
     * with the specified name and path.
     * @param forwardName the name of the forward
     * @param forwardPath the path of the forward
     */
    public void addForward(String forwardName, String forwardPath) 
    { 
        ActionForward forward = new MockActionForward(forwardName, forwardPath, false); 
        forwards.put(forwardName, forward); 
    } 
    
    /**
     * Sets multiple <code>ActionForward</code> objects
     * with equal name and path.
     * @param forwardNames the forward names
     */
    public void setupForwards(String[] forwardNames)
    {
        if(null == forwardNames) return; 
        for(int ii = 0; ii < forwardNames.length; ii++) 
        { 
            String name = forwardNames[ii];
            ActionForward forward = new MockActionForward(name, name, false); 
            forwards.put(name, forward);
        }
    }

    /**
     * Returns all forward names (set using {@link #addForward}
     * or {@link #setupForwards}).
     * @return the forward names
     */
    public String[] findForwards()
    {
        return (String[])forwards.keySet().toArray(new String[forwards.size()]);
    }
    
    /**
     * Always return a valid <code>ActionForward</code>.
     * The input parameter of this mapping will be used
     * as the name and path for the <code>ActionForward</code>.
     */
    public ActionForward getInputForward()
    {
        return new MockActionForward(getInput(), getInput(), false);
    }

    public String toString()
    {
        return "ActionConfig[path=" + getPath() + ",name=" + getName() + ",scope=" + getScope() + ",type=" + getType();
    }
}
