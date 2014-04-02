package com.enclos.component;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.json.simple.JSONArray;

import com.enclos.data.SimpleReader;
import com.enclos.data.SimpleWriter;
import com.enclos.resources.song.Speaker;
import com.enclos.ui.Board;
import com.enclos.ui.Enclos;
import com.enclos.ui.NewGameForm;

public class EnclosMenu extends JMenuBar {

	private final Enclos parent;

	public EnclosMenu(Enclos enclos) {
		this.parent = enclos;

		this.setBackground(Color.black);
		JMenu menu = new JMenu("Game");
		menu.setForeground(Color.white);
		addSubItems(menu);
	}

	public void addSubItems(JMenu menu) {

		final JMenuItem newGameItem = new JMenuItem("New Game");
		addNewGameItemListener(newGameItem);
		final JMenuItem scoreItem = new JMenuItem("Scores");
		final JMenuItem saveItem = new JMenuItem("Save");
		addSaveItemListener(saveItem);
		final JMenu loadMenu = generateLoadMenu();
		final JMenuItem soundsItem = new JCheckBoxMenuItem("Play sounds", true);
		addSoundsItemListener(soundsItem);

		menu.add(newGameItem);
		menu.add(scoreItem);
		menu.add(saveItem);
		menu.add(loadMenu);
		menu.add(soundsItem);
		this.add(menu);

	}

	private void addSoundsItemListener(final JMenuItem soundsItem) {
		soundsItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Speaker.isMute(!soundsItem.isSelected());
			}
		});

	}

	private void addSaveItemListener(JMenuItem saveItem) {

		saveItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd_HH-mm-ss");
				Date date = new Date();
				SimpleWriter.SaveGame(
						EnclosMenu.this.parent.getBoards().get(0),
						dateFormat.format(date));

				System.out.println(dateFormat.format(date));

				if (EnclosMenu.this.parent.getPlayers().size() > 0) {
					SimpleWriter.SavePlayer(
							EnclosMenu.this.parent.getPlayers(), "players_test");
				}
			}
		});

	}

	private void addNewGameItemListener(JMenuItem newGameItem) {

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
					boolean close = settings.get("close").equals("close")
							? true
							: false;
					// si on a decid� de close les autre jeux
					if (close) {
						// on jarte les autres jeux
						EnclosMenu.this.parent.getFrameContentPane()
								.resetGamePanel();
						EnclosMenu.this.parent.getFrameContentPane()
								.addToGamePanel(newGame);
					} else {
						// sinon on ajoute le jeu apr�s les autres
						EnclosMenu.this.parent.getFrameContentPane()
								.addToGamePanel(newGame);
					}
				}

			}

		});

	}

	private JMenu generateLoadMenu() {
		final JMenu loadMenu = new JMenu("Load");
		File folder = new File("resources/save/");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String saveName = listOfFiles[i].getName();
				final String exactName = saveName.substring(0,
						saveName.length() - 5);
				JMenuItem loadFile = new JMenuItem(exactName);
				addLoadFileListener(loadFile, exactName);
				loadMenu.add(loadFile);
			}
		}
		return loadMenu;
	}

	private void addLoadFileListener(JMenuItem loadFile, final String exactName) {
		loadFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Map<String, Object> params = SimpleReader.readGame(exactName);

				// // LOAD
				long loadBoardSize = (long) params.get("Boardsize");
				List<JSONArray> barriers = (List<JSONArray>) params
						.get("Barriers");
				List<JSONArray> sheepPositions = (List<JSONArray>) params
						.get("Sheepspositions");
				int numberSheepPerPlayer = Integer.parseInt(params.get("Players").toString());
				Board loadBoard = new Board(loadBoardSize, numberSheepPerPlayer);
				EnclosMenu.this.parent.getBoards().add(loadBoard);
				EnclosMenu.this.parent.getFrameContentPane().addToGamePanel(
						loadBoard);

				loadBoard.setData(barriers, sheepPositions);
				System.out.println(exactName);
			}
		});
	}
}
