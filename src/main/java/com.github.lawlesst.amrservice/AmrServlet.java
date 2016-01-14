package com.github.lawlesst.amrservice;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AmrServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String[] pathParts = request.getPathInfo().split("/");
        String idKey = pathParts[1];
        String lookupValue = pathParts[2];
        if (!idKey.equals("ut")) {
            throw new ServletException("Invalid parameters. Only /ut/ is supported.");
        }
        String value = request.getParameter("value");
        HashMap amrRsp = getResponse(lookupValue);
        JSONObject json = new JSONObject(amrRsp);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }

    private static HashMap getResponse(String ut) throws IOException {
        String requestDoc = readTemplate().replace("--UT--", ut);
        //System.out.println(requestDoc);
        AmrClient ac = new AmrClient();
        HashMap rsp = ac.getResponse(requestDoc);
        return rsp;
    }

    private static String readTemplate() throws IOException {
        URL url = Resources.getResource("template.xml");
        return Resources.toString(url, Charsets.UTF_8);
    }
}

