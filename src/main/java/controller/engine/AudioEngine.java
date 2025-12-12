package controller.engine;

import model.Pitch;
import model.Velocity;

public class AudioEngine {

    private Instrument instrument;

    public AudioEngine() {
        // No instrument yet – created during initialize()
    }

    /**
     * Initializes the audio engine and default piano instrument.
     *
     * @return true if initialization succeeds, false otherwise.
     */
    public boolean initialize() {
        try {
            this.instrument = new PianoInstrument();  // may throw
            System.out.println("AudioEngine: initialized with default piano instrument.");
            return true;
        } catch (Exception e) {
            System.err.println("AudioEngine: failed to initialize instrument: " + e.getMessage());
            return false;
        }
    }

    public void noteOn(Pitch pitch, Velocity velocity) {
        if (instrument != null) {
            instrument.noteOn(pitch, velocity);
        }
    }

    public void noteOff(Pitch pitch) {
        if (instrument != null) {
            instrument.noteOff(pitch);
        }
    }
}
