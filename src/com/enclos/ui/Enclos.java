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

import com.enclos.component.EnclosMenu;
import com.enclos.controller.State;
import com.enclos.data.Player;
import com.enclos.data.SimpleReader;
import com.enclos.data.SimpleWriter;
import com.enclos.resources.song.Speaker;

public class Enclos extends JFrame {

	private State state = null;
	private ScoreFrame scoreFrame = null;
	private FrameContentPane contentPane = null;
	private final List<Board> boards = new LinkedList<Board>();
	private List<Player> players;

	public Enclos() {
		// this.state = new State(this);
		this.scoreFrame = new ScoreFrame(this);
		Toolkit.getDefaultToolkit().setDynamicLayout(false);
		setTitle("Jeu de l'enclos");

		this.players = SimpleReader.readPlayer("players_test");
		// a revoir
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