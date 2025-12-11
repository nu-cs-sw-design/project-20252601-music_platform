package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a loop: a fixed number of measures, a tempo, and a set of notes.
 */
public class Loop {

    private final List<LoopNote> notes = new ArrayList<>();
    private Measures measures;
    private Tempo tempo;

    /**
     * Legacy convenience constructor: still allow passing an int.
     * Internally wraps it in Measures.
     */
    public Loop(int measures) {
        this(new Measures(measures));
    }

    public Loop(Measures measures) {
        if (measures == null) {
            throw new IllegalArgumentException("measures cannot be null");
        }
        this.measures = measures;
        this.tempo = new Tempo(120.0); // default tempo
    }

    public void addNote(LoopNote note) {
        if (note == null) {
            throw new IllegalArgumentException("note cannot be null");
        }
        notes.add(note);
    }

    public void removeNote(LoopNote note) {
        notes.remove(note);
    }

    public List<LoopNote> getNotes() {
        return Collections.unmodifiableList(notes);
    }

    // --- Measures ---

    public Measures getMeasures() {
        return measures;
    }

    /** Convenience for callers that just want the primitive. */
    public int getMeasureCount() {
        return measures.getValue();
    }

    public void setMeasures(Measures measures) {
        if (measures == null) {
            throw new IllegalArgumentException("measures cannot be null");
        }
        this.measures = measures;
    }

    // Optional helper if you ever want to add/sub measures from the loop directly:
    public void addMeasures(int delta) {
        this.measures = this.measures.add(delta);
    }

    public void subtractMeasures(int delta) {
        this.measures = this.measures.subtract(delta);
    }

    // --- Tempo ---

    public Tempo getTempo() {
        return tempo;
    }

    /** Convenience for callers that just want the primitive BPM. */
    public double getTempoBPM() {
        return tempo.getBpm();
    }

    public void setTempo(Tempo tempo) {
        if (tempo == null) {
            throw new IllegalArgumentException("tempo cannot be null");
        }
        this.tempo = tempo;
    }

    /** Keeps your old signature but routes through the value object. */
    public void setTempoBPM(double bpm) {
        this.tempo = new Tempo(bpm);
    }

    // Optional helpers that use Tempo.add/subtract:
    public void increaseTempo(double deltaBpm) {
        this.tempo = this.tempo.add(deltaBpm);
    }

    public void decreaseTempo(double deltaBpm) {
        this.tempo = this.tempo.subtract(deltaBpm);
    }
}
