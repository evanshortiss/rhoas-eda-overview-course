package org.acme;

import java.util.HashMap;

/**
 * A SongCounts object will be created for each artist (key) in the in the
 * songs topic. The hashmap contains a key for each song, and increments it
 * anytime a new play of that song is produced into the songs topic. 
 */
public class SongCounts extends HashMap<String, Integer> {
    public void incrementPlayCountForSong (String song) {
        // Increment the existing play count by one for this song.
        // If song (key) not present then initialise with a value of 1.
        this.compute(song, (k, v) -> v == null ? 1 : v + 1);
    }
}
