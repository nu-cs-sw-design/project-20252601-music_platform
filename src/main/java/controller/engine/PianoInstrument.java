package controller.engine;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

/**
 * Simple piano-like instrument using Java's built-in MIDI synthesizer.
 */
public class PianoInstrument implements Instrument {

    private Synthesizer synth;
    private MidiChannel channel;

    // Middle C = 60 in MIDI; we’ll treat pitch=0 as middle C
    private static final int BASE_MIDI_NOTE = 60;

    public PianoInstrument() {
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();

            MidiChannel[] channels = synth.getChannels();
            if (channels != null && channels.length > 0) {
                channel = channels[0];
            }

            System.out.println("PianoInstrument: created default piano instrument (MIDI synth opened).");
        } catch (MidiUnavailableException e) {
            System.err.println("PianoInstrument: MIDI device unavailable: " + e.getMessage());
        }
    }

    @Override
    public void noteOn(int pitch, int vel) {
        if (channel == null) {
            System.err.println("PianoInstrument: no MIDI channel, cannot play note.");
            return;
        }

        // Map your pitch index (0..11, etc.) to a MIDI note number
        int midiNote = BASE_MIDI_NOTE + pitch;

        System.out.printf("PianoInstrument: noteOn pitch=%d (midiNote=%d) vel=%d%n",
                pitch, midiNote, vel);
        channel.noteOn(midiNote, vel);
    }

    @Override
    public void noteOff(int pitch) {
        if (channel == null) {
            System.err.println("PianoInstrument: no MIDI channel, cannot stop note.");
            return;
        }

        int midiNote = BASE_MIDI_NOTE + pitch;

        System.out.printf("PianoInstrument: noteOff pitch=%d (midiNote=%d)%n",
                pitch, midiNote);
        channel.noteOff(midiNote);
    }

    @Override
    public void close() {
        System.out.println("PianoInstrument: closing instrument.");
        if (synth != null && synth.isOpen()) {
            synth.close();
        }
    }
}
