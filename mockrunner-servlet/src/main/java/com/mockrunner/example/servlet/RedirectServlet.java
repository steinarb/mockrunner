package com.mockrunner.example.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This example servlet creates an html page thats
 * redirects to a specified URL. You can specify
 * the URl with the request parameter <i>redirecturl</i>.
 * Check out {@link RedirectServletTest}
 * to see how to test this simple servlet.
 */
public class RedirectServlet extends HttpServlet
{
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String redirectUrl = request.getParameter("redirecturl");
        String output = "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"refresh\" content=\"" +
                "0;URL=" + redirectUrl + "\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h3>" +
                "You will be redirected to " +
                "<a href=\"" + redirectUrl + "\">" +
                redirectUrl + "</a>" +
                "</h3>\n" +
                "</body>\n" +
                "</html>\n";
        response.getWriter().write(output);
    }
}
