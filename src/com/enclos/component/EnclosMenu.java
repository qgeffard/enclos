package com.enclos.component;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.enclos.data.Player;
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
		// final JMenu saveItem = generateSaveMenu();
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
				
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				Date date = new Date();
				Board boardToSave = EnclosMenu.this.parent.getFrameContentPane().getDisplayedBoard();
				if (boardToSave != null) {
					if(boardToSave.getCurrentPlayer().isBeginOfTurn()){
						SimpleWriter.SaveGame(boardToSave, dateFormat.format(date));
					} else {
						JOptionPane.showMessageDialog(null, "Please finish your turn before saving","Error", JOptionPane.PLAIN_MESSAGE);
					}
				}
				if (EnclosMenu.this.parent.getPlayers().size() > 0) {
					SimpleWriter.SavePlayer(EnclosMenu.this.parent.getPlayers(), "players_test");
				}
				
				EnclosMenu.this.parent.refreshMenu();
				//EnclosMenu.this.repaint();
			}
		});

	}

	private void addNewGameItemListener(JMenuItem newGameItem) {

		newGameItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				FrameContentPane contentPane = ((FrameContentPane) EnclosMenu.this.parent.getContentPane());
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
				final String exactName = saveName.substring(0, saveName.length() - 5);
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

				System.out.println(exactName);
				// Map<String, Object> params =
				// SimpleReader.readGame(exactName);

				// LOADING
				JSONParser parser = new JSONParser();
				JSONObject root = null;
				try {
					root = (JSONObject) parser.parse(new FileReader("resources/save/" + exactName + ".json"));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				List<JSONArray> players = (List<JSONArray>) root.get("Players");
				Map<Sheep, Point> sheepsInfo = new LinkedHashMap<Sheep, Point>();
				List<Player> playersList = new LinkedList<Player>();
								
				for (JSONArray player : players) {
					for (Object obj : player) {
						JSONObject jsonobj = (JSONObject) obj;
						Player owner = EnclosMenu.this.parent.getCorrespondingPlayer((String) jsonobj.get("firstname"), (String) jsonobj.get("lastname"));
						playersList.add(owner);
					}
				}
				Board loadBoard = new Board((Long) root.get("Boardsize"), Integer.parseInt(root.get("nbSheepPerPlayer").toString()), playersList, EnclosMenu.this.parent);
				
				for (JSONArray player : players) {
					for (Object obj : player) {
						JSONObject jsonobj = (JSONObject) obj;
						Player owner = loadBoard.getCorrespondingPlayer((String) jsonobj.get("firstname"), (String) jsonobj.get("lastname"));
						playersList.add(owner);
						JSONArray sheepsPosition = (JSONArray) jsonobj.get("sheeps");
						for (Object position : sheepsPosition) {
							Sheep newSheep = new Sheep();
							newSheep.setOwner(owner);
							newSheep.setImgPath( new File((String) jsonobj.get("imgPath")));
							String[] hexaPosition = ((String) position).split(",");
							Point pos = new Point(Integer.valueOf(hexaPosition[0]), Integer.valueOf(hexaPosition[1]));
							sheepsInfo.put(newSheep, pos);
						}
					}
				}
				Player currentPlayer = loadBoard.getCorrespondingPlayer((String) root.get("currentPLayerFirstName"), (String) root.get("currentPLayerLastName"));

				List<JSONArray> barriers = (List<JSONArray>) root.get("Barriers");

				EnclosMenu.this.parent.getBoards().add(loadBoard);
				EnclosMenu.this.parent.getFrameContentPane().addToGamePanel(loadBoard);

				loadBoard.setData(barriers, sheepsInfo, currentPlayer);
			}
		});
	}
}
