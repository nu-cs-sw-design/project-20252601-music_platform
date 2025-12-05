package controller;

import controller.engine.AudioEngine;
import model.Loop;
import model.LoopNote;
import view.MainView;
import view.PianoRollListener;

public class AppController implements PianoRollListener {

    private final AudioEngine audioEngine;
    private final MainView mainView;
    private final Loop currentLoop;

    public AppController(MainView mainView, AudioEngine audioEngine, Loop loop) {
        this.mainView = mainView;
        this.audioEngine = audioEngine;
        this.currentLoop = loop;

        // Wire controller as the listener for piano roll interactions
        this.mainView.setPianoRollListener(this);
    }

    public void startApplication() {
        boolean success = audioEngine.initialize();

        if (success) {
            mainView.showMainScreen(currentLoop);
        } else {
            mainView.showAudioError();
        }
    }

    @Override
    public void onEmptyCellClicked(double beat, int pitchIndex) {
        // For now, treat pitchIndex as the pitch directly.
        int pitch = pitchIndex;
        double durationBeats = 1.0; // default 1 beat
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
}
