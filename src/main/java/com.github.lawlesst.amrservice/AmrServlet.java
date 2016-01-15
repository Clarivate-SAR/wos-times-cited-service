package com.github.lawlesst.amrservice;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.common.primitives.Booleans;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AmrServlet extends HttpServlet
{
    private static Boolean cacheAmr = true;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String[] pathParts = request.getPathInfo().split("/");
        String idKey = pathParts[1];
        String requestedUT = pathParts[2];
        if (!idKey.equals("ut")) {
            throw new ServletException("Invalid parameters. Only /ut/ is supported.");
        }
        String thisEtag = cacheKey(requestedUT);
        // check cache
        if (shouldCache(request, thisEtag))  {
            response.addHeader("ETag", thisEtag);
            response.sendError(HttpServletResponse.SC_NOT_MODIFIED, "Not Modified");
        } else {
            HashMap amrRsp = getResponse(requestedUT);
            JSONObject json = new JSONObject(amrRsp);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json.toString());
            response.addHeader("ETag", thisEtag);
        }
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

    private Boolean shouldCache(HttpServletRequest request, String thisEtag) {
        if (cacheAmr.equals(false)) {
            return false;
        }
        else if (!isConditionalRequest(request)) {
            return false;
        }
        else {
            String incoming = incomingEtag(request);
            if (thisEtag.equals(incoming)) {
                return true;
            } else {
                return false;
            }
        }
    }

    private String incomingEtag(HttpServletRequest request) {
        return request.getHeader("If-None-Match");
    }

    private static String cacheKey(String value) {
        Integer week = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        String key = week.toString() + value;
        return key;
    }

    private boolean isConditionalRequest(HttpServletRequest req) {
        if (req.getHeader("If-None-Match") == null) {
            return false;
        } else {
            return true;
        }
    }

    /*
        Calculate an ETAG for the incoming request and return true/false
        if this request should not be regenerated.
     */
    private static Boolean cached(String existing, String etag) {
        String key = cacheKey(existing);
        if ( key.equals(existing) ) {
            return true;
        } else {
            return false;
        }
    }
}

