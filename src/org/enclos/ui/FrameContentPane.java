package org.enclos.ui;

import java.awt.CardLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.enclos.data.Human;

/**
 * @author Clement CARREAU
 * @author Quentin GEFFARD
 * @author Julien TELA
 */

public class FrameContentPane extends JPanel {

	private final CardLayout contentPaneCardLayout;
	private final JPanel gamePanel;
	private final CardLayout gamePanelCardLayout;
	private final PlayersMainPanel playersPanel;
	private final ScorePanel scorePanel;
	private final HelpPanel helpPanel;
	private final AboutPanel aboutPanel;

	private final String GAMEPANELNAME = "GamePanel";
	private final String PLAYERGRIDNAME = "PlayersGrid";
	private final String SCOREPANELNAME = "ScorePanel";
	private final String HELPPANELNAME = "HelpPanel";
	private final String ABOUTPANELNAME = "AboutPanel";

	private Enclos parent = null;
	
	/**
	 * Constructor of FrameContentPane class 
	 * @param parent
	 */
	public FrameContentPane(Enclos parent) {
		this.parent = parent;

		contentPaneCardLayout = new CardLayout();
		this.setLayout(contentPaneCardLayout);

		gamePanel = new JPanel();
		gamePanelCardLayout = new CardLayout();
		gamePanel.setLayout(gamePanelCardLayout);

		helpPanel = new HelpPanel();
		
		aboutPanel = new AboutPanel();

		playersPanel = new PlayersMainPanel(parent, this);
		JScrollPane scrollPanel = new JScrollPane(playersPanel);

		scorePanel = new ScorePanel(parent.getPlayers());

		this.add(gamePanel, GAMEPANELNAME);
		this.add(scrollPanel, PLAYERGRIDNAME);
		this.add(scorePanel, SCOREPANELNAME);
		this.add(helpPanel, HELPPANELNAME);
		this.add(aboutPanel, ABOUTPANELNAME);
	}
	
	/**
	 * Add the board passed as argument to the game panel 
	 * @param board
	 */
	public void addToGamePanel(Board board) {
		parent.getBoards().add(board);
		gamePanel.add(board);
		parent.refreshMenu();
	}
	
	/**
	 * Remove of component of game panel, close all game
	 */
	public void resetGamePanel() {
		gamePanel.removeAll();
	}
	
	/**
	 * Switch to game panel 
	 */
	public void goToGamePanel() {
		contentPaneCardLayout.show(this, GAMEPANELNAME);
	}

	/**
	 * Switch to player panel
	 */
	public void goToPlayersPanel(boolean displayPlayersManagementButton) {
		playersPanel.resetSelectedPlayers();
		playersPanel.displayPlayersManagementButton(displayPlayersManagementButton);
		contentPaneCardLayout.show(this, PLAYERGRIDNAME);
	}

	/**
	 * Switch to score panel 
	 */
	public void goToScorePanel() {
		contentPaneCardLayout.show(this, SCOREPANELNAME);
	}
		
	/**
	 * Switch to about panel in card layout
	 */
	public void goToAboutPanel() {
		contentPaneCardLayout.show(this, ABOUTPANELNAME);
	}

	/**
	 * Switch to about panel in card layout
	 */
	public void goToHelpPanel() {
		contentPaneCardLayout.show(this, HELPPANELNAME);
	}
	
	/**
	 * Display the next game in card layout
	 */
	public void displayNextGame() {
		this.gamePanelCardLayout.next(this.gamePanel);
	}

	/**
	 * Display the previous game in card layout
	 */
	public void displayPreviousGame() {

		this.gamePanelCardLayout.previous(this.gamePanel);
	}
	
	/**
	 * Get the board is currently displayed
	 * @return the Board displayed or null if nothing 
	 */
	public Board getDisplayedBoard() {
		for (Component comp : this.gamePanel.getComponents()) {
			if (comp.isVisible() == true) {
				return (Board) comp;
			}
		}
		return null;
	}
	
	/**
	 * Delete the current game displayed
	 */
	public void removeDisplayedGame() {
		for (Component board : this.gamePanel.getComponents()) {
			if (board.isVisible() == true) {
				this.gamePanel.remove(board);
				parent.getBoards().remove(board);
				
				if(parent.getBoards().isEmpty()){
					parent.getBackgroundMusicSpeaker().stopMusic();
				}
				
				gamePanel.revalidate();
			}
		}
	}
	
	/**
	 * Refresh info of the score panel
	 * @param players
	 */
	public void refreshScorePanel(List<Human> players) {
		scorePanel.feedTable(players);
	}

	/**
	 * Refresh the player panel
	 * @param players
	 */
	private void refreshPlayersPanel(List<Human> players) {
		playersPanel.refresh();
	}

	/**
	 * Refresh info of the player panel
	 * @param players
	 */
	public void refreshPlayersInfo(List<Human> players) {
		refreshScorePanel(players);
		refreshPlayersPanel(players);
	}
	
	/**
	 * Getter of players panel attribute
	 * @return players panel
	 */
	public PlayersMainPanel getPlayersPanel() {
		return playersPanel;
	}

}
