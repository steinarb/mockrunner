package com.mockrunner.test.gen;

import java.util.ArrayList;
import java.util.List;

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
    
    public JavaClassGenerator()
    {
        imports = new ArrayList();
        interfaces = new ArrayList();
        memberTypes = new ArrayList();
        memberNames = new ArrayList();
    }
    
    public void setPackage(String packageInfo)
    {
        this.packageInfo = packageInfo;
    }
    
    public void setPackage(Package packageObj)
    {
        this.packageInfo = packageObj.getName();
    }
    
    public void setClassName(String className)
    {
        this.className = className;
    }
    
    public void setSuperClass(String superClass)
    {
        this.superClass = superClass;
    }
    
    public void setSuperClass(Class superClass)
    {
        this.superClass = ClassUtil.getClassName(superClass);
        addImportIfNotAlreadyImported(superClass);
    }
    
    public void addImport(String importString)
    {
        imports.add(importString);
    }
    
    public void addImport(Class importClass)
    {
        imports.add(importClass.getName());
    }
    
    public void addInterfaceImplementation(String interfaceName)
    {
        interfaces.add(interfaceName);
    }
    
    public void addInterfaceImplementation(Class interfaceClass)
    {
        interfaces.add(ClassUtil.getClassName(interfaceClass));
        addImportIfNotAlreadyImported(interfaceClass);
    }
    
    public void addMemberDeclaration(String memberType, String name)
    {
        memberTypes.add(memberType);
        memberNames.add(name);
    }
    
    public void addMemberDeclaration(Class memberType, String name)
    {
        memberTypes.add(ClassUtil.getClassName(memberType));
        memberNames.add(name);
        addImportIfNotAlreadyImported(memberType);
    }
    
    public String generate()
    {
        JavaLineAssembler assembler = new JavaLineAssembler();
        assembler.appendPackageInfo(packageInfo);
        assembler.appendImports(imports);
        assembler.appendNewLine();
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

    private void appendMembers(JavaLineAssembler assembler)
    {
        for(int ii = 0; ii < memberTypes.size(); ii++)
        {
            assembler.appendMemberDeclaration((String)memberTypes.get(ii), (String)memberNames.get(ii));
        }
    }

    private void addImportIfNotAlreadyImported(Class clazz)
    {
        if(!(imports.contains(clazz.getName())))
        {
            addImport(clazz.getName());
        }
    }
}
