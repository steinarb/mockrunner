package com.mockrunner.base;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import com.mockrunner.mock.MockJspWriter;
import com.mockrunner.mock.MockPageContext;

/**
 * Module for custom tag tests. Very simple, but will
 * be extended in future releases.
 */
public class TagTestModule
{
    private MockObjectFactory mockFactory;
    private TagSupport tag;

    public TagTestModule(MockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
    }
    
    /**
     * Creates a tag with the specified type.
     * @param tagClass the <code>Class</code> of the tag
     * @return the tag
     */
    public TagSupport createTag(Class tagClass)
    {
        try
        {
            tag = (TagSupport)tagClass.newInstance();
            tag.setPageContext(getMockPageContext());
            return tag;
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
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
     * Returns the current tag.
     * @return the tag
     */
    public TagSupport getTag()
    {
        return tag;
    }
    
    /**
     * Calls the <code>doStartTag</code> method of the current tag.
     */
    public void doStartTag()
    {
        try
        {
            tag.doStartTag();
        }
        catch(JspException exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    /**
     * Calls the <code>doEndTag</code> method of the current tag.
     */
    public void doEndTag()
    {
        try
        {
            tag.doEndTag();
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
            BodyTagSupport bodyTag = (BodyTagSupport)tag;
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
     * @throws RuntimeException if the current tag is no body tag
     */
    public void doAfterBody()
    {
        if(!isBodyTag()) throw new RuntimeException("current tag is no body tag");
        try
        {
            BodyTagSupport bodyTag = (BodyTagSupport)tag;
            bodyTag.doAfterBody();
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

    public void processTagLifecycle()
    {
        if(isBodyTag())
        {
            processBodyTagLifecycle();
        }
        else
        {
            processSimpleTagLifecycle();
        }
    }

    public void processSimpleTagLifecycle()
    {
        //TODO: implement me
    }

    public void processBodyTagLifecycle()
    {
        //TODO: implement me
    }
    
    /**
     * Gets the output data the current tag has rendered. Makes only sense
     * after calling at least [@link #doStartTag}.
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
        return (tag instanceof BodyTagSupport);
    }
}
