# Web of Science AMR Times Cited web service

This Java web service provides Web of Science Times Cited counts via a simple JSON response. Check the 'example' directory for a simple implementation using javascript.

The Article Match Retrieval (AMR) web service from the Web of Science is utilized to make real-time queries to the Web of Science, see documentation [here](http://ipscience-help.thomsonreuters.com/LAMRService/WebServicesOverviewGroup/overview.html).
You will need a subscription to the Web of Science and user name and password to access this service. Please contact your Clarivate Web of Science account manager directly or [customer care](https://support.clarivate.com) to obtain credentials and indicate that you plan to use the AMR service with this tool.

For information about other Web of Science web services and the data usage policy, please [see this page](http://clarivate.com/products/data-integration/).

![screenshot of application](example/vivo-amr-service.png?raw=true "VIVO AMR service")

## Implementation details

This web service can be built as a separate Java application (war) that runs alongside your application. The example screenshot above shows the web service integrated into the [VIVO](http://vivoweb.org/) research networking software using the sample javascript and css in the 'example' directory.

### URLs

You may call the service using a DOI, PubMed ID (PMID), or Web of Science unique identifier (UT).

`/doi/{DOI}`
`/pmid/{PubMed ID}`
`/ut/{Web of Science unique identifier UT}`

### Configuration

Copy `src/main/resources/sample_template.xml` to `src/main/resources/template.xml` and change username and password to match your credentials.

### Development

`$ mvn jetty:run`

To run on an alternative port:

`$ mvn jetty:run -Djetty.port=5000`

You should now be able to call the service: http://localhost:5000/doi/10.1109/82.486465

### Building

Build a WAR for deployment.

`$ mvn package`

Copy the war to your servlet directory. For example:

`$ cp target/amr-0.1-SNAPSHOT.war /var/lib/tomcat7/webapps/amr.war`

### Responses

The client parses the XML response from the Web of Science and returns a JSON document. A successful query will return a response like this:

 ```json
 {  
    "sourceURL":   "http://gateway.webofknowledge.com/gateway/Gateway.cgi?GWVersion=2&SrcApp=PARTNER_APP&SrcAuth=TRINTCEL&KeyUT=WOS:000179024900021&DestLinkType=FullRecord&DestApp=WOS_CPL&UsrCustomerID=dc101777b18a39292144c8423537a284",  
    "citingArticlesURL":   "http://gateway.webofknowledge.com/gateway/Gateway.cgi?GWVersion=2&SrcApp=PARTNER_APP&SrcAuth=TRINTCEL&KeyUT=WOS:000179024900021&DestLinkType=CitingArticles&DestApp=WOS_CPL&UsrCustomerID=dc101777b18a39292144c8423537a284",  
    "timesCited": "2",  
    "relatedRecordsURL":   "http://gateway.webofknowledge.com/gateway/Gateway.cgi?GWVersion=2&SrcApp=PARTNER_APP&SrcAuth=TRINTCEL&KeyUT=WOS:000179024900021&DestLinkType=RelatedRecords&DestApp=WOS_CPL&UsrCustomerID=dc101777b18a39292144c8423537a284",  
    "ut": "000179024900021",  
    "doi": "10.1785/0120010124"  
}
 ```

 If the query is not successful, the client will pass the error message from WoS:

 ```json
 {  
   "message": "No Result Found"  
}
```

```json
{  
  "Server.authentication": "Invalid Username"  
}
```

See the [documentation](http://ipscience-help.thomsonreuters.com/LAMRService/WebServicesOverviewGroup/overview.html) for a full list of errors returned from AMR.

## Embedding in VIVO

VIVO pages need to call the service and pass it the Web of Science identifier for a publication. See `example/wosUtils.js` for JavaScript that can be used to call the service. This can be added to all publication pages by adding it to the called
JavaScript in `individual.ftl`.
