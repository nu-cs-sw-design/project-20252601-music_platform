package view;

public class MainView {

    public MainView() {
        // TODO: setup UI framework here
    }

    public void showMainScreen() {
        // For now, just print. Later this could create/show your main window.
        System.out.println("LoopSketch is ready. Main screen displayed.");
    }

    public void showAudioError() {
        // For now, just print. Later this could be a dialog or error view.
        System.err.println("Error: Audio system could not be initialized.");
    }
}
