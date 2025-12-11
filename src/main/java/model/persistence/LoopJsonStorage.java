package model.persistence;

import model.Loop;
import model.LoopNote;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Handles saving Loop data as JSON files on disk.
 */
public class LoopJsonStorage implements LoopStorage {

    private final Path baseDirectory;

    /**
     * @param baseDirectory directory where loop JSON files will be written.
     *                      Will be created if it doesn't exist.
     */
    public LoopJsonStorage(Path baseDirectory) {
        this.baseDirectory = baseDirectory;
        ensureDirectoryExists();
    }

    private void ensureDirectoryExists() {
        try {
            if (!Files.exists(baseDirectory)) {
                Files.createDirectories(baseDirectory);
            }
        } catch (IOException e) {
            System.err.println("LoopJsonStorage: failed to create directory "
                    + baseDirectory + ": " + e.getMessage());
        }
    }

    /**
     * Saves the given loop as a JSON file under the base directory.
     *
     * @param loop     the loop to save
     * @param fileName file name (e.g. "loop-1.json")
     */
    @Override
    public void saveLoop(Loop loop, String fileName) throws IOException {
        if (loop == null) {
            throw new IllegalArgumentException("loop cannot be null");
        }
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("fileName cannot be null/empty");
        }

        Path filePath = baseDirectory.resolve(fileName);

        String json = buildJson(loop);
        Files.write(filePath, json.getBytes(StandardCharsets.UTF_8));

        System.out.println("LoopJsonStorage: saved loop to " + filePath.toAbsolutePath());
    }

    /**
     * Builds a simple JSON representation:
     * {
     *   "measures": 4,
     *   "tempoBPM": 120.0,
     *   "notes": [
     *     { "pitch": 3, "startBeat": 0.0, "durationBeats": 1.0, "velocity": 100 },
     *     ...
     *   ]
     * }
     */
    private String buildJson(Loop loop) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        sb.append("  \"measures\": ").append(loop.getMeasures()).append(",\n");
        sb.append("  \"tempoBPM\": ").append(loop.getTempoBPM()).append(",\n");

        sb.append("  \"notes\": [\n");

        List<LoopNote> notes = loop.getNotes();
        for (int i = 0; i < notes.size(); i++) {
            LoopNote note = notes.get(i);
            sb.append("    {\n");
            sb.append("      \"pitch\": ").append(note.getPitch().getMidiNumber()).append(",\n");
            sb.append("      \"startBeat\": ").append(note.getStartBeat().getValue()).append(",\n");
            sb.append("      \"durationBeats\": ").append(note.getDurationBeats().getValue()).append(",\n");
            sb.append("      \"velocity\": ").append(note.getVelocity().getValue()).append("\n");

            sb.append("    }");
            if (i < notes.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append("  ]\n");
        sb.append("}\n");

        return sb.toString();
    }
}
