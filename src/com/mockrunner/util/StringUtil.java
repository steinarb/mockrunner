package com.mockrunner.util;

import java.util.ArrayList;

/**
 * Simple util class for <code>String</code> related methods.
 */
public class StringUtil
{
    /**
     * Appends <i>number</i> tabs to the buffer
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
     * except that empty tokens are recognized.
     * With a delimiter of <i>";"</i> the string
     * <i>"a;;b;c;;"</i> will split into
     * <i>[a] [] [b] [c] []</i>.
     * @param string the String
     * @param delim the delimiter
     * @return the array of tokens
     */
    public static String[] split(String string, String delim)
    {
        int pos = 0, begin = 0;
        ArrayList resultList = new ArrayList();
        while((-1 != (pos = string.indexOf(delim, begin))) && (begin < string.length()))
        {
            String token = string.substring(begin, pos);
            resultList.add(token);
            begin = pos + delim.length();
        }
        if(begin < string.length()) resultList.add(string.substring(begin));
        return (String[])resultList.toArray(new String[resultList.size()]);
    }
}
