package model;

/**
 * Represents the number of measures in a loop.
 * Encapsulates validation and measure arithmetic.
 */
public final class Measures {

    private final int value;

    public Measures(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Measures must be positive, got: " + value);
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /**
     * Returns a new Measures with the given delta added.
     */
    public Measures add(int delta) {
        return new Measures(value + delta);
    }

    /**
     * Returns a new Measures with the given delta subtracted.
     */
    public Measures subtract(int delta) {
        return new Measures(value - delta);
    }

    @Override
    public String toString() {
        return "Measures(" + value + ")";
    }
}
