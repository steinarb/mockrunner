package com.mockrunner.gen.jar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MockrunnerJars
{
    private static List mockrunnerJars;
    private static List standardInterfaceJars;
    private static List jee5OnlyJars;
    private static Map permissions;
    private static Set strutsJars;
    private static Set webJ2EEJars;
    private static Set otherJ2EEJars;
    private static Set mockEJBJars;
    private static Set xmlJars;
    
    static
    {
        mockrunnerJars = new ArrayList();
        mockrunnerJars.add("mockrunner.jar");
        mockrunnerJars.add("mockrunner-ejb.jar");
        mockrunnerJars.add("mockrunner-jdbc.jar");
        mockrunnerJars.add("mockrunner-jms.jar");
        mockrunnerJars.add("mockrunner-jca.jar");
        mockrunnerJars.add("mockrunner-servlet.jar");
        mockrunnerJars.add("mockrunner-tag.jar");
        mockrunnerJars.add("mockrunner-struts.jar");
        
        permissions = new HashMap();
        permissions.put("mockrunner.jar", new Permission(true, true, true, true, true));
        permissions.put("mockrunner-ejb.jar", new Permission(false, false, true, true, false));
        permissions.put("mockrunner-jdbc.jar", new Permission(false, false, false, false, true));
        permissions.put("mockrunner-jms.jar", new Permission(false, false, true, false, false));
        permissions.put("mockrunner-jca.jar", new Permission(false, false, true, false, false));
        permissions.put("mockrunner-servlet.jar", new Permission(false, true, false, false, true));
        permissions.put("mockrunner-tag.jar", new Permission(false, true, false, false, true));
        permissions.put("mockrunner-struts.jar", new Permission(true, true, false, false, true));
    
        standardInterfaceJars = new ArrayList();
        standardInterfaceJars.add("servlet-api.jar");
        standardInterfaceJars.add("jsp-api.jar");
        standardInterfaceJars.add("el-api.jar");
        standardInterfaceJars.add("jboss-j2ee.jar");
        standardInterfaceJars.add("jboss-javaee-modified.jar");
        standardInterfaceJars.add("servlet.jar");
        
        jee5OnlyJars = new ArrayList();
        jee5OnlyJars.add("jasper.jar");
        jee5OnlyJars.add("jasper-el.jar");
        
        strutsJars = new HashSet();
        strutsJars.add("struts-core-1.3.10.jar");
        
        webJ2EEJars = new HashSet();
        webJ2EEJars.add("servlet-api.jar");
        webJ2EEJars.add("jsp-api.jar");
        webJ2EEJars.add("el-api.jar");
        webJ2EEJars.add("jasper.jar");
        webJ2EEJars.add("jasper-el.jar");
        webJ2EEJars.add("servlet.jar");
        
        otherJ2EEJars = new HashSet();
        otherJ2EEJars.add("jboss-j2ee.jar");
        otherJ2EEJars.add("jboss-javaee-modified.jar");
        
        mockEJBJars = new HashSet();
        mockEJBJars.add("mockejb.jar");
        
        xmlJars = new HashSet();
        xmlJars.add("xercesImpl.jar");
        xmlJars.add("xml-apis.jar");
        xmlJars.add("nekohtml.jar");
        xmlJars.add("jdom.jar");
    }
    
    public static List getMockrunnerJars()
    {
        return mockrunnerJars;
    }
    
    public static List getStandardInterfaceJars()
    {
        return standardInterfaceJars;
    }
    
    public static List getJEE5OnlyJars()
    {
        return jee5OnlyJars;
    }
    
    public static Permission getPermission(String jar)
    {
        return (Permission)permissions.get(jar);
    }
    
    public static class Permission
    {
        private boolean isStrutsDependencyAllowed = false;
        private boolean isWebJ2EEDependencyAllowed = false;
        private boolean isOtherJ2EEDependencyAllowed = false;
        private boolean isMockEJBDependencyAllowed = false;
        private boolean isXMLDependencyAllowed = false;
        
        public Permission(boolean isStrutsDependencyAllowed,
                          boolean isWebJ2EEDependencyAllowed,
                          boolean isOtherJ2EEDependencyAllowed,
                          boolean isMockEJBDependencyAllowed,
                          boolean isXMLDependencyAllowed)
        {
            this.isStrutsDependencyAllowed = isStrutsDependencyAllowed;
            this.isWebJ2EEDependencyAllowed = isWebJ2EEDependencyAllowed;
            this.isOtherJ2EEDependencyAllowed = isOtherJ2EEDependencyAllowed;
            this.isMockEJBDependencyAllowed = isMockEJBDependencyAllowed;
            this.isXMLDependencyAllowed = isXMLDependencyAllowed;
        }
        
        public boolean isOtherJ2EEDependencyAllowed()
        {
            return isOtherJ2EEDependencyAllowed;
        }
        
        public boolean isStrutsDependencyAllowed()
        {
            return isStrutsDependencyAllowed;
        }
        
        public boolean isWebJ2EEDependencyAllowed()
        {
            return isWebJ2EEDependencyAllowed;
        }
        
        public boolean isMockEJBDependencyAllowed()
        {
            return isMockEJBDependencyAllowed;
        }
        
        public boolean isXMLDependencyAllowed()
        {
            return isXMLDependencyAllowed;
        }
        
        public Set getProhibited(Set dependentJars)
        {
            Set jars = new HashSet(dependentJars);
            if(!isStrutsDependencyAllowed)
            {
                jars.removeAll(strutsJars);
            }
            if(!isWebJ2EEDependencyAllowed)
            {
                jars.removeAll(webJ2EEJars);
            }
            if(!isOtherJ2EEDependencyAllowed)
            {
                jars.removeAll(otherJ2EEJars);
            }
            if(!isMockEJBDependencyAllowed)
            {
                jars.removeAll(mockEJBJars);
            }
            if(!isXMLDependencyAllowed)
            {
                jars.removeAll(xmlJars);
            }
            Set finalSet = new HashSet(dependentJars);
            finalSet.removeAll(jars);
            return finalSet;
        }
    }
}
