package com.mockrunner.test.gen;

import java.beans.IntrospectionException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import junit.framework.TestCase;

import com.mockrunner.gen.proc.JavaClassGenerator;
import com.mockrunner.gen.proc.JavaClassGenerator.ConstructorDeclaration;
import com.mockrunner.gen.proc.JavaClassGenerator.MethodDeclaration;
import com.mockrunner.util.common.ClassUtil;
import com.mockrunner.util.common.CollectionUtil;
import com.mockrunner.util.web.XmlUtil;

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
        generator.addImport(HttpServlet.class);
        generator.addImport(TestCase.class);
        generator.addMemberDeclaration(XmlUtil.class, "util");
        generator.addMemberDeclaration(String.class, "name");
        generator.addConstructorDeclaration();
        ConstructorDeclaration constructor = new ConstructorDeclaration();
        constructor.setCommentLines(new String[] {"A", "comment"});
        constructor.setArguments(new Class[] {String.class, JavaClassGeneratorTest.class});
        constructor.setArgumentNames(new String[] {"string1"});
        constructor.setExceptions(new Class[] {Exception.class});
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
        method2.setArgumentNames(new String[] {"string1"});
        generator.addMethodDeclaration(method2);
        MethodDeclaration method3 = new MethodDeclaration();
        method3.setName("thirdMethod");
        method3.setReturnType(Integer.TYPE);
        method3.setCommentLines(new String[] {"Comment"});
        method3.setExceptions(new Class[] {IntrospectionException.class, RuntimeException.class});
        method3.setCodeLines(new String[] {"Line1", "Line2"});
        generator.addMethodDeclaration(method3);
        MethodDeclaration method4 = new MethodDeclaration();
        method4.setName("fourthMethod");
        method4.setReturnType(int[].class);
        method4.setArguments(new Class[] {String.class, URL[][].class, Double[].class, boolean[].class});
        generator.addMethodDeclaration(method4);
        assertEquals(getExpected(), generator.generate());
    }
    
    private String getExpected()
    {
        return "package com.mockrunner.test.gen;" + NL + NL +
               "import java.beans.IntrospectionException;" + NL +
               "import java.net.URL;" + NL +
               "import java.util.List;" + NL +
               "import java.util.Map;" + NL + NL +
               "import javax.servlet.http.HttpServlet;" + NL + NL +
               "import junit.framework.TestCase;" + NL + NL +
               "import com.mockrunner.util.common.ClassUtil;" + NL +
               "import com.mockrunner.util.common.CollectionUtil;" + NL +
               "import com.mockrunner.util.web.XmlUtil;" + NL + NL +
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
               "    /**" + NL +
               "     * A" + NL +
               "     * comment" + NL +
               "     */" + NL +
               "    public MyClass(String string1, JavaClassGeneratorTest javaClassGeneratorTest) throws Exception" + NL +
               "    {" + NL +
               "        Line1" + NL +
               "        Line2" + NL +
               "    }" + NL + NL +
               "    /**" + NL +
               "     * Another" + NL +
               "     * comment" + NL +
               "     */" + NL +
               "    public void aMethod()" + NL +
               "    {" + NL +
               "        Line1" + NL +
               "    }" + NL + NL +
               "    public CollectionUtil anotherMethod(String string1, double doubleValue)" + NL +
               "    {" + NL + NL +
               "    }" + NL + NL +
               "    /**" + NL +
               "     * Comment" + NL +
               "     */" + NL +
               "    public int thirdMethod() throws IntrospectionException, RuntimeException" + NL +
               "    {" + NL +
               "        Line1" + NL +
               "        Line2" + NL +
               "    }" + NL + NL +
               "    public int[] fourthMethod(String string, URL[][] urls, Double[] doubleValues, boolean[] booleanValues)" + NL +
               "    {" + NL + NL +
               "    }" + NL +
               "}";
    }
}
