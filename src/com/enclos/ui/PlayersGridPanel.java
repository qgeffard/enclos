package com.enclos.ui;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class PlayersGridPanel extends JPanel {

	private List<PlayerProfilePanel> profiles = null;

	public PlayersGridPanel() {
		profiles = new ArrayList<PlayerProfilePanel>();
		this.setLayout(new GridLayout());
	}
	
	public void addPlayerProfile(PlayerProfilePanel playerProfilePanel){
		add(playerProfilePanel);
		profiles.add(playerProfilePanel);
	}
	
	public void setSelectable(boolean isSelectable){
		for(PlayerProfilePanel playerProfilePanel : profiles){
			playerProfilePanel.setSelectable(isSelectable);
		}
	}

}
