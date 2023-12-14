package com.webofscience.starterservice;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Logger;

/*
Web Service for making requests to Starter and returning WoS info.

At moment, only parameter accepted is the WoS UT.
 */

public class StarterServlet extends HttpServlet
{
    private static Boolean cacheStarter = true;
    private static final Logger log = Logger.getLogger( StarterServlet.class.getName() );

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String starterKey = "841d7f50c8c12dab692c639078e5fc14bbcf2a6f";
        String[] pathParts = request.getPathInfo().split("/", 3);
        if (pathParts.length < 2) {
            response.sendError(500, "No UT provided");
        }
        String idKey = pathParts[1];
        String requestedUT = pathParts[2];
        log.info("Requested UT: " + requestedUT);
        if (!idKey.matches("ut|doi|pmid")) {
            throw new ServletException("Invalid search parameter. Only ut, pmid, and doi are supported.");
        }
        String thisEtag = cacheKey(requestedUT);
        // check cache
        if (shouldCache(request, thisEtag))  {
            response.addHeader("ETag", thisEtag);
            response.sendError(HttpServletResponse.SC_NOT_MODIFIED, "Not Modified");
        } else {
            HashMap wosRsp = getResponse(starterKey, requestedUT);
            JSONObject json = new JSONObject(wosRsp);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json.toString());
            response.addHeader("ETag", thisEtag);
        }
    }

    private static HashMap getResponse(String starterKey, String ut) throws IOException {
        StarterClient ac = new StarterClient();
        HashMap rsp = ac.getResponse(starterKey, ut);
        return rsp;
    }

    private static String readTemplate() throws IOException {
        URL url = Resources.getResource("template.xml");
        return Resources.toString(url, Charsets.UTF_8);
    }

    private Boolean shouldCache(HttpServletRequest request, String thisEtag) {
        if (cacheStarter.equals(false)) {
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
