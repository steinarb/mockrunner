package com.mockrunner.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Simple util class with some methods for searching into collections
 */
public class SearchUtil
{
    /**
     * Compares all keys in the specified <code>Map</code> with the
     * specified query string using the method {@link #doesStringMatch}.
     * If the strings match, the corresponding object from the <code>Map</code>
     * is added to the resulting <code>List</code>. If the object to add is
     * a <code>Collection</code>, all elements of the <code>Collection</code>
     * will be added.
     * @param dataMap the source <code>Map</code>
     * @param query the query string that must match the keys in <i>dataMap</i>
     * @param caseSensitive is comparison case sensitive
     * @param exactMatch compare exactly
     * @param queryContainsMapData only matters if <i>exactMatch</i> is <code>false</code>,
     *        specifies if query must be contained in the <code>Map</code> keys (<code>false</code>)
     *        or if query must contain the <code>Map</code> keys (<code>true</code>)
     * @return the result <code>List</code>
     */
    public static List getMatchingObjects(Map dataMap, String query, boolean caseSensitive, boolean exactMatch, boolean queryContainsMapData)
    {
        if(null == query) query = "";
        Iterator iterator = dataMap.keySet().iterator();
        ArrayList resultList = new ArrayList();
        while(iterator.hasNext())
        {
            String nextKey = (String)iterator.next();
            String source, currentQuery;
            if(queryContainsMapData)
            {
                source = query;
                currentQuery = nextKey;
            }
            else
            {
                source = nextKey;
                currentQuery = query;
            }
            if(doesStringMatch(source, currentQuery, caseSensitive, exactMatch))
            {
                Object matchingObject = dataMap.get(nextKey);
                if(matchingObject instanceof Collection)
                {
                    resultList.addAll((Collection)matchingObject);
                }
                else
                {
                    resultList.add(dataMap.get(nextKey));
                }    
            } 
        }
        return resultList;
    }
    
    /**
     * Compares all elements in the specified <code>Collections</code> with the
     * specified query string using the method {@link #doesStringMatch}.
     * @param col the <code>Collections</code>
     * @param query the query string that must match the keys in <i>col</i>
     * @param caseSensitive is comparison case sensitive
     * @param exactMatch compare exactly
     * @param queryContainsSetData only matters if <i>exactMatch</i> is <code>false</code>,
     *        specifies if query must be contained in the <code>Set</code> (<code>false</code>)
     *        or if query must contain the <code>Set</code> (<code>true</code>)
     * @return <code>true</code> if <i>col</i> contains <i>query</i>, false otherwise
     */
    public static boolean contains(Collection col, String query, boolean caseSensitive, boolean exactMatch, boolean queryContainsSetData)
    {
        Iterator iterator = col.iterator();
        while(iterator.hasNext())
        {
            String nextKey = (String)iterator.next();
            String source, currentQuery;
            if(queryContainsSetData)
            {
                source = query;
                currentQuery = nextKey;
            }
            else
            {
                source = nextKey;
                currentQuery = query;
            }
            if(doesStringMatch(source, currentQuery, caseSensitive, exactMatch)) return true;
        }
        return false;
    }
    
    /**
     * Simple helper method. Compares two strings and returns if
     * they match. If <i>caseSensitive</i> is set to <code>false</code>
     * the case of the strings is ignored. If <i>exactMatch</i> is
     * set to <code>true</code>, the strings must match exactly (i.e.
     * the <code>equals</code> method is used). Otherwise, the method
     * returns <code>true</code>, if <i>source</i> cotains <i>query</i>.
     * @param query the query string that must match source
     * @param source the source string
     * @param caseSensitive is comparison case sensitive
     * @param exactMatch compare exactly
     * @return <code>true</code> of the strings match, <code>false</code> otherwise
     */
    public static boolean doesStringMatch(String source, String query, boolean caseSensitive, boolean exactMatch)
    {
        if(null == source) source = "";
        if(null == query) query = "";
        if(!caseSensitive)
        {
            source = source.toLowerCase();
            query = query.toLowerCase();
        }
        if(exactMatch)
        {
            if(source.equals(query)) return true;
        }
        else
        {
            if(-1 != source.indexOf(query)) return true;
        }
        return false;
    }
}
