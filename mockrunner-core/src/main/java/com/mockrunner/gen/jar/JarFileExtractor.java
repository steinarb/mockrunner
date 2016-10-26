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
        for (Jar jar : jars) {
            if (mainJars.contains(jar.getJarFileName())) {
                finalList.add(jar);
            }
        }
        return (Jar[])finalList.toArray(new Jar[finalList.size()]);
    }
    
    public Map createDependencies(Jar jars[])
    {
        Map finalMap = new HashMap();
        for (Jar jar : jars) {
            if (mainJars.contains(jar.getJarFileName())) {
                Set currentSet = createDependencySet(jar);
                finalMap.put(jar.getJarFileName(), currentSet);
            }
        }
        return finalMap;
    }
    
    private Set createDependencySet(Jar jar)
    {
        Set resultSet = new TreeSet();
        List dependendJars = jar.getOutgoingDependencies();
        if(null == dependendJars) return resultSet;
        for (Object dependendJar : dependendJars) {
            Jar currentJar = (Jar) dependendJar;
            String currentJarFileName = currentJar.getJarFileName();
            resultSet.add(currentJarFileName);
            if (!exceptionJars.contains(currentJarFileName)) {
                resultSet.addAll(createDependencySet(currentJar));
            }
        }
        return resultSet;
    }
}
