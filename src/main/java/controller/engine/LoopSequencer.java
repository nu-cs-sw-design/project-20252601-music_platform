package controller.engine;

import model.Loop;
import model.LoopNote;

import java.util.HashSet;
import java.util.Set;

/**
 * Plays a Loop in time by calling AudioEngine.noteOn / noteOff
 * according to note start times and durations.
 *
 * - Uses a background thread for playback.
 * - Uses BPM to compute beat durations.
 * - Loops continuously until paused.
 */
public class LoopSequencer {

    private final AudioEngine audioEngine;
    private final double bpm;
    private final int beatsPerMeasure;

    private volatile boolean playing = false;
    private Thread playbackThread;

    public LoopSequencer(AudioEngine audioEngine, double bpm, int beatsPerMeasure) {
        this.audioEngine = audioEngine;
        this.bpm = bpm;
        this.beatsPerMeasure = beatsPerMeasure;
    }

    /**
     * Start playback of the given loop from the beginning.
     * If already playing, this call is ignored.
     */
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

    /**
     * Stop playback. Next play() starts from the beginning.
     */
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

    private void runPlaybackLoop(Loop loop) {
        final double beatDurationMs = 60000.0 / bpm; // 1 beat = this many ms
        final int totalBeatsInLoop = loop.getMeasures() * beatsPerMeasure;

        System.out.println("LoopSequencer: starting playback at " + bpm + " BPM");

        while (playing) {
            long loopStartNs = System.nanoTime();
            Set<Integer> activePitches = new HashSet<>();

            for (LoopNote note : loop.getNotes()) {
                if (!playing) {
                    break;
                }

                long noteOnDelayMs = (long) Math.round(note.getStartBeat() * beatDurationMs);
                long noteOffDelayMs = (long) Math.round(
                        (note.getStartBeat() + note.getDurationBeats()) * beatDurationMs
                );

                long noteOnTimeNs = loopStartNs + noteOnDelayMs * 1_000_000L;
                long noteOffTimeNs = loopStartNs + noteOffDelayMs * 1_000_000L;

                // Wait until noteOnTime
                sleepUntil(noteOnTimeNs);
                if (!playing) {
                    break;
                }

                int pitch = note.getPitch();
                int velocity = note.getVelocity();

                System.out.println("LoopSequencer: noteOn pitch=" + pitch +
                        " vel=" + velocity + " at beat=" + note.getStartBeat());
                audioEngine.noteOn(pitch, velocity);
                activePitches.add(pitch);

                // Wait until noteOffTime
                sleepUntil(noteOffTimeNs);
                if (!playing) {
                    break;
                }

                System.out.println("LoopSequencer: noteOff pitch=" + pitch);
                audioEngine.noteOff(pitch);
                activePitches.remove(pitch);
            }

            // Ensure any leftover notes are turned off
            for (int pitch : activePitches) {
                audioEngine.noteOff(pitch);
            }
            activePitches.clear();

            if (!playing) {
                break;
            }

            long loopDurationMs = (long) Math.round(totalBeatsInLoop * beatDurationMs);
            long loopEndTimeNs = loopStartNs + loopDurationMs * 1_000_000L;
            sleepUntil(loopEndTimeNs);

            // Then repeat from the top
        }

        System.out.println("LoopSequencer: playback loop thread exiting.");
    }

    /**
     * Sleep until the given target time (ns since some fixed point).
     */
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
        // Right now AudioEngine doesn't track active notes,
        // so there is nothing specific to do here. If you later
        // add a "panic" or "allNotesOff" to AudioEngine, call it here.
    }
}
