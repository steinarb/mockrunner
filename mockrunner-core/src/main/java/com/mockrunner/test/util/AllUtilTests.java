package com.mockrunner.test.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	StreamUtilTest.class, ArrayUtilTest.class, CollectionUtilTest.class, StringUtilTest.class,
	FileUtilTest.class, ClassUtilTest.class, CaseAwareMapTest.class, MethodUtilTest.class,
	FieldUtilTest.class, XmlUtilTest.class
})
public class AllUtilTests
{
}
