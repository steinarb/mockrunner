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
        StringBuffer output = new StringBuffer();
        output.append("<html>\n");
        output.append("<head>\n");
        output.append("<meta http-equiv=\"refresh\" content=\"");
        output.append("0;URL=" + redirectUrl + "\">\n");
        output.append("</head>\n");
        output.append("<body>\n");
        output.append("<h3>");
        output.append("You will be redirected to ");
        output.append("<a href=\"" + redirectUrl + "\">");
        output.append(redirectUrl + "</a>");
        output.append("</h3>\n");
        output.append("</body>\n");
        output.append("</html>\n");
        response.getWriter().write(output.toString());
    }
}
