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
        mockPageContextProc.addBlock("public void setExpressionEvaluator(ExpressionEvaluator evaluator)");
        mockPageContextProc.addBlock("public void setVariableResolver(VariableResolver resolver)");
        mockPageContextProc.addBlock("public ExpressionEvaluator getExpressionEvaluator()");
        mockPageContextProc.addBlock("public VariableResolver getVariableResolver()");
        webFiles.put("com.mockrunner.mock.web.MockPageContext", mockPageContextProc);
        
        JavaLineProcessor nestedTagProc = new JavaLineProcessor();
        nestedTagProc.addLine("import javax.servlet.jsp.tagext.JspTag;");
        nestedTagProc.addLine("public JspTag getWrappedTag();");
        nestedTagProc.addLine("public NestedTag addTagChild(JspTag tag);");
        nestedTagProc.addLine("public NestedTag addTagChild(JspTag tag, Map attributeMap);");
        webFiles.put("com.mockrunner.tag.NestedTag", nestedTagProc);
        
        JavaLineProcessor tagUtilProc = new JavaLineProcessor();
        tagUtilProc.addLine("import javax.servlet.jsp.JspContext;");
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
