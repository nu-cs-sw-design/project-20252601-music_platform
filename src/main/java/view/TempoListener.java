package view;

/**
 * Listener for tempo changes requested by the user.
 */
public interface TempoListener {

    /**
     * Called when the user requests a tempo change via the UI.
     *
     * @param bpmText the raw text entered by the user (e.g. "120")
     */
    void onTempoChangeRequested(String bpmText);
}
