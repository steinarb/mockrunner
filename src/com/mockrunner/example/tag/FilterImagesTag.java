package com.mockrunner.example.tag;

import java.io.IOException;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * The purpose of this simple tag is to filter images. If its
 * body contains HTML image tags of the form
 * <pre>
 * &lt;img src="URL"&gt;
 * </pre>
 * it replaces the URL with <i>dummy.jpg</i>.
 * Image filtering can be enabled or disabled by setting the <i>filter</i>
 * attribute. Of course this attribute can be set dynamically with the help
 * of an EL or scripting expression.
 */
public class FilterImagesTag extends SimpleTagSupport
{
    private final static Pattern pattern = Pattern.compile("<\\s*img[^<>]+src\\s*=\\s*\"[^<>]*\".*?>");
    
    private boolean doFilter = false;
    
    public void setFilter(boolean doFilter)
    {
        this.doFilter = doFilter;
    }
    
    public void doTag() throws JspException, IOException
    {
        if(!doFilter)
        {
            getJspBody().invoke(null);
        }
        else
        {
            StringWriter writer = new StringWriter();
            getJspBody().invoke(writer);
            writer.flush();
            String filteredBody = doFilter(writer.toString());
            getJspContext().getOut().print(filteredBody);
        }
    }
    
    private String doFilter(String body)
    {
        Matcher matcher = pattern.matcher(body);
        StringBuffer output = new StringBuffer();
        while(matcher.find())
        {
            String match = matcher.group();
            match = match.replaceFirst("src.*=.*\".*\"", "src=\"dummy.jpg\"");
            matcher.appendReplacement(output, match);
        }
        matcher.appendTail(output);
        return output.toString();
    }
}
