package com.mockrunner.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple util class for <code>String</code> related methods.
 */
public class StringUtil
{
    /**
     * Appends the entried in the specified <code>List</code> as strings
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
}
