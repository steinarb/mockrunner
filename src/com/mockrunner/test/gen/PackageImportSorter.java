package com.mockrunner.test.gen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PackageImportSorter
{
    private String[] order;
    
    public PackageImportSorter()
    {
        order = new String[4];
        order[0] = "java";
        order[1] = "javax";
        order[2] = "org";
        order[3] = "com";
    }
    
    public List sortBlocks(List imports)
    {
        List importLines = new ArrayList(imports);
        List groups = new ArrayList();
        for(int ii = 0; ii < order.length; ii++)
        {
            groups.add(new Group(order[ii]));
        }
        return null;
    }
    
    private class Group
    {
        private String groupName;
        private Set beforeGroup;
        private Set actualGroup;
        private Set afterGroup;
        
        public Group(String groupName)
        {
            this.groupName = groupName;
            beforeGroup = new TreeSet();
            actualGroup = new TreeSet();
            afterGroup = new TreeSet();
        }
        
        public String getGroupName()
        {
            return groupName;
        }
        
        public Set getActualGroup()
        {
            return actualGroup;
        }
        
        public Set getAfterGroup()
        {
            return afterGroup;
        }
        
        public Set getBeforeGroup()
        {
            return beforeGroup;
        }
        
        public void addToBeforeGroup(String importString)
        {
            beforeGroup.add(importString);
        }
        
        public void addToActualGroup(String importString)
        {
            actualGroup.add(importString);
        }
        
        public void addToAfterGroup(String importString)
        {
            afterGroup.add(importString);
        }
    }
}
