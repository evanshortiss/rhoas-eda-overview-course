package org.acme;

import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import io.smallrye.reactive.messaging.kafka.api.KafkaMetadataUtil;
import org.eclipse.microprofile.reactive.messaging.*;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class MyReactiveMessagingApplication {

    @Inject
    @Channel("songs")

    void onStart() {
        System.out.println("Quarkus songs consumer started...");
    }

    @Incoming("songs")
    public CompletionStage<Void> printSong(Message<String> message) {
        IncomingKafkaRecordMetadata<String, String> md = KafkaMetadataUtil.readIncomingKafkaMetadata(message).get();
        
        System.out.printf(
            "\nReceived song \"%s\" by \"%s\" from the %s topic [Partition: %d]",
            md.getRecord().value(),
            md.getKey(),
            md.getTopic(),
            md.getPartition()
        );

        return message.ack();
    }
}
