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
	private IntroFrame introFrame = null;
	private FrameContentPane contentPane = null;
	private final List<Board> boards = new LinkedList<Board>();
	private final List<Player> players;

	public Enclos() {
		// si jamais on veut utiliser le principe de la fen�tre carr�e
		// this.state = new State(this);
		this.scoreFrame = new ScoreFrame(this);

		Toolkit.getDefaultToolkit().setDynamicLayout(false);
		setTitle("Jeu de l'enclos");

		this.players = SimpleReader.readPlayer("players");

		// enabled tab listener
		setFocusTraversalKeysEnabled(false);
		setSize(1200, 700);

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
				// introFrame.setSize(getSize());
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
					Enclos.this.contentPane.goToPlayersPanel();
					Enclos.this.contentPane.setPlayersPanelSelectable(false);
				}
				if (e.getKeyCode() == KeyEvent.VK_G) {
					Enclos.this.contentPane.goToGamePanel();
					Enclos.this.contentPane.setPlayersPanelSelectable(false);
				}
				if (e.getKeyCode() == KeyEvent.VK_S) {
					Enclos.this.contentPane.goToScorePanel();
					Enclos.this.contentPane.setPlayersPanelSelectable(false);
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					Enclos.this.contentPane.displayNextGame();
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					Enclos.this.contentPane.displayPreviousGame();
				}

				if ((e.getKeyCode() == KeyEvent.VK_Z) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					Board targetBoard = Enclos.this.contentPane.getDisplayedBoard();
					if (targetBoard != null) {
						targetBoard.cancelAction();
					}
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

		 this.introFrame = new IntroFrame(this);
		 this.introFrame.setLocation(getLocation());

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

	public Player getCorrespondingPlayer(String firstName, String lastName) {
		for (Player player : players) {
			if (player.getLastName().equals(lastName) && player.getFirstName().equals(firstName)) {
				return player;
			}
		}
		return null;
	}

	public void refreshMenu() {
		EnclosMenu menu = new EnclosMenu(this);
		this.setJMenuBar(menu);
	}

	public void refreshPlayersInfo() {
		getFrameContentPane().refreshPlayersInfo(players);
	}

}
