package org.enclos.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.enclos.data.Human;
import org.enclos.data.SimpleReader;
import org.enclos.data.SimpleWriter;
import org.enclos.resources.song.Speaker;

/**
 * @author Clement CARREAU
 * @author Quentin GEFFARD
 * @author Julien TELA
 */

public class Enclos extends JFrame {

	private IntroFrame introFrame = null;
	private FrameContentPane contentPane = null;
	private final List<Board> boards = new LinkedList<Board>();
	private final List<Human> players;
	private int screenWidth;
	private int screenHeight;
	private Speaker enclosSpeaker = new Speaker();

	/**
	 * Constructor of Enclos class
	 */
	public Enclos() {
		Toolkit.getDefaultToolkit().setDynamicLayout(false);
		setTitle("Jeu de l'enclos");

		this.players = SimpleReader.readPlayer("players");

		setFocusTraversalKeysEnabled(false);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.screenWidth = screenSize.width;
		this.screenHeight = screenSize.height;

		setSize(screenWidth, screenHeight);

		this.setUndecorated(true);

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
				// introFrame.setSize(getSize());
			}
		});

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);

				if (e.getKeyCode() == KeyEvent.VK_P) {
					Enclos.this.contentPane.goToPlayersPanel(true);
				}
				if (e.getKeyCode() == KeyEvent.VK_G) {
					Enclos.this.contentPane.goToGamePanel();
				}
				if (e.getKeyCode() == KeyEvent.VK_S && ((e.getModifiers() & KeyEvent.CTRL_MASK) == 0)) {
					Enclos.this.contentPane.goToScorePanel();
				}
				if (e.getKeyCode() == KeyEvent.VK_A) {
					Enclos.this.contentPane.goToAboutPanel();
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					Enclos.this.contentPane.displayNextGame();
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					Enclos.this.contentPane.displayPreviousGame();
				}

				if (e.getKeyCode() == KeyEvent.VK_F11) {
					if (Enclos.this.isUndecorated()) {
						Enclos.this.dispose();
						Enclos.this.setUndecorated(false);
						Enclos.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
						Enclos.this.setVisible(true);
						Enclos.this.repaint();
					} else {
						Enclos.this.dispose();
						Enclos.this.setSize(Enclos.this.screenWidth, Enclos.this.screenHeight);
						Enclos.this.setLocationRelativeTo(null);
						Enclos.this.setUndecorated(true);
						Enclos.this.setVisible(true);
						Enclos.this.repaint();
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_F1) {
					Enclos.this.contentPane.goToHelpPanel();
				}

				if ((e.getKeyCode() == KeyEvent.VK_Z) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					Board targetBoard = Enclos.this.contentPane.getDisplayedBoard();
					if (targetBoard != null) {
						targetBoard.cancelAction();
					}
				}

				if ((e.getKeyCode() == KeyEvent.VK_N) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					Enclos.this.contentPane.goToPlayersPanel(false);
				}

				if ((e.getKeyCode() == KeyEvent.VK_S) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
					Date date = new Date();
					Board boardToSave = Enclos.this.contentPane.getDisplayedBoard();
					if (boardToSave != null) {
						SimpleWriter.SaveGame(boardToSave, dateFormat.format(date));
					} else {
						JOptionPane.showMessageDialog(null, "Please start a game before saving", "Error", JOptionPane.PLAIN_MESSAGE);
					}
				}

				if ((e.getKeyCode() == KeyEvent.VK_Q) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					System.exit(0);
				}
			}
		});

		this.introFrame = new IntroFrame(this);

		setVisible(true);
	}

	/**
	 * Get the maximal dimension screen of the user
	 * 
	 * @return Dimension max
	 */
	private Dimension getScreenMaximumSize() {
		Dimension maxDimension = null;
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		if (screenDimension.getHeight() > screenDimension.getWidth())
			return new Dimension(screenDimension.width, screenDimension.width);
		else
			return new Dimension(screenDimension.height, screenDimension.height);
	}

	/**
	 * Getter players list attribute
	 * 
	 * @return list players
	 */
	public List<Human> getPlayers() {
		return this.players;
	}

	/**
	 * Getter of the FrameContentPane attribute
	 * 
	 * @return the attribute
	 */
	public FrameContentPane getFrameContentPane() {
		return this.contentPane;
	}

	/**
	 * Getter of boards attribute
	 * 
	 * @return boards attributes, list of the game launched
	 */
	public List<Board> getBoards() {
		return this.boards;
	}

	/**
	 * Get the Human who corresponding to the firstname and lastname given as
	 * arguments
	 * 
	 * @param firstName
	 * @param lastName
	 * @return Human corresponding
	 */
	public Human getCorrespondingPlayer(String firstName, String lastName) {
		for (Human player : players) {
			if (player.getLastName().equals(lastName) && player.getFirstName().equals(firstName)) {
				return player;
			}
		}
		return null;
	}

	/**
	 * refresh the menu, call on save to refresh the load item
	 */
	public void refreshMenu() {
		EnclosMenu menu = new EnclosMenu(this);
		this.setJMenuBar(menu);
	}

	/**
	 * refresh player info
	 */
	public void refreshPlayersInfo() {
		getFrameContentPane().refreshPlayersInfo(players);
	}

	/**
	 * Getter content pane
	 */
	public FrameContentPane getContentPane() {
		return this.contentPane;
	}

	/**
	 * Getter speaker attribute
	 * 
	 * @return Speaker who used to play music theme
	 */
	public Speaker getBackgroundMusicSpeaker() {
		return enclosSpeaker;
	}

}
