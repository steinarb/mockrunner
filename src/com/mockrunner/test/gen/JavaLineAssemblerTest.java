package com.mockrunner.test.gen;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class JavaLineAssemblerTest extends TestCase
{
    private final static String NL = System.getProperty("line.separator");
    
    private JavaLineAssembler assembler;
     
    protected void setUp() throws Exception
    {
        super.setUp();
        assembler = new JavaLineAssembler();
    }
    
    public void testReset()
    {
        assembler.appendLine("xyz");
        assembler.reset();
        assertEquals("", assembler.getResult());
    }
    
    public void testAppendBlankAndNewline()
    {
        assembler.appendBlank(3);
        assembler.appendBlank();
        assembler.appendNewLine(3);
        assembler.appendNewLine();
        assertEquals("    " + NL + NL + NL + NL , assembler.getResult());
    }
    
    public void testAppendLine()
    {
        assembler.appendLine(null);
        assertEquals("", assembler.getResult());
        assembler.appendLine("");
        assertEquals("", assembler.getResult());
        assembler.appendLine("testLine");
        assertEquals("testLine" + NL, assembler.getResult());
    }
    
    public void testAppendIndent()
    {
        assembler.appendIndent();
        assertEquals("", assembler.getResult());
        assembler.setIndentLevel(2);
        assembler.appendIndent();
        assertEquals("        ", assembler.getResult());
        assembler.reset();
        assembler.setIndentLevel(1);
        assembler.appendLine("testLine");
        assertEquals("    testLine" + NL, assembler.getResult());
    }
    
    public void testAppendPackageInfo()
    {
        assembler.appendPackageInfo(null);
        assertEquals("", assembler.getResult());
        assembler.appendPackageInfo("");
        assertEquals("", assembler.getResult());
        assembler.appendPackageInfo("myPackage");
        assertEquals("package myPackage;" + NL + NL, assembler.getResult());
    }
    
    public void testAppendImports()
    {
        assembler.appendImports(null);
        assertEquals("", assembler.getResult());
        assembler.appendImports(new ArrayList());
        assertEquals("", assembler.getResult());
        assembler.appendImport("com.MyClass");
        assertEquals("import com.MyClass;" + NL, assembler.getResult());
        assembler.reset();
        List importList = new ArrayList();
        importList.add("com.MyClass1");
        importList.add("com.MyClass2");
        importList.add("com.MyClass3");
        assembler.appendImports(importList);
        assertEquals("import com.MyClass1;" + NL + "import com.MyClass2;" + NL + "import com.MyClass3;" + NL, assembler.getResult()); 
    }
    
    public void testAppendClassDefintion()
    {
        assembler.appendClassDefintion(null);
        assertEquals("", assembler.getResult());
        assembler.appendClassDefintion("");
        assertEquals("", assembler.getResult());
        assembler.appendClassDefintion("MyClass");
        assertEquals("public class MyClass" + NL, assembler.getResult());
        assembler.reset();
        assembler.appendClassDefintion("MyClass", "");
        assertEquals("public class MyClass" + NL, assembler.getResult());
        assembler.reset();
        assembler.appendClassDefintion("MyClass", new String[0]);
        assertEquals("public class MyClass" + NL, assembler.getResult());
        assembler.reset();
        assembler.appendClassDefintion("MyClass", "", null);
        assertEquals("public class MyClass" + NL, assembler.getResult());
    }
    
    public void testAppendClassDefintionWithSuperClass()
    {
        assembler.appendClassDefintion(null, "MySuperClass");
        assertEquals("", assembler.getResult());
        assembler.appendClassDefintion("", "MySuperClass");
        assertEquals("", assembler.getResult());
        assembler.appendClassDefintion("MyClass", "MySuperClass");
        assertEquals("public class MyClass extends MySuperClass" + NL, assembler.getResult());
        assembler.reset();
        assembler.appendClassDefintion("MyClass", "MySuperClass", null);
        assertEquals("public class MyClass extends MySuperClass" + NL, assembler.getResult());
    }
    
    public void testAppendClassDefintionWithInterfaces()
    {
        assembler.appendClassDefintion(null, new String[] {"1"});
        assertEquals("", assembler.getResult());
        assembler.appendClassDefintion("", new String[] {"1"});
        assertEquals("", assembler.getResult());
        assembler.appendClassDefintion("MyClass", new String[] {"1"});
        assertEquals("public class MyClass implements 1" + NL, assembler.getResult());
        assembler.reset();
        assembler.appendClassDefintion("MyClass", new String[] {"Interface1", "Interface2", "Interface3"});
        assertEquals("public class MyClass implements Interface1, Interface2, Interface3" + NL, assembler.getResult());
        assembler.reset();
        assembler.appendClassDefintion("MyClass", "", new String[] {"1", "2"});
        assertEquals("public class MyClass implements 1, 2" + NL, assembler.getResult());
    }
    
    public void testAppendClassDefintionWithSuperClassAndInterfaces()
    {
        assembler.setIndentLevel(2);
        assembler.appendClassDefintion("MyClass", "MySuperClass", new String[] {"1", "2"});
        assertEquals("        public class MyClass extends MySuperClass implements 1, 2" + NL, assembler.getResult());
    }
    
    public void testAppendMemberDeclaration()
    {
        assembler.setIndentLevel(1);
        assembler.appendMemberDeclaration("", "myName");
        assertEquals("", assembler.getResult());
        assembler.appendMemberDeclaration("MyType", null);
        assertEquals("", assembler.getResult());
        assembler.appendMemberDeclaration("MyType", "myName");
        assertEquals("    private MyType myName;" + NL, assembler.getResult());
    }
    
    public void testAppendMethodDeclaration()
    {
        assembler.setIndentLevel(2);
        assembler.appendMethodDeclaration("");
        assertEquals("", assembler.getResult());
        assembler.appendMethodDeclaration(null);
        assertEquals("", assembler.getResult());
        assembler.appendMethodDeclaration("myMethod");
        assertEquals("        void myMethod()" + NL, assembler.getResult());
        assembler.reset();
        assembler.setDefaultMethodModifier("protected");
        assembler.setIndentLevel(1);
        assembler.appendMethodDeclaration("myMethod");
        assertEquals("    protected void myMethod()" + NL, assembler.getResult());
    }
}
