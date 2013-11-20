package com.mockrunner.test.jdbc;

import java.util.HashMap;
import java.util.Map;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.jdbc.CallableStatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockConnection;

public class AbstractOutParameterResultSetHandlerTest extends BaseTestCase
{
    private MockConnection connection;
    private CallableStatementResultSetHandler callableStatementHandler;

    protected void setUp() throws Exception
    {
        super.setUp();
        connection = getJDBCMockObjectFactory().getMockConnection();
        callableStatementHandler = connection.getCallableStatementResultSetHandler();
    }

    public void testGetOutParameter()
    {
        Map outParameter = new HashMap();
        outParameter.put("name", "value");
        callableStatementHandler.prepareOutParameter("myc[a]ll", outParameter);
        assertNull(callableStatementHandler.getOutParameter("acall"));
        assertNull(callableStatementHandler.getOutParameter("mycall"));
        callableStatementHandler.setUseRegularExpressions(true);
        assertEquals(outParameter, callableStatementHandler.getOutParameter("mycall"));
    }
    
    public void testGetOutParameterWithParameter()
    {
        Map parameter = new HashMap();
        parameter.put("name", "value");
        callableStatementHandler.prepareOutParameter("myc[a]ll.*", new HashMap(), parameter);
        callableStatementHandler.setUseRegularExpressions(true);
        assertNull(callableStatementHandler.getOutParameter("mycall xyz"));
        parameter = new HashMap();
        parameter.put("name", "value");
        parameter.put("name1", "value1");
        assertEquals(new HashMap(), callableStatementHandler.getOutParameter("mycall xyz", parameter));
        callableStatementHandler.setExactMatchParameter(true);
        assertNull(callableStatementHandler.getOutParameter("mycall xyz", parameter));
    }
    
    public void testPreparedSQLOrdered()
    {
        Map outParameterMap1 = new HashMap();
        outParameterMap1.put("1", "1");
        Map outParameterMap2 = new HashMap();
        outParameterMap2.put("2", "2");
        callableStatementHandler.prepareOutParameter("select", outParameterMap1);
        callableStatementHandler.prepareOutParameter("SelecT", outParameterMap2);
        callableStatementHandler.prepareOutParameter("SelecT", outParameterMap1, new HashMap());
        callableStatementHandler.prepareOutParameter("select", outParameterMap2, new HashMap());
        assertEquals(outParameterMap2, callableStatementHandler.getOutParameter("select"));
        assertEquals(outParameterMap1, callableStatementHandler.getOutParameter("select", new HashMap()));
    }
}
