package com.mockrunner.gen;

import java.util.HashMap;
import java.util.Map;

import com.mockrunner.gen.proc.JavaLineProcessor;

public class J2EEVersionGenerator extends AbstractVersionGenerator
{
    public static void main(String[] args) throws Exception
    {
        J2EEVersionGenerator synchVersionUtil = new J2EEVersionGenerator();
        synchVersionUtil.doSynchronize();
    }
    
    protected String getGeneratorName()
    {
        return "Web J2EE1.3";
    }
    
    protected String getRootTargetDir()
    {
        return "srcj2ee1.3";
    }
    
    protected String getRootSourceDir()
    {
        return "src";
    }
    
    protected String[] getProcessedPackages()
    {
        return new String[] {"com/mockrunner/tag", "com/mockrunner/mock/web"};
    }
    
    protected Map prepareProcessorMap()
    {
        Map webFiles = new HashMap();
        
        JavaLineProcessor mockPageContextProc = new JavaLineProcessor();
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
        webFiles.put("com.mockrunner.mock.web.MockPageContext", mockPageContextProc);
        
        JavaLineProcessor mockRequestProc = new JavaLineProcessor();
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
        webFiles.put("com.mockrunner.mock.web.MockHttpServletRequest", mockRequestProc);
        
        JavaLineProcessor nestedTagProc = new JavaLineProcessor();
        nestedTagProc.addLine("import javax.servlet.jsp.tagext.JspTag;");
        nestedTagProc.addLine("public JspTag getWrappedTag();");
        nestedTagProc.addLine("public NestedTag addTagChild(JspTag tag);");
        nestedTagProc.addLine("public NestedTag addTagChild(JspTag tag, Map attributeMap);");
        webFiles.put("com.mockrunner.tag.NestedTag", nestedTagProc);
        
        JavaLineProcessor nestedStandardTagProc = new JavaLineProcessor();
        nestedStandardTagProc.addLine("import javax.servlet.jsp.tagext.JspTag;");
        nestedStandardTagProc.addLine("import javax.servlet.jsp.tagext.SimpleTag;");
        nestedStandardTagProc.addBlock("public JspTag getWrappedTag()");
        nestedStandardTagProc.addBlock("public NestedTag addTagChild(JspTag tag)");
        nestedStandardTagProc.addBlock("public NestedTag addTagChild(JspTag tag, Map attributeMap)");
        nestedStandardTagProc.addBlock("else if(child instanceof SimpleTag)");
        nestedStandardTagProc.addBlock("else if(childTag instanceof SimpleTag)");
        webFiles.put("com.mockrunner.tag.NestedStandardTag", nestedStandardTagProc);
        
        JavaLineProcessor nestedBodyTagProc = new JavaLineProcessor();
        nestedBodyTagProc.addLine("import javax.servlet.jsp.tagext.JspTag;");
        nestedBodyTagProc.addLine("import javax.servlet.jsp.tagext.SimpleTag;");
        nestedBodyTagProc.addBlock("public JspTag getWrappedTag()");
        nestedBodyTagProc.addBlock("public NestedTag addTagChild(JspTag tag)");
        nestedBodyTagProc.addBlock("public NestedTag addTagChild(JspTag tag, Map attributeMap)");
        nestedBodyTagProc.addBlock("else if(child instanceof SimpleTag)");
        nestedBodyTagProc.addBlock("else if(childTag instanceof SimpleTag)");
        webFiles.put("com.mockrunner.tag.NestedBodyTag", nestedBodyTagProc);
        
        JavaLineProcessor tagTestModuleProc = new JavaLineProcessor();
        tagTestModuleProc.addLine("import javax.servlet.jsp.tagext.JspTag;");
        tagTestModuleProc.addBlock("public JspTag createWrappedTag(Class tagClass)");
        tagTestModuleProc.addBlock("public JspTag createWrappedTag(Class tagClass, Map attributes)");
        tagTestModuleProc.addBlock("public NestedTag setTag(JspTag tag)");
        tagTestModuleProc.addBlock("public NestedTag setTag(JspTag tag, Map attributes)");
        tagTestModuleProc.addBlock("public JspTag getWrappedTag()");
        tagTestModuleProc.addBlock("public void doTag()");
        tagTestModuleProc.addBlock("if(isSimpleTag())");
        tagTestModuleProc.addBlock("private boolean isSimpleTag()");
        webFiles.put("com.mockrunner.tag.TagTestModule", tagTestModuleProc);
        
        JavaLineProcessor tagTestAdapterProc = new JavaLineProcessor();
        tagTestAdapterProc.addLine("import javax.servlet.jsp.tagext.JspTag;");
        tagTestAdapterProc.addBlock("protected JspTag createWrappedTag(Class tagClass)");
        tagTestAdapterProc.addBlock("protected JspTag createWrappedTag(Class tagClass, Map attributes)");
        tagTestAdapterProc.addBlock("protected JspTag getWrappedTag()");
        tagTestAdapterProc.addBlock("protected NestedTag setTag(JspTag tag)");
        tagTestAdapterProc.addBlock("protected NestedTag setTag(JspTag tag, Map attributes)");
        tagTestAdapterProc.addBlock("protected void doTag()");
        webFiles.put("com.mockrunner.tag.TagTestCaseAdapter", tagTestAdapterProc);
        webFiles.put("com.mockrunner.tag.BasicTagTestCaseAdapter", tagTestAdapterProc);
        
        JavaLineProcessor tagUtilProc = new JavaLineProcessor();
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
        webFiles.put("com.mockrunner.tag.TagUtil", tagUtilProc);
        
        webFiles.put("com.mockrunner.tag.DynamicAttribute", new Boolean(false));
        webFiles.put("com.mockrunner.tag.NestedSimpleTag", new Boolean(false));
        
        webFiles.put("com.mockrunner.mock.web.MockVariableResolver", new Boolean(false));
        webFiles.put("com.mockrunner.mock.web.MockFunctionMapper", new Boolean(false));
        webFiles.put("com.mockrunner.mock.web.MockJspFragment", new Boolean(false));
        webFiles.put("com.mockrunner.mock.web.MockExpression", new Boolean(false));
        webFiles.put("com.mockrunner.mock.web.MockExpressionEvaluator", new Boolean(false));
        
        return webFiles;
    }
}
