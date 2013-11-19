package com.mockrunner.example.struts;

import java.util.HashMap;

import javax.servlet.ServletContext;

/**
 * A simple data repository stored in the <ServletContext>.
 * The implementation is not thread safe. Used by
 * {@link StoreDataActionTest}.
 */
public class MemoryBasedRepository
{
    private HashMap dataStore;
    
    private MemoryBasedRepository()
    {
        dataStore = new HashMap();
    }
    
    public static MemoryBasedRepository instance(ServletContext context)
    {
        MemoryBasedRepository instance = (MemoryBasedRepository)context.getAttribute(MemoryBasedRepository.class.getName());
        if(null != instance) return instance;
        instance = new MemoryBasedRepository();
        context.setAttribute(MemoryBasedRepository.class.getName(), instance);
        return instance;
    }
    
    public void set(String id, Object data)
    {
        dataStore.put(id, data);
    }
    
    public Object get(String id)
    {
        return dataStore.get(id);
    }
}
