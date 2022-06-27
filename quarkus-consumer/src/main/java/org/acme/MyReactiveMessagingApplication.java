package org.acme;

import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.reactive.messaging.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.stream.Stream;

@ApplicationScoped
public class MyReactiveMessagingApplication {

    @Inject
    @Channel("songs")

    void onStart() {
        System.out.println("Quarkus songs consumer started...");
    }

    @Incoming("songs")
    public void printSong(String message) {
        System.out.println(message);
    }
}
