package model;

/**
 * Represents musical tempo in beats per minute (BPM).
 * Encapsulates validation and simple arithmetic operations.
 */
public final class Tempo {

    private final double bpm;

    public Tempo(double bpm) {
        if (bpm <= 0.0) {
            throw new IllegalArgumentException("Tempo BPM must be positive, got: " + bpm);
        }
        this.bpm = bpm;
    }

    public double getBpm() {
        return bpm;
    }

    /**
     * Returns a new Tempo with BPM increased by deltaBpm.
     */
    public Tempo add(double deltaBpm) {
        return new Tempo(bpm + deltaBpm);
    }

    /**
     * Returns a new Tempo with BPM decreased by deltaBpm.
     */
    public Tempo subtract(double deltaBpm) {
        return new Tempo(bpm - deltaBpm);
    }

    @Override
    public String toString() {
        return "Tempo(" + bpm + " BPM)";
    }
}
