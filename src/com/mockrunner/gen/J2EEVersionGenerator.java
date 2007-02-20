package com.mockrunner.gen;

import java.util.HashMap;
import java.util.Map;

import com.mockrunner.gen.proc.JavaLineProcessor;

public class J2EEVersionGenerator
{
    public static void main(String[] args) throws Exception
    {
        VersionGenerator generator = new VersionGenerator(prepareProcessorMap(), getGeneratorName(), getRootTargetDir(), getRootSourceDir(), getProcessedPackages());
        generator.doSynchronize();
    }
    
    private static String getGeneratorName()
    {
        return "Web J2EE1.3";
    }
    
    private static String getRootTargetDir()
    {
        return "srcj2ee1.3";
    }
    
    private static String getRootSourceDir()
    {
        return "src";
    }
    
    private static String[] getProcessedPackages()
    {
        return new String[] {"com/mockrunner/tag", "com/mockrunner/mock/web"};
    }
    
    private static Map prepareProcessorMap()
    {
        Map webFiles = new HashMap();
        
        JavaLineProcessor mockPageContextProc = getProcessorForClass("com.mockrunner.mock.web.MockPageContext", webFiles);
        mockPageContextProc.addLine("import javax.servlet.jsp.el.ExpressionEvaluator;");
        mockPageContextProc.addLine("import javax.servlet.jsp.el.VariableResolver;");
        mockPageContextProc.addLine("private ExpressionEvaluator evaluator;");
        mockPageContextProc.addLine("private VariableResolver resolver;");
        mockPageContextProc.addLine("evaluator = new MockExpressionEvaluator();");
        mockPageContextProc.addLine("resolver = new MockVariableResolver();");
        mockPageContextProc.addBlock("public void setExpressionEvaluator(ExpressionEvaluator evaluator)");
        mockPageContextProc.addBlock("public void setVariableResolver(VariableResolver resolver)");
        mockPageContextProc.addBlock("public ExpressionEvaluator getExpressionEvaluator()");
        mockPageContextProc.addBlock("public VariableResolver getVariableResolver()");
        
        JavaLineProcessor mockRequestProc = getProcessorForClass("com.mockrunner.mock.web.MockHttpServletRequest", webFiles);
        mockRequestProc.addLine("import javax.servlet.ServletRequestAttributeEvent;");
        mockRequestProc.addLine("import javax.servlet.ServletRequestAttributeListener;");
        mockRequestProc.addLine("private List attributeListener;");
        mockRequestProc.addLine("attributeListener = new ArrayList();");
        mockRequestProc.addLine("callAttributeListenersRemovedMethod(key, value);");
        mockRequestProc.addLine("handleAttributeListenerCalls(key, value, oldValue);");
        mockRequestProc.addBlock("public void addAttributeListener(ServletRequestAttributeListener listener)");
        mockRequestProc.addBlock("private void handleAttributeListenerCalls(String key, Object value, Object oldValue)");
        mockRequestProc.addBlock("private void callAttributeListenersAddedMethod(String key, Object value)");
        mockRequestProc.addBlock("private void callAttributeListenersReplacedMethod(String key, Object value)");
        mockRequestProc.addBlock("private void callAttributeListenersRemovedMethod(String key, Object value)");
        mockRequestProc.addBlock("private ServletContext getServletContext()");
        
        JavaLineProcessor nestedTagProc = getProcessorForClass("com.mockrunner.tag.NestedTag", webFiles);
        nestedTagProc.addLine("import javax.servlet.jsp.tagext.JspTag;");
        nestedTagProc.addLine("public JspTag getWrappedTag();");
        nestedTagProc.addLine("public NestedTag addTagChild(JspTag tag);");
        nestedTagProc.addLine("public NestedTag addTagChild(JspTag tag, Map attributeMap);");
        
        JavaLineProcessor nestedStandardTagProc = getProcessorForClass("com.mockrunner.tag.NestedStandardTag", webFiles);
        nestedStandardTagProc.addLine("import javax.servlet.jsp.tagext.JspTag;");
        nestedStandardTagProc.addLine("import javax.servlet.jsp.tagext.SimpleTag;");
        nestedStandardTagProc.addBlock("public JspTag getWrappedTag()");
        nestedStandardTagProc.addBlock("public NestedTag addTagChild(JspTag tag)");
        nestedStandardTagProc.addBlock("public NestedTag addTagChild(JspTag tag, Map attributeMap)");
        nestedStandardTagProc.addBlock("else if(child instanceof SimpleTag)");
        nestedStandardTagProc.addBlock("else if(childTag instanceof SimpleTag)");
        
        JavaLineProcessor nestedBodyTagProc = getProcessorForClass("com.mockrunner.tag.NestedBodyTag", webFiles);
        nestedBodyTagProc.addLine("import javax.servlet.jsp.tagext.JspTag;");
        nestedBodyTagProc.addLine("import javax.servlet.jsp.tagext.SimpleTag;");
        nestedBodyTagProc.addBlock("public JspTag getWrappedTag()");
        nestedBodyTagProc.addBlock("public NestedTag addTagChild(JspTag tag)");
        nestedBodyTagProc.addBlock("public NestedTag addTagChild(JspTag tag, Map attributeMap)");
        nestedBodyTagProc.addBlock("else if(child instanceof SimpleTag)");
        nestedBodyTagProc.addBlock("else if(childTag instanceof SimpleTag)");
        
        JavaLineProcessor tagTestModuleProc = getProcessorForClass("com.mockrunner.tag.TagTestModule", webFiles);
        tagTestModuleProc.addLine("import javax.servlet.jsp.tagext.JspTag;");
        tagTestModuleProc.addBlock("public JspTag createWrappedTag(Class tagClass)");
        tagTestModuleProc.addBlock("public JspTag createWrappedTag(Class tagClass, Map attributes)");
        tagTestModuleProc.addBlock("public NestedTag setTag(JspTag tag)");
        tagTestModuleProc.addBlock("public NestedTag setTag(JspTag tag, Map attributes)");
        tagTestModuleProc.addBlock("public JspTag getWrappedTag()");
        tagTestModuleProc.addBlock("public void doTag()");
        tagTestModuleProc.addBlock("if(isSimpleTag())");
        tagTestModuleProc.addBlock("private boolean isSimpleTag()");
        
        JavaLineProcessor tagTestAdapterProc = getProcessorForClass("com.mockrunner.tag.TagTestCaseAdapter", webFiles);
        tagTestAdapterProc.addLine("import javax.servlet.jsp.tagext.JspTag;");
        tagTestAdapterProc.addBlock("protected JspTag createWrappedTag(Class tagClass)");
        tagTestAdapterProc.addBlock("protected JspTag createWrappedTag(Class tagClass, Map attributes)");
        tagTestAdapterProc.addBlock("protected JspTag getWrappedTag()");
        tagTestAdapterProc.addBlock("protected NestedTag setTag(JspTag tag)");
        tagTestAdapterProc.addBlock("protected NestedTag setTag(JspTag tag, Map attributes)");
        tagTestAdapterProc.addBlock("protected void doTag()");
        webFiles.put("com.mockrunner.tag.BasicTagTestCaseAdapter", tagTestAdapterProc);
        
        JavaLineProcessor tagUtilProc = getProcessorForClass("com.mockrunner.tag.TagUtil", webFiles);
        tagUtilProc.addLine("import javax.servlet.jsp.JspContext;");
        tagUtilProc.addLine("import javax.servlet.jsp.tagext.DynamicAttributes;");
        tagUtilProc.addLine("import javax.servlet.jsp.tagext.SimpleTag;");
        tagUtilProc.addBlock("else if(tag instanceof SimpleTag)");
        tagUtilProc.addBlock("private static void checkJspContext(Object pageContext)");
        tagUtilProc.addBlock("Object currentValue = attributes.get(currentName);");
        tagUtilProc.addBlock("else if(tag instanceof DynamicAttributes)");
        tagUtilProc.addBlock("private static void populateDynamicAttribute(Object tag, String name, DynamicAttribute attribute) throws JspException");
        tagUtilProc.addBlock("else if(nextChild instanceof NestedSimpleTag)");
        tagUtilProc.addBlock("else if(pageContext instanceof JspContext)");
        
        webFiles.put("com.mockrunner.tag.DynamicAttribute", Boolean.FALSE);
        webFiles.put("com.mockrunner.tag.NestedSimpleTag", Boolean.FALSE);
        
        webFiles.put("com.mockrunner.mock.web.MockVariableResolver", Boolean.FALSE);
        webFiles.put("com.mockrunner.mock.web.MockFunctionMapper", Boolean.FALSE);
        webFiles.put("com.mockrunner.mock.web.MockJspFragment", Boolean.FALSE);
        webFiles.put("com.mockrunner.mock.web.MockExpression", Boolean.FALSE);
        webFiles.put("com.mockrunner.mock.web.MockExpressionEvaluator", Boolean.FALSE);
        
        return webFiles;
    }
    
    private static JavaLineProcessor getProcessorForClass(String className, Map jdbcFiles)
    {
        JavaLineProcessor processor = (JavaLineProcessor)jdbcFiles.get(className);
        if(null == processor)
        {
            processor = new JavaLineProcessor();
            jdbcFiles.put(className, processor);
        }
        return processor;
    }
}
