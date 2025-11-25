package controller.engine;

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
        // 1. Initialize audio backend (device, mixer, etc.)
        //    This is a stub. In real life, you'd have try/catch here.

        boolean audioBackendReady = simulateAudioBackendInit();

        if (!audioBackendReady) {
            return false;
        }

        // 2. Create default piano instrument
        this.instrument = new PianoInstrument();
        System.out.println("AudioEngine: initialized with default piano instrument.");
        return true;
    }

    public void noteOn(int pitch, int vel) {
        if (instrument != null) {
            instrument.noteOn(pitch, vel);
        } else {
            System.err.println("AudioEngine: noteOn ignored, instrument not initialized.");
        }
    }

    public void noteOff(int pitch) {
        if (instrument != null) {
            instrument.noteOff(pitch);
        } else {
            System.err.println("AudioEngine: noteOff ignored, instrument not initialized.");
        }
    }

    /**
     * Example stub to simulate success/failure.
     * Replace with real audio device initialization.
     */
    private boolean simulateAudioBackendInit() {
        // return false here if you want to test the error path
        return true;
    }
}
