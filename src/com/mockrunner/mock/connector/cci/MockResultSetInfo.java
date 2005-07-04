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
        Boolean deletesAreDetected = (Boolean)deletesAreDetectedMap.get(new Integer(type));
        if(null == deletesAreDetected) return true;
        return deletesAreDetected.booleanValue();
    }

    public boolean insertsAreDetected(int type) throws ResourceException
    {
        Boolean insertsAreDetected = (Boolean)insertsAreDetectedMap.get(new Integer(type));
        if(null == insertsAreDetected) return true;
        return insertsAreDetected.booleanValue();
    }
    
    public boolean updatesAreDetected(int type) throws ResourceException
    {
        Boolean updatesAreDetected = (Boolean)updatesAreDetectedMap.get(new Integer(type));
        if(null == updatesAreDetected) return true;
        return updatesAreDetected.booleanValue();
    }

    public boolean othersDeletesAreVisible(int type) throws ResourceException
    {
        Boolean othersDeletesAreVisible = (Boolean)othersDeletesAreVisibleMap.get(new Integer(type));
        if(null == othersDeletesAreVisible) return true;
        return othersDeletesAreVisible.booleanValue();
    }

    public boolean othersInsertsAreVisible(int type) throws ResourceException
    {
        Boolean othersInsertsAreVisible = (Boolean)othersInsertsAreVisibleMap.get(new Integer(type));
        if(null == othersInsertsAreVisible) return true;
        return othersInsertsAreVisible.booleanValue();
    }

    public boolean othersUpdatesAreVisible(int type) throws ResourceException
    {
        Boolean othersUpdatesAreVisible = (Boolean)othersUpdatesAreVisibleMap.get(new Integer(type));
        if(null == othersUpdatesAreVisible) return true;
        return othersUpdatesAreVisible.booleanValue();
    }

    public boolean ownDeletesAreVisible(int type) throws ResourceException
    {
        Boolean ownDeletesAreVisible = (Boolean)ownDeletesAreVisibleMap.get(new Integer(type));
        if(null == ownDeletesAreVisible) return true;
        return ownDeletesAreVisible.booleanValue();
    }

    public boolean ownInsertsAreVisible(int type) throws ResourceException
    {
        Boolean ownInsertsAreVisible = (Boolean)ownInsertsAreVisibleMap.get(new Integer(type));
        if(null == ownInsertsAreVisible) return true;
        return ownInsertsAreVisible.booleanValue();
    }

    public boolean ownUpdatesAreVisible(int type) throws ResourceException
    {
        Boolean ownUpdatesAreVisible = (Boolean)ownUpdatesAreVisibleMap.get(new Integer(type));
        if(null == ownUpdatesAreVisible) return true;
        return ownUpdatesAreVisible.booleanValue();
    }

    public boolean supportsResultSetType(int type) throws ResourceException
    {
        Boolean supportsResultSetType = (Boolean)supportsResultSetTypeMap.get(new Integer(type));
        if(null == supportsResultSetType) return true;
        return supportsResultSetType.booleanValue();
    }

    public boolean supportsResultTypeConcurrency(int type, int concurrency) throws ResourceException
    {
        Boolean supportsResultTypeConcurrency = (Boolean)supportsResultTypeConcurrencyMap.get(new SupportsResultTypeConcurrencyKey(type, concurrency));
        if(null == supportsResultTypeConcurrency) return true;
        return supportsResultTypeConcurrency.booleanValue();
    }
    
    public void setDeletesAreDetected(int type, boolean deletesAreDetected)
    {
        deletesAreDetectedMap.put(new Integer(type), new Boolean(deletesAreDetected));
    }

    public void setInsertsAreDetected(int type, boolean insertsAreDetected)
    {
        insertsAreDetectedMap.put(new Integer(type), new Boolean(insertsAreDetected));
    }
    
    public void setUpdatesAreDetected(int type, boolean updatesAreDetected)
    {
        updatesAreDetectedMap.put(new Integer(type), new Boolean(updatesAreDetected));
    }

    public void setOthersDeletesAreVisible(int type, boolean othersDeletesAreVisible)
    {
        othersDeletesAreVisibleMap.put(new Integer(type), new Boolean(othersDeletesAreVisible));
    }

    public void setOthersInsertsAreVisible(int type, boolean othersInsertsAreVisible)
    {
        othersInsertsAreVisibleMap.put(new Integer(type), new Boolean(othersInsertsAreVisible));
    }

    public void setOthersUpdatesAreVisible(int type, boolean othersUpdatesAreVisible)
    {
        othersUpdatesAreVisibleMap.put(new Integer(type), new Boolean(othersUpdatesAreVisible));
    }

    public void setOwnDeletesAreVisible(int type, boolean ownDeletesAreVisible)
    {
        ownDeletesAreVisibleMap.put(new Integer(type), new Boolean(ownDeletesAreVisible));
    }

    public void setOwnInsertsAreVisible(int type, boolean ownInsertsAreVisible)
    {
        ownInsertsAreVisibleMap.put(new Integer(type), new Boolean(ownInsertsAreVisible));
    }

    public void setOwnUpdatesAreVisible(int type, boolean ownUpdatesAreVisible)
    {
        ownUpdatesAreVisibleMap.put(new Integer(type), new Boolean(ownUpdatesAreVisible));
    }

    public void setSupportsResultSetType(int type, boolean supportsResultSetType)
    {
        supportsResultSetTypeMap.put(new Integer(type), new Boolean(supportsResultSetType));
    }

    public void setSupportsResultTypeConcurrency(int type, int concurrency, boolean supportsResultTypeConcurrency)
    {
        supportsResultTypeConcurrencyMap.put(new SupportsResultTypeConcurrencyKey(type, concurrency), new Boolean(supportsResultTypeConcurrency));
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
            return (31 * type) + (31 * concurrency);
        }
    }
}
