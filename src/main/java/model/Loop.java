package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a loop made up of a fixed number of measures.
 * For now, this is just a container for notes; no playback logic.
 */
public class Loop {

    private final List<LoopNote> notes = new ArrayList<>();
    private final int measures;
    private double tempoBPM;

    public Loop(int measures) {
        if (measures <= 0) {
            throw new IllegalArgumentException("measures must be > 0");
        }
        this.measures = measures;
    }

    public double getTempoBPM() {
        return tempoBPM;
    }

    public void setTempoBPM(int tempoBPM) {
        int lowerClamp = 40;
        int higherClamp = 240;
        if (tempoBPM < lowerClamp) {
            this.tempoBPM = lowerClamp;
        }
        else if (tempoBPM > higherClamp) {
            this.tempoBPM = higherClamp;
        }
        else {
            this.tempoBPM = tempoBPM;
        }
    }

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
