package com.mockrunner.mock.connector.cci;

import java.util.HashMap;
import java.util.Map;

import javax.resource.ResourceException;
import javax.resource.cci.ResultSetInfo;

/**
 * Mock implementation of <code>ResultSetInfo</code>.
 */
public class MockResultSetInfo implements ResultSetInfo
{
    private Map deletesAreDetectedMap;
    private Map insertsAreDetectedMap;
    private Map updatesAreDetectedMap;
    private Map othersDeletesAreVisibleMap;
    private Map othersInsertsAreVisibleMap;
    private Map othersUpdatesAreVisibleMap;
    private Map ownDeletesAreVisibleMap;
    private Map ownInsertsAreVisibleMap;
    private Map ownUpdatesAreVisibleMap;
    private Map supportsResultSetTypeMap;
    private Map supportsResultTypeConcurrencyMap;
    
    public MockResultSetInfo()
    {
        deletesAreDetectedMap = new HashMap();
        insertsAreDetectedMap = new HashMap();
        updatesAreDetectedMap = new HashMap();
        othersDeletesAreVisibleMap = new HashMap();
        othersInsertsAreVisibleMap = new HashMap();
        othersUpdatesAreVisibleMap = new HashMap();
        ownDeletesAreVisibleMap = new HashMap();
        ownInsertsAreVisibleMap = new HashMap();
        ownUpdatesAreVisibleMap = new HashMap();
        supportsResultSetTypeMap = new HashMap();
        supportsResultTypeConcurrencyMap = new HashMap();
    }

    public boolean deletesAreDetected(int type) throws ResourceException
    {
        Boolean deletesAreDetected = (Boolean)deletesAreDetectedMap.get(type);
        if(null == deletesAreDetected) return true;
        return deletesAreDetected;
    }

    public boolean insertsAreDetected(int type) throws ResourceException
    {
        Boolean insertsAreDetected = (Boolean)insertsAreDetectedMap.get(type);
        if(null == insertsAreDetected) return true;
        return insertsAreDetected;
    }
    
    public boolean updatesAreDetected(int type) throws ResourceException
    {
        Boolean updatesAreDetected = (Boolean)updatesAreDetectedMap.get(type);
        if(null == updatesAreDetected) return true;
        return updatesAreDetected;
    }

    public boolean othersDeletesAreVisible(int type) throws ResourceException
    {
        Boolean othersDeletesAreVisible = (Boolean)othersDeletesAreVisibleMap.get(type);
        if(null == othersDeletesAreVisible) return true;
        return othersDeletesAreVisible;
    }

    public boolean othersInsertsAreVisible(int type) throws ResourceException
    {
        Boolean othersInsertsAreVisible = (Boolean)othersInsertsAreVisibleMap.get(type);
        if(null == othersInsertsAreVisible) return true;
        return othersInsertsAreVisible;
    }

    public boolean othersUpdatesAreVisible(int type) throws ResourceException
    {
        Boolean othersUpdatesAreVisible = (Boolean)othersUpdatesAreVisibleMap.get(type);
        if(null == othersUpdatesAreVisible) return true;
        return othersUpdatesAreVisible;
    }

    public boolean ownDeletesAreVisible(int type) throws ResourceException
    {
        Boolean ownDeletesAreVisible = (Boolean)ownDeletesAreVisibleMap.get(type);
        if(null == ownDeletesAreVisible) return true;
        return ownDeletesAreVisible;
    }

    public boolean ownInsertsAreVisible(int type) throws ResourceException
    {
        Boolean ownInsertsAreVisible = (Boolean)ownInsertsAreVisibleMap.get(type);
        if(null == ownInsertsAreVisible) return true;
        return ownInsertsAreVisible;
    }

    public boolean ownUpdatesAreVisible(int type) throws ResourceException
    {
        Boolean ownUpdatesAreVisible = (Boolean)ownUpdatesAreVisibleMap.get(type);
        if(null == ownUpdatesAreVisible) return true;
        return ownUpdatesAreVisible;
    }

    public boolean supportsResultSetType(int type) throws ResourceException
    {
        Boolean supportsResultSetType = (Boolean)supportsResultSetTypeMap.get(type);
        if(null == supportsResultSetType) return true;
        return supportsResultSetType;
    }

    public boolean supportsResultTypeConcurrency(int type, int concurrency) throws ResourceException
    {
        Boolean supportsResultTypeConcurrency = (Boolean)supportsResultTypeConcurrencyMap.get(new SupportsResultTypeConcurrencyKey(type, concurrency));
        if(null == supportsResultTypeConcurrency) return true;
        return supportsResultTypeConcurrency;
    }
    
    public void setDeletesAreDetected(int type, boolean deletesAreDetected)
    {
        deletesAreDetectedMap.put(type, deletesAreDetected);
    }

    public void setInsertsAreDetected(int type, boolean insertsAreDetected)
    {
        insertsAreDetectedMap.put(type, insertsAreDetected);
    }
    
    public void setUpdatesAreDetected(int type, boolean updatesAreDetected)
    {
        updatesAreDetectedMap.put(type, updatesAreDetected);
    }

    public void setOthersDeletesAreVisible(int type, boolean othersDeletesAreVisible)
    {
        othersDeletesAreVisibleMap.put(type, othersDeletesAreVisible);
    }

    public void setOthersInsertsAreVisible(int type, boolean othersInsertsAreVisible)
    {
        othersInsertsAreVisibleMap.put(type, othersInsertsAreVisible);
    }

    public void setOthersUpdatesAreVisible(int type, boolean othersUpdatesAreVisible)
    {
        othersUpdatesAreVisibleMap.put(type, othersUpdatesAreVisible);
    }

    public void setOwnDeletesAreVisible(int type, boolean ownDeletesAreVisible)
    {
        ownDeletesAreVisibleMap.put(type, ownDeletesAreVisible);
    }

    public void setOwnInsertsAreVisible(int type, boolean ownInsertsAreVisible)
    {
        ownInsertsAreVisibleMap.put(type, ownInsertsAreVisible);
    }

    public void setOwnUpdatesAreVisible(int type, boolean ownUpdatesAreVisible)
    {
        ownUpdatesAreVisibleMap.put(type, ownUpdatesAreVisible);
    }

    public void setSupportsResultSetType(int type, boolean supportsResultSetType)
    {
        supportsResultSetTypeMap.put(type, supportsResultSetType);
    }

    public void setSupportsResultTypeConcurrency(int type, int concurrency, boolean supportsResultTypeConcurrency)
    {
        supportsResultTypeConcurrencyMap.put(new SupportsResultTypeConcurrencyKey(type, concurrency), supportsResultTypeConcurrency);
    }

    private class SupportsResultTypeConcurrencyKey
    {
        private int type;
        private int concurrency;
        
        public SupportsResultTypeConcurrencyKey(int type, int concurrency)
        {
            this.type = type;
            this.concurrency = concurrency;
        }
        
        public int getType()
        {
            return type;
        }

        public int getConcurrency()
        {
            return concurrency;
        }

        public boolean equals(Object object)
        {
            if(null == object) return false;
            if(!object.getClass().equals(this.getClass())) return false;
            SupportsResultTypeConcurrencyKey other = (SupportsResultTypeConcurrencyKey)object;
            return (this.getType() == other.getType()) && (this.getConcurrency() == other.getConcurrency());
        }

        public int hashCode()
        {
            return ((31 + type) * 31) + concurrency;
        }
    }
}
