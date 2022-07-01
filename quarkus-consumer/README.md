# Quarkus Kafka Songs Consumer

## Usage

### Run Locally in Dev Mode

```
# run this in the root of the repo after creating the .env file
source .env

# mvn quarkus:dev
```

### Deploy on OpenShift

```
# login to your openshift cluster using a token, or username and password
oc login --token $TOKEN --server $URL

# select the project you'd like to deploy the application into
oc project $PROJECT_NAME

# create a buildconfig, start a build, and spin up a deployment
mvnw clean package -Dquarkus.kubernetes.deploy=true
```
