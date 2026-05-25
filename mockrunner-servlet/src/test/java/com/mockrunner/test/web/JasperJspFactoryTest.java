package com.mockrunner.test.web;

import static org.assertj.core.api.Assertions.assertThat;

import javax.el.ELResolver;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.el.ValueExpression;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;

import org.apache.jasper.el.ELContextImpl;
import org.apache.jasper.runtime.JspApplicationContextImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mockrunner.mock.web.JasperJspFactory;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.WebMockObjectFactory;

class JasperJspFactoryTest
{
    private WebMockObjectFactory mockFactory;
    private JasperJspFactory jasperFactory;

    @BeforeEach
    protected void setUp() throws Exception
    {
        mockFactory = new WebMockObjectFactory();
        jasperFactory = new JasperJspFactory();
    }

    @Test
    void testConfigure()
    {
        assertThat(jasperFactory).isSameAs(jasperFactory.configure(mockFactory));
        assertThat(jasperFactory.getJspApplicationContext(null)).isInstanceOf(JspApplicationContextImpl.class);
        assertThat(jasperFactory.getJspApplicationContext(null)).isSameAs(mockFactory.getMockServletContext().getAttribute(JspApplicationContextImpl.class.getName()));
        assertThat(jasperFactory.getPageContext(null, null, null, null, true, 1, true)).isSameAs(mockFactory.getMockPageContext());
        assertThat(mockFactory.getMockPageContext().getELContext()).isInstanceOf(ELContextImpl.class);
    }

    @Test
    void testResolveVariable()
    {
        mockFactory.setDefaultJspFactory(jasperFactory.configure(mockFactory));
        mockFactory.getMockSession().setAttribute("test", "value");
        ELResolver resolver = mockFactory.getMockPageContext().getELContext().getELResolver();
        assertThat(resolver.getValue(mockFactory.getMockPageContext().getELContext(), null, "test")).isEqualTo("value");
    }

    @Test
    void testValueExpressionGetAndSet()
    {
        mockFactory.setDefaultJspFactory(jasperFactory.configure(mockFactory));
        TestObject testObject = new TestObject();
        testObject.setTestProperty("value");
        mockFactory.getMockSession().setAttribute("test", testObject);
        JspApplicationContext applicationContext = JspFactory.getDefaultFactory().getJspApplicationContext(mockFactory.getMockPageContext().getServletContext());
        ValueExpression valueExpression = applicationContext.getExpressionFactory().createValueExpression(mockFactory.getMockPageContext().getELContext(), "${test.testProperty}", String.class);
        assertThat(valueExpression.getValue(mockFactory.getMockPageContext().getELContext())).isEqualTo("value");
        valueExpression.setValue(mockFactory.getMockPageContext().getELContext(), "anotherValue");
        assertThat(testObject.getTestProperty()).isEqualTo("anotherValue");
    }

    @Test
    void testValueExpressionAttributes()
    {
        mockFactory.setDefaultJspFactory(jasperFactory.configure(mockFactory));
        TestObject testObject = new TestObject();
        testObject.setTestProperty("value");
        mockFactory.getMockSession().setAttribute("test", testObject);
        JspApplicationContext applicationContext = JspFactory.getDefaultFactory().getJspApplicationContext(mockFactory.getMockPageContext().getServletContext());
        ValueExpression valueExpression = applicationContext.getExpressionFactory().createValueExpression(mockFactory.getMockPageContext().getELContext(), "${test.testProperty}", String.class);
        assertThat(valueExpression.getExpectedType()).isEqualTo(String.class);
        assertThat(valueExpression.getType(mockFactory.getMockPageContext().getELContext())).isEqualTo(String.class);
        assertThat(valueExpression.isReadOnly(mockFactory.getMockPageContext().getELContext())).isFalse();
        assertThat(valueExpression.getExpressionString()).isEqualTo("${test.testProperty}");
        valueExpression = applicationContext.getExpressionFactory().createValueExpression(mockFactory.getMockPageContext().getELContext(), "${test.testReadOnlyProperty}", Integer.class);
        assertThat(valueExpression.getExpectedType()).isEqualTo(Integer.class);
        assertThat(valueExpression.getType(mockFactory.getMockPageContext().getELContext())).isEqualTo(Integer.class);
        assertThat(valueExpression.isReadOnly(mockFactory.getMockPageContext().getELContext())).isTrue();
        assertThat(valueExpression.getExpressionString()).isEqualTo("${test.testReadOnlyProperty}");
    }

    @Test
    void testArithmeticValueExpression()
    {
        mockFactory.setDefaultJspFactory(jasperFactory.configure(mockFactory));
        TestObject testObject = new TestObject();
        mockFactory.getMockRequest().setAttribute("test", testObject);
        assertExpressionEquals("${(test['testReadOnlyProperty'] + test.testArrayProperty[0]) == 26}", "true");
    }

    @Test
    void testDeferredValueExpression()
    {
        mockFactory.setDefaultJspFactory(jasperFactory.configure(mockFactory));
        TestObject testObject = new TestObject();
        testObject.setTestProperty("value");
        mockFactory.getMockRequest().setAttribute("test", testObject);
        assertExpressionEquals("#{test.testProperty}", "value");
        mockFactory.getMockSession().setAttribute("test", "xyz");
        assertExpressionEquals("#{sessionScope.test}", "xyz");
    }

    @Test
    void testDeferredMethodExpression()
    {
        mockFactory.setDefaultJspFactory(jasperFactory.configure(mockFactory));
        TestObject testObject = new TestObject();
        mockFactory.getMockServletContext().setAttribute("test", testObject);
        JspApplicationContext applicationContext = JspFactory.getDefaultFactory().getJspApplicationContext(mockFactory.getMockPageContext().getServletContext());
        MethodExpression methodExpression = applicationContext.getExpressionFactory().createMethodExpression(mockFactory.getMockPageContext().getELContext(), "#{test.testMethod}", String.class, new Class[] {String.class,});
        assertThat(methodExpression.invoke(mockFactory.getMockPageContext().getELContext(), new String[] {"Hello World"})).isEqualTo("Hello World");
        MethodInfo methodInfo = methodExpression.getMethodInfo(mockFactory.getMockPageContext().getELContext());
        assertThat(methodInfo.getName()).isEqualTo("testMethod");
        assertThat(methodInfo.getReturnType()).isEqualTo(String.class);
        assertThat(methodInfo.getParamTypes()).isEqualTo(new Class[] {String.class});
        assertThat(methodExpression.getExpressionString()).isEqualTo("#{test.testMethod}");
    }

    @Test
    void testExpressionScopes()
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

    @Test
    void testImplicitObjects()
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
        assertThat(valueExpression.getValue(mockFactory.getMockPageContext().getELContext())).isEqualTo(value);
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
