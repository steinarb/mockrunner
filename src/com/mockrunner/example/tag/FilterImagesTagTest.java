package com.mockrunner.example.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
    
    public void testDoNotFilter()
    {
        prepareFilterImagesTag("false");
        processTagLifecycle();
        verifyOutputContains("src=\"/anotherimage.jpg\"");
        verifyOutputContains("src=\"/testimage.jpg\"");
        assertTrue(-1 == getOutput().indexOf("dummy.jpg"));
    }
    
    public void testDoFilter()
    {
        prepareFilterImagesTag("true");
        processTagLifecycle();
        verifyOutputContains("src=\"dummy.jpg\"");
        assertTrue(-1 == getOutput().indexOf("anotherimage.jpg"));
        assertTrue(-1 == getOutput().indexOf("testimage.jpg"));
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
