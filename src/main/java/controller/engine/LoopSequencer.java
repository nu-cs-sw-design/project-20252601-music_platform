package controller.engine;

import model.Loop;
import model.LoopNote;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.*;

public class LoopSequencer {

    private final AudioEngine audioEngine;
    private final int beatsPerMeasure;

    private volatile boolean playing = false;
    private Thread playbackThread;

    public LoopSequencer(AudioEngine audioEngine, int beatsPerMeasure) {
        this.audioEngine = audioEngine;
        this.beatsPerMeasure = beatsPerMeasure;
    }

    public synchronized void play(Loop loop) {
        if (playing) {
            System.out.println("LoopSequencer: already playing, ignoring play() request.");
            return;
        }
        if (loop == null) {
            System.out.println("LoopSequencer: loop is null, cannot play.");
            return;
        }

        playing = true;

        playbackThread = new Thread(() -> runPlaybackLoop(loop), "LoopSequencer-Playback");
        playbackThread.setDaemon(true);
        playbackThread.start();
    }

    public synchronized void pause() {
        if (!playing) {
            return;
        }
        playing = false;
        if (playbackThread != null) {
            playbackThread.interrupt();
        }
        stopAllNotes();
        System.out.println("LoopSequencer: playback paused.");
    }

    /** Public read-only access so controller can enforce UC5. */
    public boolean isPlaying() {
        return playing;
    }

// inside LoopSequencer

    private static class NoteEvent {
        final double beat;
        final boolean isNoteOn;
        final LoopNote note;

        NoteEvent(double beat, boolean isNoteOn, LoopNote note) {
            this.beat = beat;
            this.isNoteOn = isNoteOn;
            this.note = note;
        }
    }

    private void runPlaybackLoop(Loop loop) {
        double bpm = loop.getTempoBPM();
        final double beatDurationMs = 60000.0 / bpm;
        final int totalBeatsInLoop = loop.getMeasures() * beatsPerMeasure;

        // Build event list (note-on and note-off)
        List<NoteEvent> events = new ArrayList<>();
        for (LoopNote note : loop.getNotes()) {
            double startBeat = note.getStartBeat().getValue();
            double endBeat = startBeat + note.getDurationBeats().getValue();

            events.add(new NoteEvent(startBeat, true, note));   // NOTE_ON
            events.add(new NoteEvent(endBeat, false, note));    // NOTE_OFF
        }

        // Sort by beat time; if times equal, do NOTE_ONs first (or NOTE_OFFs, up to you)
        events.sort((e1, e2) -> {
            int cmp = Double.compare(e1.beat, e2.beat);
            if (cmp != 0) return cmp;
            // ensure a deterministic order at same beat
            if (e1.isNoteOn == e2.isNoteOn) return 0;
            return e1.isNoteOn ? -1 : 1; // NOTE_ON before NOTE_OFF at same time
        });

        System.out.println("LoopSequencer: starting playback at " + bpm + " BPM");

        while (playing) {
            long loopStartNs = System.nanoTime();
            Set<Integer> activePitches = new HashSet<>();

            for (NoteEvent event : events) {
                if (!playing) break;

                long eventDelayMs = Math.round(event.beat * beatDurationMs);
                long eventTimeNs = loopStartNs + eventDelayMs * 1_000_000L;

                sleepUntil(eventTimeNs);
                if (!playing) break;

                int pitch = event.note.getPitch().getMidiNumber();
                int velocity = event.note.getVelocity().getValue();

                if (event.isNoteOn) {
                    System.out.println("LoopSequencer: noteOn pitch=" + pitch +
                            " vel=" + velocity + " at beat=" + event.beat);
                    audioEngine.noteOn(pitch, velocity);
                    activePitches.add(pitch);
                } else {
                    System.out.println("LoopSequencer: noteOff pitch=" + pitch +
                            " at beat=" + event.beat);
                    audioEngine.noteOff(pitch);
                    activePitches.remove(pitch);
                }
            }

            // turn off any leftovers (in case we stopped early)
            for (int pitch : activePitches) {
                audioEngine.noteOff(pitch);
            }
            activePitches.clear();

            if (!playing) break;

            long loopDurationMs = Math.round(totalBeatsInLoop * beatDurationMs);
            long loopEndTimeNs = loopStartNs + loopDurationMs * 1_000_000L;
            sleepUntil(loopEndTimeNs);
            // then loop again from the top
        }

        System.out.println("LoopSequencer: playback loop thread exiting.");
    }


    private void sleepUntil(long targetTimeNs) {
        while (playing) {
            long nowNs = System.nanoTime();
            long remainingNs = targetTimeNs - nowNs;
            if (remainingNs <= 0) {
                break;
            }

            long remainingMs = remainingNs / 1_000_000L;
            if (remainingMs > 1) {
                try {
                    Thread.sleep(remainingMs - 1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                Thread.yield();
            }
        }
    }

    private void stopAllNotes() {
        // hook for future "all notes off"
    }
}
