package com.mockrunner.test;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class TestFilter implements Filter
{
    private boolean initCalled  = false;
    private boolean doFilterCalled  = false;
    private FilterChain lastChain;

    public void init(FilterConfig arg0) throws ServletException
    {
        initCalled = true;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        doFilterCalled = true;
        lastChain = chain;
        chain.doFilter(request, response);
    }

    public void destroy()
    {
       
    }

    public boolean wasDoFilterCalled()
    {
        return doFilterCalled;
    }

    public boolean wasInitCalled()
    {
        return initCalled;
    }

    public FilterChain getLastFilterChain()
    {
        return lastChain;
    }
}
