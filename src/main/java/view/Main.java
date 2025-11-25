package view;

import controller.AppController;
import controller.engine.AudioEngine;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        // Make sure UI is created on the Event Dispatch Thread (Swing best practice)
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            AudioEngine audioEngine = new AudioEngine();
            AppController controller = new AppController(mainView, audioEngine);
            controller.startApplication();   // UC1: Start the System
        });
    }
}
