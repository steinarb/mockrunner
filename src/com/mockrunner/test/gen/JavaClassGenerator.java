package com.mockrunner.test.gen;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.mockrunner.util.ArrayUtil;
import com.mockrunner.util.ClassUtil;

public class JavaClassGenerator
{
    private String packageInfo;
    private List imports;
    private String className;
    private String superClass;
    private List interfaces;
    private List memberTypes;
    private List memberNames;
    private String[] classCommentLines;
    private boolean createJavaDocComments;
    private List methods;
    
    public JavaClassGenerator()
    {
        reset();
    }
    
    public void reset()
    {
        imports = new ArrayList();
        interfaces = new ArrayList();
        memberTypes = new ArrayList();
        memberNames = new ArrayList();
        createJavaDocComments = true;
        methods = new ArrayList();
    }

    public void setCreateJavaDocComments(boolean createJavaDocComments)
    {
        this.createJavaDocComments = createJavaDocComments;
    }
    
    public void setPackage(Package packageObj)
    {
        this.packageInfo = packageObj.getName();
    }
    
    public void setClassName(String className)
    {
        this.className = className;
    }
    
    public void setSuperClass(Class superClass)
    {
        this.superClass = ClassUtil.getClassName(superClass);
        addImportIfNecessary(superClass);
    }
    
    public void addImport(Class importClass)
    {
        imports.add(importClass.getName());
    }
    
    public void addInterfaceImplementation(Class interfaceClass)
    {
        interfaces.add(ClassUtil.getClassName(interfaceClass));
        addImportIfNecessary(interfaceClass);
    }
    
    public void setClassComment(String[] commentLines)
    {
        classCommentLines = (String[])ArrayUtil.copyArray(commentLines);
    }
    
    public void addMemberDeclaration(Class memberType, String name)
    {
        memberTypes.add(ClassUtil.getClassName(memberType));
        memberNames.add(name);
        addImportIfNecessary(memberType);
    }
    
    public void addMethodDeclaration(MethodDeclaration method)
    {
        methods.add(method);
    }

    public String generate()
    {
        JavaLineAssembler assembler = new JavaLineAssembler();
        assembler.appendPackageInfo(packageInfo);
        assembler.appendImports(imports);
        assembler.appendNewLine();
        appendCommentBlock(assembler, classCommentLines);
        assembler.appendClassDefintion(className, superClass, (String[])interfaces.toArray(new String[interfaces.size()]));
        assembler.appendLeftBrace();
        assembler.appendNewLine();
        assembler.setIndentLevel(1);
        appendMembers(assembler);
        assembler.setIndentLevel(0);
        assembler.appendNewLine();
        assembler.appendRightBrace();
        return assembler.getResult();
    }
    
    private void appendCommentBlock(JavaLineAssembler assembler, String[] commentLines)
    {
        if(createJavaDocComments)
        {
            assembler.appendJavaDocComment(commentLines);
        }
        else
        {
            assembler.appendBlockComment(commentLines);
        }
    }

    private void appendMembers(JavaLineAssembler assembler)
    {
        for(int ii = 0; ii < memberTypes.size(); ii++)
        {
            assembler.appendMemberDeclaration((String)memberTypes.get(ii), (String)memberNames.get(ii));
        }
    }

    private void addImportIfNecessary(Class clazz)
    {
        if(!(imports.contains(clazz.getName())) && !(clazz.getName().startsWith("java.lang")))
        {
            addImport(clazz);
        }
    }
    
    public final static class MethodDeclaration
    {
        private int modifier;
        private Class returnType;
        private String name;
        private Class[] arguments;
        private String[] argumentNames;
        private String[] codeLines;
        private String[] commentLines;
  
        public MethodDeclaration()
        {
            this("method");
        }
        
        public MethodDeclaration(String name)
        {
            this(Modifier.PUBLIC, name);
        }
        
        public MethodDeclaration(int modifier, String name)
        {
            setModifier(modifier);
            setReturnType(Void.TYPE);
            setName(name);
            arguments = null;
            argumentNames = null;
            codeLines = null;
            commentLines = null;
        }
        
        public String[] getCodeLines()
        {
            return (String[])ArrayUtil.copyArray(codeLines);
        }
        
        public void setCodeLines(String[] codeLines)
        {
            this.codeLines = (String[])ArrayUtil.copyArray(codeLines);
        }
        
        public String[] getCommentLines()
        {
            return (String[])ArrayUtil.copyArray(commentLines);
        }
        
        public void setCommentLines(String[] commentLines)
        {
            this.commentLines = (String[])ArrayUtil.copyArray(commentLines);
        }
        
        public int getModifier()
        {
            return modifier;
        }
        
        public void setModifier(int modifier)
        {
            this.modifier = modifier;
        }
        
        public String getName()
        {
            return name;
        }
        
        public void setName(String name)
        {
            this.name = name;
        }
        
        public Class getReturnType()
        {
            return returnType;
        }
        
        public void setReturnType(Class returnType)
        {
            this.returnType = returnType;
        }
        
        public String[] getArgumentNames()
        {
            return (String[])ArrayUtil.copyArray(argumentNames);
        }
        
        public void setArgumentNames(String[] argumentNames)
        {
            this.argumentNames = (String[])ArrayUtil.copyArray(argumentNames);
        }
        
        public Class[] getArguments()
        {
            return (Class[])ArrayUtil.copyArray(arguments);
        }
        
        public void setArguments(Class[] arguments)
        {
            this.arguments = (Class[])ArrayUtil.copyArray(arguments);
        }
    }
}
