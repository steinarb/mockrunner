package com.mockrunner.example.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This example servlet invalidates the session and redirects
 * to a goodbye page when the user clicks the logout button
 * (i.e. the request contains a parameter <i>logout=true</i>).
 * Check out {@link LogoutServletTest}
 * to see how to test this simple servlet.
 */
public class LogoutServlet extends HttpServlet
{
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String logout = request.getParameter("logout");
        if(null != logout)
        {
            request.getSession().invalidate();
            request.getRequestDispatcher("/html/goodbye.html").forward(request, response);
        }
    }
}
