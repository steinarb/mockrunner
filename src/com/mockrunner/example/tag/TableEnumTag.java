package com.mockrunner.example.tag;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * This example tag creates an html table with multiple rows. It iterates
 * through a <code>Collection</code> which must be stored in the session
 * (attribute key is <i>currentCollection</i>). The elements are stored
 * in the request (attribute key is <i>currentObject</i>) one after another.
 * The body of the tag will evaluated multiple times, and if it's not empty,
 * an html row will be created.
 * If you store a <code>List</code> with one entry <i>Entry1</i> in the session 
 * and specify the following JSP code (<i>enumtag</i> is our tag):
 * 
 * <pre>
 * &lt;mytags:enumtag label="myLabel"&gt;
 *   &lt;bean:write scope="request" name="currentObject"/&gt;
 * &lt;/mytags:enumtag&gt;
 * </pre>
 * 
 * then the html output is:
 * 
 * <pre>
 * &lt;table&gt;
 * &lt;tr&gt;
 *  &lt;td&gt;
 *      myLabel
 *  &lt;/td&gt;
 *  &lt;td&gt;
 *      Entry1
 *  &lt;/td&gt;
 * &lt;/tr&gt;
 * &lt;/table&gt;
 * </pre>
 * 
 * Note: The <i>bean:write</i> tag is the common Struts tag.
 * Check out {@link TableEnumTagTest} to see how to write a test for
 * this tag.
 */
public class TableEnumTag extends BodyTagSupport
{
    private String label;
    private Iterator iterator;
    
    public void setLabel(String label)
    {
        this.label = label;
    }
    
    public void release()
    {       
        super.release();
        label = null;
        iterator = null;
    }
    
    private void copyNextObjectToRequest()
    {
        Object nextAttribute = iterator.next();
        pageContext.setAttribute("currentObject", nextAttribute, PageContext.REQUEST_SCOPE);
    }
    
    public int doStartTag() throws JspException
    {
        try
        {
            Collection col = (Collection)pageContext.getAttribute("currentCollection", PageContext.SESSION_SCOPE);
            iterator = col.iterator();
            if(iterator.hasNext())
            {
                pageContext.getOut().println("<table>");
                copyNextObjectToRequest();
                return EVAL_BODY_BUFFERED;
            }
            return SKIP_BODY;
        }
        catch(IOException exc)
        {
            throw new JspException(exc.getMessage());
        }
    }
    
    public int doAfterBody() throws JspException
    {
        String bodyString = getBodyContent().getString();
        JspWriter out = getBodyContent().getEnclosingWriter();
        try
        {
            if(null != bodyString && bodyString.length() != 0)
            {
                out.println("<tr>");
                out.println("\t<td>");
                out.println("\t\t" + label);
                out.println("\t</td>");
                out.println("\t<td>");
                out.println("\t\t" + bodyString);
                out.println("\t</td>");
                out.println("</tr>");
            }
            if(iterator.hasNext())
            {
                copyNextObjectToRequest();
                getBodyContent().clear();
                return EVAL_BODY_AGAIN;
            }
            out.println("</table>");
            return SKIP_BODY;
        }
        catch(IOException exc)
        {
            throw new JspException(exc.getMessage());
        }  
    }

    public int doEndTag() throws JspException
    {
        return EVAL_PAGE;
    }
}
