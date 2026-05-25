package com.mockrunner.test.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.Expression;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mockrunner.mock.web.MockExpressionEvaluator;
import com.mockrunner.mock.web.MockFunctionMapper;
import com.mockrunner.mock.web.MockVariableResolver;

@SuppressWarnings("deprecation")
class MockExpressionEvaluatorTest
{
    private MockExpressionEvaluator evaluator;

    @BeforeEach
    protected void setUp() throws Exception
    {
        evaluator = new MockExpressionEvaluator();
    }

    @Test
    void testEvaluateFailure() throws Exception
    {
        try
        {
            evaluator.evaluate("myexp", String.class, new MockVariableResolver(), new MockFunctionMapper());
            fail();
        }
        catch(ELException exc)
        {
            //should throw exception
        }
        evaluator.addObject("myexp", 3);
        try
        {
            evaluator.evaluate("myexp", String.class, new MockVariableResolver(), new MockFunctionMapper());
            fail();
        }
        catch(ELException exc)
        {
            //should throw exception
        }
    }

    @Test
    void testEvaluateOk() throws Exception
    {
        evaluator.addObject("myexp", 3);
        assertEquals(3, evaluator.evaluate("myexp", Integer.class, null, null));
        Object object = new Object();
        evaluator.addObject("myexp", object);
        assertEquals(object, evaluator.evaluate("myexp", Object.class, null, null));
    }

    @Test
    void testExpression() throws Exception
    {
        Expression expression = evaluator.parseExpression("myexp", String.class, null);
        try
        {
            expression.evaluate(null);
            fail();
        }
        catch(ELException exc)
        {
            //should throw exception
        }
        evaluator.addObject("myexp", "abc");
        assertEquals("abc", expression.evaluate(null));
        evaluator.addObject("myexp", 'a');
        try
        {
            expression.evaluate(new MockVariableResolver());
            fail();
        }
        catch(ELException exc)
        {
            //should throw exception
        }
    }

    void testDummy(){}
}
