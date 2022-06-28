package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.Stores;
import org.jboss.logging.Logger;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

@ApplicationScoped
public class SongCounterTopology {
    static final String SONG_PLAY_COUNTS_STORE = "song-play-counts-store";

    private static final Logger Log = Logger.getLogger(SongCounterTopology.class);
    private static final String SONGS_TOPIC = "songs";
    private static final String SONG_PLAY_COUNTS_TOPIC = "song-play-counts";

    @Produces
    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        ObjectMapperSerde<SongCounts> songCountsSerde = new ObjectMapperSerde<>(SongCounts.class);
        KeyValueBytesStoreSupplier storeSupplier = Stores.persistentKeyValueStore(SONG_PLAY_COUNTS_STORE);

        builder.stream(
                SONGS_TOPIC,
                Consumed.with(Serdes.String(), Serdes.String())
            )
            .groupByKey()
            .aggregate(
                SongCounts::new,
                (artist, song, artistPlayCounts) -> {
                    Log.infov("Recording a new play for song {0}", song);

                    artistPlayCounts.incrementPlayCountForSong(song);

                    Log.infov("Play counts for artist {0} are: {1}", artist, artistPlayCounts);

                    return artistPlayCounts;
                },
                Materialized.<String, SongCounts>as(storeSupplier)
                    .withKeySerde(Serdes.String())
                    .withValueSerde(songCountsSerde)
            )
            .toStream()
            .to(
                SONG_PLAY_COUNTS_TOPIC,
                Produced.with(Serdes.String(), songCountsSerde)
            );

        return builder.build();
    }
}
