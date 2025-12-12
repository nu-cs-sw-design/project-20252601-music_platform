package model;

public final class Tempo {

    public static final double MIN_BPM = 20.0;
    public static final double MAX_BPM = 300.0;

    private final double bpm;

    public Tempo(double bpm) {
        this.bpm = clamp(bpm);
    }

    public double getBpm() {
        return bpm;
    }

    public Tempo add(double deltaBpm) {
        return new Tempo(bpm + deltaBpm);
    }

    public Tempo subtract(double deltaBpm) {
        return new Tempo(bpm - deltaBpm);
    }

    private static double clamp(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Tempo BPM must be a real number, got: " + value);
        }
        return Math.max(MIN_BPM, Math.min(MAX_BPM, value));
    }

    @Override
    public String toString() {
        return "Tempo(" + bpm + " BPM)";
    }
}
