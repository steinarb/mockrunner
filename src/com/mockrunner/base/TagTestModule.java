package com.mockrunner.base;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.mockrunner.mock.MockBodyContent;
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
    private String body;

    public TagTestModule(MockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
    }
    
    /**
     * Creates a tag with the specified type. Sets the correct
     * <code>MockPageContext</code> and calls <code>release()</code>.
     * @param tagClass the <code>Class</code> of the tag
     * @return the tag
     */
    public TagSupport createTag(Class tagClass)
    {
        try
        {
            tag = (TagSupport)tagClass.newInstance();
            tag.setPageContext(getMockPageContext());
            release();
            return tag;
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    /**
     * Sets the body of the tag.
     * @param body the body
     */
    public void setBody(String body)
    {
        this.body = body;
    }
    
    /**
     * Gets the body of the tag.
     * @param body the body
     */
    public String getBody()
    {
        return body;
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

    public int processSimpleTagLifecycle()
    {
        int result = doStartTag();
        if(Tag.EVAL_BODY_INCLUDE == result)
        {
            dumpBodyToOutput();
        }
        return doEndTag();
    }

    public int processIterationTagLifecycle()
    {
        int result = doStartTag();
        if(Tag.EVAL_BODY_INCLUDE == result)
        {
            dumpBodyToOutput();
            while(IterationTag.EVAL_BODY_AGAIN == doAfterBody())
            {
                dumpBodyToOutput();
            }
        }
        return doEndTag();
    }

    public int processBodyTagLifecycle()
    {
        if(!isBodyTag()) throw new RuntimeException("current tag is no body tag");
        int result = doStartTag();
        if(Tag.EVAL_BODY_INCLUDE == result)
        {
            dumpBodyToOutput();
            while(IterationTag.EVAL_BODY_AGAIN == doAfterBody())
            {
                dumpBodyToOutput();
            }
        }
        else if(BodyTag.EVAL_BODY_BUFFERED == result)
        {
            BodyTagSupport bodyTag = (BodyTagSupport)tag;
            MockBodyContent bodyContent = (MockBodyContent)getMockPageContext().pushBody();
            bodyContent.setBody(getBody());
            bodyTag.setBodyContent(bodyContent);
            doInitBody();
            dumpBodyToOutput();
            while(IterationTag.EVAL_BODY_AGAIN == doAfterBody())
            {
                dumpBodyToOutput();
            }
            getMockPageContext().popBody();
        }
        return doEndTag();
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
    
    private void dumpBodyToOutput()
    { 
        try
        {
            if(null == getBody()) return;
            getMockPageContext().getOut().print(getBody());
        }
        catch(IOException exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }

    
    private boolean isBodyTag()
    {
        return (tag instanceof BodyTagSupport);
    }
}
