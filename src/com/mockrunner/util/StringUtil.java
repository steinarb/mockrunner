package com.mockrunner.util;

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
}
