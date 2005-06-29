package com.mockrunner.gen;

import java.util.ArrayList;
import java.util.List;

import com.mockrunner.connector.ConnectorTestModule;
import com.mockrunner.ejb.EJBTestModule;
import com.mockrunner.gen.proc.BasicAdapterProcessor;
import com.mockrunner.gen.proc.EJBBasicAdapterProcessor;
import com.mockrunner.gen.proc.JDBCBasicAdapterProcessor;
import com.mockrunner.gen.proc.StandardAdapterProcessor;
import com.mockrunner.jdbc.JDBCTestModule;
import com.mockrunner.jms.JMSTestModule;
import com.mockrunner.servlet.ServletTestModule;
import com.mockrunner.struts.ActionTestModule;
import com.mockrunner.tag.TagTestModule;

public class AdapterGenerator extends AbstractAdapterGenerator
{
    public static void main(String[] args) throws Exception
    {
        AdapterGenerator generator = new AdapterGenerator();
        generator.generate();
    }
    
    protected String getSrcDir()
    {
        return "src";
    }
    
    protected List initialize()
    {
        List units = new ArrayList();
        List actionExcluded = new ArrayList();
        actionExcluded.add("getOutput");
        units.add(new ProcessingUnit(ActionTestModule.class, new StandardAdapterProcessor(), actionExcluded));
        units.add(new ProcessingUnit(ActionTestModule.class, new BasicAdapterProcessor(), actionExcluded));
        List servletExcluded = new ArrayList();
        servletExcluded.add("getOutput");
        units.add(new ProcessingUnit(ServletTestModule.class, new StandardAdapterProcessor(), servletExcluded));
        units.add(new ProcessingUnit(ServletTestModule.class, new BasicAdapterProcessor(), servletExcluded));
        List tagExcluded = new ArrayList();
        tagExcluded.add("getOutput");
        units.add(new ProcessingUnit(TagTestModule.class, new StandardAdapterProcessor(), tagExcluded));
        units.add(new ProcessingUnit(TagTestModule.class, new BasicAdapterProcessor(), tagExcluded));
        units.add(new ProcessingUnit(EJBTestModule.class, new StandardAdapterProcessor(), null));
        units.add(new ProcessingUnit(EJBTestModule.class, new EJBBasicAdapterProcessor(), null));
        units.add(new ProcessingUnit(JDBCTestModule.class, new StandardAdapterProcessor(), null));
        units.add(new ProcessingUnit(JDBCTestModule.class, new JDBCBasicAdapterProcessor(), null));
        units.add(new ProcessingUnit(JMSTestModule.class, new StandardAdapterProcessor(), null));
        units.add(new ProcessingUnit(JMSTestModule.class, new BasicAdapterProcessor(), null));
        units.add(new ProcessingUnit(ConnectorTestModule.class, new StandardAdapterProcessor(), null));
        units.add(new ProcessingUnit(ConnectorTestModule.class, new BasicAdapterProcessor(), null));
        return units;
    } 
}
