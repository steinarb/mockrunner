package com.mockrunner.example.tag;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts.taglib.bean.WriteTag;

import com.mockrunner.tag.NestedTag;
import com.mockrunner.tag.TagTestCaseAdapter;

/**
 * Example test for the {@link TableEnumTag}.
 * Demonstrates the usage of {@link com.mockrunner.tag.TagTestModule} 
 * resp. {@link com.mockrunner.tag.TagTestCaseAdapter} and
 * {@link com.mockrunner.tag.NestedTag}.
 * Tests the html output of {@link TableEnumTag} with static
 * and dynamic body content (simulated by nesting the <code>WriteTag</code>):
 */
public class TableEnumTagTest extends TagTestCaseAdapter
{
    private NestedTag nestedTag;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        Map attributeMap = new HashMap();
        attributeMap.put("label", "myLabel");
        nestedTag = createNestedTag(TableEnumTag.class, attributeMap);
        storeTestListInSession();
    }

    private void storeTestListInSession()
    {
        ArrayList list = new ArrayList();
        list.add("Entry1");
        list.add("Entry2");
        list.add("Entry3");
        getMockObjectFactory().getMockSession().setAttribute("currentCollection", list);
    }
    
    public void testNoBody() throws Exception
    {
        processTagLifecycle();
        BufferedReader reader = getOutputAsBufferedReader();
        assertEquals("<table>", reader.readLine().trim());
        assertEquals("</table>", reader.readLine().trim());
    }
    
    public void testStaticBody() throws Exception
    {
        nestedTag.addTextChild("myStaticValue");
        processTagLifecycle();
        BufferedReader reader = getOutputAsBufferedReader();
        assertEquals("<table>", reader.readLine().trim());
        for(int ii = 0; ii < 3; ii++)
        {
            assertEquals("<tr>", reader.readLine().trim());
            assertEquals("<td>", reader.readLine().trim());
            assertEquals("myLabel", reader.readLine().trim());
            assertEquals("</td>", reader.readLine().trim());  
            assertEquals("<td>", reader.readLine().trim());
            assertEquals("myStaticValue", reader.readLine().trim());
            assertEquals("</td>", reader.readLine().trim());
            assertEquals("</tr>", reader.readLine().trim());
        }
        assertEquals("</table>", reader.readLine().trim());
    }
    
    public void testDynamicBody() throws Exception
    {
        Map attributeMap = new HashMap();
        attributeMap.put("scope", "request");
        attributeMap.put("name", "currentObject");
        nestedTag.addTagChild(WriteTag.class, attributeMap);
        processTagLifecycle();
        BufferedReader reader = getOutputAsBufferedReader();
        assertEquals("<table>", reader.readLine().trim());
        for(int ii = 1; ii <= 3; ii++)
        {
            assertEquals("<tr>", reader.readLine().trim());
            assertEquals("<td>", reader.readLine().trim());
            assertEquals("myLabel", reader.readLine().trim());
            assertEquals("</td>", reader.readLine().trim());  
            assertEquals("<td>", reader.readLine().trim());
            assertEquals("Entry" + ii, reader.readLine().trim());
            assertEquals("</td>", reader.readLine().trim());
            assertEquals("</tr>", reader.readLine().trim());
        }
        assertEquals("</table>", reader.readLine().trim());
    }
}
