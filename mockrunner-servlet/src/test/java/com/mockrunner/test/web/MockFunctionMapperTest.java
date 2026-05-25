package com.mockrunner.test.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mockrunner.mock.web.MockFunctionMapper;

public class MockFunctionMapperTest
{
    private MockFunctionMapper mapper;
    private Method method1;
    private Method method2;
    private Method method3;

    @BeforeEach
    protected void setUp() throws Exception
    {
        mapper = new MockFunctionMapper();
        method1 = this.getClass().getMethod("method1", null);
        method2 = this.getClass().getMethod("method2", null);
        method3 = this.getClass().getMethod("method3", null);
    }

    @Test
    public void testAddFuntion()
    {
        mapper.addFunction(null, "method1", method1);
        mapper.addFunction("myPrefix", "method2", method2);
        mapper.addFunction(null, null, method3);
        assertEquals(method1, mapper.resolveFunction(null, "method1"));
        assertEquals(method2, mapper.resolveFunction("myPrefix", "method2"));
        assertEquals(method3, mapper.resolveFunction(null, null));
        assertNull(mapper.resolveFunction(null, "method2"));
        assertNull(mapper.resolveFunction("myPrefix", "method3"));
    }

    public void method1()
    {

    }

    public void method2()
    {

    }

    public void method3()
    {

    }

    public void testDummy(){}
}
