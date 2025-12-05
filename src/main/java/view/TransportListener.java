package view;

/**
 * Listener for transport controls like Play and Pause.
 */
public interface TransportListener {

    /**
     * User requested to start loop playback from the beginning.
     */
    void onPlayRequested();

    /**
     * User requested to pause/stop playback.
     */
    void onPauseRequested();
}
