package controller.engine;

public class PianoInstrument implements Instrument {

    public PianoInstrument() {
        // Set up any piano-specific state here (soundfont, samples, etc.)
        System.out.println("PianoInstrument: created default piano instrument.");
    }

    @Override
    public void noteOn(int pitch, int vel) {
        // Placeholder – eventually this would talk to the audio/MIDI backend
        System.out.printf("PianoInstrument: noteOn pitch=%d vel=%d%n", pitch, vel);
    }

    @Override
    public void noteOff(int pitch) {
        // Placeholder
        System.out.printf("PianoInstrument: noteOff pitch=%d%n", pitch);
    }

    @Override
    public void close() {
        // Release audio resources
        System.out.println("PianoInstrument: closing instrument.");
    }
}
