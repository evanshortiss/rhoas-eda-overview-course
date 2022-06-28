# Quarkus Kafka Streams Song Play Count


## Usage

**NOTE**: Complete setup steps from the *README.md* in this repository, and run
the *nodejs-producer* before using this application.

### Configure Kafka Topics and ACLs

```bash
# Create required topics
rhoas kafka topic create --name song-play-counts --partitions 3
rhoas kafka topic create --name song-play-counter-song-play-counts-store-changelog --partitions 3

# Provide consume access to songs topic
rhoas kafka acl grant-access --consumer --service-account $CLIENT_ID --topic songs --group '*'

# Configure produce/consume for kafka streams topics
rhoas kafka acl grant-access --producer --consumer --service-account $CLIENT_ID --topic song-play-counts --group '*'
rhoas kafka acl grant-access --producer --consumer --service-account $CLIENT_ID --topic song-play-counter-song-play-counts-store-changelog --group '*'
```

### Run the Kafka Streams Application

Start the application using Maven:

```bash
mvn quarkus:dev
```

Once the application starts it will take a few seconds to start up Kafka
Streams processing of the *songs* topic. Once it starts it will print logs
similar to:

```
Recording a new play for song Pyro
Play counts for artist Kings of Leon are: {Pyro=76}
Recording a new play for song Take Me to Church
Play counts for artist Hozier are: {Like Real People Do=100, Take Me to Church=108}
```

You can query the full list of song play counts at
http://localhost:8081/song-counts.