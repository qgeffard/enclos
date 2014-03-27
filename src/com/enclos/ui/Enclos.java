package com.enclos.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.json.simple.JSONArray;

import com.enclos.component.Bridge;
import com.enclos.controller.State;
import com.enclos.data.SimpleReader;
import com.enclos.data.SimpleWriter;

public class Enclos extends JFrame {

	private State state = null;
	private ScoreFrame scoreFrame = null;
	private JPanel contentPane = null;
	private List<Board> boards = new LinkedList<Board>();

	public Enclos() {
		this.state = new State(this);
		this.scoreFrame = new ScoreFrame(this);
		Toolkit.getDefaultToolkit().setDynamicLayout(false);
		setTitle("Jeu de l'enclos");

		// à revoir
		// setMaximumSize(getScreenMaximumSize());
		// enabled tab listener
		setFocusTraversalKeysEnabled(false);
		setSize(500, 500);

		this.contentPane = new FrameContentPane();
		this.contentPane.setLayout(new FlowLayout());

		setContentPane(this.contentPane);
		/*this.boards.add(new Board(3, 6));
		contentPane.add(boards.get(0));*/

		SimpleReader reader = new SimpleReader("2014-03-28_00-14-17");
		Map<String, Object> params = reader.read();

		// LOAD
		long loadBoardSize = (long) params.get("Boardsize");
		List<JSONArray> barriers = (List<JSONArray>) params.get("Barriers");
		List<JSONArray> sheepPositions = (List<JSONArray>) params.get("Sheepspositions");
		Board loadBoard = new Board(loadBoardSize, sheepPositions.size());
		this.boards.add(loadBoard);
		contentPane.add(loadBoard);
		loadBoard.setData(barriers, sheepPositions);
		// LOAD
		
		contentPane.add(loadBoard);

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
					System.out.println("pressed");
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
		JMenuItem newGameItem = new JMenuItem("New Game");
		JMenuItem scoreItem = new JMenuItem("Scores");
		JMenuItem playerItem = new JMenuItem("Players");
		JMenuItem saveItem = new JMenuItem("Save");
		menu.add(newGameItem);
		menu.add(scoreItem);
		menu.add(playerItem);
		menu.add(saveItem);
		menuBar.add(menu);
		setJMenuBar(menuBar);

		newGameItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// on recup les paramètres du nouveau game
				Map<String, String> settings = NewGameForm.display();
				// si on a pas fait cancel
				if (settings != null) {
					String boardSize = settings.get("boardSize");
					// on crée le board qui va bien
					Board newGame = new Board(Integer.valueOf(boardSize), 6);
					boolean close = settings.get("close").equals("close")
							? true
							: false;
					// si on a decidé de close les autre jeux
					if (close) {
						// on jarte les autres jeux
						Enclos.this.contentPane.removeAll();
						Enclos.this.contentPane.add(newGame);
					} else {
						// sinon on ajoute le jeu après les autres
						Enclos.this.contentPane.add(newGame);
					}
				}

			}
		});

		playerItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PlayersFrame playersFrame = new PlayersFrame();
			}
		});

		saveItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd_HH-mm-ss");
				Date date = new Date();
				SimpleWriter writer = new SimpleWriter(boards.get(0),
						dateFormat.format(date));
				System.out.println(dateFormat.format(date));
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

}