#Web of Science AMR Times Cited web service

This web service supports embedding times cited counts and links to the [Web of Science](http://ipscience.thomsonreuters.com/product/web-of-science/) from within the [VIVO](http://vivoweb.org) application via real-time queries to the Web of Science.

The Article Match Retrieval (AMR) web service from the Web of Science is utilized, see [documentation](http://ipscience-help.thomsonreuters.com/LAMRService/WebServicesOverviewGroup/overview.html).
You will need a subscription to the Web of Science and user name a password to access this service. Please contact [tech support](http://ip-science.thomsonreuters.com/techsupport/) to obtain credentials and indicate that you plan to use the AMR service with this tool.

For information about other Web of Science web services and the data usage policy, please [see this page](http://ip-science.interest.thomsonreuters.com/data-integration).

![screenshot of application](vivo-amr-service.png?raw=true "VIVO AMR service")

## Implementation details

This web service can be built as a separate Java application (war) that runs alongside your VIVO application. 

### URLs

Currently, only Web of Science unique identifiers (UT) are supported. This identifiers must be stored in your local system.

`/ut/{Web of Science unique identifier UT}`

### Configuration

Copy `src/main/resources/sample_template.xml` to `src/main/resources/template.xml` and change username and password to
match your credentials.

### Development

`$ mvn jetty:run`

To run on an alternative port:

`$ mvn jetty:run -Djetty.port=5000`

### Building

Build a WAR for deployment.

`$ mvn package`

Copy the war to your servlet directory. For example:

`$ cp target/amr-0.1-SNAPSHOT.war /var/lib/tomcat7/webapps/amr.war`


## Embedding in VIVO

VIVO pages need to call the service and pass it the Web of Science identifier for a publication. See `example/wosUtils.js`
for JavaScript that can be used to call the service. This can be added to all publication pages by adding it to the called
JavaScript in `individual.ftl`.

There's sample css to style the Times Cited information in `example/sample.css`
