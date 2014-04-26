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
import com.enclos.ui.FrameContentPane;

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
			/*	SimpleWriter.SaveGame(
						EnclosMenu.this.parent.getBoards().get(0),
						dateFormat.format(date));*/

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

				FrameContentPane contentPane = ((FrameContentPane) EnclosMenu.this.parent
						.getContentPane());
				contentPane.goToPlayersGrid();
				contentPane.setPlayersPanelSelectable(true);
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

				// LOADING
				long loadBoardSize = (long) params.get("Boardsize");
				List<JSONArray> barriers = (List<JSONArray>) params
						.get("Barriers");
				List<JSONArray> sheepPositions = (List<JSONArray>) params
						.get("Sheepspositions");
				int numberSheepPerPlayer = Integer.parseInt(params.get(
						"Players").toString());
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
