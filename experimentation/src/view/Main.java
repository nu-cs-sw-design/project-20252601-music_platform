package view;

import controller.AppController;
import engine.AudioEngine;

public class Main {

    public static void main(String[] args) {
        // 1. Create the view
        MainView mainView = new MainView();

        // 2. Create the audio engine (no instrument yet; it will create its own default piano)
        AudioEngine audioEngine = new AudioEngine();

        // 3. Create the controller wiring view + engine
        AppController appController = new AppController(mainView, audioEngine);

        // 4. Start UC1: Start the System
        appController.startApplication();
    }
}
