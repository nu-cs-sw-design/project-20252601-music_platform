package view;

import model.LoopNote;

/**
 * Listener for high-level interactions in the PianoRollView.
 * Now supports:
 * - clicking an empty cell (add note)
 * - clicking on an existing note (remove or edit)
 */
public interface PianoRollListener {

    /**
     * Called when the user clicks on an empty cell in the grid.
     *
     * @param beat       the beat position within the loop (e.g., 0.0, 1.0, 2.5)
     * @param pitchIndex an index representing the pitch row (0 = lowest row)
     */
    void onEmptyCellClicked(double beat, int pitchIndex);

    /**
     * Called when the user clicks on an existing note block.
     *
     * @param note the note that was clicked
     */
    void onNoteClicked(LoopNote note);
}
