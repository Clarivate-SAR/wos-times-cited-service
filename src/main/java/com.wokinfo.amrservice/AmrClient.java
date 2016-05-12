package com.wokinfo.amrservice;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;

/**
 * Client to post requests to AMR and read responses.
 */
public class AmrClient {

    public HashMap getResponse(String docRequest) throws IOException {
        String raw = getAPIResponse(docRequest);
        HashMap rsp = AmrReader.readResponse(raw);
        return rsp;
    }

    public String getAPIResponse(String docRequest) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost("https://ws.isiknowledge.com/cps/xrpc");
            httpPost.setEntity(new StringEntity(docRequest));
            CloseableHttpResponse response = httpclient.execute(httpPost);
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
