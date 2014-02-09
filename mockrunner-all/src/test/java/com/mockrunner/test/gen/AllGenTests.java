package com.mockrunner.test.gen;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	JavaLineProcessorTest.class, JavaLineAssemblerTest.class, JavaClassGeneratorTest.class,
	PackageImportSorterTest.class, BCELClassAnalyzerTest.class, JarFileExtractorTest.class,
	MockrunnerJarsTest.class
})
public class AllGenTests
{
}
