package com.webofscience.starterservice;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;

/**
 * Client to post requests to Starter and read responses.
 */
public class StarterClient {

    public HashMap getResponse(String apiKey, String ut) throws IOException {
        String raw = getAPIResponse(apiKey, ut);
        HashMap rsp = new StarterReader().readResponse(raw);
        return rsp;
    }

    public String getAPIResponse(String apiKey, String ut) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet("https://api.clarivate.com/apis/wos-starter/v1/documents/" + ut);
            httpGet.setHeader("X-ApiKey", apiKey);
            httpGet.addHeader("detail", "short");
            CloseableHttpResponse response = httpclient.execute(httpGet);
            try {
                return EntityUtils.toString(response.getEntity());
            } finally {
                response.close();
            }

        } finally {
            httpclient.close();
        }
    }
}
