package com.mockrunner.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Helper class for finding matching SQL statements based on various
 * search parameters.
 */
public class SQLStatementMatcher
{
    private boolean isCaseSensitive = false;
    private boolean isExactMatch = false;
    private boolean isUseRegularExpression = false;
    
    public SQLStatementMatcher(boolean isCaseSensitive, boolean isExactMatch)
    {
        this(isCaseSensitive, isExactMatch, false);
    }
    
    public SQLStatementMatcher(boolean isCaseSensitive, boolean isExactMatch, boolean isUseRegularExpression)
    {
        this.isCaseSensitive = isCaseSensitive;
        this.isExactMatch = isExactMatch;
        this.isUseRegularExpression = isUseRegularExpression;
    }
    
    /**
     * Compares all keys in the specified <code>Map</code> with the
     * specified query string using the method {@link #doesStringMatch}.
     * If the strings match, the corresponding object from the <code>Map</code>
     * is added to the resulting <code>List</code>.
     * @param dataMap the source <code>Map</code>
     * @param query the query string that must match the keys in <i>dataMap</i>
     * @param queryContainsMapData only matters if <i>isExactMatch</i> is <code>false</code>,
     *        specifies if query must be contained in the <code>Map</code> keys (<code>false</code>)
     *        or if query must contain the <code>Map</code> keys (<code>true</code>)
     * @return the result <code>List</code>
     */
    public List getMatchingObjects(Map dataMap, String query, boolean resolveCollection, boolean queryContainsMapData)
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
			if(doesStringMatch(source, currentQuery))
			{
				Object matchingObject = dataMap.get(nextKey);
				if(resolveCollection && (matchingObject instanceof Collection))
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
     * Compares all elements in the specified <code>Collection</code> with the
     * specified query string using the method {@link #doesStringMatch}.
     * @param col the <code>Collections</code>
     * @param query the query string that must match the keys in <i>col</i>
     * @param queryContainsData only matters if <i>exactMatch</i> is <code>false</code>,
     *        specifies if query must be contained in the <code>Collection</code> data (<code>false</code>)
     *        or if query must contain the <code>Collection</code> data (<code>true</code>)
     * @return <code>true</code> if <i>col</i> contains <i>query</i>, false otherwise
     */
    public boolean contains(Collection col, String query, boolean queryContainsData)
    {
        Iterator iterator = col.iterator();
        while(iterator.hasNext())
        {
            String nextKey = (String)iterator.next();
            String source, currentQuery;
            if(queryContainsData)
            {
                source = query;
                currentQuery = nextKey;
            }
            else
            {
                source = nextKey;
                currentQuery = query;
            }
            if(doesStringMatch(source, currentQuery)) return true;
        }
        return false;
    }
    
    /**
     * Compares two strings and returns if they match. 
     * @param query the query string that must match source
     * @param source the source string
     * @return <code>true</code> of the strings match, <code>false</code> otherwise
     */
    public boolean doesStringMatch(String source, String query)
    {
        if(null == source) source = "";
        if(null == query) query = "";
        if(!isCaseSensitive)
        {
            source = source.toLowerCase();
            query = query.toLowerCase();
        }
        if(isExactMatch)
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
