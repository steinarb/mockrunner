package com.mockrunner.test.gen;

import java.util.List;
import java.util.Map;

import com.mockrunner.test.gen.JavaClassGenerator.ConstructorDeclaration;
import com.mockrunner.test.gen.JavaClassGenerator.MethodDeclaration;
import com.mockrunner.util.ClassUtil;
import com.mockrunner.util.CollectionUtil;
import com.mockrunner.util.FileUtil;
import com.mockrunner.util.XmlUtil;

import junit.framework.TestCase;

public class JavaClassGeneratorTest extends TestCase
{
    private final static String NL = System.getProperty("line.separator");
    
    private JavaClassGenerator generator;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        generator = new JavaClassGenerator();
    }
    
    public void testGenerate()
    {
        generator.setClassName("MyClass");
        String[] commentLines = new String[] {"This is", "a comment"};
        generator.setClassComment(commentLines);
        generator.setPackage(this.getClass().getPackage());
        generator.setSuperClass(ClassUtil.class);
        generator.addInterfaceImplementation(Map.class);
        generator.addInterfaceImplementation(List.class);
        generator.addImport(XmlUtil.class);
        generator.addMemberDeclaration(XmlUtil.class, "util");
        generator.addMemberDeclaration(String.class, "name");
        generator.addConstructorDeclaration();
        ConstructorDeclaration constructor = new ConstructorDeclaration();
        constructor.setCommentLines(new String[] {"A", "comment"});
        constructor.setArguments(new Class[] {String.class, FileUtil.class});
        constructor.setArgumentNames(new String[] {"string1", "util1"});
        constructor.setCodeLines(new String[] {"Line1", "Line2"});
        generator.addConstructorDeclaration(constructor);
        MethodDeclaration method1 = new MethodDeclaration();
        method1.setName("aMethod");
        method1.setCommentLines(new String[] {"Another", "comment"});
        method1.setCodeLines(new String[] {"Line1"});
        generator.addMethodDeclaration(method1);
        MethodDeclaration method2 = new MethodDeclaration();
        method2.setName("anotherMethod");
        method2.setReturnType(CollectionUtil.class);
        method2.setArguments(new Class[] {String.class, Double.TYPE});
        method2.setArgumentNames(new String[] {"string1", "double1"});
        generator.addMethodDeclaration(method2);
        MethodDeclaration method3 = new MethodDeclaration();
        method3.setName("thirdMethod");
        method3.setReturnType(Integer.TYPE);
        method3.setCommentLines(new String[] {"Comment"});
        method3.setCodeLines(new String[] {"Line1", "Line2"});
        generator.addMethodDeclaration(method3);
        assertEquals(getExpected(), generator.generate());
    }
    
    private String getExpected()
    {
        return "package com.mockrunner.test.gen;" + NL + NL +
               "import com.mockrunner.util.ClassUtil;" + NL +
               "import java.util.Map;" + NL +
               "import java.util.List;" + NL +
               "import com.mockrunner.util.XmlUtil;" + NL +
               "import com.mockrunner.util.FileUtil;" + NL +
               "import com.mockrunner.util.CollectionUtil;" + NL + NL +
               "/**" + NL +
               " * This is" + NL +
               " * a comment" + NL +
               " */" + NL +
               "public class MyClass extends ClassUtil implements Map, List" + NL +
               "{" + NL +
               "    private XmlUtil util;" + NL + 
               "    private String name;" + NL + NL +
               "    public MyClass()" + NL +
               "    {" + NL + NL +
               "    }" + NL + NL +
               "    /*" + NL +
               "     * A" + NL +
               "     * comment" + NL +
               "     */" + NL +
               "    public MyClass(String string1, FileUtil util1)" + NL +
               "    {" + NL +
               "        Line1" + NL +
               "        Line2" + NL +
               "    }" + NL + NL +
               "    /*" + NL +
               "     * Another" + NL +
               "     * comment" + NL +
               "     */" + NL +
               "    public void aMethod()" + NL +
               "    {" + NL +
               "        Line1" + NL +
               "    }" + NL + NL +
               "    public CollectionUtil anotherMethod(String string1, double double1)" + NL +
               "    {" + NL + NL +
               "    }" + NL + NL +
               "    /*" + NL +
               "     * Comment" + NL +
               "     */" + NL +
               "    public int thirdMethod()" + NL +
               "    {" + NL +
               "        Line1" + NL +
               "        Line2" + NL +
               "    }" + NL +
               "}";
    }
}
