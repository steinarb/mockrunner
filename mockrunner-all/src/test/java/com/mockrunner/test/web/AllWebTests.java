package com.mockrunner.test.web;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ActionTestModuleTest.class, 
	MockHttpSessionTest.class,
	MockHttpServletRequestTest.class,
	MockHttpServletResponseTest.class,
	MockServletContextTest.class,
	NestedTagTest.class,
	TagLifecycleTest.class,
	ServletTestModuleTest.class,
	TagTestModuleTest.class,
	MockPageContextTest.class,
	WebMockObjectFactoryTest.class,
	MapMessageResourcesTest.class,
	MockModuleConfigTest.class,
	MockFunctionMapperTest.class,
	MockExpressionEvaluatorTest.class,
	MockActionForwardTest.class,
	HTMLOutputModuleTest.class,
	MockActionMappingTest.class,
	TagUtilTest.class,
	MockJspFragmentTest.class,
	MockJspWriterTest.class,
	MockFilterChainTest.class,
	WebTestModuleTest.class,
	ActionMockObjectFactoryTest.class,
	BasicActionTestCaseAdapterTest.class,
	BasicServletTestCaseAdapterTest.class,
	BasicTagTestCaseAdapterTest.class,
	DynamicMockProxyGeneratorTest.class,
	ActionMappingProxyGeneratorTest.class,
	MockServletConfigTest.class,
	MockFilterConfigTest.class,
	DefaultExceptionHandlerConfigTest.class,
	MockVariableResolverTest.class,
	JasperJspFactoryTest.class
	})
public class AllWebTests
{
}
