package com.mockrunner.test.gen;

import java.util.HashSet;
import java.util.Set;

import com.mockrunner.gen.jar.MockrunnerJars.Permission;

import junit.framework.TestCase;

public class MockrunnerJarsTest extends TestCase
{
    private Set getTestDependencies()
    {
        Set dependencies = new HashSet();
        dependencies.add("struts.jar");
        dependencies.add("servlet-api.jar");
        dependencies.add("jboss-j2ee.jar");
        dependencies.add("nekohtml.jar");
        dependencies.add("xercesImpl.jar");
        dependencies.add("xyz.jar");
        return dependencies;
    }
    
    public void testPermission()
    {
        Permission permission = new Permission(false, true, true, false, true);
        Set prohibited = permission.getProhibited(getTestDependencies());
        assertEquals(1, prohibited.size());
        assertTrue(prohibited.contains("struts.jar"));
        permission = new Permission(false, true, true, false, false);
        prohibited = permission.getProhibited(getTestDependencies());
        assertEquals(3, prohibited.size());
        assertTrue(prohibited.contains("struts.jar"));
        assertTrue(prohibited.contains("nekohtml.jar"));
        assertTrue(prohibited.contains("xercesImpl.jar"));
        permission = new Permission(false, false, true, false, false);
        prohibited = permission.getProhibited(getTestDependencies());
        assertEquals(4, prohibited.size());
        assertTrue(prohibited.contains("struts.jar"));
        assertTrue(prohibited.contains("servlet-api.jar"));
        assertTrue(prohibited.contains("nekohtml.jar"));
        assertTrue(prohibited.contains("xercesImpl.jar"));
        permission = new Permission(false, false, false, false, false);
        prohibited = permission.getProhibited(getTestDependencies());
        assertEquals(5, prohibited.size());
        assertFalse(prohibited.contains("xyz.jar"));
        permission = new Permission(true, true, true, true, true);
        prohibited = permission.getProhibited(getTestDependencies());
        assertTrue(prohibited.isEmpty());
    }
}
