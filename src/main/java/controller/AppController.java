package controller;

import controller.engine.AudioEngine;
import controller.engine.LoopSequencer;
import model.Loop;
import model.LoopNote;
import view.MainView;
import view.PianoRollListener;
import view.TransportListener;

public class AppController implements PianoRollListener, TransportListener {

    private final AudioEngine audioEngine;
    private final LoopSequencer loopSequencer;
    private final MainView mainView;
    private final Loop currentLoop;

    public AppController(MainView mainView, AudioEngine audioEngine, Loop loop) {
        this.mainView = mainView;
        this.audioEngine = audioEngine;
        this.currentLoop = loop;

        // For now, fix BPM = 120, beatsPerMeasure = 4
        this.loopSequencer = new LoopSequencer(audioEngine, 120.0, 4);

        // Wire controller as listeners
        this.mainView.setPianoRollListener(this);
        this.mainView.setTransportListener(this);
    }

    public void startApplication() {
        boolean success = audioEngine.initialize();

        if (success) {
            mainView.showMainScreen(currentLoop);
            mainView.setStatusMessage("System ready. Click on the grid to add notes, then Play.");
        } else {
            mainView.showAudioError();
        }
    }

    // --- PianoRollListener (editing) ---

    @Override
    public void onEmptyCellClicked(double beat, int pitchIndex) {
        int pitch = pitchIndex;            // later: map to MIDI pitch range
        double durationBeats = 1.0;        // default 1 beat
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
        mainView.setStatusMessage("Playing loop...");
        loopSequencer.play(currentLoop);
    }

    @Override
    public void onPauseRequested() {
        mainView.setStatusMessage("Playback paused.");
        loopSequencer.pause();
    }
}
