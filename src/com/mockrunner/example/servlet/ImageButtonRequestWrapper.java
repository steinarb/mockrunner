package com.mockrunner.example.servlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * The request wrapper for {@link ImageButtonFilter}.
 * Replaces request parameters.
 */
public class ImageButtonRequestWrapper extends HttpServletRequestWrapper
{
    public ImageButtonRequestWrapper(HttpServletRequest request)
    {
        super(request);
    }
    
    public String getParameter(String key)
    {
        String[] parameters = getParameterValues(key);
        if(null != parameters && parameters.length > 0) return parameters[0];
        return null;
    }

    public Enumeration getParameterNames()
    {
        return new Vector(getParameterMap().keySet()).elements();
    }

    public String[] getParameterValues(String key)
    {
        return (String[])getParameterMap().get(key);
    }

    public Map getParameterMap()
    {
        Map parameterMap = super.getParameterMap();
        Map newMap = new HashMap();
        
        if(null != parameterMap)
        {
	        Iterator keys = parameterMap.keySet().iterator();
            while(keys.hasNext())
            {
                String key = (String)keys.next();
                Object value = parameterMap.get(key);
                if(!key.endsWith(".y"))
                {
                    if(key.endsWith(".x"))
                    {
                        key = key.substring(0, key.length() -2);
                    }
                    newMap.put(key, value);
                }
	        }
        }
        return Collections.unmodifiableMap(newMap);
    }
}