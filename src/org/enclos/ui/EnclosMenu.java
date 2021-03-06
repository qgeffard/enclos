package org.enclos.ui;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
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

import org.enclos.component.Sheep;
import org.enclos.data.Difficulty;
import org.enclos.data.Human;
import org.enclos.data.SimpleWriter;
import org.enclos.resources.song.Speaker;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author Clement CARREAU
 * @author Quentin GEFFARD
 * @author Julien TELA
 */

public class EnclosMenu extends JMenuBar {
	
	/**
	 * Static attribute used to check if sounds game are enable
	 */
	private static boolean isSoundActivated = true; 
	
	/**
	 * Static attribute used to check if music game are enable
	 */
	public static boolean isMusicActivated = true; 
	
	private final Enclos parent;

	
	/**
	 * EnclosMenu Constructor 
	 * 
	 * @param enclos
	 */
	public EnclosMenu(Enclos enclos) {
		this.parent = enclos;

		this.setBackground(Color.black);
		JMenu file = new JMenu("File");
		JMenu game = new JMenu("Game");
		JMenu sounds = new JMenu("Sounds");
		JMenu help = new JMenu("Help");
		addFileSubItems(file);
		addGameSubItems(game);
		addSoundsSubItems(sounds);
		addHelpGameSubItems(help);
	}
	
	/**
	 * Used to add sub menu item to the menu pass as argument
	 * @param menu
	 */
	private void addSoundsSubItems(JMenu menu) {
		final JMenuItem soundsItem = new JCheckBoxMenuItem("Play sounds", EnclosMenu.isSoundActivated);
		addSoundsItemListener(soundsItem);
		final JMenuItem musicItem = new JCheckBoxMenuItem("Play music", EnclosMenu.isMusicActivated);
		addMusicItemListener(musicItem);
		menu.add(soundsItem);
		menu.add(musicItem);
		
		add(menu);
		
	}

	/**
	 * Used to add a new listener on the menu item pass as argument
	 * @param musicItem
	 */
	private void addMusicItemListener(final JMenuItem musicItem) {
		musicItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(musicItem.isSelected() && parent.getBoards().size()>0){
					EnclosMenu.isMusicActivated = true;
					parent.getBackgroundMusicSpeaker().playMusic();

				}else{
					EnclosMenu.isMusicActivated = false;
					parent.getBackgroundMusicSpeaker().stopMusic();
				}
			}
		});
	}
	
	/**
	 * Used to add sub menu item to the menu pass as argument
	 * @param menu
	 */
	private void addHelpGameSubItems(JMenu menu) {
		final JMenuItem shortcutsItem = new JMenuItem("Shortcuts");
		addShortcutItemListener(shortcutsItem);
		
		final JMenuItem about = new JMenuItem("About Enclos");
		addAboutGameItemListener(about);
		
		menu.add(about);
		menu.add(shortcutsItem);
		
		this.add(menu);
	}
	
	
	/**
	 * Used to add a new listener on the menu item pass as argument
	 *
	 * @param about
	 */
	private void addAboutGameItemListener(JMenuItem about) {
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.getFrameContentPane().goToAboutPanel();
			}
		});
		
	}

	/**
	 * Used to add a new listener on the menu item pass as argument
	 *  
	 * @param shortcutsItem
	 */
	private void addShortcutItemListener(JMenuItem shortcutsItem) {
		shortcutsItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.getFrameContentPane().goToHelpPanel();
			}
		});
	}

	/**
	 * Used to add sub menu item to the menu pass as argument
	 * @param menu
	 */
	private void addGameSubItems(JMenu menu) {
		final JMenuItem previousGameItem = new JMenuItem("Previous Game");
		addPreviousGameItemListener(previousGameItem);
		final JMenuItem nextGameItem = new JMenuItem("Next Game");
		addNextGameItemListener(nextGameItem);
		final JMenuItem closeGameItem = new JMenuItem("Close");
		addCloseGameItemListener(closeGameItem);
		final JMenuItem saveItem = new JMenuItem("Save");
		addSaveItemListener(saveItem);

		menu.add(previousGameItem);
		menu.add(nextGameItem);
		menu.add(closeGameItem);
		menu.add(saveItem);
		this.add(menu);
	}

	/**
	 *  Used to add a new listener on the menu item pass as argument
	 * @param nextGameItem
	 */
	private void addNextGameItemListener(JMenuItem nextGameItem) {
		nextGameItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.getFrameContentPane().displayNextGame();
			}
		});
	}

	/**
	 *  Used to add a new listener on the menu item pass as argument
	 * @param previousGameItem
	 */
	private void addPreviousGameItemListener(JMenuItem previousGameItem) {
		previousGameItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.getFrameContentPane().displayPreviousGame();
			}
		});		
	}
	
	/**
	 * Used to add sub menu item to the menu pass as argument
	 * @param menu
	 */
	public void addFileSubItems(JMenu menu) {

		final JMenuItem newGameItem = new JMenuItem("New Game");
		addNewGameItemListener(newGameItem);
		final JMenuItem scoreItem = new JMenuItem("Scores");
		addScoreItemListener(scoreItem);
		final JMenuItem playersItem = new JMenuItem("Players");
		addPlayersItemListener(playersItem);
		final JMenu loadMenu = generateLoadMenu();
		final JMenuItem exitItem = new JMenuItem("Exit");
		addExitItemListener(exitItem);

		menu.add(newGameItem);
		menu.add(scoreItem);
		menu.add(playersItem);
		menu.add(loadMenu);
		menu.add(exitItem);
		this.add(menu);

	}
	
	/**
	 * Used to add a new listener on the menu item pass as argument
	 * @param exitItem
	 */
	private void addExitItemListener(JMenuItem exitItem) {
		exitItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

	}
	
	/**
	 * Used to add a new listener on the menu item pass as argument
	 * @param playersItem
	 */
	private void addPlayersItemListener(JMenuItem playersItem) {
		playersItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.getFrameContentPane().goToPlayersPanel(true);
			}
		});
	}
	
	/**
	 * Used to add a new listener on the menu item pass as argument
	 * @param scoreItem
	 */
	private void addScoreItemListener(JMenuItem scoreItem) {
		scoreItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.getFrameContentPane().goToScorePanel();
			}
		});

	}
	
	/**
	 * Used to add a new listener on the menu item pass as argument
	 * @param soundsItem
	 */
	private void addSoundsItemListener(final JMenuItem soundsItem) {
		soundsItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Speaker.isMute(!soundsItem.isSelected());
				if(soundsItem.isSelected()){
					EnclosMenu.isSoundActivated = true;
				}else{
					EnclosMenu.isSoundActivated = false;
				}
			}
		});

	}

	/**
	 * Used to add a new listener on the menu item pass as argument
	 * 
	 * This listener save the current game shown
	 * @param saveItem
	 */
	private void addSaveItemListener(JMenuItem saveItem) {

		saveItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				Date date = new Date();
				Board boardToSave = EnclosMenu.this.parent.getFrameContentPane().getDisplayedBoard();
				if (boardToSave != null) {
					if (boardToSave.getCurrentPlayer().isBeginOfTurn()) {
						SimpleWriter.SaveGame(boardToSave, dateFormat.format(date));
					} else {
						JOptionPane.showMessageDialog(null, "Please finish your turn before saving", "Error", JOptionPane.PLAIN_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "No game to save", "Error", JOptionPane.PLAIN_MESSAGE);
				}
				if (EnclosMenu.this.parent.getPlayers().size() > 0) {
					SimpleWriter.SavePlayer(EnclosMenu.this.parent.getPlayers(), "players");
				}

				EnclosMenu.this.parent.refreshMenu();
				EnclosMenu.this.parent.revalidate();
				// EnclosMenu.this.repaint();
			}
		});

	}
	
	/**
	 * Used to add a new listener on the menu item pass as argument
	 * @param newGameItem
	 */
	private void addNewGameItemListener(JMenuItem newGameItem) {

		newGameItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				FrameContentPane contentPane = ((FrameContentPane) EnclosMenu.this.parent.getContentPane());
				contentPane.goToPlayersPanel(false);
			}
		});
	}
	
	/**
	 * Used to add a new listener on the menu item pass as argument
	 * @param closeGameItem
	 */
	private void addCloseGameItemListener(JMenuItem closeGameItem) {

		closeGameItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				EnclosMenu.this.parent.getFrameContentPane().removeDisplayedGame();
				if(EnclosMenu.this.parent.getBoards().size() == 0){
					EnclosMenu.this.parent.getBackgroundMusicSpeaker().stopMusic();
				}
			}
		});
	}
	
	/**
	 * Used to construct the sub menu list item of the load menu
	 * 
	 * @return the sub list menu item
	 */
	private JMenu generateLoadMenu() {
		final JMenu loadMenu = new JMenu("Open");
		File folder = new File("resources/save/");
		File[] listOfFiles = folder.listFiles();

		if (listOfFiles != null) {
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					String saveName = listOfFiles[i].getName();
					final String exactName = saveName.substring(0, saveName.length() - 5);
					JMenuItem loadFile = new JMenuItem(exactName);
					addLoadFileListener(loadFile, exactName);
					loadMenu.add(loadFile);
				}
			}
		} else {
			loadMenu.add(new JMenuItem("Please save a game before load"));
		}
		return loadMenu;
	}
	
	/**
	 * Used to add a new listener on the menu item pass as argument
	 * 
	 * Charge the new game with informations read in the json file
	 * 
	 * @param loadFile
	 * @param exactName
	 */
	private void addLoadFileListener(JMenuItem loadFile, final String exactName) {
		loadFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FileReader reader = null;
				try {
					reader = new FileReader("resources/save/" + exactName + ".json");
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				// LOADING

				JSONParser parser = new JSONParser();
				JSONObject root = null;
				try {

					root = (JSONObject) parser.parse(reader);

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
				try {
					List<JSONArray> players = (List<JSONArray>) root.get("Players");
					Map<Sheep, Point> sheepsInfo = new LinkedHashMap<Sheep, Point>();
					List<Human> playersList = new LinkedList<Human>();

					for (JSONArray player : players) {
						for (Object obj : player) {
							JSONObject jsonobj = (JSONObject) obj;
							if (!jsonobj.get("firstname").equals("add9cf0f98bd686c95909c8c9160fa5463225c10")) {
								Human owner = EnclosMenu.this.parent.getCorrespondingPlayer((String) jsonobj.get("firstname"), (String) jsonobj.get("lastname"));
								playersList.add(owner);
							}
						}
					}
					Board loadBoard = null;
					if (playersList.size() < 2) {
						loadBoard = new Board((Long) root.get("Boardsize"), Integer.parseInt(root.get("nbSheepPerPlayer").toString()), playersList, EnclosMenu.this.parent, Difficulty.valueOf((String) root.get("difficulty").toString().toUpperCase()));
					} else {
						loadBoard = new Board((Long) root.get("Boardsize"), Integer.parseInt(root.get("nbSheepPerPlayer").toString()), playersList, EnclosMenu.this.parent);
					}
					for (JSONArray player : players) {
						for (Object obj : player) {
							JSONObject jsonobj = (JSONObject) obj;
							if (!jsonobj.get("firstname").equals("add9cf0f98bd686c95909c8c9160fa5463225c10")) {
								Human owner = loadBoard.getCorrespondingPlayer((String) jsonobj.get("firstname"), (String) jsonobj.get("lastname"));
								JSONArray sheepsPosition = (JSONArray) jsonobj.get("sheeps");
								for (Object position : sheepsPosition) {
									Sheep newSheep = new Sheep();
									newSheep.setOwner(owner);
									newSheep.setImgPath(new File((String) jsonobj.get("imgPath")));
									String[] hexaPosition = ((String) position).split(",");
									Point pos = new Point(Integer.valueOf(hexaPosition[0]), Integer.valueOf(hexaPosition[1]));
									sheepsInfo.put(newSheep, pos);
								}
							} else {
								JSONArray sheepsPosition = (JSONArray) jsonobj.get("sheeps");
								for (Object position : sheepsPosition) {
									Sheep newSheep = new Sheep();
									newSheep.setImgPath(new File((String) jsonobj.get("imgPath")));
									String[] hexaPosition = ((String) position).split(",");
									Point pos = new Point(Integer.valueOf(hexaPosition[0]), Integer.valueOf(hexaPosition[1]));
									sheepsInfo.put(newSheep, pos);
								}
							}
						}
					}
					Human currentPlayer = loadBoard.getCorrespondingPlayer((String) root.get("currentPLayerFirstName"), (String) root.get("currentPLayerLastName"));

					List<JSONArray> barriers = (List<JSONArray>) root.get("Barriers");

					EnclosMenu.this.parent.getBoards().add(loadBoard);
					EnclosMenu.this.parent.getFrameContentPane().addToGamePanel(loadBoard);

					loadBoard.setData(barriers, sheepsInfo, currentPlayer);

					FrameContentPane contentPane = ((FrameContentPane) EnclosMenu.this.parent.getContentPane());
					contentPane.goToGamePanel();
					EnclosMenu.this.parent.revalidate();
				} catch (Exception e1) {
					int deleteLoad = JOptionPane.showConfirmDialog(parent, "Load file was corrupted, do you want delete it ?");

					if (deleteLoad == JOptionPane.OK_OPTION) {
						try {
							reader.close();
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						File save = new File("resources/save/" + exactName + ".json");
						System.out.println(save.getAbsolutePath());
						save.delete();
						EnclosMenu.this.parent.refreshMenu();
						EnclosMenu.this.parent.revalidate();
					}

				}
			}
		});
	}
}
