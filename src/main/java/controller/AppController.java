package controller;

import controller.engine.AudioEngine;
import controller.engine.LoopSequencer;
import model.*;
import model.persistence.LoopStorage;
import view.MainView;
import view.PianoRollListener;
import view.TransportListener;
import view.TempoListener;
import view.SaveLoopListener;

import java.io.IOException;

public class AppController implements PianoRollListener, TransportListener, TempoListener, SaveLoopListener {

    private final AudioEngine audioEngine;
    private final LoopSequencer loopSequencer;
    private final MainView mainView;
    private final Loop currentLoop;
    private final LoopStorage loopStorage;

    public AppController(MainView mainView,
                         AudioEngine audioEngine,
                         Loop loop,
                         LoopStorage loopStorage) {
        this.mainView = mainView;
        this.audioEngine = audioEngine;
        this.currentLoop = loop;
        this.loopStorage = loopStorage;

        // 4 beats per measure for now
        this.loopSequencer = new LoopSequencer(audioEngine, 4);

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

        // Map UI grid to domain concepts
        Pitch pitch = new Pitch(pitchIndex);                 // still a simple mapping
        BeatPosition startBeat = new BeatPosition(beat);
        BeatDuration durationBeats = new BeatDuration(1.0);  // 1 beat for now
        Velocity velocity = new Velocity(100);               // fixed velocity for now

        LoopNote note = new LoopNote(pitch, startBeat, durationBeats, velocity);
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
                String.format("%.1f", currentLoop.getTempo().getBpm()) + " BPM...");
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

        Tempo newTempo = new Tempo(bpm);
        currentLoop.setTempo(newTempo);

        double effectiveBpm = newTempo.getBpm();
        mainView.setTempoDisplay(effectiveBpm);
        mainView.setStatusMessage("Tempo set to " +
                String.format("%.1f", effectiveBpm) + " BPM.");
    }


    // --- SaveLoopListener (UC6: save loop) ---

    @Override
    public void onSaveLoopRequested() {
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
