package com.enclos.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import com.enclos.component.EnclosMenu;
import com.enclos.data.Player;
import com.enclos.data.SimpleReader;

public class Enclos extends JFrame {

    // private final State state = null;
    private ScoreFrame scoreFrame = null;
    private FrameContentPane contentPane = null;
    private final List<Board> boards = new LinkedList<Board>();
    private final List<Player> players;

    public Enclos() {
        // si jamais on veut utiliser le principe de la fenêtre carrée
        // this.state = new State(this);
        this.scoreFrame = new ScoreFrame(this);
        Toolkit.getDefaultToolkit().setDynamicLayout(false);
        setTitle("Jeu de l'enclos");

        this.players = SimpleReader.readPlayer("players_test");

        // enabled tab listener
        setFocusTraversalKeysEnabled(false);
        setSize(500, 500);

        this.contentPane = new FrameContentPane(this);

        setContentPane(this.contentPane);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        EnclosMenu menu = new EnclosMenu(this);
        this.setJMenuBar(menu);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                // state.setSize(getSize(), true);
                scoreFrame.setSize(getSize());
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    if (!scoreFrame.isVisible()) {
                        scoreFrame.setLocation(getLocation());
                        scoreFrame.setVisible(true);
                        requestFocus();
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    System.out.println("pressed");
                    Enclos.this.contentPane.switchPanel();
                    Enclos.this.contentPane.getPlayersGrid().setSelectable(false);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    scoreFrame.setVisible(false);

                    System.out.println("released");
                }
            }
        });

        setVisible(true);
    }

    private Dimension getScreenMaximumSize() {
        Dimension maxDimension = null;
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        if (screenDimension.getHeight() > screenDimension.getWidth())
            return new Dimension(screenDimension.width, screenDimension.width);
        else
            return new Dimension(screenDimension.height, screenDimension.height);
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public FrameContentPane getFrameContentPane() {
        return this.contentPane;
    }

    public List<Board> getBoards() {
        return this.boards;
    }

}