package view;

import model.Loop;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {

    private JLabel statusLabel;
    private PianoRollView pianoRollView;
    private Loop currentLoop;

    private TransportListener transportListener;

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

        // --- Bottom transport bar (Play / Pause) ---
        JPanel transportPanel = new JPanel();
        transportPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton playButton = new JButton("Play");
        JButton pauseButton = new JButton("Pause");

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

        transportPanel.add(playButton);
        transportPanel.add(pauseButton);

        add(transportPanel, BorderLayout.SOUTH);
    }

    public void showMainScreen(Loop loop) {
        this.currentLoop = loop;
        pianoRollView.setLoop(loop);

        statusLabel.setText("System ready. Main screen displayed.");
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
        pianoRollView.setPianoRollListener(listener);
    }

    public void refreshPianoRoll() {
        pianoRollView.repaint();
    }

    public void setTransportListener(TransportListener transportListener) {
        this.transportListener = transportListener;
    }

    public void setStatusMessage(String message) {
        statusLabel.setText(message);
    }
}
