package com.enclos.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import com.enclos.data.Player;

public class FrameContentPane extends JPanel {

    private final CardLayout contentPaneCardLayout;
    private final JPanel gamePanel;
    private final CardLayout gamePanelCardLayout;
    private final PlayersMainPanel playersPanel;
    private final ScorePanel scorePanel;
    
    private final String GAMEPANELNAME = "GamePanel";
    private final String PLAYERGRIDNAME = "PlayersGrid";
    private final String SCOREPANELNAME = "ScorePanel";


    private Enclos parent = null;
    
    public FrameContentPane(Enclos parent) {
    	this.parent = parent;
    	
        contentPaneCardLayout = new CardLayout();
        this.setLayout(contentPaneCardLayout);

        gamePanel = new JPanel();
        gamePanelCardLayout = new CardLayout();
        gamePanel.setLayout(gamePanelCardLayout);
        
        playersPanel = new PlayersMainPanel(parent, this);
        JScrollPane scrollPanel = new JScrollPane(playersPanel);

        scorePanel = new ScorePanel(parent.getPlayers());        
        
        this.add(gamePanel, GAMEPANELNAME);
        this.add(scrollPanel, PLAYERGRIDNAME);
        this.add(scorePanel, SCOREPANELNAME); 
    }

    public void addToGamePanel(Board board) {
    	parent.getBoards().add(board);
        gamePanel.add(board);
        parent.refreshMenu();
    }

    public void resetGamePanel() {
        gamePanel.removeAll();
    }

    public void switchPanel() {
        contentPaneCardLayout.next(this);
    }

    public void goToGamePanel() {
        contentPaneCardLayout.show(this, GAMEPANELNAME);
    }
    
    public void goToPlayersPanel() {
        contentPaneCardLayout.show(this, PLAYERGRIDNAME);
    }
    
    public void goToScorePanel() {
        contentPaneCardLayout.show(this, SCOREPANELNAME);
    }

    public void setPlayersPanelSelectable(boolean isPlayerPanelSelectable) {
        this.playersPanel.setSelectable(isPlayerPanelSelectable);
    }
    
    public void displayNextGame(){
    	this.gamePanelCardLayout.next(this.gamePanel);
    }

	public void displayPreviousGame() {
    	this.gamePanelCardLayout.previous(this.gamePanel);
	}
	
	public Board getDisplayedBoard(){
		for (Component comp : this.gamePanel.getComponents()) {
		    if (comp.isVisible() == true) {
		        return (Board) comp;
		    }
		}
		return null;
	}

	public void removeDisplayedGame() {
		for (Component comp : this.gamePanel.getComponents()) {
		    if (comp.isVisible() == true) {
		        this.gamePanel.remove(comp);
		    }
		}
	}
	
	
	public void refreshScorePanel(List<Player> players){
		scorePanel.feedTable(players);
	}
}
