package com.mockrunner.test.gen;

import java.util.List;

public class JavaLineAssembler
{
    private StringBuffer buffer;
    private String newLine;
    private int indentLevel;
    private String defaultMethodModifier;
      
    public JavaLineAssembler()
    {
        reset();
        newLine = System.getProperty("line.separator");
    }
    
    public void reset()
    {
        buffer = new StringBuffer();
        indentLevel = 0;
        defaultMethodModifier = "";
    }
    
    public String getResult()
    {
        return buffer.toString();
    }
    
    public StringBuffer getResultAsBuffer()
    {
        return new StringBuffer(getResult());
    }
    
    public void setIndentLevel(int indentLevel)
    {
        this.indentLevel = indentLevel;
    }
    
    public void setDefaultMethodModifier(String defaultMethodModifier)
    {
        this.defaultMethodModifier = defaultMethodModifier;
    }
    
    public void appendBlank()
    {
        buffer.append(" ");
    }
    
    public void appendBlank(int number)
    {
        for(int ii = 0; ii < number; ii++)
        {
            appendBlank();
        }
    }
    
    public void appendNewLine()
    {
        buffer.append(newLine);
    }
    
    public void appendNewLine(int number)
    {
        for(int ii = 0; ii < number; ii++)
        {
            appendNewLine();
        }
    }
    
    public void appendLeftBrace()
    {
        buffer.append("{");
    }
    
    public void appendRightBrace()
    {
        buffer.append("}");
    }
    
    public void appendLeftParenthesis()
    {
        buffer.append("(");
    }
    
    public void appendRightParenthesis()
    {
        buffer.append(")");
    }
    
    public void appendComma()
    {
        buffer.append(",");
    }
    
    public void appendIndent()
    {
        appendBlank(indentLevel * 4);
    }
    
    public void appendLine(String line)
    {
        if(null != line && line.length() > 0)
        {
            appendIndent();
            buffer.append(line);
            appendNewLine();
        }
    }
    
    public void appendPackageInfo(String packageName)
    {
        if(null != packageName && packageName.length() > 0)
        {
            appendLine("package " + packageName + ";");
            appendNewLine();
        }
    }
    
    public void appendImport(String importLine)
    {
        appendLine("import " + importLine + ";");
    }
    
    public void appendImports(List imports)
    {
        if(null == imports) return;
        for(int ii = 0; ii < imports.size(); ii++)
        {
            appendImport((String)imports.get(ii));
        }
    }
    
    public void appendClassDefintion(String className)
    {
        if(null != className && className.length() > 0)
        {
            appendLine("public class " + className);
        }
    }
    
    public void appendClassDefintion(String className, String superClass)
    {
        if(null == className || className.length() <= 0) return;
        if(null == superClass || superClass.length() <= 0)
        {
            appendClassDefintion(className);
        }
        else
        {
            appendLine("public class " + className + " extends " + superClass);
        }
    }
    
    public void appendClassDefintion(String className, String[] interfaceDef)
    {
        if(null == className || className.length() <= 0) return;
        if(null == interfaceDef || interfaceDef.length <= 0)
        {
            appendClassDefintion(className);
            return;
        }
        appendLine("public class " + className + " implements " + prepareCommaSeparatedList(interfaceDef));
    }
    
    public void appendClassDefintion(String className, String superClass, String[] interfaceDef)
    {
        if(null == className || className.length() <= 0) return;
        if(null == superClass || superClass.length() <= 0)
        {
            appendClassDefintion(className, interfaceDef);
            return;
        }
        if(null == interfaceDef || interfaceDef.length <= 0)
        {
            appendClassDefintion(className, superClass);
            return;
        }
        appendLine("public class " + className + " extends " + superClass + " implements " + prepareCommaSeparatedList(interfaceDef));
    }
    
    public void appendMemberDeclaration(String type, String name)
    {
        if(null == type || type.length() <= 0) return;
        if(null == name || name.length() <= 0) return;
        appendLine("private " + type + " " + name + ";");
    }
    
    public void appendMethodDeclaration(String name)
    {
        if(null == name || name.length() <= 0) return;
        StringBuffer buffer = new StringBuffer(30);
        if(null != defaultMethodModifier && defaultMethodModifier.length() > 0)
        {
            buffer.append(defaultMethodModifier + " ");
        }
        buffer.append("void ");
        buffer.append(name);
        buffer.append("()");
        appendLine(buffer.toString());
    }
    
    public void appendMethodDeclaration(String returnType, String name)
    {
        
    }
    
    public void appendMethodDeclaration(String returnType, String name, String[] parameterTypes, String[] parameterNames)
    {
        
    }
    
    public void appendMethodDeclaration(String[] modifiers, String returnType, String name, String[] parameterTypes, String[] parameterNames)
    {
        
    }

    private String prepareModifierList(String[] modifiers)
    {
        StringBuffer listBuffer = new StringBuffer(50);
        for(int ii = 0; ii < modifiers.length; ii++)
        {
            listBuffer.append(modifiers[ii] + " ");
        }
        return listBuffer.toString();
    }
    
    private String prepareCommaSeparatedList(String[] params)
    {
        StringBuffer listBuffer = new StringBuffer(50);
        for(int ii = 0; ii < params.length; ii++)
        {
            listBuffer.append(params[ii]);
            if(ii < params.length - 1)
            {
                listBuffer.append(", ");
            }
        }
        return listBuffer.toString();
    }
}
