package com.mockrunner.test.web;

import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.Expression;

import junit.framework.TestCase;

import com.mockrunner.mock.web.MockExpressionEvaluator;
import com.mockrunner.mock.web.MockFunctionMapper;
import com.mockrunner.mock.web.MockVariableResolver;

public class MockExpressionEvaluatorTest extends TestCase
{
    private MockExpressionEvaluator evaluator;

    protected void setUp() throws Exception
    {
        super.setUp();
        evaluator = new MockExpressionEvaluator();
    }

    public void testEvaluateFailure() throws Exception
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
        evaluator.addObject("myexp", new Integer(3));
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
    
    public void testEvaluateOk() throws Exception
    {
        evaluator.addObject("myexp", new Integer(3));
        assertEquals(new Integer(3), evaluator.evaluate("myexp", Integer.class, null, null));
        Object object = new Object();
        evaluator.addObject("myexp", object);
        assertEquals(object, evaluator.evaluate("myexp", Object.class, null, null));
    }
    
    public void testExpression() throws Exception
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
        evaluator.addObject("myexp", new Character('a'));
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
    
    public void testDummy(){}
}
