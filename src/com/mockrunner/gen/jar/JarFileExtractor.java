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
    private List jars;

    public JarFileExtractor(List jars)
    {
        this.jars = new ArrayList(jars);
    }
    
    public Map createDependencies(JarBundle jarBundles[])
    {
        Map finalMap = new HashMap();
        for(int ii = 0; ii < jarBundles.length; ii++) 
        {
            if(jars.contains(jarBundles[ii].getJarFileName()))
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
        if(null == dependendJars)
        {
            return new TreeSet();
        }
        for(int ii = 0; ii < dependendJars.size(); ii++)
        {
            JarBundle currentBundle = (JarBundle)dependendJars.get(ii);
            resultSet.add(currentBundle.getJarFileName());
            resultSet.addAll(createDependencySet(currentBundle));
        }
        return resultSet;
    }
}
