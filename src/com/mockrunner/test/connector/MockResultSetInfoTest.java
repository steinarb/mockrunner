package com.mockrunner.test.connector;

import com.mockrunner.mock.connector.cci.MockResultSetInfo;

import junit.framework.TestCase;

public class MockResultSetInfoTest extends TestCase
{
    private MockResultSetInfo info;

    protected void setUp() throws Exception
    {
        info = new MockResultSetInfo();
    }

    protected void tearDown() throws Exception
    {
        info = null;
    }
    
    public void testSetAndGet() throws Exception
    {
        info.setInsertsAreDetected(2, false);
        info.setDeletesAreDetected(3, false);
        info.setUpdatesAreDetected(4, false);
        info.setOthersInsertsAreVisible(1, false);
        info.setOthersDeletesAreVisible(3, false);
        info.setOthersUpdatesAreVisible(5, false);
        info.setOwnInsertsAreVisible(2, false);
        info.setOwnDeletesAreVisible(1,false);
        info.setOwnUpdatesAreVisible(3, false);
        info.setSupportsResultSetType(1, false);
        info.setSupportsResultTypeConcurrency(1, 3, false);
        info.setSupportsResultTypeConcurrency(1, 2, true);
        info.setSupportsResultTypeConcurrency(1, 5, false);
        info.setSupportsResultTypeConcurrency(2, 3, false);
        info.setSupportsResultTypeConcurrency(2, 2, true);
        assertFalse(info.insertsAreDetected(2));
        assertTrue(info.insertsAreDetected(1));
        assertFalse(info.deletesAreDetected(3));
        assertTrue(info.deletesAreDetected(2));
        assertFalse(info.updatesAreDetected(4));
        assertTrue(info.updatesAreDetected(3));
        assertFalse(info.othersInsertsAreVisible(1));
        assertTrue(info.othersInsertsAreVisible(2));
        assertFalse(info.othersDeletesAreVisible(3));
        assertTrue(info.othersDeletesAreVisible(4));
        assertFalse(info.othersUpdatesAreVisible(5));
        assertTrue(info.othersUpdatesAreVisible(4));
        assertFalse(info.ownInsertsAreVisible(2));
        assertTrue(info.ownInsertsAreVisible(1));
        assertFalse(info.ownDeletesAreVisible(1));
        assertTrue(info.ownDeletesAreVisible(2));
        assertFalse(info.ownUpdatesAreVisible(3));
        assertTrue(info.ownUpdatesAreVisible(2));
        assertFalse(info.supportsResultSetType(1));
        assertTrue(info.supportsResultSetType(2));
        assertFalse(info.supportsResultTypeConcurrency(1, 3));
        assertFalse(info.supportsResultTypeConcurrency(1, 5));
        assertFalse(info.supportsResultTypeConcurrency(2, 3));
        assertTrue(info.supportsResultTypeConcurrency(1, 2));
        assertTrue(info.supportsResultTypeConcurrency(2, 2));
        assertTrue(info.supportsResultTypeConcurrency(4, 2));
        assertTrue(info.supportsResultTypeConcurrency(5, 5));
        assertTrue(info.supportsResultTypeConcurrency(0, 0));
    }
}
