package com.mockrunner.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.mockrunner.base.MockObjectFactory;
import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.mock.MockJspWriter;
import com.mockrunner.mock.MockPageContext;
import com.mockrunner.util.*;

/**
 * Module for custom tag tests.
 */
public class TagTestModule
{
    private MockObjectFactory mockFactory;
    private TagSupport tag;
    private String body;

    public TagTestModule(MockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
    }
    
    /**
     * Creates a tag. Internally a {@link NestedTag}
     * is created but the wrapped tag is returned. If you
     * simply want to test the output of the tag without 
     * nesting other tags, you do not have to care about the
     * {@link NestedTag}, just use the returned instance.
     * An empty attribute <code>Map</code> will be used for
     * the tag.
     * @param tagClass the class of the tag
     * @return instance of <code>TagSupport</code> or <code>BodyTagSupport</code>
     */
    public TagSupport createTag(Class tagClass)
    {
        return createTag(tagClass, new HashMap());
    }
    
    /**
     * Creates a tag. Internally a {@link NestedTag}
     * is created but the wrapped tag is returned. If you
     * simply want to test the output of the tag without 
     * nesting other tags, you do not have to care about the
     * {@link NestedTag}, just use the returned instance.
     * The attributes <code>Map</code> contains the attributes
     * of this tag (<i>propertyname</i> maps to <i>propertyvalue</i>).
     * The attributes are populated (i.e. the tags setters are called)
     * during the lifecycle or with an explicit call of
     * {@link #populateAttributes}.
     * @param tagClass the class of the tag
     * @param attributes the attribute map
     * @return instance of <code>TagSupport</code> or <code>BodyTagSupport</code>
     */
    public TagSupport createTag(Class tagClass, Map attributes)
    {
        return createNestedTag(tagClass, attributes).getTag();
    }
    
    /**
     * Creates a {@link NestedTag} and returns it. You can
     * add child tags or body blocks to the {@link NestedTag}.
     * Use {@link #getTag} to get the wrapped tag.
     * An empty attribute <code>Map</code> will be used for
     * the tag.
     * @param tagClass the class of the tag
     * @return instance of {@link NestedStandardTag} or {@link NestedBodyTag}
     */
    public NestedTag createNestedTag(Class tagClass)
    {
        return createNestedTag(tagClass, new HashMap());
    }
    
    /**
     * Creates a {@link NestedTag} and returns it. You can
     * add child tags or body blocks to the {@link NestedTag}.
     * Use {@link #getTag} to get the wrapped tag.
     * The attributes <code>Map</code> contains the attributes
     * of this tag (<i>propertyname</i> maps to <i>propertyvalue</i>).
     * The attributes are populated (i.e. the tags setters are called)
     * during the lifecycle or with an explicit call of
     * {@link #populateAttributes}.
     * @param tagClass the class of the tag
     * @param attributes the attribute map
     * @return instance of {@link NestedStandardTag} or {@link NestedBodyTag}
     */
    public NestedTag createNestedTag(Class tagClass, Map attributes)
    {
        try
        {
            tag = TagUtil.createNestedTagInstance(tagClass, getMockPageContext(), attributes);
            return (NestedTag)tag;
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    /**
     * Populates the attributes of the underlying tag by
     * calling {@link NestedTag#populateAttributes}. The setters
     * of the tag are called. Please note that child tags are not
     * populated. This is done during the lifecycle.
     */
    public void populateAttributes()
    {
        ((NestedTag)tag).populateAttributes();
    }
    
    /**
     * Sets the body of the tag as a static string. Please
     * note that all childs of the underlying {@link NestedTag}
     * are deleted and the static content is set. If you want
     * to use nesting tags, please use the method {@link NestedTag#addTextChild}
     * to set static content.
     * @param body the static body content
     */
    public void setBody(String body)
    {
        ((NestedTag)tag).removeChilds();
        ((NestedTag)tag).addTextChild(body);
    }
    
    /**
     * Returns the current wrapped tag.
     * @return instance of <code>TagSupport</code> or <code>BodyTagSupport</code>
     */
    public TagSupport getTag()
    {
        return ((NestedTag)tag).getTag();
    }
    
    /**
     * Returns the current nested tag. You can
     * add child tags or body blocks to the {@link NestedTag}.
     * Use {@link #getTag} to get the wrapped tag.
     * @return instance of {@link NestedStandardTag} or {@link NestedBodyTag}
     */
    public NestedTag getNestedTag()
    {
        return (NestedTag)tag;
    }
    
    /**
     * Returns the <code>MockPageContext</code> object.
     * Delegates to {@link MockObjectFactory#getMockPageContext}.
     * @return the MockPageContext
     */
    public MockPageContext getMockPageContext()
    {
        return mockFactory.getMockPageContext();
    }
    
    /**
     * Calls the <code>doStartTag</code> method of the current tag.
     * @return the result of <code>doStartTag</code>
     */
    public int doStartTag()
    {
        try
        {
            return tag.doStartTag();
        }
        catch(JspException exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    /**
     * Calls the <code>doEndTag</code> method of the current tag.
     * @return the result of <code>doEndTag</code>
     */
    public int doEndTag()
    {
        try
        {
            return tag.doEndTag();
        }
        catch(JspException exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    /**
     * Calls the <code>doInitBody</code> method of the current tag.
     * @throws RuntimeException if the current tag is no body tag
     */
    public void doInitBody()
    {
        if(!isBodyTag()) throw new RuntimeException("current tag is no body tag");
        try
        {
            NestedBodyTag bodyTag = (NestedBodyTag)tag;
            bodyTag.doInitBody();
        }
        catch(JspException exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }

    /**
     * Calls the <code>doAfterBody</code> method of the current tag.
     * @return the result of <code>doAfterBody</code>
     */
    public int doAfterBody()
    {
        try
        {
            return tag.doAfterBody();
        }
        catch(JspException exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }

    /**
     * Calls the <code>release</code> method of the current tag.
     */
    public void release()
    {
        tag.release();
    }
    
    /**
     * Performs the tags lifecycle by calling {@link NestedTag#doLifecycle}.
     * All <code>doBody</code> and <code>doTag</code> methods are called as 
     * in the real web container. The evaluation of the body is simulated 
     * by performing the lifecycle recursively for all childs of the 
     * {@link NestedTag}.
     * @return the result of the final <code>doEndTag</code> call
     */
    public int processTagLifecycle()
    {
        try
        {
            return ((NestedTag)tag).doLifecycle();
        }
        catch(JspException exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    /**
     * Gets the output data the current tag has rendered. Makes only sense
     * after calling at least [@link #doStartTag} or [@link #processTagLifecycle}
     * @return the output data
     */
    public String getOutput()
    {
        MockJspWriter writer = (MockJspWriter)mockFactory.getMockPageContext().getOut();
        return writer.getOutputAsString();
    }
    
    /**
     * Verifies the output data.
     * @param output the expected output.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyOutput(String output)
    {
        String actualOutput = getOutput();
        if(!output.equals(actualOutput))
        {
            throw new VerifyFailedException("actual output: " + actualOutput + " does not match expected output");
        }
    }
    
    /**
     * Verifies if the output data contains the specified data.
     * @param output the data
     * @throws VerifyFailedException if verification fails
     */
    public void verifyOutputContains(String output)
    {
        String actualOutput = getOutput();
        if(-1 == actualOutput.indexOf(output))
        {
            throw new VerifyFailedException("actual output: " + actualOutput + " does not match expected output");
        }
    }
    
    private boolean isBodyTag()
    {
        return (tag instanceof NestedBodyTag);
    }
}
