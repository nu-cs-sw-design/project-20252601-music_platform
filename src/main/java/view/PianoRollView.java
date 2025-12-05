package view;

import model.Loop;
import model.LoopNote;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PianoRollView extends JPanel {

    private int measures = 4;
    private int beatsPerMeasure = 4;
    private int stepsPerBeat = 4;
    private int numPitches = 12;

    private Loop loop;  // current loop to render
    private PianoRollListener listener;

    private static final int PADDING_LEFT = 40;
    private static final int PADDING_RIGHT = 20;
    private static final int PADDING_TOP = 20;
    private static final int PADDING_BOTTOM = 30;

    public PianoRollView() {
        setBackground(Color.DARK_GRAY);
        setPreferredSize(new Dimension(800, 300));

        // Install a mouse listener to detect clicks on the grid
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });
    }

    public void setMeasures(int measures) {
        if (measures <= 0) return;
        this.measures = measures;
        repaint();
    }

    public void setLoop(Loop loop) {
        this.loop = loop;
        repaint();
    }

    public void setPianoRollListener(PianoRollListener listener) {
        this.listener = listener;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int gridX = PADDING_LEFT;
        int gridY = PADDING_TOP;
        int gridWidth = width - PADDING_LEFT - PADDING_RIGHT;
        int gridHeight = height - PADDING_TOP - PADDING_BOTTOM;

        drawGrid(g2, gridX, gridY, gridWidth, gridHeight);

        if (loop != null) {
            drawNotes(g2, gridX, gridY, gridWidth, gridHeight);
        }
    }

    private void drawGrid(Graphics2D g2, int gridX, int gridY, int gridWidth, int gridHeight) {
        g2.setColor(new Color(40, 40, 40));
        g2.fillRect(gridX, gridY, gridWidth, gridHeight);

        int totalSteps = measures * beatsPerMeasure * stepsPerBeat;
        double stepWidth = (double) gridWidth / totalSteps;
        double rowHeight = (double) gridHeight / numPitches;

        // vertical time lines
        for (int step = 0; step <= totalSteps; step++) {
            int x = gridX + (int) Math.round(step * stepWidth);

            boolean isMeasureLine = (step % (beatsPerMeasure * stepsPerBeat) == 0);
            boolean isBeatLine = (step % stepsPerBeat == 0);

            if (isMeasureLine) {
                g2.setColor(new Color(220, 220, 220));
                g2.setStroke(new BasicStroke(2f));
            } else if (isBeatLine) {
                g2.setColor(new Color(120, 120, 120));
                g2.setStroke(new BasicStroke(1.5f));
            } else {
                g2.setColor(new Color(80, 80, 80));
                g2.setStroke(new BasicStroke(1f));
            }

            g2.drawLine(x, gridY, x, gridY + gridHeight);
        }

        // horizontal pitch lines
        for (int row = 0; row <= numPitches; row++) {
            int y = gridY + (int) Math.round(row * rowHeight);
            g2.setColor(new Color(90, 90, 90));
            g2.setStroke(new BasicStroke(1f));
            g2.drawLine(gridX, y, gridX + gridWidth, y);
        }

        // border
        g2.setColor(new Color(230, 230, 230));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRect(gridX, gridY, gridWidth, gridHeight);

        // measure labels
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12f));
        g2.setColor(new Color(200, 200, 200));

        for (int m = 0; m < measures; m++) {
            int measureStartStep = m * beatsPerMeasure * stepsPerBeat;
            int x = gridX + (int) Math.round(measureStartStep * stepWidth);
            String label = "M" + (m + 1);
            g2.drawString(label, x + 4, gridY + gridHeight + 18);
        }
    }

    private void drawNotes(Graphics2D g2, int gridX, int gridY, int gridWidth, int gridHeight) {
        if (loop.getNotes().isEmpty()) {
            return;
        }

        int totalBeats = measures * beatsPerMeasure;
        double beatWidth = (double) gridWidth / totalBeats;
        double rowHeight = (double) gridHeight / numPitches;

        for (LoopNote note : loop.getNotes()) {
            Rectangle rect = computeNoteRect(note, gridX, gridY, gridWidth, gridHeight,
                    totalBeats, beatWidth, rowHeight);

            g2.setColor(new Color(0, 180, 255));
            g2.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 6, 6);

            g2.setColor(new Color(0, 80, 140));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 6, 6);
        }
    }

    private Rectangle computeNoteRect(LoopNote note,
                                      int gridX, int gridY,
                                      int gridWidth, int gridHeight,
                                      int totalBeats, double beatWidth, double rowHeight) {

        double startBeat = note.getStartBeat();
        double durationBeats = note.getDurationBeats();
        int pitch = note.getPitch();

        int pitchIndex = Math.floorMod(pitch, numPitches);

        int x = gridX + (int) Math.round(startBeat * beatWidth);
        int w = (int) Math.max(4, Math.round(durationBeats * beatWidth));

        int rowFromTop = (numPitches - 1) - pitchIndex;
        int y = gridY + (int) Math.round(rowFromTop * rowHeight);
        int h = (int) Math.max(4, Math.round(rowHeight) - 2);

        return new Rectangle(x, y, w, h);
    }

    private void handleClick(int mouseX, int mouseY) {
        if (listener == null) {
            return;
        }

        int width = getWidth();
        int height = getHeight();

        int gridX = PADDING_LEFT;
        int gridY = PADDING_TOP;
        int gridWidth = width - PADDING_LEFT - PADDING_RIGHT;
        int gridHeight = height - PADDING_TOP - PADDING_BOTTOM;

        // Check if click is inside the grid area
        if (mouseX < gridX || mouseX > gridX + gridWidth ||
                mouseY < gridY || mouseY > gridY + gridHeight) {
            return;
        }

        int totalBeats = measures * beatsPerMeasure;
        double beatWidth = (double) gridWidth / totalBeats;
        double rowHeight = (double) gridHeight / numPitches;

        // 1) First, see if the click hit an existing note
        if (loop != null) {
            for (LoopNote note : loop.getNotes()) {
                Rectangle rect = computeNoteRect(note, gridX, gridY,
                        gridWidth, gridHeight,
                        totalBeats, beatWidth, rowHeight);
                if (rect.contains(mouseX, mouseY)) {
                    listener.onNoteClicked(note);
                    return; // do not treat as empty cell
                }
            }
        }

        // 2) Otherwise, treat as an empty cell click → add a note
        double beat = (mouseX - gridX) / beatWidth;
        // Snap beat to nearest 0.5 (half-beat) for now
        beat = Math.round(beat * 2.0) / 2.0;

        double yOffset = mouseY - gridY;
        int rowFromTop = (int) Math.floor(yOffset / rowHeight);
        int pitchIndex = (numPitches - 1) - rowFromTop; // 0 = lowest row

        if (pitchIndex < 0 || pitchIndex >= numPitches) {
            return;
        }

        listener.onEmptyCellClicked(beat, pitchIndex);
    }
}
