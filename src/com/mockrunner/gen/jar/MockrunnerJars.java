package com.mockrunner.gen.jar;

import java.util.ArrayList;
import java.util.List;

public class MockrunnerJars
{
    private static List mockrunnerJars;
    
    static
    {
        mockrunnerJars = new ArrayList();
        mockrunnerJars.add("mockrunner.jar");
        mockrunnerJars.add("mockrunner-jdk1.3.jar");
        mockrunnerJars.add("mockrunner-ejb.jar");
        mockrunnerJars.add("mockrunner-jdbc.jar");
        mockrunnerJars.add("mockrunner-jdbc-jdk1.3.jar");
        mockrunnerJars.add("mockrunner-jms.jar");
        mockrunnerJars.add("mockrunner-servlet.jar");
        mockrunnerJars.add("mockrunner-tag.jar");
        mockrunnerJars.add("mockrunner-struts.jar");
    }
    
    public static List getMockrunnerJars()
    {
        return mockrunnerJars;
    }
}
