# OpenShift Streams for Apache Kafka Examples

This repository contains an example Kafka producer written in Node.js, and
example Kafka consumers written in Java using the Apache Kafka client and
Quarkus with SmallRye Reactive Messaging.

These examples are intended for use with Red Hat OpenShift Streams for Apache
Kafka, but can be used with any correctly configured Kafka cluster.

## Requirements 

The applications in this repository are tested with the following software
versions:

* JDK 11
* Maven 3.8.3
* Node.js 16.3
* npm 7.15

## Usage

Sign-up for an account at [console.redhat.com](https://console.redhat.com/).
Then visit the Application Services section of the UI to create an evaluation
tier Kafka cluster for free.

While you wait 2 minutes for the cluster to become ready, make a copy of the
*.env.example* file contained in this repository named *.env*. Return to
console.redhat.com and obtain the Bootstrap Server URL for your Kafka cluster, 
and place it in the appropriate position in your *.env* file.

Next, create a Service Account from console.redhat.com and place the client ID
and Secret in the *.env* file. The file should look similar to this:

```
export BOOTSTRAP_SERVER=named-kafka-casvrd--qd--me-adqea.bf2.kafka.rhcloud.com:443
export CLIENT_ID=srvc-acct-11abadd4-a4fa-5af7-1111-3fba0190e111
export CLIENT_SECRET=1cdab11e-aec2-4578-b061-76b47d016b13
export OAUTH_TOKEN_URL=https://identity.api.openshift.com/auth/realms/rhoas/protocol/openid-connect/token
```

You can now run the sample applications by opening a terminal in the root of
this repository and running these commands on Linux or macOS:

```
source .env

# replace this with the desired application
cd nodejs-producer

./start.sh
```