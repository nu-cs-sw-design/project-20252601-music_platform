package model;

/**
 * Represents a single note in the loop.
 * Immutable object with private fields.
 */
public class LoopNote {

    private final int pitch;
    private final double startBeat;
    private final double durationBeats;
    private final int velocity;

    public LoopNote(int pitch, double startBeat, double durationBeats, int velocity) {
        this.pitch = pitch;
        this.startBeat = startBeat;
        this.durationBeats = durationBeats;
        this.velocity = velocity;
    }

    public int getPitch() {
        return pitch;
    }

    public double getStartBeat() {
        return startBeat;
    }

    public double getDurationBeats() {
        return durationBeats;
    }

    public int getVelocity() {
        return velocity;
    }

    @Override
    public String toString() {
        return "LoopNote{" +
                "pitch=" + pitch +
                ", startBeat=" + startBeat +
                ", durationBeats=" + durationBeats +
                ", velocity=" + velocity +
                '}';
    }
}
