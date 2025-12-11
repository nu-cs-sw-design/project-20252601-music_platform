package model;

/**
 * Represents a position in the loop in units of beats.
 * For example, 0.0 is the first beat, 1.5 is halfway between beats 1 and 2.
 */
public final class BeatPosition {

    private final double value;

    public BeatPosition(double value) {
        if (value < 0.0) {
            throw new IllegalArgumentException("Beat position cannot be negative, got: " + value);
        }
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "BeatPosition(" + value + ")";
    }
}
