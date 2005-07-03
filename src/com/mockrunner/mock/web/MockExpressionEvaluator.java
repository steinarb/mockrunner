package com.mockrunner.mock.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.Expression;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.FunctionMapper;
import javax.servlet.jsp.el.VariableResolver;

/**
 * Mock implementation of <code>ExpressionEvaluator</code>.
 */
public class MockExpressionEvaluator extends ExpressionEvaluator
{
    private Map expressions = new HashMap();
    
    /**
     * Adds an object as a result for the specified expression.
     * @param expression the expression
     * @param object the object
     */
    public void addObject(String expression, Object object)
    {
        expressions.put(expression, object);
    }
    
    /**
     * Clears all expressions and corresponding objects.
     */
    public void clearObjects()
    {
        expressions.clear();
    }

    public Object evaluate(String expression, Class expectedType, VariableResolver resolver, FunctionMapper mapper) throws ELException
    {
        Object object = expressions.get(expression);
        if(null == object)
        {
            throw new ELException("No object for expression " + expression + " defined.");
        }
        if(!object.getClass().equals(expectedType))
        {
            throw new ELException("Expected type " + expectedType + " for expression " + expression + " but actual type is " + object.getClass());
        }
        return object;
    }

    public Expression parseExpression(String expression, Class expectedType, FunctionMapper mapper) throws ELException
    {
        return new MockExpression(this, expression, expectedType, mapper);
    }
}
