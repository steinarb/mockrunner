package com.mockrunner.gen;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.base.HTMLOutputModule;
import com.mockrunner.base.HTMLOutputTestCase;
import com.mockrunner.base.WebTestModule;
import com.mockrunner.gen.JavaClassGenerator.ConstructorDeclaration;
import com.mockrunner.gen.JavaClassGenerator.MethodDeclaration;
import com.mockrunner.util.ClassUtil;
import com.mockrunner.util.StringUtil;

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
        String memberName = StringUtil.lowerCase(ClassUtil.getClassName(module), 0);
        classGenerator.addMemberDeclaration(module, memberName);
        addConstructors(classGenerator);
        addTearDownMethod(classGenerator, memberName);
        addSetUpMethod(classGenerator, module, memberName);
        addHTMLOutputAndWebTestMethods(classGenerator, module, memberName);
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
        String name = module.getName();
        MethodDeclaration method = createProtectedMethod();
        method.setName("setUp");
        method.setExceptions(new Class[] {Exception.class});
        String[] comment = new String[2];
        comment[0] = "Creates the {@link " + name + "}. If you";
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
        comment[0] = "Returns the {@link " + module.getName() + "} as";
        comment[1] = "{@link com.mockrunner.base.WebTestModule}.";
        comment[2] = "@return the {@link com.mockrunner.base.WebTestModule}";
        webTestMethod.setCommentLines(comment);
        webTestMethod.setCodeLines(codeLines);
        classGenerator.addMethodDeclaration(webTestMethod);
        MethodDeclaration htmlOutputMethod = createProtectedMethod();
        htmlOutputMethod.setName("getHTMLOutputModule");
        htmlOutputMethod.setReturnType(HTMLOutputModule.class);
        comment = new String[3];
        comment[0] = "Returns the {@link " + module.getName() + "} as";
        comment[1] = "{@link com.mockrunner.base.HTMLOutputModule}.";
        comment[2] = "@return the {@link com.mockrunner.base.HTMLOutputModule}";
        htmlOutputMethod.setCommentLines(comment);
        htmlOutputMethod.setCodeLines(codeLines);
        classGenerator.addMethodDeclaration(htmlOutputMethod);
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
