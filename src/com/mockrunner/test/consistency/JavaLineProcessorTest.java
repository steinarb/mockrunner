package com.mockrunner.test.consistency;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.mockrunner.test.consistency.JavaLineParser.Block;
import com.mockrunner.test.consistency.JavaLineParser.Line;

public class JavaLineProcessorTest extends TestCase
{
    private final static String NL = System.getProperty("line.separator");
    
    private String getValidTestCode()
    {
        StringBuffer testJava = new StringBuffer();
        testJava.append("package com.mockrunner.test.consistency;\n");
        testJava.append("\n");
        testJava.append("import java.io.FileInputStream;\n");
        testJava.append("import java.io.FileReader;\n");
        testJava.append("\n");
        testJava.append("import junit.framework.TestCase;\n");
        testJava.append("\n");
        testJava.append("public class JavaLineParserTest extends TestCase\n");
        testJava.append("{\n");
        testJava.append("    private String test;\n");
        testJava.append("    protected int myInt;\n");
        testJava.append("    public test()\n");
        testJava.append("{\n");
        testJava.append(" //do it\n");
        testJava.append(" if(true)\n");
        testJava.append("{\n");
        testJava.append("  \t}\n");
        testJava.append("}\n");
        testJava.append("\n");
        testJava.append("public anotherMethod()\n");
        testJava.append("{\n");
        testJava.append("}\n");
        testJava.append("}");
        return testJava.toString();
    }
    
    private String getInvalidTestCode()
    {
        StringBuffer testJava = new StringBuffer();
        testJava.append("package com.mockrunner.test.consistency;\n");
        testJava.append("\n");
        testJava.append("import java.io.FileInputStream;\n");
        testJava.append("import java.io.FileReader;\n");
        testJava.append("");
        testJava.append("import junit.framework.TestCase;\n");
        testJava.append("\n");
        testJava.append("public class JavaLineParserTest extends TestCase\n");
        testJava.append("{\n");
        testJava.append("    private String test;\n");
        testJava.append("    protected int myInt;\n");
        testJava.append("    public test()\n");
        testJava.append("{\n");
        testJava.append(" //do it\n");
        testJava.append(" if(true)\n");
        testJava.append("{\n");
        testJava.append("{\n");
        testJava.append("  \t}\n");
        testJava.append("}}}}}}}}}}}}\n");
        return testJava.toString();
    }
    
    private String getNestedTestCode()
    {
        StringBuffer testJava = new StringBuffer();
        testJava.append("test\n");
        testJava.append("\n");
        testJava.append("\n");
        testJava.append("{\n");
        testJava.append("{\n");
        testJava.append("ff{sfs\n");
        testJava.append("yxv{yxvx\n");
        testJava.append("\n");
        testJava.append("abc\n");
        testJava.append("}}\n");
        testJava.append("abc\n");
        testJava.append("\n");
        testJava.append("\n");
        testJava.append("}\n");
        testJava.append("}\n");
        testJava.append("}}}\n");
        return testJava.toString();
    }
    
    public void testParseValid() throws Exception
    {
        JavaLineParser parser = new JavaLineParser(getValidTestCode());
        List lineList = new ArrayList();
        lineList.add("import java.io.FileReader");
        lineList.add("java.io.FileInputStream");
        parser.addLines(lineList);
        parser.addLine("blic clas");
        List blockList = new ArrayList();
        blockList.add("public test()");
        parser.addBlocks(blockList);
        parser.addBlock("anotherMethod");
        List result = parser.parse();
        assertEquals(5, result.size());
        Line line1 = (Line)result.get(0);
        assertEquals("import java.io.FileInputStream;", line1.getLine());
        assertEquals(3, line1.getLineNumber());
        Line line2 = (Line)result.get(1);
        assertEquals("import java.io.FileReader;", line2.getLine());
        assertEquals(4, line2.getLineNumber());
        Line line3 = (Line)result.get(2);
        assertEquals("public class JavaLineParserTest extends TestCase", line3.getLine());
        assertEquals(8, line3.getLineNumber());
        Block block1 = (Block)result.get(3);
        assertEquals("    public test()", block1.getLine());
        assertEquals(12, block1.getLineNumber());
        assertEquals(18, block1.getEndLineNumber());
        Block block2 = (Block)result.get(4);
        assertEquals("public anotherMethod()", block2.getLine());
        assertEquals(20, block2.getLineNumber());
        assertEquals(22, block2.getEndLineNumber());
    }
    
    public void testParseInvalid() throws Exception
    {
        JavaLineParser parser = new JavaLineParser(getInvalidTestCode());
        parser.addLine("import java.io.FileReader");
        parser.addBlock("public test()");
        List result = parser.parse();
        assertEquals(1, result.size());
        Line line1 = (Line)result.get(0);
        assertEquals("import java.io.FileReader;", line1.getLine());
        assertEquals(4, line1.getLineNumber());
    }
    
    public void testDeeplyNested() throws Exception
    {
        String testJava = getNestedTestCode();
        JavaLineParser parser = new JavaLineParser(testJava);
        parser.addBlock("test");
        List result = parser.parse();
        assertEquals(1, result.size());
        Block block1 = (Block)result.get(0);
        assertEquals("test", block1.getLine());
        assertEquals(1, block1.getLineNumber());
        assertEquals(16, block1.getEndLineNumber());
    }
    
    public void testProcessValid() throws Exception
    {
        String testCode = getValidTestCode();
        JavaLineProcessor processor = new JavaLineProcessor(testCode);
        List lineList = new ArrayList();
        lineList.add("import java.io.FileReader");
        lineList.add("java.io.FileInputStream");
        processor.addLines(lineList);
        List blockList = new ArrayList();
        blockList.add("public test()");
        blockList.add("anotherMethod");
        processor.addBlocks(blockList);
        String result = processor.process();
        assertTrue(-1 != result.indexOf("//import java.io.FileReader"));
        assertTrue(-1 != result.indexOf("//import java.io.FileInputStream"));
        assertTrue(-1 != result.indexOf("    /*public test()" + NL + "{" + NL + " //do it" + NL + " if(true)" + NL + "{" + NL + "  \t}" + NL + "}*/"));
        assertTrue(-1 != result.indexOf("/*public anotherMethod()" + NL + "{" + NL + "}*/"));
        assertEquals(stripChars(testCode), stripChars(result));
    }
    
    public void testProcessDeeplyNested() throws Exception
    {
        String testJava = getNestedTestCode();
        JavaLineProcessor processor = new JavaLineProcessor(testJava);
        List blockList = new ArrayList();
        blockList.add("test");
        processor.addBlocks(blockList);
        String result = processor.process();
        assertTrue(result.trim().startsWith("/*"));
        assertTrue(result.trim().endsWith("*/"));
    }
    
    public void testProcessCommentAll() throws Exception
    {
        String testCode = getValidTestCode();
        JavaLineProcessor processor = new JavaLineProcessor(testCode);
        List lineList = new ArrayList();
        lineList.add("");
        processor.addLines(lineList);
        String result = processor.process();
        BufferedReader reader = new BufferedReader(new StringReader(result));
        String currentLine = null;
        while(null != (currentLine = reader.readLine()))
        {
            assertTrue(currentLine.trim().startsWith("//"));
        }
    }
    
    private String stripChars(String theString)
    {
        StringBuffer buffer = new StringBuffer(theString);
        int ii = 0;
        while(ii < buffer.length())
        {
            char currentChar = buffer.charAt(ii);
            if(Character.isWhitespace(currentChar) || currentChar == '/' || currentChar == '*')
            {
                buffer.deleteCharAt(ii);
            }
            else
            {
                ii++;
            }
        }
        return buffer.toString();
    }
}
