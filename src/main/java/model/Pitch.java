package model;

/**
 * Represents a musical pitch as a MIDI note number (0-127).
 * Encapsulates validation and pitch-related operations (e.g., transposition).
 */
public final class Pitch {

    private final int midiNumber;

    public Pitch(int midiNumber) {
        if (midiNumber < 0 || midiNumber > 127) {
            throw new IllegalArgumentException("MIDI pitch must be in [0, 127], got: " + midiNumber);
        }
        this.midiNumber = midiNumber;
    }

    public int getMidiNumber() {
        return midiNumber;
    }

    /**
     * Returns a new Pitch transposed by the given number of semitones.
     */
    public Pitch transpose(int semitones) {
        return new Pitch(midiNumber + semitones);
    }

    @Override
    public String toString() {
        return "Pitch(" + midiNumber + ")";
    }
}
