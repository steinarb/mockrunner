package com.mockrunner.test.web;

import java.io.StringWriter;

import javax.servlet.jsp.tagext.TagAdapter;

import junit.framework.TestCase;

import com.mockrunner.mock.web.MockJspFragment;
import com.mockrunner.mock.web.MockJspWriter;
import com.mockrunner.mock.web.MockPageContext;
import com.mockrunner.tag.NestedBodyTag;
import com.mockrunner.tag.NestedSimpleTag;
import com.mockrunner.tag.NestedStandardTag;

public class MockJspFragmentTest extends TestCase
{
    private MockJspFragment fragment;
    private MockPageContext context;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        context = new MockPageContext();
        fragment = new MockJspFragment(context);
    }
    
    public void testTextChilds()
    {
        assertEquals(0, fragment.getChilds().size());
        fragment.addTextChild("atext");
        fragment.addTextChild("anothertext");
        assertEquals(2, fragment.getChilds().size());
        assertEquals("atext", fragment.getChild(0));
        assertEquals("anothertext", fragment.getChild(1));
        try
        {
            fragment.getChild(2);
            fail();
        } 
        catch (IndexOutOfBoundsException exc)
        {
            //should throw exception
        }
        fragment.removeChilds();
        assertEquals(0, fragment.getChilds().size());
    }
    
    public void testAddChildWithNullParent()
    {
        assertNull(fragment.getParent());
        NestedStandardTag standardTag = (NestedStandardTag)fragment.addTagChild(TestTag.class);
        NestedBodyTag bodyTag = (NestedBodyTag)fragment.addTagChild(TestBodyTag.class);
        NestedSimpleTag simpleTag = (NestedSimpleTag)fragment.addTagChild(TestSimpleTag.class);
        assertNotNull(standardTag);
        assertNotNull(bodyTag);
        assertNotNull(simpleTag);
        assertNull(standardTag.getParent());
        assertNull(bodyTag.getParent());
        assertNull(simpleTag.getParent());
        assertEquals(3, fragment.getChilds().size());
        assertSame(standardTag, fragment.getChild(0));
        assertSame(bodyTag, fragment.getChild(1));
        assertSame(simpleTag, fragment.getChild(2));
    }
    
    public void testAddChildWithSimpleTagParent()
    {
        TestSimpleTag parent = new TestSimpleTag();
        fragment.setParent(parent);
        NestedStandardTag standardTag = (NestedStandardTag)fragment.addTagChild(TestTag.class);
        NestedBodyTag bodyTag = (NestedBodyTag)fragment.addTagChild(TestBodyTag.class);
        NestedSimpleTag simpleTag = (NestedSimpleTag)fragment.addTagChild(TestSimpleTag.class);
        assertNotNull(standardTag);
        assertNotNull(bodyTag);
        assertNotNull(simpleTag);
        assertTrue(standardTag.getParent() instanceof TagAdapter);
        assertSame(parent, ((TagAdapter)standardTag.getParent()).getAdaptee());
        assertTrue(bodyTag.getParent() instanceof TagAdapter);
        assertSame(parent, ((TagAdapter)bodyTag.getParent()).getAdaptee());
        assertSame(parent, simpleTag.getParent());
        assertEquals(3, fragment.getChilds().size());
        assertSame(standardTag, fragment.getChild(0));
        assertSame(bodyTag, fragment.getChild(1));
        assertSame(simpleTag, fragment.getChild(2));
    }
    
    public void testAddChildWithBodyTagParent()
    {
        TestBodyTag parent = new TestBodyTag();
        fragment.setParent(parent);
        NestedStandardTag standardTag = (NestedStandardTag)fragment.addTagChild(TestTag.class);
        NestedBodyTag bodyTag = (NestedBodyTag)fragment.addTagChild(TestBodyTag.class);
        NestedSimpleTag simpleTag = (NestedSimpleTag)fragment.addTagChild(TestSimpleTag.class);
        assertNotNull(standardTag);
        assertNotNull(bodyTag);
        assertNotNull(simpleTag);
        assertSame(parent, standardTag.getParent());
        assertSame(parent, bodyTag.getParent());
        assertSame(parent, simpleTag.getParent());
        assertEquals(3, fragment.getChilds().size());
        assertSame(standardTag, fragment.getChild(0));
        assertSame(bodyTag, fragment.getChild(1));
        assertSame(simpleTag, fragment.getChild(2));
    }
    
    public void testInvoke() throws Exception
    {
        fragment.addTextChild("text1");
        fragment.addTagChild(TestTag.class);
        fragment.addTagChild(TestBodyTag.class);
        fragment.addTagChild(TestSimpleTag.class);
        fragment.addTextChild("text2");
        StringWriter writer = new StringWriter();
        context.getOut().print("before");
        fragment.invoke(writer);
        context.getOut().print("after");
        assertEquals("text1TestTagTestBodyTagTestSimpleTagtext2", writer.toString());
        String outText = ((MockJspWriter)context.getOut()).getOutputAsString();
        assertEquals("beforeafter", outText);
        context.getOut().clearBuffer();
        context.getOut().print("before");
        fragment.invoke(null);
        context.getOut().print("after");
        outText = ((MockJspWriter)context.getOut()).getOutputAsString();
        assertEquals("beforetext1TestTagTestBodyTagTestSimpleTagtext2after", outText);
    }
}
