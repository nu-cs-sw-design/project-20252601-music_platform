package model;

/**
 * Represents MIDI velocity (0-127).
 * Encapsulates validation and simple scaling behavior.
 */
public final class Velocity {

    private final int value;

    public Velocity(int value) {
        if (value < 0 || value > 127) {
            throw new IllegalArgumentException("Velocity must be in [0, 127], got: " + value);
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /**
     * Returns a new Velocity scaled by the given factor (e.g., 0.5 for softer, 1.5 for louder),
     * clamped to [0, 127].
     */
    public Velocity scaled(double factor) {
        int scaled = (int) Math.round(value * factor);
        scaled = Math.max(0, Math.min(127, scaled));
        return new Velocity(scaled);
    }

    @Override
    public String toString() {
        return "Velocity(" + value + ")";
    }
}
