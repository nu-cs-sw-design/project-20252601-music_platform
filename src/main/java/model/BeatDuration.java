package model;

/**
 * Represents a duration in beats (e.g., 1.0 beat, 0.5 beats, etc.).
 */
public final class BeatDuration {

    private final double value;

    public BeatDuration(double value) {
        if (value <= 0.0) {
            throw new IllegalArgumentException("Beat duration must be positive, got: " + value);
        }
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "BeatDuration(" + value + ")";
    }
}
