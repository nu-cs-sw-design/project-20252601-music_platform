package controller;

import controller.engine.AudioEngine;
import controller.engine.LoopSequencer;
import model.Loop;
import model.LoopNote;
import view.MainView;
import view.PianoRollListener;
import view.TransportListener;
import view.TempoListener;
import view.SaveLoopListener;

import model.persistence.LoopJsonStorage;

import java.io.IOException;
import java.nio.file.Paths;


public class AppController implements PianoRollListener, TransportListener, TempoListener, SaveLoopListener {

    private final AudioEngine audioEngine;
    private final LoopSequencer loopSequencer;
    private final MainView mainView;
    private final Loop currentLoop;
    private final LoopJsonStorage loopStorage;


    public AppController(MainView mainView, AudioEngine audioEngine, Loop loop) {
        this.mainView = mainView;
        this.audioEngine = audioEngine;
        this.currentLoop = loop;

        // 4 beats per measure for now
        this.loopSequencer = new LoopSequencer(audioEngine, 4);

        // Save loops into a "loops" directory in the project working dir
        this.loopStorage = new LoopJsonStorage(Paths.get("loops"));

        // Wire listeners
        this.mainView.setPianoRollListener(this);
        this.mainView.setTransportListener(this);
        this.mainView.setTempoListener(this);
        this.mainView.setSaveLoopListener(this);
    }


    public void startApplication() {
        boolean success = audioEngine.initialize();

        if (success) {
            mainView.showMainScreen(currentLoop);
            mainView.setStatusMessage("System ready. Add notes, set tempo, then Play or Save.");
        } else {
            mainView.showAudioError();
        }
    }

    // --- PianoRollListener (editing) ---

    @Override
    public void onEmptyCellClicked(double beat, int pitchIndex) {
        if (loopSequencer.isPlaying()) {
            mainView.setStatusMessage("Stop playback before saving the loop.");
            return;
        }
        int pitch = pitchIndex;            // simple mapping for now
        double durationBeats = 1.0;        // 1 beat
        int velocity = 100;

        LoopNote note = new LoopNote(pitch, beat, durationBeats, velocity);
        currentLoop.addNote(note);

        mainView.refreshPianoRoll();
    }

    @Override
    public void onNoteClicked(LoopNote note) {
        if (loopSequencer.isPlaying()) {
            mainView.setStatusMessage("Stop playback before saving the loop.");
            return;
        }
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

        currentLoop.setTempoBPM(bpm);
        double effectiveBpm = currentLoop.getTempoBPM();

        mainView.setTempoDisplay(effectiveBpm);
        mainView.setStatusMessage("Tempo set to " +
                String.format("%.1f", effectiveBpm) + " BPM.");
    }

    // --- SaveLoopListener (UC6: save loop) ---

    @Override
    public void onSaveLoopRequested() {
        // You could forbid saving while playing if you want; for now we allow it.

        if (loopSequencer.isPlaying()) {
            mainView.setStatusMessage("Stop playback before saving the loop.");
            return;
        }

        String fileName = "loop-" + System.currentTimeMillis() + ".json";

        try {
            loopStorage.saveLoop(currentLoop, fileName);
            mainView.setStatusMessage("Loop saved as " + fileName + " in /loops.");
        } catch (IOException e) {
            System.err.println("AppController: failed to save loop: " + e.getMessage());
            mainView.setStatusMessage("Failed to save loop. See console for details.");
        }
    }

}
