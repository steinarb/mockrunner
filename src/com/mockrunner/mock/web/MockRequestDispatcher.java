package com.mockrunner.mock.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Mock implementation of <code>RequestDispatcher</code>.
 */
public class MockRequestDispatcher implements RequestDispatcher
{
    private ServletRequest forwardedRequest;
    private ServletResponse forwardedResponse;
    private ServletRequest includedRequest;
    private ServletResponse includedResponse;
    
    public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException
    {
        forwardedRequest = request;
        forwardedResponse = response;
    }

    public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException
    {
        includedRequest = request;
        includedResponse = response;
    }
    
    public ServletRequest getForwardedRequest()
    {
        return forwardedRequest;
    }

    public ServletResponse getForwardedResponse()
    {
        return forwardedResponse;
    }

    public ServletRequest getIncludedRequest()
    {
        return includedRequest;
    }

    public ServletResponse getIncludedResponse()
    {
        return includedResponse;
    }
}
