package com.mockrunner.test.ejb;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	EJBTestModuleTest.class, EJBMockObjectFactoryTest.class, MockUserTransactionTest.class,
	EJBTestCaseAdapterTest.class, BasicEJBTestCaseAdapterTest.class,
	EJBTestCaseAdapterDelegateEnvJNDITest.class, EJBTestCaseAdapterExternalJNDITest.class,
	JNDIUtilTest.class
})
public class AllEJBTests
{
}
