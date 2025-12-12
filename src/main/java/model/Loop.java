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


    public void setMeasures(Measures measures) {
        if (measures == null) {
            throw new IllegalArgumentException("measures cannot be null");
        }
        this.measures = measures;
    }

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

    public void setTempo(Tempo tempo) {
        if (tempo == null) {
            throw new IllegalArgumentException("tempo cannot be null");
        }
        this.tempo = tempo;
    }

    public void increaseTempo(double deltaBpm) {
        this.tempo = this.tempo.add(deltaBpm);
    }

    public void decreaseTempo(double deltaBpm) {
        this.tempo = this.tempo.subtract(deltaBpm);
    }
}
