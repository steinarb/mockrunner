package com.mockrunner.gen.jar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.kirkk.analyzer.framework.JarBundle;

public class JarFileExtractor
{
    private List mainJars;
    private List exceptionJars;

    public JarFileExtractor(List mainJars, List exceptionJars)
    {
        this.mainJars = new ArrayList(mainJars);
        this.exceptionJars = new ArrayList(exceptionJars);
    }
    
    public JarBundle[] filterBundles(JarBundle jarBundles[])
    {
        List finalList = new ArrayList();
        for(int ii = 0; ii < jarBundles.length; ii++) 
        {
            if(mainJars.contains(jarBundles[ii].getJarFileName()))
            {
                finalList.add(jarBundles[ii]);
            }
        }
        return (JarBundle[])finalList.toArray(new JarBundle[finalList.size()]);
    }
    
    public Map createDependencies(JarBundle jarBundles[])
    {
        Map finalMap = new HashMap();
        for(int ii = 0; ii < jarBundles.length; ii++) 
        {
            if(mainJars.contains(jarBundles[ii].getJarFileName()))
            {
                Set currentSet = createDependencySet(jarBundles[ii]);
                finalMap.put(jarBundles[ii].getJarFileName(), currentSet);
            }
        }
        return finalMap;
    }
    
    private Set createDependencySet(JarBundle jarBundle)
    {
        Set resultSet = new TreeSet();
        List dependendJars = jarBundle.getDependentJars();
        if(null == dependendJars) return resultSet;
        for(int ii = 0; ii < dependendJars.size(); ii++)
        {
            JarBundle currentBundle = (JarBundle)dependendJars.get(ii);
            String currentJarFileName = currentBundle.getJarFileName();
            resultSet.add(currentJarFileName);
            if(!exceptionJars.contains(currentJarFileName))
            {
                resultSet.addAll(createDependencySet(currentBundle));
            }
        }
        return resultSet;
    }
}
