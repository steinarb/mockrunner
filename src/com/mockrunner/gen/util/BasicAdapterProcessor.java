package com.mockrunner.gen.util;

import com.mockrunner.base.BasicHTMLOutputTestCase;
import com.mockrunner.base.HTMLOutputModule;
import com.mockrunner.util.ClassUtil;

public class BasicAdapterProcessor extends StandardAdapterProcessor
{
    protected void addMemberDeclarations(JavaClassGenerator classGenerator, MemberInfo memberInfo)
    {
        super.addMemberDeclarations(classGenerator, memberInfo);
        String factoryName = ClassUtil.getArgumentName(memberInfo.getFactory());
        memberInfo.setFactoryMember(factoryName);
        classGenerator.addMemberDeclaration(memberInfo.getFactory(), factoryName);
    }
    
    protected String[] getSetUpMethodCodeLines(MemberInfo memberInfo)
    {
        String[] codeLines = new String[3];
        codeLines[0] = "super.setUp();";
        codeLines[1] = memberInfo.getFactoryMember() + " = create" + ClassUtil.getClassName(memberInfo.getFactory()) + "();";
        String getFactoryCall = "get" + ClassUtil.getClassName(memberInfo.getFactory());
        codeLines[2] = memberInfo.getModuleMember() + " = create" + ClassUtil.getClassName(memberInfo.getModule()) + "(" + getFactoryCall +"());";
        return codeLines;
    }
    
    protected String[] getTearDownMethodCodeLines(MemberInfo memberInfo)
    {
        String[] codeLines = new String[3];
        codeLines[0] = "super.tearDown();";
        codeLines[1] = memberInfo.getModuleMember() + " = null;";
        codeLines[2] = memberInfo.getFactoryMember() + " = null;";
        return codeLines;
    }
    
    protected void addAdditionalControlMethods(JavaClassGenerator classGenerator, MemberInfo memberInfo)
    {
        super.addAdditionalControlMethods(classGenerator, memberInfo);
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
