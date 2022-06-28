mvn compile exec:java \
-Dlog4j.configurationFile=log4j2.properties \
-Dexec.mainClass="com.rhoas.kafka.Consumer"
