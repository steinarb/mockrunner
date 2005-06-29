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
    private static List mockrunnerJ2EE13Jars;
    private static List mockrunnerJ2EE14Jars;
    private static List standardInterfaceJars;
    private static List standardInterfaceJ2EE14Jars;
    private static Map permissions;
    private static Set strutsJars;
    private static Set webJ2EEJars;
    private static Set otherJ2EEJars;
    private static Set mockEJBJars;
    private static Set xmlJars;
    
    static
    {
        mockrunnerJars = new ArrayList();
        mockrunnerJ2EE13Jars = new ArrayList();
        mockrunnerJ2EE14Jars = new ArrayList();
        mockrunnerJ2EE14Jars.add("mockrunner.jar");
        mockrunnerJ2EE14Jars.add("mockrunner-jdk1.3.jar");
        mockrunnerJ2EE14Jars.add("mockrunner-ejb.jar");
        mockrunnerJ2EE14Jars.add("mockrunner-jdbc.jar");
        mockrunnerJ2EE14Jars.add("mockrunner-jdbc-jdk1.3.jar");
        mockrunnerJ2EE14Jars.add("mockrunner-jms.jar");
        mockrunnerJ2EE14Jars.add("mockrunner-jca.jar");
        mockrunnerJ2EE14Jars.add("mockrunner-servlet.jar");
        mockrunnerJ2EE14Jars.add("mockrunner-tag.jar");
        mockrunnerJ2EE14Jars.add("mockrunner-struts.jar");
        mockrunnerJ2EE13Jars.add("mockrunner-j2ee1.3.jar");
        mockrunnerJ2EE13Jars.add("mockrunner-jdk1.3-j2ee1.3.jar");
        mockrunnerJ2EE13Jars.add("mockrunner-servlet-j2ee1.3.jar");
        mockrunnerJ2EE13Jars.add("mockrunner-struts-j2ee1.3.jar");
        mockrunnerJ2EE13Jars.add("mockrunner-tag-j2ee1.3.jar");
        mockrunnerJars.addAll(mockrunnerJ2EE13Jars);
        mockrunnerJars.addAll(mockrunnerJ2EE14Jars);
        
        permissions = new HashMap();
        permissions.put("mockrunner.jar", new Permission(true, true, true, true, true));
        permissions.put("mockrunner-jdk1.3.jar", new Permission(true, true, true, true, true));
        permissions.put("mockrunner-ejb.jar", new Permission(false, false, true, true, false));
        permissions.put("mockrunner-jdbc.jar", new Permission(false, false, false, false, true));
        permissions.put("mockrunner-jdbc-jdk1.3.jar", new Permission(false, false, false, false, true));
        permissions.put("mockrunner-jms.jar", new Permission(false, false, true, false, false));
        permissions.put("mockrunner-jca.jar", new Permission(false, false, true, false, false));
        permissions.put("mockrunner-servlet.jar", new Permission(false, true, false, false, true));
        permissions.put("mockrunner-tag.jar", new Permission(false, true, false, false, true));
        permissions.put("mockrunner-struts.jar", new Permission(true, true, false, false, true));
        permissions.put("mockrunner-j2ee1.3.jar", new Permission(true, true, true, true, true));
        permissions.put("mockrunner-jdk1.3-j2ee1.3.jar", new Permission(true, true, true, true, true));
        permissions.put("mockrunner-servlet-j2ee1.3.jar", new Permission(false, true, false, false, true));
        permissions.put("mockrunner-struts-j2ee1.3.jar", new Permission(true, true, false, false, true));
        permissions.put("mockrunner-tag-j2ee1.3.jar", new Permission(false, true, false, false, true));
    
        standardInterfaceJars = new ArrayList();
        standardInterfaceJ2EE14Jars = new ArrayList();
        standardInterfaceJ2EE14Jars.add("servlet-api.jar");
        standardInterfaceJ2EE14Jars.add("jsp-api.jar");
        standardInterfaceJ2EE14Jars.add("jboss-j2ee.jar");
        standardInterfaceJars.add("servlet.jar");
        standardInterfaceJars.addAll(standardInterfaceJ2EE14Jars);
        
        strutsJars = new HashSet();
        strutsJars.add("struts.jar");
        
        webJ2EEJars = new HashSet();
        webJ2EEJars.add("servlet-api.jar");
        webJ2EEJars.add("jsp-api.jar");
        
        otherJ2EEJars = new HashSet();
        otherJ2EEJars.add("jboss-j2ee.jar");
        
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
    
    public static List getMockrunnerJ2EE13Jars()
    {
        return mockrunnerJ2EE13Jars;
    }
    
    public static List getMockrunnerJ2EE14Jars()
    {
        return mockrunnerJ2EE13Jars;
    }
    
    public static List getStandardInterfaceJars()
    {
        return standardInterfaceJars;
    }
    
    public static List getStandardInterfaceJ2EE14Jars()
    {
        return standardInterfaceJ2EE14Jars;
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
