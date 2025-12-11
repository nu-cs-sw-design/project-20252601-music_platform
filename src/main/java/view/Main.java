package view;

import controller.AppController;
import controller.engine.AudioEngine;
import model.Loop;
import model.persistence.LoopJsonStorage;
import model.persistence.LoopStorage;

import javax.swing.*;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            AudioEngine audioEngine = new AudioEngine();

            Loop loop = new Loop(4);  // 4-measure loop, initially empty

            // Composition root: choose the persistence implementation here
            LoopStorage loopStorage = new LoopJsonStorage(Paths.get("loops"));

            AppController controller = new AppController(mainView, audioEngine, loop, loopStorage);
            controller.startApplication();
        });
    }
}
