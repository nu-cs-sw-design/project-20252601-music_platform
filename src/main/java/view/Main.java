package view;

import controller.AppController;
import controller.engine.AudioEngine;
import model.Loop;
import model.LoopNote;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            AudioEngine audioEngine = new AudioEngine();

            Loop loop = new Loop(4);  // 4-measure loop, initially empty
//        loop.addNote(new LoopNote(0, 0.0, 1.0, 100));   // low row, first beat
//            loop.addNote(new LoopNote(5, 2.0, 0.5, 100));   // mid row, later in measure
//            loop.addNote(new LoopNote(11, 7.0, 2.0, 100));  // high row, later in loop


            AppController controller = new AppController(mainView, audioEngine, loop);
            controller.startApplication();
        });
    }
}
