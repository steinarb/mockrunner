package com.mockrunner.gen.util;

import com.mockrunner.base.BasicHTMLOutputTestCase;
import com.mockrunner.base.HTMLOutputModule;

public class BasicAdapterProcessor extends StandardAdapterProcessor
{
    protected String[] getSetUpMethodCodeLines(Class module, String memberName)
    {
        return super.getSetUpMethodCodeLines(module, memberName);
    }
    
    protected String[] getTearDownMethodCodeLines(String memberName)
    {
        return super.getTearDownMethodCodeLines(memberName);
    }
    
    protected void addAdditionalControlMethods(Class module, JavaClassGenerator classGenerator, String memberName)
    {
        super.addAdditionalControlMethods(module, classGenerator, memberName);
    }
    
    protected String[] getClassComment(Class module)
    {
        String name = module.getName();
        String[] comment = new String[9];
        comment[0] = "Delegator for {@link " + name + "}. You can";
        comment[1] = "subclass this adapter or use {@link " + name + "}";
        comment[2] = "directly (so your test case can use another base class).";
        comment[3] = "This basic adapter can be used if you don't need any other modules. It";
        comment[4] = "does not extend {@link com.mockrunner.base.BaseTestCase}. If you want";
        comment[5] = "to use several modules in conjunction, consider subclassing";
        comment[6] = "{@link com.mockrunner.servlet.ServletTestCaseAdapter}.";
        comment[7] = "<b>This class is generated from the {@link " + name + "}";
        comment[8] = "and should not be edited directly</b>.";
        return comment;
    }
    
    protected Class getSuperClass(Class module)
    {
        if(HTMLOutputModule.class.isAssignableFrom(module))
        {
            return BasicHTMLOutputTestCase.class;
        }
        return null;
    }

    protected String prepareClassNameFromBaseName(String className)
    {
        return "TestBasic" + className + "CaseAdapter";
    }
}
