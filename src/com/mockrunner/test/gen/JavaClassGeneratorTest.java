package com.mockrunner.test.gen;

import java.util.List;
import java.util.Map;

import com.mockrunner.util.ClassUtil;
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
        generator.addMemberDeclaration("String", "name");
        System.out.println(generator.generate());
        assertEquals(getExpected(), generator.generate());
    }
    
    private String getExpected()
    {
        return "package com.mockrunner.test.gen;" + NL + NL +
               "import com.mockrunner.util.ClassUtil;" + NL +
               "import java.util.Map;" + NL +
               "import java.util.List;" + NL +
               "import com.mockrunner.util.XmlUtil;" + NL + NL +
               "/**" + NL +
               " * This is" + NL +
               " * a comment" + NL +
               " */" + NL +
               "public class MyClass extends ClassUtil implements Map, List" + NL +
               "{" + NL +
               "    private XmlUtil util;" + NL + 
               "    private String name;" + NL + NL +
               "}";
    }
}
