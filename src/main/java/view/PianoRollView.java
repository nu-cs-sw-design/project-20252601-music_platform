package view;

import model.Loop;

import javax.swing.*;
import java.awt.*;

public class PianoRollView extends JPanel {

    private int measures = 4;
    private int beatsPerMeasure = 4;
    private int stepsPerBeat = 4;
    private int numPitches = 12;

    private Loop loop;  // current loop to eventually render

    private static final int PADDING_LEFT = 40;
    private static final int PADDING_RIGHT = 20;
    private static final int PADDING_TOP = 20;
    private static final int PADDING_BOTTOM = 30;

    public PianoRollView() {
        setBackground(Color.DARK_GRAY);
        setPreferredSize(new Dimension(800, 300));
    }

    public void setMeasures(int measures) {
        if (measures <= 0) return;
        this.measures = measures;
        repaint();
    }

    public void setLoop(Loop loop) {
        this.loop = loop;
        // For now we still just draw the empty grid;
        // later we'll use loop.getNotes() inside paintComponent.
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid((Graphics2D) g);
        // later: drawNotes(g2, loop);
    }

    private void drawGrid(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int gridX = PADDING_LEFT;
        int gridY = PADDING_TOP;
        int gridWidth = width - PADDING_LEFT - PADDING_RIGHT;
        int gridHeight = height - PADDING_TOP - PADDING_BOTTOM;

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
}
