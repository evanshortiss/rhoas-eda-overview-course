# OpenShift Streams for Apache Kafka Java Example

Consumes messages from a `songs` topic in a Kafka cluster.

## Usage

It's assumed that the Kafka cluster is secured using SASL PLAIN and SSL.

The following environment variables must be defined to run the application:

* `BOOTSTRAP_SERVER` (Comma separated list of URLs)
* `CLIENT_ID` (Username for SASL authentication)
* `CLIENT_SECRET` (Password for SASL authentication)

## Start the Consumer

Maven and a modern JDK are required to run the application.

```bash
mvn compile exec:java \
-Dlog4j.configurationFile=log4j2.properties \
-Dexec.mainClass="com.rhoas.kafka.Consumer"
```
