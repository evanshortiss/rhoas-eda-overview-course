package com.rhoas.kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.SaslConfigs;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Consumer {
    
    final static String bootstrapServer = System.getenv("BOOTSTRAP_SERVER");
    final static String username = System.getenv("CLIENT_ID");
    final static String password = System.getenv("CLIENT_SECRET");

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();

        if (bootstrapServer == null || username == null || password == null) {
            throw new Exception("Ensure that the BOOTSTRAP_SERVER, CLIENT_ID and CLIENT_SECRET environment variables are set");
        }

        // Define the bootstrap server(s)
        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        
        // Configure SSL, SASL mechanism, and username/password 
        properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        properties.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        properties.put(
            SaslConfigs.SASL_JAAS_CONFIG,
            String.format(
                "org.apache.kafka.common.security.plain.PlainLoginModule required username='%s' password='%s';",
                username,
                password
            )
        );
            
        // Configure deserializer used when processing message value and key
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        // Specify the consumer group that this application belongs to
        properties.put(CommonClientConfigs.GROUP_ID_CONFIG, "song-consumer");

        // Create a Kafka consumer and subscribe to the "songs" topic
        System.out.println("Creating consumer for songs topic...");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        List<String> topics = new ArrayList<String>();
        topics.add("songs");
        consumer.subscribe(topics);
        
        
        System.out.println("Begin consuming from songs topic. This might take a moment...");
        try {
            while (true) {
                // Poll for messages every second and print their details to stdout 
                ConsumerRecords<String, String> messages = consumer.poll(Duration.ofSeconds(1));

                // Log the partition, key, and value for each of the received messages
                messages.forEach(message -> {
                    System.out.println(
                        String.format(
                            "Partition: %d, Key: %s, Value: %s",
                            message.partition(),
                            message.key(),
                            message.value()
                        )
                    );
                });
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Finished consuming from songs topic");
            consumer.close();
        }
    }
}
