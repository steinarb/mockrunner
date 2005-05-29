package com.mockrunner.struts;

import java.lang.reflect.Method;

import org.apache.struts.action.ActionMapping;

import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.mock.web.MockActionMapping;

/**
 * Helper class to generate CGLib proxies for <code>ActionMapping</code>. Not meant for application use.
 */
public class ActionMappingProxyGenerator
{
    private final static Method[] delegateMethods;
    static
    {
        delegateMethods = new Method[3];
        try
        {
            delegateMethods[0] = MockActionMapping.class.getDeclaredMethod("findForward", new Class[] {String.class});
            delegateMethods[1] = MockActionMapping.class.getDeclaredMethod("findForwards", null);
            delegateMethods[2] = MockActionMapping.class.getDeclaredMethod("getInputForward", null);
        } 
        catch(Exception exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    
    private MockActionMapping delegateMapping;
    
    public ActionMappingProxyGenerator(MockActionMapping delegateMapping)
    {
        this.delegateMapping = delegateMapping;
    }
    
    public ActionMapping createActionMappingProxy(Class mappingClass)
    {
        if(null == mappingClass) return null;
        if(!ActionMapping.class.isAssignableFrom(mappingClass))
        {
            throw new ClassCastException(mappingClass.getClass().getName() + " must be an instance of " + ActionMapping.class.getName());
        }
        DynamicMockProxyGenerator generator = new DynamicMockProxyGenerator(mappingClass, delegateMapping, delegateMethods);
        return (ActionMapping)generator.createProxy();
    }
}
