package com.enclos.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.enclos.controller.State;
import com.enclos.data.Player;
import com.enclos.data.SimpleWriter;
import com.enclos.resources.song.Speaker;

public class Enclos extends JFrame {

    private State state = null;
    private ScoreFrame scoreFrame = null;
    private FrameContentPane contentPane = null;
    private final List<Board> boards = new LinkedList<Board>();
    private final List<Player> players = new ArrayList<>();

    public Enclos() {
        this.state = new State(this);
        this.scoreFrame = new ScoreFrame(this);
        Toolkit.getDefaultToolkit().setDynamicLayout(false);
        setTitle("Jeu de l'enclos");

        // � revoir
        // setMaximumSize(getScreenMaximumSize());
        // enabled tab listener
        setFocusTraversalKeysEnabled(false);
        setSize(500, 500);

        this.contentPane = new FrameContentPane(this);

        setContentPane(this.contentPane);
        this.boards.add(new Board(3L, 3));
        contentPane.addToGamePanel(boards.get(0));

        // SimpleReader reader = new SimpleReader("2014-03-28_00-14-17");
        // Map<String, Object> params = reader.read();
        //
        // // LOAD
        // long loadBoardSize = (long) params.get("Boardsize");
        // List<JSONArray> barriers = (List<JSONArray>) params.get("Barriers");
        // List<JSONArray> sheepPositions = (List<JSONArray>)
        // params.get("Sheepspositions");
        // Board loadBoard = new Board(loadBoardSize, sheepPositions.size());
        // this.boards.add(loadBoard);
        // contentPane.add(loadBoard);
        // loadBoard.setData(barriers, sheepPositions);
        // LOAD

        // contentPane.add(loadBoard);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        generateMenu();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                state.setSize(getSize(), true);
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

    private void generateMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.black);

        JMenu menu = new JMenu("Game");
        menu.setForeground(Color.white);
        final JMenuItem newGameItem = new JMenuItem("New Game");
        final JMenuItem scoreItem = new JMenuItem("Scores");
        final JMenuItem playerItem = new JMenuItem("Players");
        final JMenuItem saveItem = new JMenuItem("Save");
        final JMenuItem soundsItem = new JCheckBoxMenuItem("Play sounds", true);
        menu.add(newGameItem);
        menu.add(scoreItem);
        menu.add(playerItem);
        menu.add(saveItem);
        menu.add(soundsItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        newGameItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // on recup les param�tres du nouveau game
                Map<String, String> settings = NewGameForm.display();
                // si on a pas fait cancel
                if (settings != null) {
                    String boardSize = settings.get("boardSize");
                    // on cr�e le board qui va bien
                    Board newGame = new Board(Integer.valueOf(boardSize), 3);
                    boolean close = settings.get("close").equals("close") ? true : false;
                    // si on a decid� de close les autre jeux
                    if (close) {
                        // on jarte les autres jeux
                        Enclos.this.contentPane.resetGamePanel();
                        Enclos.this.contentPane.addToGamePanel(newGame);
                    } else {
                        // sinon on ajoute le jeu apr�s les autres
                        Enclos.this.contentPane.addToGamePanel(newGame);
                    }
                }

            }

        });

        playerItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Speaker.playClickEvent();
                PlayersFrame playersFrame = new PlayersFrame();
            }
        });

        saveItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                Date date = new Date();
                SimpleWriter writer = new SimpleWriter(boards.get(0), dateFormat.format(date));
                System.out.println(dateFormat.format(date));

                if (players.size() > 0) {
                    SimpleWriter playerWriter = new SimpleWriter(players, "players_test");
                }
            }
        });

        soundsItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Speaker.isMute(!soundsItem.isSelected());
            }
        });

    }

    private Dimension getScreenMaximumSize() {
        Dimension maxDimension = null;
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        if (screenDimension.getHeight() > screenDimension.getWidth())
            return new Dimension(screenDimension.width, screenDimension.width);
        else
            return new Dimension(screenDimension.height, screenDimension.height);
    }

    public List<Player> GetPlayers() {
        return this.players;
    }

}