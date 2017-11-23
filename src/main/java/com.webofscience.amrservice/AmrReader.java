package com.webofscience.amrservice;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

import org.w3c.dom.Document;

/**
 * Reads XML responses from AMR and processes into JSON friendly structure.
 */
public class AmrReader {

    private static final Logger log = Logger.getLogger( AmrReader.class.getName() );
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
            log.info("Response: " + getStringFromDocument(document));
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
        NodeList enList = document.getElementsByTagName("error");
        for (int temp = 0; temp < nList.getLength(); temp++)
        {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) node;
                String value = eElement.getTextContent().trim();
                String key = eElement.getAttribute("name");
                hash.put(key, value);
            }
        }
        for (int temp = 0; temp < enList.getLength(); temp++)
        {
            Node node = enList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) node;
                String value = eElement.getTextContent().trim();
                String key = eElement.getAttribute("code");
                hash.put(key, value);
            }
        }
        return hash;
    }

    private static String readTemplate() throws IOException {
        URL url = Resources.getResource("sample_response.xml");
        return Resources.toString(url, Charsets.UTF_8);
    }

    //method to convert Document to String
    //https://stackoverflow.com/questions/10356258/how-do-i-convert-a-org-w3c-dom-document-object-to-a-string
    public static String getStringFromDocument(Document doc)
    {
        try
        {
           DOMSource domSource = new DOMSource(doc);
           StringWriter writer = new StringWriter();
           StreamResult result = new StreamResult(writer);
           TransformerFactory tf = TransformerFactory.newInstance();
           Transformer transformer = tf.newTransformer();
           transformer.transform(domSource, result);
           return writer.toString();
        }
        catch(TransformerException ex)
        {
           ex.printStackTrace();
           return null;
        }
    }
}
