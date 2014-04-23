package com.enclos.ui;

import java.awt.CardLayout;
import java.awt.FlowLayout;

import javax.swing.JPanel;

public class FrameContentPane extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel gamePanel;
    private final PlayersMainPanel playersPanel;
    private final Enclos parent;

    private final String GAMEPANELNAME = "GamePanel";

    private final String PLAYERGRIDNAME = "PlayersGrid";

    public FrameContentPane(Enclos parent) {
        this.parent = parent;

        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        gamePanel = new JPanel();
        gamePanel.setLayout(new FlowLayout());

        playersPanel = new PlayersMainPanel(parent, this);

        this.add(gamePanel, GAMEPANELNAME);
        this.add(playersPanel, PLAYERGRIDNAME);
    }

    public void addToGamePanel(Board board) {
        gamePanel.add(board);
    }

    public void resetGamePanel() {
        gamePanel.removeAll();
    }

    public void switchPanel() {
        cardLayout.next(this);
    }

    public void goToPlayersGrid() {
        cardLayout.show(this, PLAYERGRIDNAME);
    }

    @Override
    public Enclos getParent() {
        return this.parent;
    }

    public void setPlayersPanelSelectable(boolean isPlayerPanelSelectable) {
        this.playersPanel.setSelectable(isPlayerPanelSelectable);
    }
}
