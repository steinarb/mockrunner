package com.mockrunner.test.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import com.kirkk.analyzer.framework.JarBundle;
import com.kirkk.analyzer.framework.bcelbundle.BCELJarBundle;
import com.mockrunner.gen.jar.JarFileExtractor;

public class JarFileExtractorTest extends TestCase
{
    private JarFileExtractor extractor;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        List jars = new ArrayList();
        jars.add("test1.jar");
        jars.add("test2.jar");
        jars.add("test3.jar");
        extractor = new JarFileExtractor(jars);
    }
    
    private BCELJarBundle createEmptyJarBundle(String name)
    {
        return new BCELJarBundle(name);
    }
    
    private BCELJarBundle createJarBundleWithDependencies(String name, String[] dependencies)
    {
        BCELJarBundle bundle = createEmptyJarBundle(name);
        for(int ii = 0; ii < dependencies.length; ii++)
        {
            bundle.addDependentJar(createEmptyJarBundle(dependencies[ii]));
        }
        return bundle;
    }
    
    public void testEmpty()
    {
        Map dependencies = extractor.createDependencies(new JarBundle[0]);
        assertEquals(0, dependencies.size());
    }
    
    public void testNoDependencies()
    {
        JarBundle[] bundles = new JarBundle[3];
        bundles[0] = createEmptyJarBundle("test1.jar");
        bundles[1] = createEmptyJarBundle("test2.jar");
        bundles[2] = createEmptyJarBundle("test3.jar");
        Map dependencies = extractor.createDependencies(bundles);
        assertEquals(3, dependencies.size());
        assertTrue(((Set)dependencies.get("test1.jar")).isEmpty());
        assertTrue(((Set)dependencies.get("test2.jar")).isEmpty());
        assertTrue(((Set)dependencies.get("test3.jar")).isEmpty());
    }
    
    public void testDependencies()
    {
        JarBundle[] bundles = new JarBundle[1];
        bundles[0] = createJarBundleWithDependencies("test1.jar", new String[] {"1", "2", "3"});
        Map dependencies = extractor.createDependencies(bundles);
        assertEquals(1, dependencies.size());
        Set jars = (Set)dependencies.get("test1.jar");
        assertEquals(3, jars.size());
        assertTrue(jars.contains("1"));
        assertTrue(jars.contains("2"));
        assertTrue(jars.contains("3"));
    }
    
    public void testRecursiveDependencies()
    {
        BCELJarBundle bundle1 = createEmptyJarBundle("test1.jar");
        BCELJarBundle nested1 = createJarBundleWithDependencies("nested1.jar", new String[] {"1", "2", "3"});
        BCELJarBundle nested2 = createJarBundleWithDependencies("nested2.jar", new String[] {"4", "5", "6"});
        bundle1.addDependentJar(nested1);
        bundle1.addDependentJar(nested2);
        BCELJarBundle bundle2 = createJarBundleWithDependencies("test2.jar", new String[] {"7", "8", "9"});
        JarBundle[] bundles = new JarBundle[] {bundle1, bundle2};
        Map dependencies = extractor.createDependencies(bundles);
        assertEquals(2, dependencies.size());
        Set jars1 = (Set)dependencies.get("test1.jar");
        assertEquals(8, jars1.size());
        assertTrue(jars1.contains("1"));
        assertTrue(jars1.contains("2"));
        assertTrue(jars1.contains("3"));
        assertTrue(jars1.contains("4"));
        assertTrue(jars1.contains("5"));
        assertTrue(jars1.contains("6"));
        assertTrue(jars1.contains("nested1.jar"));
        assertTrue(jars1.contains("nested2.jar"));
        Set jars2 = (Set)dependencies.get("test2.jar");
        assertEquals(3, jars2.size());
        assertTrue(jars2.contains("7"));
        assertTrue(jars2.contains("8"));
        assertTrue(jars2.contains("9"));
    }
}
