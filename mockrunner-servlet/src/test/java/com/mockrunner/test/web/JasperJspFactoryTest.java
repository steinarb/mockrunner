package com.mockrunner.test.web;

import java.util.Arrays;

import javax.el.ELResolver;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.el.ValueExpression;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;

import org.apache.jasper.el.ELContextImpl;
import org.apache.jasper.runtime.JspApplicationContextImpl;

import com.mockrunner.mock.web.JasperJspFactory;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.WebMockObjectFactory;

import junit.framework.TestCase;

public class JasperJspFactoryTest extends TestCase
{
    private WebMockObjectFactory mockFactory;
    private JasperJspFactory jasperFactory;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        mockFactory = new WebMockObjectFactory();
        jasperFactory = new JasperJspFactory();
    }
    
    public void testConfigure()
    {
        assertSame(jasperFactory, jasperFactory.configure(mockFactory));
        assertTrue(jasperFactory.getJspApplicationContext(null) instanceof JspApplicationContextImpl);
        assertSame(jasperFactory.getJspApplicationContext(null), mockFactory.getMockServletContext().getAttribute(JspApplicationContextImpl.class.getName()));
        assertSame(jasperFactory.getPageContext(null, null, null, null, true, 1, true), mockFactory.getMockPageContext());
        assertTrue(mockFactory.getMockPageContext().getELContext() instanceof ELContextImpl);
    }
    
    public void testResolveVariable()
    {
        mockFactory.setDefaultJspFactory(jasperFactory.configure(mockFactory));
        mockFactory.getMockSession().setAttribute("test", "value");
        ELResolver resolver = mockFactory.getMockPageContext().getELContext().getELResolver();
        assertEquals("value", resolver.getValue(mockFactory.getMockPageContext().getELContext(), null, "test"));
    }
    
    public void testValueExpressionGetAndSet()
    {
        mockFactory.setDefaultJspFactory(jasperFactory.configure(mockFactory));
        TestObject testObject = new TestObject();
        testObject.setTestProperty("value");
        mockFactory.getMockSession().setAttribute("test", testObject);
        JspApplicationContext applicationContext = JspFactory.getDefaultFactory().getJspApplicationContext(mockFactory.getMockPageContext().getServletContext());
        ValueExpression valueExpression = applicationContext.getExpressionFactory().createValueExpression(mockFactory.getMockPageContext().getELContext(), "${test.testProperty}", String.class);
        assertEquals("value", valueExpression.getValue(mockFactory.getMockPageContext().getELContext()));
        valueExpression.setValue(mockFactory.getMockPageContext().getELContext(), "anotherValue");
        assertEquals("anotherValue", testObject.getTestProperty());
    }
    
    public void testValueExpressionAttributes()
    {
        mockFactory.setDefaultJspFactory(jasperFactory.configure(mockFactory));
        TestObject testObject = new TestObject();
        testObject.setTestProperty("value");
        mockFactory.getMockSession().setAttribute("test", testObject);
        JspApplicationContext applicationContext = JspFactory.getDefaultFactory().getJspApplicationContext(mockFactory.getMockPageContext().getServletContext());
        ValueExpression valueExpression = applicationContext.getExpressionFactory().createValueExpression(mockFactory.getMockPageContext().getELContext(), "${test.testProperty}", String.class);
        assertEquals(String.class, valueExpression.getExpectedType());
        assertEquals(String.class, valueExpression.getType(mockFactory.getMockPageContext().getELContext()));
        assertFalse(valueExpression.isReadOnly(mockFactory.getMockPageContext().getELContext()));
        assertEquals("${test.testProperty}", valueExpression.getExpressionString());
        valueExpression = applicationContext.getExpressionFactory().createValueExpression(mockFactory.getMockPageContext().getELContext(), "${test.testReadOnlyProperty}", Integer.class);
        assertEquals(Integer.class, valueExpression.getExpectedType());
        assertEquals(Integer.class, valueExpression.getType(mockFactory.getMockPageContext().getELContext()));
        assertTrue(valueExpression.isReadOnly(mockFactory.getMockPageContext().getELContext()));
        assertEquals("${test.testReadOnlyProperty}", valueExpression.getExpressionString());
    }
    
    public void testArithmeticValueExpression()
    {
        mockFactory.setDefaultJspFactory(jasperFactory.configure(mockFactory));
        TestObject testObject = new TestObject();
        mockFactory.getMockRequest().setAttribute("test", testObject);
        assertExpressionEquals("${(test['testReadOnlyProperty'] + test.testArrayProperty[0]) == 26}", "true");
    }
    
    public void testDeferredValueExpression()
    {
        mockFactory.setDefaultJspFactory(jasperFactory.configure(mockFactory));
        TestObject testObject = new TestObject();
        testObject.setTestProperty("value");
        mockFactory.getMockRequest().setAttribute("test", testObject);
        assertExpressionEquals("#{test.testProperty}", "value");
        mockFactory.getMockSession().setAttribute("test", "xyz");
        assertExpressionEquals("#{sessionScope.test}", "xyz");
    }
    
    public void testDeferredMethodExpression()
    {
        mockFactory.setDefaultJspFactory(jasperFactory.configure(mockFactory));
        TestObject testObject = new TestObject();
        mockFactory.getMockServletContext().setAttribute("test", testObject);
        JspApplicationContext applicationContext = JspFactory.getDefaultFactory().getJspApplicationContext(mockFactory.getMockPageContext().getServletContext());
        MethodExpression methodExpression = applicationContext.getExpressionFactory().createMethodExpression(mockFactory.getMockPageContext().getELContext(), "#{test.testMethod}", String.class, new Class[] {String.class,});
        assertEquals("Hello World", methodExpression.invoke(mockFactory.getMockPageContext().getELContext(), new String[] {"Hello World"}));
        MethodInfo methodInfo = methodExpression.getMethodInfo(mockFactory.getMockPageContext().getELContext());
        assertEquals("testMethod", methodInfo.getName());
        assertEquals(String.class, methodInfo.getReturnType());
        assertTrue(Arrays.equals(new Class[] {String.class}, methodInfo.getParamTypes()));
        assertEquals("#{test.testMethod}", methodExpression.getExpressionString());
    }
    
    public void testExpressionScopes()
    {
        mockFactory.setDefaultJspFactory(jasperFactory.configure(mockFactory));
        mockFactory.getMockRequest().setAttribute("requesttest", "requestvalue");
        mockFactory.getMockSession().setAttribute("sessiontest", "sessionvalue");
        mockFactory.getMockPageContext().setAttribute("pagetest", "pagevalue");
        mockFactory.getMockServletContext().setAttribute("applicationtest", "applicationvalue");
        assertExpressionEquals("${requestScope.requesttest}", "requestvalue");
        assertExpressionEquals("${sessionScope.sessiontest}", "sessionvalue");
        assertExpressionEquals("${pageScope.pagetest}", "pagevalue");
        assertExpressionEquals("${applicationScope.applicationtest}", "applicationvalue");
        assertExpressionEquals("${requestScope.sessiontest}", "");
        assertExpressionEquals("${pageScope.sessiontest}", "");
        assertExpressionEquals("${applicationScope.requesttest}", "");
        assertExpressionEquals("${sessionScope.requesttest}", "");
    }
    
    public void testImplicitObjects()
    {
        mockFactory.setDefaultJspFactory(jasperFactory.configure(mockFactory));
        MockHttpServletRequest request = mockFactory.getMockRequest();
        request.setRequestURI("myRequestURI");
        assertExpressionEquals("${pageContext.request.requestURI}", "myRequestURI");
        request.setupAddParameter("key1", "value1");
        request.setupAddParameter("arraykey", new String[] {"arrayvalue1", "arrayvalue2"});
        assertExpressionEquals("${paramValues.key1[0]}", "value1");
        assertExpressionEquals("${param['key1']}", "value1");
        assertExpressionEquals("${paramValues.arraykey[0]}", "arrayvalue1");
        assertExpressionEquals("${paramValues.arraykey[1]}", "arrayvalue2");
        assertExpressionEquals("${param.arraykey}", "arrayvalue1");
        request.addHeader("header", "value1");
        request.addHeader("header", "value2");
        assertExpressionEquals("${headerValues.header[0]}", "value1");
        assertExpressionEquals("${headerValues.header[1]}", "value2");
        assertExpressionEquals("${header.header}", "value1");
    }
    
    private void assertExpressionEquals(String expression, String value)
    {
        JspApplicationContext applicationContext = JspFactory.getDefaultFactory().getJspApplicationContext(mockFactory.getMockPageContext().getServletContext());
        ValueExpression valueExpression = applicationContext.getExpressionFactory().createValueExpression(mockFactory.getMockPageContext().getELContext(), expression, String.class);
        assertEquals(value, valueExpression.getValue(mockFactory.getMockPageContext().getELContext()));
    }
    
    public static class TestObject
    {
        private String testProperty;

        public String getTestProperty()
        {
            return testProperty;
        }

        public void setTestProperty(String testProperty)
        {
            this.testProperty = testProperty;
        }
        
        public String[] getTestArrayProperty()
        {
            return new String[] {"1"};
        }
        
        public Integer getTestReadOnlyProperty()
        {
            return 25;
        }
        
        public String testMethod(String test)
        {
            return test;
        }
    }
}
