package org.acme.kafka.producer;

import io.smallrye.mutiny.Multi;
import org.acme.kafka.quarkus.Quote;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.Random;
import java.util.UUID;
import java.util.Date;

/**
 * A bean producing data to the "quotes" Kafka topic.
 */
@ApplicationScoped
public class QuotesProducer {

    private final Random random = new Random();

    @Outgoing("quotes")
    public Multi<Quote> generate() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(5))
                .onOverflow().drop()
                .map(tick -> {
                    // Enable backwards compatibility rules in the service
                    // registry, then Add a "timestamp" field with a "long"
                    // type and try this code. The producer startup will fail,
                    // since the rule was violated by the new schema. Fix it by
                    // adding this to the "timestamp" field:
                    // "type": [
                    //      "null",
                    //      "long"
                    // ],
                    // "default": null
                    // Date date = new Date();
                    // Quote q = new Quote(UUID.randomUUID().toString(), random.nextInt(100), date.getTime());
                    Quote q = new Quote(UUID.randomUUID().toString(), random.nextInt(100));

                    System.out.println("Sending a Quote with ID " + q.getId() + " to Kafka");

                    return q;
                });
    }
}
