package com.mockrunner.gen.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.base.HTMLOutputModule;
import com.mockrunner.base.HTMLOutputTestCase;
import com.mockrunner.base.WebTestModule;
import com.mockrunner.gen.util.JavaClassGenerator.ConstructorDeclaration;
import com.mockrunner.gen.util.JavaClassGenerator.MethodDeclaration;
import com.mockrunner.util.ArrayUtil;
import com.mockrunner.util.ClassUtil;

public class StandardAdapterProcessor implements AdapterProcessor
{
    private String name;
    private String output;
    
    public void process(Class module, List excludedMethods)
    {
        JavaClassGenerator classGenerator = new JavaClassGenerator();
        BCELClassAnalyzer analyzer = new BCELClassAnalyzer(module);
        classGenerator.setCreateJavaDocComments(true);
        classGenerator.setPackage(module.getPackage());
        classGenerator.setClassName(prepareName(module));
        Class superClass = getSuperClass(module);
        if(null != superClass)
        {
            classGenerator.setSuperClass(getSuperClass(module));
        }
        classGenerator.setClassComment(getClassComment(module));
        String memberName = ClassUtil.getArgumentName(module);
        classGenerator.addMemberDeclaration(module, memberName);
        addConstructors(classGenerator);
        addTearDownMethod(classGenerator, memberName);
        addSetUpMethod(classGenerator, module, memberName);
        addHTMLOutputAndWebTestMethods(classGenerator, module, memberName);
        addGetAndSetModuleMethods(classGenerator, module, memberName);
        addDelegatorMethods(classGenerator, analyzer, module, excludedMethods, memberName);
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
    
    private Class determineFactoryClass(Class module)
    {
        Constructor[] constructors = module.getDeclaredConstructors();
        for(int ii = 0; ii < constructors.length; ii++)
        {
            Constructor constructor = constructors[ii];
            if(constructor.getParameterTypes().length == 1)
            {
                return constructor.getParameterTypes()[0];
            }
        }
        throw new RuntimeException("Module " + module.getName() + " has no constructor with mock object factory argument");
    }
    
    private void addTearDownMethod(JavaClassGenerator classGenerator, String memberName)
    {
        MethodDeclaration method = createProtectedMethod();
        method.setName("tearDown");
        method.setExceptions(new Class[] {Exception.class});
        method.setCodeLines(new String[] {"super.tearDown();", memberName + " = null;"});
        classGenerator.addMethodDeclaration(method);
    }
    
    private void addSetUpMethod(JavaClassGenerator classGenerator, Class module, String memberName)
    {
        MethodDeclaration method = createProtectedMethod();
        method.setName("setUp");
        method.setExceptions(new Class[] {Exception.class});
        String[] comment = new String[2];
        comment[0] = "Creates the " + getJavaDocModuleLink(module) + ". If you";
        comment[1] = "overwrite this method, you must call <code>super.setUp()</code>.";
        method.setCommentLines(comment);
        String[] codeLines = new String[2];
        codeLines[0] = "super.setUp();";
        String factoryCall = "get" + ClassUtil.getClassName(determineFactoryClass(module));
        codeLines[1] = memberName + " = create" + ClassUtil.getClassName(module) + "(" + factoryCall +"());";
        method.setCodeLines(codeLines);
        classGenerator.addMethodDeclaration(method);
    }
    
    private void addHTMLOutputAndWebTestMethods(JavaClassGenerator classGenerator, Class module, String memberName)
    {
        if(!HTMLOutputModule.class.isAssignableFrom(module)) return;
        String[] codeLines = new String[] {"return " + memberName + ";"};
        MethodDeclaration webTestMethod = createProtectedMethod();
        webTestMethod.setName("getWebTestModule");
        webTestMethod.setReturnType(WebTestModule.class);
        String[] comment = new String[3];
        comment[0] = "Returns the " + getJavaDocModuleLink(module) + " as";
        comment[1] = "{@link com.mockrunner.base.WebTestModule}.";
        comment[2] = "@return the {@link com.mockrunner.base.WebTestModule}";
        webTestMethod.setCommentLines(comment);
        webTestMethod.setCodeLines(codeLines);
        classGenerator.addMethodDeclaration(webTestMethod);
        MethodDeclaration htmlOutputMethod = createProtectedMethod();
        htmlOutputMethod.setName("getHTMLOutputModule");
        htmlOutputMethod.setReturnType(HTMLOutputModule.class);
        comment = new String[3];
        comment[0] = "Returns the " + getJavaDocModuleLink(module) + " as";
        comment[1] = "{@link com.mockrunner.base.HTMLOutputModule}.";
        comment[2] = "@return the {@link com.mockrunner.base.HTMLOutputModule}";
        htmlOutputMethod.setCommentLines(comment);
        htmlOutputMethod.setCodeLines(codeLines);
        classGenerator.addMethodDeclaration(htmlOutputMethod);
    }
    
    private void addGetAndSetModuleMethods(JavaClassGenerator classGenerator, Class module, String memberName)
    {
        MethodDeclaration getMethod = createProtectedMethod();
        getMethod.setName("get" + ClassUtil.getClassName(module));
        getMethod.setReturnType(module);
        String[] comment = new String[2];
        comment[0] = "Gets the " + getJavaDocModuleLink(module) + ".";
        comment[1] = "@return the {@link " + module.getName() + "}";
        getMethod.setCommentLines(comment);
        getMethod.setCodeLines(new String[] {"return " + memberName + ";"});
        classGenerator.addMethodDeclaration(getMethod);
        MethodDeclaration setMethod = createProtectedMethod();
        setMethod.setName("set" + ClassUtil.getClassName(module));
        comment = new String[2];
        comment[0] = "Sets the " + getJavaDocModuleLink(module) + ".";
        comment[1] = "@param " + memberName + " the {@link " + module.getName() + "}";
        setMethod.setCommentLines(comment);
        setMethod.setArguments(new Class[] {module});
        setMethod.setArgumentNames(new String[] {memberName});
        setMethod.setCodeLines(new String[] {"this." + memberName + " = " + memberName + ";"});
        classGenerator.addMethodDeclaration(setMethod);
    }
    
    private void addDelegatorMethods(JavaClassGenerator classGenerator, BCELClassAnalyzer analyzer, Class module, List excludedMethods, String memberName)
    {
        Method[] moduleMethods = getDelegateMethods(module, excludedMethods);
        for(int ii = 0; ii < moduleMethods.length; ii++)
        {
            Method method = moduleMethods[ii];
            MethodDeclaration delegationMethod = createProtectedMethod();
            delegationMethod.setName(method.getName());
            delegationMethod.setReturnType(method.getReturnType());
            Class[] exceptions = method.getExceptionTypes();
            if(exceptions.length > 0)
            {
                delegationMethod.setExceptions(exceptions);
            }
            Class[] parameters = method.getParameterTypes();
            String[] argumentNames = null;
            if(parameters.length > 0)
            {
                delegationMethod.setArguments(parameters);
                argumentNames = analyzer.getArgumentNames(method);
                if(null == argumentNames || argumentNames.length <= 0)
                {
                    argumentNames = prepareSuitableArgumentNames(parameters);
                }
                delegationMethod.setArgumentNames(argumentNames);
            }
            String delegationCodeLine = createDelegationCodeLine(method, memberName, argumentNames);
            delegationMethod.setCodeLines(new String[] {delegationCodeLine});
            String[] delegationMethodComment = createDelegationMethodComment(analyzer, module, method);
            delegationMethod.setCommentLines(delegationMethodComment);
            classGenerator.addMethodDeclaration(delegationMethod);
        }
    }
    
    private void addConstructors(JavaClassGenerator classGenerator)
    {
        classGenerator.addConstructorDeclaration();
        ConstructorDeclaration constructor = new ConstructorDeclaration();
        constructor.setArguments(new Class[] {String.class});
        constructor.setArgumentNames(new String[] {"name"});
        constructor.setCodeLines(new String[] {"super(name);"});
        classGenerator.addConstructorDeclaration(constructor);
    }
    
    private String[] prepareSuitableArgumentNames(Class[] arguments)
    {
        String[] names = new String[arguments.length];
        for(int ii = 0; ii < arguments.length; ii++)
        {
            names[ii] = ClassUtil.getArgumentName(arguments[ii]);
        }
        ArrayUtil.ensureUnique(names);
        return names;
    }
    
    private Method[] getDelegateMethods(Class module, List excludedMethods)
    {
        Method[] moduleMethods = module.getDeclaredMethods();
        List delegateMethods = new ArrayList();
        for(int ii = 0; ii < moduleMethods.length; ii++)
        {
            Method currentMethod = moduleMethods[ii];
            if(shouldMethodBeAdded(currentMethod, excludedMethods))
            {
                delegateMethods.add(currentMethod);
            }
        }
        return (Method[])delegateMethods.toArray(new Method[delegateMethods.size()]);
    }
    
    private boolean shouldMethodBeAdded(Method currentMethod, List excludedMethods)
    {
        if(!Modifier.isPublic(currentMethod.getModifiers())) return false;
        if(null == excludedMethods) return true;
        if(excludedMethods.contains(currentMethod.getName())) return false;
        return true;
    }
    
    private String[] createDelegationMethodComment(BCELClassAnalyzer analyzer, Class module, Method method)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Delegates to {@link ");
        buffer.append(module.getName());
        buffer.append("#");
        buffer.append(method.getName());
        Class[] argumentTypes = method.getParameterTypes();
        if(argumentTypes.length > 0)
        {
            buffer.append("(");
            for(int ii = 0; ii < argumentTypes.length; ii++)
            {
                buffer.append(ClassUtil.getClassName(argumentTypes[ii]));
                if(ii < argumentTypes.length-1)
                {
                    buffer.append(", ");
                }
            }
            buffer.append(")");
        }
        buffer.append("}");
        if(analyzer.isMethodDeprecated(method))
        {
            return new String[] {buffer.toString(), "@deprecated"};
        }
        return new String[] {buffer.toString()};
    }
    
    private String createDelegationCodeLine(Method method, String memberName, String[] argumentNames)
    {
        StringBuffer buffer = new StringBuffer();
        if(!Void.TYPE.equals(method.getReturnType()))
        {
            buffer.append("return ");
        }
        buffer.append(memberName);
        buffer.append(".");
        buffer.append(method.getName());
        buffer.append("(");
        if(null != argumentNames)
        {
            for(int ii = 0; ii < argumentNames.length; ii++)
            {
                buffer.append(argumentNames[ii]);
                if(ii < argumentNames.length-1)
                {
                    buffer.append(", ");
                }
            }
        }
        buffer.append(");");
        return buffer.toString();
    }
    
    private String getJavaDocModuleLink(Class module)
    {
        return "{@link " + module.getName() + "}";
    }

    private MethodDeclaration createProtectedMethod()
    {
        MethodDeclaration method = new MethodDeclaration();
        method.setModifier(Modifier.PROTECTED);
        return method;
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
        name = ClassUtil.getPackageName(module).replace('.', '/') + "/" + className + ".java";
        return className;
    }
    
    private String[] getClassComment(Class module)
    {
        String name = module.getName();
        String[] comment = new String[5];
        comment[0] = "Delegator for {@link " + name + "}. You can";
        comment[1] = "subclass this adapter or use {@link " + name + "}";
        comment[2] = "directly (so your test case can use another base class).";
        comment[3] = "<b>This class is generated from the " + ClassUtil.getClassName(module) + " and should not be";
        comment[4] = "edited directly</b>.";
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
        return className + "CaseAdapter";
    }
}
