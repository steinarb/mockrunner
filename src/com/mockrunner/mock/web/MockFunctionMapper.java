package com.mockrunner.mock.web;

/*import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.el.FunctionMapper;*/

/**
 * Mock implementation of <code>FunctionMapper</code>.
 * <b>Ommited in J2EE 1.3 version</b>
 */
public class MockFunctionMapper //implements FunctionMapper
{
    /*private Map functions = new HashMap();
    
    public void addFunction(String prefix, String localName, Method function)
    {
        FunctionMappingEntry entry = new FunctionMappingEntry(prefix, localName);
        functions.put(entry, function);
    }
    
    public void clearFunctions()
    {
        functions.clear();
    }
    
    public Method resolveFunction(String prefix, String localName)
    {
        FunctionMappingEntry entry = new FunctionMappingEntry(prefix, localName);
        return (Method)functions.get(entry);
    }

    private class FunctionMappingEntry
    {
        private String prefix;
        private String localName;
        
        public FunctionMappingEntry(String prefix, String localName)
        {
            this.prefix = prefix;
            this.localName = localName;
        }
        
        public String getLocalName()
        {
            return localName;
        }

        public String getPrefix()
        {
            return prefix;
        }
        
        public boolean equals(Object obj)
        {
            if(!(obj instanceof FunctionMappingEntry)) return false;
            if(obj == this) return true;
            FunctionMappingEntry otherEntry = (FunctionMappingEntry)obj;
            boolean prefixOk = false;
            boolean nameOk = false;
            if(null == prefix)
            {
                prefixOk = (otherEntry.getPrefix() == null);
            }
            else
            {
                prefixOk = prefix.equals(otherEntry.getPrefix());
            }
            if(null == localName)
            {
                nameOk = (otherEntry.getLocalName() == null);
            }
            else
            {
                nameOk = localName.equals(otherEntry.getLocalName());
            }
            return (prefixOk && nameOk);
        }

        public int hashCode()
        {
            int prefixHash = 0;
            if(null != prefix)
            {
                prefixHash = prefix.hashCode();
            }
            int localHash = 0;
            if(null != localName)
            {
                localHash = localName.hashCode();
            }
            return (prefixHash + localHash);
        }
    }*/
}
