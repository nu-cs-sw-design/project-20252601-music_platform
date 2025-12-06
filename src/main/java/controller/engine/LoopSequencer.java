package controller.engine;

import model.Loop;
import model.LoopNote;

import java.util.HashSet;
import java.util.Set;

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

    private void runPlaybackLoop(Loop loop) {
        double bpm = loop.getTempoBPM();
        final double beatDurationMs = 60000.0 / bpm;
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

                sleepUntil(noteOffTimeNs);
                if (!playing) {
                    break;
                }

                System.out.println("LoopSequencer: noteOff pitch=" + pitch);
                audioEngine.noteOff(pitch);
                activePitches.remove(pitch);
            }

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
