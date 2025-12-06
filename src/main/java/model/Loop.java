package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a loop made up of a fixed number of measures.
 * Holds notes and tempo; no playback logic.
 */
public class Loop {

    private final List<LoopNote> notes = new ArrayList<>();
    private final int measures;

    // --- Tempo support ---
    private static final double MIN_BPM = 40.0;
    private static final double MAX_BPM = 240.0;

    // Default tempo for new loops
    private double tempoBPM = 120.0;

    public Loop(int measures) {
        if (measures <= 0) {
            throw new IllegalArgumentException("measures must be > 0");
        }
        this.measures = measures;
        // tempoBPM already defaults to 120.0
    }

    // --- Tempo API ---

    public double getTempoBPM() {
        return tempoBPM;
    }

    /**
     * Sets the tempo in BPM, clamped to [MIN_BPM, MAX_BPM].
     * Accepts double so we can support non-integer BPM later.
     */
    public void setTempoBPM(double tempoBPM) {
        if (Double.isNaN(tempoBPM) || Double.isInfinite(tempoBPM)) {
            return; // ignore nonsense
        }

        if (tempoBPM < MIN_BPM) {
            this.tempoBPM = MIN_BPM;
        } else if (tempoBPM > MAX_BPM) {
            this.tempoBPM = MAX_BPM;
        } else {
            this.tempoBPM = tempoBPM;
        }
    }

    // --- Measures / notes ---

    public int getMeasures() {
        return measures;
    }

    public void addNote(LoopNote note) {
        if (note == null) return;
        notes.add(note);
    }

    public void removeNote(LoopNote note) {
        notes.remove(note);
    }

    /**
     * Returns an unmodifiable view of the notes.
     * Prevents callers from mutating the internal list directly.
     */
    public List<LoopNote> getNotes() {
        return Collections.unmodifiableList(notes);
    }
}
