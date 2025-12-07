package view;

import model.Loop;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {

    private JLabel statusLabel;
    private PianoRollView pianoRollView;
    private Loop currentLoop;

    private TransportListener transportListener;
    private PianoRollListener pianoRollListener; // optional, but okay to keep
    private TempoListener tempoListener;
    private SaveLoopListener saveLoopListener;

    private JTextField tempoField;

    public MainView() {
        super("LoopSketch");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        pianoRollView = new PianoRollView();
        setLayout(new BorderLayout());

        // --- Header (title + status) ---
        JPanel header = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("LoopSketch", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24f));

        statusLabel = new JLabel("Initializing audio...", SwingConstants.CENTER);
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.PLAIN, 14f));

        header.add(titleLabel, BorderLayout.NORTH);
        header.add(statusLabel, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);

        // --- Center (piano roll) ---
        add(pianoRollView, BorderLayout.CENTER);

        // --- Bottom panel: transport + save + tempo ---
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Transport + Save (left side)
        JPanel transportPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton playButton = new JButton("Play");
        JButton pauseButton = new JButton("Pause");
        JButton saveButton = new JButton("Save Loop");

        playButton.addActionListener(e -> {
            if (transportListener != null) {
                transportListener.onPlayRequested();
            }
        });

        pauseButton.addActionListener(e -> {
            if (transportListener != null) {
                transportListener.onPauseRequested();
            }
        });

        saveButton.addActionListener(e -> {
            if (saveLoopListener != null) {
                saveLoopListener.onSaveLoopRequested();
            }
        });

        transportPanel.add(playButton);
        transportPanel.add(pauseButton);
        transportPanel.add(saveButton);

        bottomPanel.add(transportPanel, BorderLayout.WEST);

        // Tempo controls (right side)
        JPanel tempoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel tempoLabel = new JLabel("Tempo (BPM):");
        tempoField = new JTextField(5);

        JButton setTempoButton = new JButton("Set");
        setTempoButton.addActionListener(e -> {
            if (tempoListener != null) {
                String text = tempoField.getText();
                tempoListener.onTempoChangeRequested(text);
            }
        });

        tempoPanel.add(tempoLabel);
        tempoPanel.add(tempoField);
        tempoPanel.add(setTempoButton);

        bottomPanel.add(tempoPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void showMainScreen(Loop loop) {
        this.currentLoop = loop;
        pianoRollView.setLoop(loop);

        // Initialize tempo display from model
        setTempoDisplay(loop.getTempoBPM());

        statusLabel.setText("System ready. Add notes, set tempo, then Play.");
        if (!isVisible()) {
            setVisible(true);
        }
    }

    public void showAudioError() {
        if (!isVisible()) {
            setVisible(true);
        }

        statusLabel.setText("Audio initialization failed.");

        JOptionPane.showMessageDialog(
                this,
                "Unable to initialize the audio system.\n" +
                        "Please check your audio configuration and restart LoopSketch.",
                "Audio Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void setPianoRollListener(PianoRollListener listener) {
        this.pianoRollListener = listener;
        pianoRollView.setPianoRollListener(listener);
    }

    public void setTransportListener(TransportListener transportListener) {
        this.transportListener = transportListener;
    }

    public void setTempoListener(TempoListener tempoListener) {
        this.tempoListener = tempoListener;
    }

    public void setSaveLoopListener(SaveLoopListener saveLoopListener) {
        this.saveLoopListener = saveLoopListener;
    }

    public void refreshPianoRoll() {
        pianoRollView.repaint();
    }

    public void setStatusMessage(String message) {
        statusLabel.setText(message);
    }

    /**
     * Updates the tempo text field to show the current BPM.
     */
    public void setTempoDisplay(double bpm) {
        if (tempoField != null) {
            tempoField.setText(String.format("%.1f", bpm));
        }
    }
}
