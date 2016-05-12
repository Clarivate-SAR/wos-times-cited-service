#AMR web service



## URLs

`/amr/ut/{Web of Science unique identifier UT}.

## Configuration

Copy `src/main/resources/sample_template.xml` to `src/main/resources/template.xml` and change username and password to
match your credentials.

## Development

`$ mvn jetty:run`

To run on an alternative port:

`$ mvn jetty:run -Djetty.port=5000`

## Building

Build a WAR for deployment.

`$mvn package`

Copy the war to your servlet directory.

`cp cp target/wos-0.1-SNAPSHOT.war /var/lib/tomcat7/webapps/wos.war`


## Embedding in VIVO

VIVO pages need to call the service and pass it the Web of Science identifier for a publication. See `example/wosUtils.js`
for JavaScript that can be used to call the service. This can be added to all publication pages by adding it to the called
JavaScript in `individual.ftl`.

There's sample css to style the Times Cited information in `example/sample.css`
