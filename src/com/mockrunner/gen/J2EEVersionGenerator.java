package com.mockrunner.gen;

import java.util.HashMap;
import java.util.Map;

public class J2EEVersionGenerator extends AbstractVersionGenerator
{
    public static void main(String[] args) throws Exception
    {
        J2EEVersionGenerator synchVersionUtil = new J2EEVersionGenerator();
        synchVersionUtil.doSynchronize();
    }
    
    protected String getGeneratorName()
    {
        return "Web J2EE1.3";
    }
    
    protected String getRootTargetDir()
    {
        return "srcj2ee1.3";
    }
    
    protected String getRootSourceDir()
    {
        return "src";
    }
    
    protected String[] getProcessedPackages()
    {
        return new String[] {"com/mockrunner/tag", "com/mockrunner/mock/web"};
    }
    
    protected Map prepareProcessorMap()
    {
        Map webFiles = new HashMap();
        
        return webFiles;
    }
}
