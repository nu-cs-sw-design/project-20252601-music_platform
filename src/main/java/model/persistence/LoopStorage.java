package model.persistence;

import model.Loop;

import java.io.IOException;

/**
 * Abstraction for saving loop data.
 * Different implementations can persist loops to JSON, a database, etc.
 */
public interface LoopStorage {

    /**
     * Saves the given loop under the given file name or identifier.
     *
     * @param loop     the loop to save
     * @param fileName a logical name for the loop (e.g. "loop-1.json")
     * @throws IOException if the save operation fails
     */
    void saveLoop(Loop loop, String fileName) throws IOException;
}
