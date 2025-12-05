package controller;

import controller.engine.AudioEngine;
import model.Loop;
import view.MainView;

public class AppController {

    private final AudioEngine audioEngine;
    private final MainView mainView;
    private final Loop currentLoop;

    public AppController(MainView mainView, AudioEngine audioEngine, Loop loop) {
        this.mainView = mainView;
        this.audioEngine = audioEngine;
        this.currentLoop = loop;
    }

    /**
     * UC1: Start the System
     */
    public void startApplication() {
        boolean success = audioEngine.initialize();

        if (success) {
            // Pass the loop into the view
            mainView.showMainScreen(currentLoop);
        } else {
            mainView.showAudioError();
        }
    }
}
