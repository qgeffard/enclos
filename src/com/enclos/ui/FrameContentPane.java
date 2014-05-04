package com.enclos.ui;

import java.awt.CardLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.enclos.data.Human;

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

	public void addToGamePanel(Board board) {
		parent.getBoards().add(board);
		gamePanel.add(board);
		parent.refreshMenu();
	}

	public void resetGamePanel() {
		gamePanel.removeAll();
	}

	public void goToGamePanel() {
		contentPaneCardLayout.show(this, GAMEPANELNAME);
	}

	public void goToPlayersPanel(boolean displayPlayersManagementButton) {
		playersPanel.resetSelectedPlayers();
		playersPanel.displayPlayersManagementButton(displayPlayersManagementButton);
		contentPaneCardLayout.show(this, PLAYERGRIDNAME);
	}

	public void goToScorePanel() {
		contentPaneCardLayout.show(this, SCOREPANELNAME);
	}
	
	public void goToAboutPanel() {
		contentPaneCardLayout.show(this, ABOUTPANELNAME);
	}


	public void goToHelpPanel() {
		contentPaneCardLayout.show(this, HELPPANELNAME);
	}

	public void displayNextGame() {
		this.gamePanelCardLayout.next(this.gamePanel);
	}

	public void displayPreviousGame() {

		this.gamePanelCardLayout.previous(this.gamePanel);
	}

	public Board getDisplayedBoard() {
		for (Component comp : this.gamePanel.getComponents()) {
			if (comp.isVisible() == true) {
				return (Board) comp;
			}
		}
		return null;
	}

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

	public void refreshScorePanel(List<Human> players) {
		scorePanel.feedTable(players);
	}

	private void refreshPlayersPanel(List<Human> players) {
		playersPanel.refresh();
	}

	public void refreshPlayersInfo(List<Human> players) {
		refreshScorePanel(players);
		refreshPlayersPanel(players);
	}

	public PlayersMainPanel getPlayersPanel() {
		return playersPanel;
	}

}
