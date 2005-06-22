package com.mockrunner.test.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.mockrunner.util.common.StringUtil;

public class StringUtilTest extends TestCase
{
    public void testReplaceAll()
    {
        try
        {
            StringUtil.replaceAll(null, "test", "test");
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        try
        {
            StringUtil.replaceAll("test", null, "test");
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        try
        {
            StringUtil.replaceAll("test", "test", null);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        try
        {
            StringUtil.replaceAll("test", "", "test");
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        assertEquals("test", StringUtil.replaceAll("test", "test", "test"));
        assertEquals("", StringUtil.replaceAll("", "test", "test"));
        assertEquals("abcd", StringUtil.replaceAll("abcd", "test", "test"));
        assertEquals("test", StringUtil.replaceAll("abcd", "abcd", "test"));
        assertEquals("test", StringUtil.replaceAll("test", "testabc", "1"));
        assertEquals("tfst", StringUtil.replaceAll("test", "e", "f"));
        assertEquals("tft", StringUtil.replaceAll("test", "es", "f"));
        assertEquals("", StringUtil.replaceAll("test", "test", ""));
        assertEquals("hello world", StringUtil.replaceAll("test", "test", "hello world"));
        assertEquals("hello worldhello world", StringUtil.replaceAll("testtest", "test", "hello world"));
        assertEquals("ab123abkkjuuababab", StringUtil.replaceAll("a123akkjuuaaa", "a", "ab"));
        assertEquals("aaaa", StringUtil.replaceAll("aaaaaaa", "aa", "a"));
        assertEquals("this is a test", StringUtil.replaceAll("this is a xxxx", "xxxx", "test"));
        assertEquals("aaaaa", StringUtil.replaceAll("aaaaa", "a", "a"));
        assertEquals("bbbbb", StringUtil.replaceAll("aaaaa", "a", "b"));
        assertEquals("bbbb", StringUtil.replaceAll("aabbaa", "aa", "b"));
        assertEquals("Hella warld", StringUtil.replaceAll("Hello world", "o", "a"));
        assertEquals("Hello world", StringUtil.replaceAll("a", "a", "Hello world"));
        assertEquals("Hello world ", StringUtil.replaceAll("   ", "  ", "Hello world"));
        assertEquals("aabc", StringUtil.replaceAll("abcdabc", "abcd", "a"));
    }
    
    public void testCompare()
    {
        assertEquals(-1, StringUtil.compare("", ""));
        assertEquals(-1, StringUtil.compare("", "abc"));
        assertEquals(-1, StringUtil.compare("123", ""));
        assertEquals(-1, StringUtil.compare("123", "abc"));
        assertEquals(-1, StringUtil.compare("ASD", "asd"));
        assertEquals(0, StringUtil.compare("a", "a"));
        assertEquals(0, StringUtil.compare("a", "ab"));
        assertEquals(0, StringUtil.compare("123", "1bc"));
        assertEquals(2, StringUtil.compare("123", "123"));
        assertEquals(2, StringUtil.compare("123", "1234"));
        assertEquals(2, StringUtil.compare("1234", "123"));
        assertEquals(11, StringUtil.compare(" Hello World", " Hello World "));
    }
    
    public void testLowerCase()
    {
        assertEquals("aBC", StringUtil.lowerCase("ABC", 0));
        assertEquals("ABc", StringUtil.lowerCase("ABC", 2));
        assertEquals("abc", StringUtil.lowerCase("abc", 1));
        try
        {
            StringUtil.lowerCase("abc", 3);
            fail();
        } 
        catch(IndexOutOfBoundsException exc)
        {
            //should throw exception
        }
    }
    
    public void testLowerCaseWithEndIndex()
    {
        assertEquals("abC", StringUtil.lowerCase("ABC", 0, 2));
        assertEquals("AbC", StringUtil.lowerCase("ABC", 1, 2));
        assertEquals("abc", StringUtil.lowerCase("abc", 0, 1));
        assertEquals("aBC", StringUtil.lowerCase("ABC", 0, 0));
        assertEquals("aBC", StringUtil.lowerCase("ABC", 0, -1));
        assertEquals("abc", StringUtil.lowerCase("ABC", 0, 3));
        try
        {
            StringUtil.lowerCase("abc", 0, 4);
            fail();
        } 
        catch(IndexOutOfBoundsException exc)
        {
            //should throw exception
        }
    }
    
    public void testSplit()
    {
        String test = "This;;is;a;;  ;test; String;;";
        String[] tokens = StringUtil.split(test, ";", true);
        assertTrue(tokens.length == 9);
        assertEquals("This", tokens[0]);
        assertEquals(null, tokens[1]);
        assertEquals("is", tokens[2]);
        assertEquals("a", tokens[3]);
        assertEquals(null, tokens[4]);
        assertEquals(null, tokens[5]);
        assertEquals("test", tokens[6]);
        assertEquals("String", tokens[7]);
        assertEquals(null, tokens[8]);
        tokens = StringUtil.split(test, "test", false);
        assertTrue(tokens.length == 2);
        assertEquals("This;;is;a;;  ;", tokens[0]);
        assertEquals("; String;;", tokens[1]);
    }
    
    public void testAppendObjectsAsString()
    {
        List list = new ArrayList();
        list.add("Test");
        list.add("Test");
        list.add(new Integer(1000));
        list.add(null);
        list.add(new Long(3));
        StringBuffer buffer = new StringBuffer();
        StringUtil.appendObjectsAsString(buffer, list);
        assertEquals("Test\nTest\n1000\nnull\n3\n", buffer.toString());
    }
    
    public void testCountMatches()
    {
        assertEquals(0, StringUtil.countMatches(null, null));
        assertEquals(0, StringUtil.countMatches(null, ""));
        assertEquals(0, StringUtil.countMatches("", null));
        assertEquals(0, StringUtil.countMatches("", ""));
        assertEquals(1, StringUtil.countMatches("a", "a"));
        assertEquals(3, StringUtil.countMatches("aaa", "a"));
        assertEquals(2, StringUtil.countMatches("abcabcab", "abc"));
        assertEquals(2, StringUtil.countMatches("abcababc", "abc"));
        assertEquals(3, StringUtil.countMatches("abcababc", "a"));
        assertEquals(1, StringUtil.countMatches("Hello World", "ld"));
        assertEquals(3, StringUtil.countMatches(" Hello World ", " "));
        assertEquals(3, StringUtil.countMatches("112211111", "11"));
        assertEquals(1, StringUtil.countMatches("aaaaa", "aaa"));
        assertEquals(1, StringUtil.countMatches("bbb", "bb"));
        assertEquals(0, StringUtil.countMatches("bbb", "bbbb"));
    }
    
    public void testMatchesExact()
    {
        assertTrue(StringUtil.matchesExact("", "", true));
        assertTrue(StringUtil.matchesExact("", "", false));
        assertTrue(StringUtil.matchesExact("abc", "abc", true));
        assertTrue(StringUtil.matchesExact("aBc", "abc", false));
        assertFalse(StringUtil.matchesExact("aBc", "abc", true));
        assertFalse(StringUtil.matchesExact("", "abc", true));
        assertFalse(StringUtil.matchesExact("abc", "abcd", true));
        assertFalse(StringUtil.matchesExact("abcd", "abc", false));
    }
    
    public void testMatchesContains()
    {
        assertTrue(StringUtil.matchesContains("", "", true));
        assertTrue(StringUtil.matchesContains("", "", false));
        assertTrue(StringUtil.matchesContains("abc", "abc", true));
        assertTrue(StringUtil.matchesContains("abcd", "abc", true));
        assertTrue(StringUtil.matchesContains("aBcDDD", "abc", false));
        assertTrue(StringUtil.matchesContains("aBcDDD", "", true));
        assertFalse(StringUtil.matchesContains("aBc", "abc", true));
        assertFalse(StringUtil.matchesContains("", "abc", true));
        assertFalse(StringUtil.matchesContains("abc", "abcd", true));
    }
    
    public void testMatchesPerl5()
    {
        assertTrue(StringUtil.matchesPerl5("abc", "abc", true));
        assertTrue(StringUtil.matchesPerl5("abc", "a[abc]c", true));
        assertTrue(StringUtil.matchesPerl5("aBc", "a[abc]c", false));
        assertTrue(StringUtil.matchesPerl5("abc", ".*", true));
        assertFalse(StringUtil.matchesPerl5("aBc", "abc", true));
        assertFalse(StringUtil.matchesPerl5("aBc", "a[abc]c", true));
        assertFalse(StringUtil.matchesPerl5("a[abc]c", "abc", true));
    }
}
