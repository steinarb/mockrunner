package com.mockrunner.example.connector;

import javax.resource.cci.InteractionSpec;

/**
 * <code>InteractionSpec</code> which can be used to call a 
 * database stored procedure through CCI.
 */
public class CCIInteractionSpec implements InteractionSpec
{
    private String functionName;
    private String schema;
    private String catalog;
    
    public String getCatalog()
    {
        return catalog;
    }
    
    public void setCatalog(String catalog)
    {
        this.catalog = catalog;
    }
    
    public String getFunctionName()
    {
        return functionName;
    }
    
    public void setFunctionName(String functionName)
    {
        this.functionName = functionName;
    }
    
    public String getSchema()
    {
        return schema;
    }
    
    public void setSchema(String schema)
    {
        this.schema = schema;
    }
}
