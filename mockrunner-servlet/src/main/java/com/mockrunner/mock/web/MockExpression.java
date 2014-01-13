package com.mockrunner.mock.web;

import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.Expression;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.FunctionMapper;
import javax.servlet.jsp.el.VariableResolver;

/**
 * Mock implementation of <code>Expression</code>.
 */
public class MockExpression extends Expression
{
    private ExpressionEvaluator evaluator;
    private String expression;
    private Class expectedType;
    private FunctionMapper mapper;
    
    public MockExpression(ExpressionEvaluator evaluator, String expression, Class expectedType, FunctionMapper mapper)
    {
        this.evaluator = evaluator;
        this.expression = expression;
        this.expectedType = expectedType;
        this.mapper = mapper;
    }

    public Object evaluate(VariableResolver resolver) throws ELException
    {
        return evaluator.evaluate(expression, expectedType, resolver, mapper);
    }
}
