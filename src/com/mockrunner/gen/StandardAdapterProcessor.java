package com.mockrunner.gen;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.base.HTMLOutputModule;
import com.mockrunner.base.HTMLOutputTestCase;
import com.mockrunner.util.ClassUtil;

public class StandardAdapterProcessor implements AdapterProcessor
{
    private String name;
    private String output;
    
    public void process(Class module)
    {
        JavaClassGenerator classGenerator = new JavaClassGenerator();
        classGenerator.setCreateJavaDocComments(true);
        classGenerator.setPackage(module.getPackage());
        classGenerator.setClassName(prepareName(module));
        Class superClass = getSuperClass(module);
        if(null != superClass)
        {
            classGenerator.setSuperClass(getSuperClass(module));
        }
        classGenerator.setClassComment(getClassComment(module));
        output = classGenerator.generate();
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getOutput()
    {
        return output;
    }
    
    private String prepareName(Class module)
    {
        String className = ClassUtil.getClassName(module);
        int moduleIndex = className.indexOf("Module");
        if(moduleIndex > -1)
        {
            className = className.substring(0, moduleIndex);
        }
        className = prepareClassNameFromBaseName(className);
        name = "com/mockrunner/servlet/" + className + ".java";
        return className;
    }
    
    private String[] getClassComment(Class module)
    {
        String name = module.getName();
        String[] comment = new String[3];
        comment[0] = "Delegator for {@link " + name + "}. You can";
        comment[1] = "subclass this adapter or use {@link " + name + "}";
        comment[2] = "directly (so your test case can use another base class).";
        return comment;
    }
    
    private Class getSuperClass(Class module)
    {
        if(HTMLOutputModule.class.isAssignableFrom(module))
        {
            return HTMLOutputTestCase.class;
        }
        return BaseTestCase.class;
    }

    private String prepareClassNameFromBaseName(String className)
    {
        return "Test" + className + "CaseAdapter";
    }
}
