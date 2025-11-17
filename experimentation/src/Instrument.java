import javax.sound.midi.*;

public class Instrument {
    private Synthesizer synth;
    private MidiChannel channel;

    public Instrument() {
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();

            MidiChannel[] channels = synth.getChannels();
            channel = channels[0];

            // Patch 0 = Acoustic Grand Piano
            channel.programChange(0);
        } catch (MidiUnavailableException e) {
            throw new RuntimeException("MIDI synth failed to initialize", e);
        }
    }

    public void noteOn(int pitch, int velocity) {
        channel.noteOn(pitch, velocity);
    }

    public void noteOff(int pitch) {
        channel.noteOff(pitch);
    }

    public void close() {
        synth.close();
    }
}
