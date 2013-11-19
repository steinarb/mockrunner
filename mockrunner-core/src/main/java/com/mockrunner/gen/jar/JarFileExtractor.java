package com.mockrunner.gen.jar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.kirkk.analyzer.framework.Jar;

public class JarFileExtractor
{
    private List mainJars;
    private List exceptionJars;

    public JarFileExtractor(List mainJars, List exceptionJars)
    {
        this.mainJars = new ArrayList(mainJars);
        this.exceptionJars = new ArrayList(exceptionJars);
    }
    
    public Jar[] filter(Jar jars[])
    {
        List finalList = new ArrayList();
        for(int ii = 0; ii < jars.length; ii++) 
        {
            if(mainJars.contains(jars[ii].getJarFileName()))
            {
                finalList.add(jars[ii]);
            }
        }
        return (Jar[])finalList.toArray(new Jar[finalList.size()]);
    }
    
    public Map createDependencies(Jar jars[])
    {
        Map finalMap = new HashMap();
        for(int ii = 0; ii < jars.length; ii++) 
        {
            if(mainJars.contains(jars[ii].getJarFileName()))
            {
                Set currentSet = createDependencySet(jars[ii]);
                finalMap.put(jars[ii].getJarFileName(), currentSet);
            }
        }
        return finalMap;
    }
    
    private Set createDependencySet(Jar jar)
    {
        Set resultSet = new TreeSet();
        List dependendJars = jar.getOutgoingDependencies();
        if(null == dependendJars) return resultSet;
        for(int ii = 0; ii < dependendJars.size(); ii++)
        {
            Jar currentJar = (Jar)dependendJars.get(ii);
            String currentJarFileName = currentJar.getJarFileName();
            resultSet.add(currentJarFileName);
            if(!exceptionJars.contains(currentJarFileName))
            {
                resultSet.addAll(createDependencySet(currentJar));
            }
        }
        return resultSet;
    }
}
