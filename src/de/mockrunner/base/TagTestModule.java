package de.mockrunner.base;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import de.mockrunner.mock.MockJspWriter;



public class TagTestModule
{
    private MockObjectFactory mockFactory;
    private TagSupport tag;

    public TagTestModule(MockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
    }
    
    public TagSupport createTag(Class tagClass)
    {
        try
        {
            tag = (TagSupport)tagClass.newInstance();
            tag.setPageContext(mockFactory.getMockPageContext());
            return tag;
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    public TagSupport getTag()
    {
        return tag;
    }
    
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
    
    public String getOutput()
    {
        try
        {
            MockJspWriter writer = (MockJspWriter)mockFactory.getMockPageContext().getOut();
            return writer.getOutputAsString();
        }
        catch(IOException exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    public void verifyOutput(String output)
    {
        String actualOutput = getOutput();
        if(!output.equals(actualOutput))
        {
            throw new VerifyFailedException("actual output: " + actualOutput + " does not match expected output");
        }
    }
    
    public void verifyOutputContains(String output)
    {
        String actualOutput = getOutput();
        if(-1 == actualOutput.indexOf(output))
        {
            throw new VerifyFailedException("actual output: " + actualOutput + " does not match expected output");
        }
    }
}
