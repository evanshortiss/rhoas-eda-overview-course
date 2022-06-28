package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

@ApplicationScoped
@Path("/song-counts")
public class SongCountsEndpoint {

    @Inject
    KafkaStreams streams;

    @GET
    @Path("/")
    public Response getAllSongCounts() {
        ReadOnlyKeyValueStore<String, SongCounts> store = getSongCountsStore();

        return Response.ok(store.all()).build();
        
    }

    private ReadOnlyKeyValueStore<String, SongCounts> getSongCountsStore() {
        while (true) {
            try {
                return streams.store(
                    StoreQueryParameters.fromNameAndType(
                        SongCounterTopology.SONG_PLAY_COUNTS_STORE,
                        QueryableStoreTypes.keyValueStore()
                    )
                );
            } catch (InvalidStateStoreException e) {
                // ignore, store not ready yet
            }
        }
    }
}