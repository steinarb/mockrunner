package com.mockrunner.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet
{
    private boolean doGetCalled = false;
    private boolean doPostCalled = false;
    private boolean doDeleteCalled = false;
    private boolean doOptionsCalled = false;
    private boolean doPutCalled = false;
    private boolean doTraceCalled = false;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doGetCalled = true;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doPostCalled = true;
    }
    
    protected void doDelete(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException
    {
        doDeleteCalled = true;
    }

    protected void doOptions(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException
    {
        doOptionsCalled = true;
    }

    protected void doPut(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException
    {
        doPutCalled = true;
    }

    protected void doTrace(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException
    {
        doTraceCalled = true;
    }
    
    public boolean wasDoDeleteCalled()
    {
        return doDeleteCalled;
    }

    public boolean wasDoGetCalled()
    {
        return doGetCalled;
    }

    public boolean wasDoOptionsCalled()
    {
        return doOptionsCalled;
    }

    public boolean wasDoPostCalled()
    {
        return doPostCalled;
    }

    public boolean wasDoPutCalled()
    {
        return doPutCalled;
    }

    public boolean wasDoTraceCalled()
    {
        return doTraceCalled;
    }
}
