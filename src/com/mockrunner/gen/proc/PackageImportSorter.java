package com.mockrunner.gen.proc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
        Map groups = new HashMap();
        Set sortedGroups = new TreeSet();
        List resultList = new ArrayList();
        initializeGroups(imports, groups, sortedGroups);
        if(groups.isEmpty())
        {
            prepareSingleBlock(imports, resultList);
            return resultList;
        }
        classifyImports(groups, sortedGroups);
        prepareResultList(groups, resultList);
        return resultList;
    }
    
    private void prepareSingleBlock(List imports, List resultList)
    {
        Set block = new TreeSet(imports);
        resultList.add(block);
    }

    private List getGroupsAsSortedList()
    {
        Set allGroupSet = new TreeSet();
        for(int ii = 0; ii < order.length; ii++)
        {
            allGroupSet.add(order[ii]);
        }
        List allGroups = new ArrayList(allGroupSet);
        Collections.reverse(allGroups);
        return allGroups;
    }
    
    private void prepareResultList(Map groups, List resultList)
    {
        for(int ii = 0; ii < order.length; ii++)
        {
            Group currentGroup = (Group)groups.get(order[ii]);
            if(null != currentGroup)
            {
                addIfNotEmpty(resultList, currentGroup.getBeforeGroup());
                addIfNotEmpty(resultList, currentGroup.getActualGroup());
                addIfNotEmpty(resultList, currentGroup.getAfterGroup());
            }
        }
    }

    private void addIfNotEmpty(List list, Set set)
    {
        if(null == set || set.isEmpty()) return;
        list.add(set);
    }
    
    private void initializeGroups(List imports, Map groups, Set sortedGroups)
    {   
        List allGroups = getGroupsAsSortedList();
        sortedGroups.addAll(imports);
        for(int ii = 0; ii < imports.size(); ii++)
        {
            String currentImport = (String)imports.get(ii);
            createGroupIfMatching(allGroups, groups, sortedGroups, currentImport);
        }
        sortedGroups.addAll(groups.keySet());
    }

    private void createGroupIfMatching(List allGroups, Map groups, Set sortedGroups, String currentImport)
    {
        for(int ii = 0; ii < allGroups.size(); ii++)
        {
            String groupName = (String)allGroups.get(ii);
            if(currentImport.startsWith(groupName))
            {
                Group group = getGroupByName(groups, groupName);
                group.addToActualGroup(currentImport);
                sortedGroups.remove(currentImport);
                return;
            }
        }
    }

    private Group getGroupByName(Map groups, String groupName)
    {
        Group group = (Group)groups.get(groupName);
        if(null == group)    
        {
            group = new Group(groupName);
            groups.put(groupName, group);
        }
        return group;
    }

    private void classifyImports(Map groups, Set sortedGroups)
    {
        Group currentGroup = null;
        Set tempBeforeGroup = new TreeSet();
        Iterator iterator = sortedGroups.iterator();
        while(iterator.hasNext())
        {
            String currentImport = (String)iterator.next();
            Group tempGroup = (Group)groups.get(currentImport);
            currentGroup = handleTempGroup(currentGroup, tempBeforeGroup, currentImport, tempGroup);
        }
    }
    
    private Group handleTempGroup(Group currentGroup, Set tempBeforeGroup, String currentImport, Group tempGroup)
    {
        if(null != tempGroup)
        {
            if(null == currentGroup)
            {
                tempGroup.addAllToBeforeGroup(tempBeforeGroup);
            }
            currentGroup = tempGroup;
        }
        else
        {
            if(null == currentGroup)
            {
                tempBeforeGroup.add(currentImport);
            }
            else
            {
                currentGroup.addToAfterGroup(currentImport);
            }
        }
        return currentGroup;
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
        
        public void addAllToBeforeGroup(Set importSet)
        {
            beforeGroup.addAll(importSet);
        }
        
        public void addAllToActualGroup(Set importSet)
        {
            actualGroup.addAll(importSet);
        }
        
        public void addAllToAfterGroup(Set importSet)
        {
            afterGroup.addAll(importSet);
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
