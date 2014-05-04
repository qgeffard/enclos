package org.enclos.ui;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.enclos.data.Difficulty;
import org.enclos.data.Human;
import org.enclos.data.SimpleWriter;

/**
 * @author Clement CARREAU
 * @author Quentin GEFFARD
 * @author Julien TELA
 */

public class PlayersMainPanel extends JPanel {

	private final Enclos enclos;
	private final FrameContentPane parent;
	private final PlayersGridPanel playersGridPanel;

	private final JPanel buttonPanel;
	private JButton createGameButton;
	JButton addPlayerButton;
	JButton removePlayerButton;

	/**
	 * Constructor of PlayersMainPanel 
	 * @param enclos
	 * @param parent
	 */
	public PlayersMainPanel(final Enclos enclos, FrameContentPane parent) {
		this.enclos = enclos;
		this.parent = parent;

		this.setLayout(new BorderLayout());
		playersGridPanel = new PlayersGridPanel();

		buttonPanel = new JPanel();

		this.add(playersGridPanel, BorderLayout.NORTH);

		generatePlayersCard();

		addPlayerButton = new JButton("Add player");
		removePlayerButton = new JButton("Delete selected players");

		createGameButton = new JButton("Create game");
		createGameButton.setFocusable(false);
		createGameButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				int nbPlayers = PlayersMainPanel.this.playersGridPanel.getPlayerSelectedCount();

				if(nbPlayers == 1){
					Map<String, String> params = NewGameForm.display(PlayersMainPanel.this.playersGridPanel.getPlayerSelectedCount());
					if (params != null) {
						Long size = Long.valueOf(params.get("boardSize"));
						int nbSheep = Integer.valueOf(params.get("nbSheepPerPlayer"));
						Difficulty difficulty =  Difficulty.valueOf(params.get("difficulty").toUpperCase());
						Board newBoard = new Board(size, nbSheep, PlayersMainPanel.this.playersGridPanel.getPlayersSelected(), PlayersMainPanel.this.enclos, difficulty);
						
						if (params.get("close").equals("close")) {
							PlayersMainPanel.this.parent.resetGamePanel();
						}
						PlayersMainPanel.this.parent.addToGamePanel(newBoard);
						PlayersMainPanel.this.parent.goToGamePanel();
					}
				}
				else if (nbPlayers >= 2 && nbPlayers < 6) {
					Map<String, String> params = NewGameForm.display(PlayersMainPanel.this.playersGridPanel.getPlayerSelectedCount());
					if (params != null) {
						Long size = Long.valueOf(params.get("boardSize"));
						int nbSheep = Integer.valueOf(params.get("nbSheepPerPlayer"));
						Board newBoard = new Board(size, nbSheep, PlayersMainPanel.this.playersGridPanel.getPlayersSelected(), PlayersMainPanel.this.enclos);

						if (params.get("close").equals("close")) {
							PlayersMainPanel.this.parent.resetGamePanel();
						}
						PlayersMainPanel.this.parent.addToGamePanel(newBoard);
						PlayersMainPanel.this.parent.goToGamePanel();
					}
				} else {
					JOptionPane.showConfirmDialog(PlayersMainPanel.this, "The number of player must be between 1 and 5");
				}
				PlayersMainPanel.this.createGameButton.setEnabled(false);
				PlayersMainPanel.this.createGameButton.setEnabled(true);
			}
		});

		removePlayerButton.setFocusable(false);
		removePlayerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				for (Human playerToRemove : PlayersMainPanel.this.playersGridPanel.getPlayersSelected()) {
					boolean inGame = false;
					for (Board board : PlayersMainPanel.this.enclos.getBoards()) {
						if (board.getRealPlayerList().contains(playerToRemove)) {
							inGame = true;
						}
					}
					if (inGame) {
						JOptionPane.showMessageDialog(PlayersMainPanel.this, "Cannot delete "+playerToRemove.getLastName()+" "+playerToRemove.getFirstName()+", player in a game");
					} else
					PlayersMainPanel.this.enclos.getPlayers().remove(playerToRemove);
					SimpleWriter.SavePlayer(PlayersMainPanel.this.enclos.getPlayers(), "players");
					PlayersMainPanel.this.enclos.getContentPane().refreshPlayersInfo(PlayersMainPanel.this.enclos.getPlayers());
					PlayersMainPanel.this.enclos.getFrameContentPane().refreshScorePanel(PlayersMainPanel.this.enclos.getPlayers());
					PlayersMainPanel.this.playersGridPanel.revalidate();
				}
				revalidate();
			}
		});


		addPlayerButton.setFocusable(false);

		addPlayerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				Human newPlayer = createNewPlayer();
				if (newPlayer != null) {
					PlayerProfilePanel playerProfile = new PlayerProfilePanel(newPlayer, PlayersMainPanel.this);
					PlayersMainPanel.this.playersGridPanel.addPlayerProfile(playerProfile);
					PlayersMainPanel.this.enclos.getPlayers().add(newPlayer);
					SimpleWriter.SavePlayer(PlayersMainPanel.this.enclos.getPlayers(), "players");
					PlayersMainPanel.this.enclos.getFrameContentPane().refreshScorePanel(PlayersMainPanel.this.enclos.getPlayers());
					revalidate();
				}
			}

			private Human createNewPlayer() {
				Human newPlayer = null;
				final JTextField firstName = new JTextField();
				final JTextField lastName = new JTextField();
				final JTextField age = new JTextField();
				final JButton profilPictureButton = new JButton("Choose a profile picture");

				final JFileChooser fileChooser = new JFileChooser();

				final FileDialog fileDialog = new FileDialog(new JFrame(), "Choose picture", FileDialog.LOAD);
				profilPictureButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						fileChooser.showDialog(PlayersMainPanel.this, "ok");
					}
				});
				final JComponent[] inputs = new JComponent[] { new JLabel("First Name"), firstName, new JLabel("Last Name"), lastName, new JLabel("Age"), age, profilPictureButton };
				JOptionPane.showMessageDialog(null, inputs, "Add a new player", JOptionPane.PLAIN_MESSAGE);
				try {
					File file = fileChooser.getSelectedFile();
					if (enclos.getCorrespondingPlayer(firstName.getText(), lastName.getText()) == null) {
						if (file != null) {
							newPlayer = new Human(firstName.getText(), lastName.getText(), Integer.parseInt(age.getText()), file.getAbsolutePath());
						} else {
							newPlayer = new Human(firstName.getText(), lastName.getText(), Integer.parseInt(age.getText()));
						}
					} else {
						JOptionPane.showMessageDialog(null, "The Couple lastname, firstname already exists, plaese choose another one", "Error", JOptionPane.PLAIN_MESSAGE);
					}

				} catch (Exception e) {
					return null;
				}
				return newPlayer;
			}
		});

		buttonPanel.add(addPlayerButton);
		buttonPanel.add(removePlayerButton);
		buttonPanel.add(createGameButton);
		this.add(buttonPanel, BorderLayout.SOUTH);

	}

	/**
	 * Generate all player card from player list attribute of enclos
	 */
	private void generatePlayersCard() {
		final List<Human> players = this.enclos.getPlayers();
		if (players != null && players.size() > 0) {
			for (final Human currentPlayer : players) {
				final PlayerProfilePanel playerProfile = new PlayerProfilePanel(currentPlayer, this);
				this.playersGridPanel.addPlayerProfile(playerProfile);
			}
		}
	}
	
	/**
	 * Getter enclos attribute
	 * @return enclos
	 */
	public Enclos getEnclos() {
		return this.enclos;
	}
	
	/**
	 * Getter playersGridPanel attribute
	 * @return playersGridPanel 
	 */
	public PlayersGridPanel getGridPanel() {
		return this.playersGridPanel;
	}

	/**
	 * Unselect selected player
	 */
	public void resetSelectedPlayers() {
		playersGridPanel.reset();
	}
	
	/**
	 * refresh the panel
	 */
	public void refresh() {
		playersGridPanel.removeAll();
		generatePlayersCard();
	}
	
	/**
	 * Change the title and buttons shown 
	 * @param displayPlayersManagementButton
	 */
	public void displayPlayersManagementButton(boolean displayPlayersManagementButton) {
		if (displayPlayersManagementButton) {
			setPanelTitle("Player Manager");
			
			addPlayerButton.setVisible(true);
			removePlayerButton.setVisible(true);
			createGameButton.setVisible(false);
		} else {
			setPanelTitle("Choose your characters");
			
			createGameButton.setVisible(true);
			addPlayerButton.setVisible(false);
			removePlayerButton.setVisible(false);
		}
	}
	
	/**
	 * Add the title given as argument 
	 * @param title
	 */
	private void setPanelTitle(String title){
		Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder titleBorder = BorderFactory.createTitledBorder(lowerEtched, title);
		setBorder(titleBorder);

	}
}
