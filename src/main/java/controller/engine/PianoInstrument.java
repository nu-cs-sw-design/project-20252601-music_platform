package controller.engine;

import model.Pitch;
import model.Velocity;

import javax.sound.midi.*;

public class PianoInstrument implements Instrument {

    private Synthesizer synth;
    private MidiChannel channel;

    private static final int BASE_MIDI_NOTE = 60; // Middle C

    public PianoInstrument() {
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();

            MidiChannel[] channels = synth.getChannels();
            if (channels != null && channels.length > 0) {
                channel = channels[0];
            }

            System.out.println("PianoInstrument: MIDI synth opened.");
        } catch (MidiUnavailableException e) {
            System.err.println("PianoInstrument: MIDI unavailable: " + e.getMessage());
        }
    }

    @Override
    public void noteOn(Pitch pitch, Velocity velocity) {
        if (channel == null) {
            System.err.println("PianoInstrument: no MIDI channel.");
            return;
        }

        int midiNote = BASE_MIDI_NOTE + pitch.getMidiNumber();
        int vel = velocity.getValue();

        System.out.printf(
                "PianoInstrument: noteOn pitch=%d midiNote=%d vel=%d%n",
                pitch.getMidiNumber(), midiNote, vel);

        channel.noteOn(midiNote, vel);
    }

    @Override
    public void noteOff(Pitch pitch) {
        if (channel == null) {
            System.err.println("PianoInstrument: no MIDI channel.");
            return;
        }

        int midiNote = BASE_MIDI_NOTE + pitch.getMidiNumber();

        System.out.printf(
                "PianoInstrument: noteOff pitch=%d midiNote=%d%n",
                pitch.getMidiNumber(), midiNote);

        channel.noteOff(midiNote);
    }

    @Override
    public void close() {
        System.out.println("PianoInstrument: closing synth.");
        if (synth != null && synth.isOpen()) {
            synth.close();
        }
    }
}
