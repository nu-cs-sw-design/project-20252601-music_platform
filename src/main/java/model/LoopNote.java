package model;

public class LoopNote {

    private final Pitch pitch;
    private final BeatPosition startBeat;
    private final BeatDuration durationBeats;
    private final Velocity velocity;

    public LoopNote(Pitch pitch,
                    BeatPosition startBeat,
                    BeatDuration durationBeats,
                    Velocity velocity) {
        if (pitch == null || startBeat == null || durationBeats == null || velocity == null) {
            throw new IllegalArgumentException("LoopNote fields cannot be null");
        }
        this.pitch = pitch;
        this.startBeat = startBeat;
        this.durationBeats = durationBeats;
        this.velocity = velocity;
    }

    public Pitch getPitch() {
        return pitch;
    }

    public BeatPosition getStartBeat() {
        return startBeat;
    }

    public BeatDuration getDurationBeats() {
        return durationBeats;
    }

    public Velocity getVelocity() {
        return velocity;
    }
}
