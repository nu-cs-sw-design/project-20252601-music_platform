package view;

import controller.AppController;
import controller.engine.AudioEngine;
import model.Loop;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            AudioEngine audioEngine = new AudioEngine();

            Loop loop = new Loop(4);  // 4-measure loop, initially empty

            AppController controller = new AppController(mainView, audioEngine, loop);
            controller.startApplication();
        });
    }
}
