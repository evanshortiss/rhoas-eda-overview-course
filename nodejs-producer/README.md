# OpenShift Streams for Apache Kafka Node.js Example

Writes a set of messages containing song data to a Kafka cluster.

## Usage

It's assumed that the Kafka cluster is secured using SASL PLAIN and SSL.

Tested with Node.js v16.3.

The following environment variables must be defined to run the application. See
the instructions in the root-level *README.md* for more details:

* `BOOTSTRAP_SERVER` (Comma separated list of URLs)
* `CLIENT_ID` (Username for SASL authentication)
* `CLIENT_SECRET` (Password for SASL authentication)

### Local Development

Use the `dev` script to compile and run locally:

```
npm i
npm run dev
```

## Production Build

Run the `build` script, then the `start` script.

```
npm i
npm run build
npm start
```

