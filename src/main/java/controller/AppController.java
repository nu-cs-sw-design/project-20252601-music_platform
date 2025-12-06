package controller;

import controller.engine.AudioEngine;
import controller.engine.LoopSequencer;
import model.Loop;
import model.LoopNote;
import view.MainView;
import view.PianoRollListener;
import view.TransportListener;
import view.TempoListener;

public class AppController implements PianoRollListener, TransportListener, TempoListener {

    private final AudioEngine audioEngine;
    private final LoopSequencer loopSequencer;
    private final MainView mainView;
    private final Loop currentLoop;

    public AppController(MainView mainView, AudioEngine audioEngine, Loop loop) {
        this.mainView = mainView;
        this.audioEngine = audioEngine;
        this.currentLoop = loop;

        // 4 beats per measure for now
        this.loopSequencer = new LoopSequencer(audioEngine, 4);

        // Wire listeners
        this.mainView.setPianoRollListener(this);
        this.mainView.setTransportListener(this);
        this.mainView.setTempoListener(this);
    }

    public void startApplication() {
        boolean success = audioEngine.initialize();

        if (success) {
            mainView.showMainScreen(currentLoop);
            mainView.setStatusMessage("System ready. Click to add notes, set tempo, then Play.");
        } else {
            mainView.showAudioError();
        }
    }

    // --- PianoRollListener (editing) ---

    @Override
    public void onEmptyCellClicked(double beat, int pitchIndex) {
        int pitch = pitchIndex;            // simple mapping for now
        double durationBeats = 1.0;        // 1 beat
        int velocity = 100;

        LoopNote note = new LoopNote(pitch, beat, durationBeats, velocity);
        currentLoop.addNote(note);

        mainView.refreshPianoRoll();
    }

    @Override
    public void onNoteClicked(LoopNote note) {
        currentLoop.removeNote(note);
        mainView.refreshPianoRoll();
    }

    // --- TransportListener (playback) ---

    @Override
    public void onPlayRequested() {
        mainView.setStatusMessage("Playing loop at " +
                String.format("%.1f", currentLoop.getTempoBPM()) + " BPM...");
        loopSequencer.play(currentLoop);
    }

    @Override
    public void onPauseRequested() {
        loopSequencer.pause();
        mainView.setStatusMessage("Playback paused.");
    }

    // --- TempoListener (UC5: change tempo) ---

    @Override
    public void onTempoChangeRequested(String bpmText) {
        // Enforce: Only possible if loop is stopped
        if (loopSequencer.isPlaying()) {
            mainView.setStatusMessage("Stop playback before changing tempo.");
            return;
        }

        double bpm;
        try {
            bpm = Double.parseDouble(bpmText.trim());
        } catch (NumberFormatException e) {
            mainView.setStatusMessage("Invalid tempo. Please enter a number.");
            return;
        }

        // Let the model clamp to allowed range (40–240)
        currentLoop.setTempoBPM(bpm);
        double effectiveBpm = currentLoop.getTempoBPM();

        // Reflect clamped value back into the UI
        mainView.setTempoDisplay(effectiveBpm);
        mainView.setStatusMessage("Tempo set to " +
                String.format("%.1f", effectiveBpm) + " BPM.");
    }
}
