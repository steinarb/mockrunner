package com.mockrunner.example.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * This example filter replaces request parameters from
 * submit images (<code>&lt;input type="image"/&gt;</code>)
 * with the appropriate submit button parameters
 * (<code>&lt;input type="submit"/&gt;</code>), i.e. if the
 * image name is <i>image</i>, the two parameters
 * <i>image.x</i> and <i>image.y</i> will be replaced
 * by one parameter <i>image</i>. This makes it possible
 * to handle submit images like normal submit buttons.
 */
public class ImageButtonFilter implements Filter
{
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
   {
        if(request instanceof HttpServletRequest)
        {
            ImageButtonRequestWrapper wrapper = new ImageButtonRequestWrapper((HttpServletRequest)request);
		    chain.doFilter(wrapper, response);
		}
        else
        {
            chain.doFilter(request, response);
        }
   }

   public void init(FilterConfig filterConfig)
   {

   }

   public void destroy()
   {

   }
}