package com.mockrunner.test.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.mockrunner.util.StringUtil;

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
        List groups = new ArrayList();
        for(int ii = 0; ii < order.length; ii++)
        {
            groups.add(new Group(order[ii]));
        }
        for(int ii = 0; ii < imports.size(); ii++)
        {
            String currentImport = (String)imports.get(ii);
            classifyImport(groups, currentImport);
        }
        return null;
    }
    
    private void classifyImport(List groups, String currentImport)
    {
        int maxMatchingCharsBefore = 0;
        int maxMatchingCharsAfter = 0;
        List beforeGroups = new ArrayList();
        List afterGroups = new ArrayList();
        for(int ii = 0; ii < groups.size(); ii++)
        {
            Group currentGroup = (Group)groups.get(ii);
            int match = determineGroupMatch(currentGroup, currentImport);
            if(0 == match)
            {
                currentGroup.addToActualGroup(currentImport);
                return;
            }
            int matchingChars = StringUtil.compare(currentImport, currentGroup.getGroupName());
            if(match < 0)
            {
                if(matchingChars >= maxMatchingCharsBefore)
                {
                    if(matchingChars > maxMatchingCharsBefore)
                    {
                        beforeGroups.clear();
                    }
                    maxMatchingCharsBefore = matchingChars;
                    beforeGroups.add(currentGroup);
                }
                
            }
            else
            {
                if(matchingChars >= maxMatchingCharsAfter)
                {
                    if(matchingChars > maxMatchingCharsAfter)
                    {
                        afterGroups.clear();
                    }
                    maxMatchingCharsAfter = matchingChars;
                    afterGroups.add(currentGroup);
                }
            }
        }
    }
    
    private int determineGroupMatch(Group group, String currentImport)
    {
        if(currentImport.startsWith(group.getGroupName())) return 0;
        return currentImport.compareTo(group.getGroupName());
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
