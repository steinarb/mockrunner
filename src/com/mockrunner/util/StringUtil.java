package com.mockrunner.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

import com.mockrunner.base.NestedApplicationException;

/**
 * Simple util class for <code>String</code> related methods.
 */
public class StringUtil
{
    private final static Log log = LogFactory.getLog(StringUtil.class);
    
    /**
     * Compares two strings and returns the last
     * index where the two string are equal. If
     * the first character of the two string does
     * not match or if at least one of the two strings
     * is empty, -1 is returned.
     * @param string1 the first string
     * @param string2 the second string
     * @return the last index, where the strings are equal
     */
    public static int compare(String string1, String string2)
    {
        int endIndex = Math.min(string1.length(), string2.length());
        for(int ii = 0; ii < endIndex; ii++)
        {
            if(string1.charAt(ii) != string2.charAt(ii)) return ii - 1;
        }
        return endIndex - 1;
    }
    
    /**
     * Appends the entries in the specified <code>List</code> as strings
     * with a terminating <i>"\n"</i> after each row.
     * @param buffer the buffer
     * @param data the <code>List</code> with the data
     */
    public static void appendObjectsAsString(StringBuffer buffer, List data)
    {
        for(int ii = 0; ii < data.size(); ii++)
        {
            buffer.append(data.get(ii));
            buffer.append("\n");
        }
    }
    
    /**
     * Appends <i>number</i> tabs (\t) to the buffer.
     * @param buffer the buffer
     * @param number the number of tabs to append
     */
    public static void appendTabs(StringBuffer buffer, int number)
    {
        for(int ii = 0; ii < number; ii++)
        {
            buffer.append("\t");
        }
    }
    
    /**
     * Splits a string into tokens. Similar to <code>StringTokenizer</code>
     * except that empty tokens are recognized and added as <code>null</code>.
     * With a delimiter of <i>";"</i> the string
     * <i>"a;;b;c;;"</i> will split into
     * <i>["a"] [null] ["b"] ["c"] [null]</i>.
     * @param string the String
     * @param delim the delimiter
     * @param doTrim should each token be trimmed
     * @return the array of tokens
     */
    public static String[] split(String string, String delim, boolean doTrim)
    {
        int pos = 0, begin = 0;
        ArrayList resultList = new ArrayList();
        while((-1 != (pos = string.indexOf(delim, begin))) && (begin < string.length()))
        {
            String token = string.substring(begin, pos);
            if(doTrim) token = token.trim();
            if(token.length() == 0) token = null;
            resultList.add(token);
            begin = pos + delim.length();
        }
        if(begin < string.length())
        {
            String token = string.substring(begin);
            if(doTrim) token = token.trim();
            if(token.length() == 0) token = null;
            resultList.add(token);
        }  
        return (String[])resultList.toArray(new String[resultList.size()]);
    }
    
    /**
     * Returns if the specified strings are equal, ignoring
     * case, if <code>caseSensitive</code> is <code>false</code>.
     * @param source the source String
     * @param target the target String
     * @param caseSensitive is the comparison case sensitive
     * @return <code>true</code> if the strings matches
     *         <code>false</code> otherwise
     */
    public static boolean matchesExact(String source, String target, boolean caseSensitive)
    {
        if(!caseSensitive)
        {
            source = source.toLowerCase();
            target = target.toLowerCase();
        }
        return (source.equals(target));
    }
    
    /**
     * Returns if <code>source</code> contains <code>target</code>, 
     * ignoring case, if <code>caseSensitive</code> is <code>false</code>.
     * @param source the source String
     * @param target the target String
     * @param caseSensitive is the comparison case sensitive
     * @return <code>true</code> if the strings matches
     *         <code>false</code> otherwise
     */
    public static boolean matchesContains(String source, String target, boolean caseSensitive)
    {
        if(!caseSensitive)
        {
            source = source.toLowerCase();
            target = target.toLowerCase();
        }
        return (-1 != source.indexOf(target));
    }
    
    /**
     * Returns if the regular expression <code>target</code> matches 
     * <code>source</code>, ignoring case, if <code>caseSensitive</code> 
     * is <code>false</code>.
     * @param source the source String
     * @param target the target String
     * @param caseSensitive is the comparison case sensitive
     * @return <code>true</code> if the strings matches
     *         <code>false</code> otherwise
     */
    public static boolean matchesPerl5(String source, String target, boolean caseSensitive)
    {
        int mask = Perl5Compiler.CASE_INSENSITIVE_MASK;
        if(caseSensitive)
        {
            mask = Perl5Compiler.DEFAULT_MASK;
        }
        try
        {
            Pattern pattern = new Perl5Compiler().compile(target, mask);
            return (new Perl5Matcher().matches(source, pattern));
        } 
        catch(MalformedPatternException exc)
        {
            log.error("Malformed pattern", exc);
            throw new NestedApplicationException(exc);
        }
    }
}
