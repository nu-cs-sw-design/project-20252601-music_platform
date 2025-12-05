package controller;

import view.MainView;
import controller.engine.AudioEngine;
import model.Loop;

public class AppController {

    private final AudioEngine audioEngine;
    private final MainView mainView;
    private final Loop currentLoop;

    public AppController(MainView mainView, AudioEngine audioEngine) {
        this.mainView = mainView;
        this.audioEngine = audioEngine;
        this.currentLoop = new Loop(4);
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
