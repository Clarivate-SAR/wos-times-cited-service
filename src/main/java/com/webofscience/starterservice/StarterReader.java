package com.webofscience.starterservice;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

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
import java.util.Map;
import java.util.logging.Logger;

import org.w3c.dom.Document;

/**
 * Reads XML responses from Starter and processes into JSON friendly structure.
 */
public class StarterReader {

    private static final Logger log = Logger.getLogger( StarterReader.class.getName() );
    public static void main(String[] args) throws IOException, org.xml.sax.SAXException, ParserConfigurationException, XPathExpressionException {
        String sample = readTemplate();
        HashMap rsp = readResponse(sample);
        System.out.println(Arrays.toString(rsp.entrySet().toArray()));
    }

    // Read the response from Starter
    public static HashMap readResponse(String starterResponse) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(starterResponse, HashMap.class);

        HashMap newMap = new HashMap<String, String>();
        newMap.put("uid", map.get("uid"));
        newMap.put("timesCited", map.get("citations"));

        return newMap;
      
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
