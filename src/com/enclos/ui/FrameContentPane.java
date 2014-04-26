package com.enclos.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class FrameContentPane extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel gamePanel;
    private final PlayersMainPanel playersPanel;
    
    private final String GAMEPANELNAME = "GamePanel";

    private final String PLAYERGRIDNAME = "PlayersGrid";

    private Enclos parent = null;
    
    public FrameContentPane(Enclos parent) {
    	this.parent = parent;
    	
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());
        
        playersPanel = new PlayersMainPanel(parent, this);

        this.add(gamePanel, GAMEPANELNAME);
        this.add(playersPanel, PLAYERGRIDNAME);

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
        cardLayout.next(this);
    }

    public void goToGameGrid() {
        cardLayout.show(this, GAMEPANELNAME);
    }
    
    public void goToPlayersGrid() {
        cardLayout.show(this, PLAYERGRIDNAME);
    }

    public void setPlayersPanelSelectable(boolean isPlayerPanelSelectable) {
        this.playersPanel.setSelectable(isPlayerPanelSelectable);
    }
}
