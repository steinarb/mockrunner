package com.mockrunner.example.tag;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import com.mockrunner.mock.web.WebMockObjectFactory;
import com.mockrunner.tag.AbstractDynamicChild;
import com.mockrunner.tag.BasicTagTestCaseAdapter;
import com.mockrunner.tag.NestedSimpleTag;

/**
 * Example test for the {@link FilterImagesTag}.
 * The example demonstrates how to test simple tags
 * and the usage of dynamic childs (i.e. simulated expressions or scriptlets).
 * The simulated body of the tag consists of a static <i>img</i>-tag
 * and a dynamic version that takes the url from request. The JSP
 * for the dynamic version may look like
 * <pre>
 * &lt;img src="${requestScope.url}"&gt;
 * </pre>
 * The EL expression is simulated by a subclass of
 * {@link com.mockrunner.tag.AbstractDynamicChild}.
 */
public class FilterImagesTagTest extends BasicTagTestCaseAdapter
{ 
    private void prepareFilterImagesTag(String filterAttribute)
    {
        Map attributeMap = new HashMap();
        attributeMap.put("filter", filterAttribute);
        NestedSimpleTag tag = (NestedSimpleTag)createNestedTag(FilterImagesTag.class, attributeMap);
        getWebMockObjectFactory().getMockRequest().setAttribute("url", "/testimage.jpg");
        tag.addTextChild("<img src=\"/anotherimage.jpg\">");
        tag.addDynamicChild(new SessionImageChild(getWebMockObjectFactory()));
    }
    
    @Test
    public void testDoNotFilter()
    {
        prepareFilterImagesTag("false");
        processTagLifecycle();
        verifyOutputContains("src=\"/anotherimage.jpg\"");
        verifyOutputContains("src=\"/testimage.jpg\"");
        assertTrue(!getOutput().contains("dummy.jpg"));
    }
    
    @Test
    public void testDoFilter()
    {
        prepareFilterImagesTag("true");
        processTagLifecycle();
        verifyOutputContains("src=\"dummy.jpg\"");
        assertTrue(!getOutput().contains("anotherimage.jpg"));
        assertTrue(!getOutput().contains("testimage.jpg"));
    }

    private class SessionImageChild extends AbstractDynamicChild
    { 
        public SessionImageChild(WebMockObjectFactory factory)
        {
            super(factory);
        }
        
        public Object evaluate()
        {
            HttpServletRequest request = getWebMockObjectFactory().getMockRequest();
            return "<img src=\"" +  request.getAttribute("url") + "\">";
        }
    }
}
