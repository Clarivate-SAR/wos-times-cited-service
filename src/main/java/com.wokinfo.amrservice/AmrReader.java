package com.wokinfo.amrservice;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

import org.w3c.dom.Document;

/**
 * Reads XML responses from AMR and processes into JSON friendly structure.
 */
public class AmrReader {
    public static void main(String[] args) throws IOException, org.xml.sax.SAXException, ParserConfigurationException, XPathExpressionException {
        String sample = readTemplate();
        HashMap rsp = readResponse(sample);
        System.out.println(Arrays.toString(rsp.entrySet().toArray()));
    }

    // Read the response from AMR
    public static HashMap readResponse(String amrResponse) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            return null;
        }
        InputStream stream = new ByteArrayInputStream(amrResponse.getBytes(StandardCharsets.UTF_8));
        Document document = null;
        try {
            document = builder.parse(stream);
        } catch (org.xml.sax.SAXException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        document.getDocumentElement().normalize();

        // store response here
        HashMap hash = new HashMap<String, String>();
        // Get all the values
        NodeList nList = document.getElementsByTagName("val");
        for (int temp = 0; temp < nList.getLength(); temp++)
        {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) node;
                String value = eElement.getTextContent().trim();
                String key = eElement.getAttribute("name");
                // no results found will have message key.
                if (!key.equals("message")) {
                    hash.put(key, value);
                }
            }
        }
        return hash;
    }

    private static String readTemplate() throws IOException {
        URL url = Resources.getResource("sample_response.xml");
        return Resources.toString(url, Charsets.UTF_8);
    }
}