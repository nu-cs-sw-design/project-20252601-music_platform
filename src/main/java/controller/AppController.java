package controller;

import view.MainView;
import controller.engine.AudioEngine;

public class AppController {

    private final AudioEngine audioEngine;
    private final MainView mainView;

    public AppController(MainView mainView, AudioEngine audioEngine) {
        this.mainView = mainView;
        this.audioEngine = audioEngine;
    }

    /**
     * UC1: Start the System
     * 1. audioEngine.initialize()
     * 2. if success → mainView.showMainScreen()
     * 3. else → mainView.showAudioError()
     */
    public void startApplication() {
        boolean success = audioEngine.initialize();

        if (success) {
            mainView.showMainScreen();
        } else {
            mainView.showAudioError();
        }
    }
}
