package com.enclos.ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.enclos.data.Human;
import com.enclos.data.PlayerProfilePanelState;

public class PlayersGridPanel extends JPanel {

	private List<PlayerProfilePanel> profiles = null;

	public PlayersGridPanel() {
        
		profiles = new ArrayList<PlayerProfilePanel>();
		
		this.setLayout(new GridLayout((int) Math.ceil(this.profiles.size()/2),4));
	}

	public void addPlayerProfile(PlayerProfilePanel playerProfilePanel) {
		add(playerProfilePanel);
		profiles.add(playerProfilePanel);
	}
	
	public void removePlayerProfile(PlayerProfilePanel playerProfilePanel) {
		remove(playerProfilePanel);
		profiles.remove(playerProfilePanel);
	}

	public List<PlayerProfilePanel> getProfiles() {
		return profiles;
	}

	public int getPlayerSelectedCount() {
		int count = 0;

		for (PlayerProfilePanel profile : profiles) {
			if (profile.getState() == PlayerProfilePanelState.SELECTED) {
				count++;
			}
		}

		return count;
	}

	public List<Human> getPlayersSelected() {

		List<Human> selectedPlayers = new ArrayList<Human>();

		for (PlayerProfilePanel profile : profiles) {
			if (profile.getState() == PlayerProfilePanelState.SELECTED) {
				selectedPlayers.add(profile.getPlayer());
			}
		}

		return selectedPlayers;
	}

	public void reset() {
		for (PlayerProfilePanel profile : profiles)
		{
			profile.unSelect();
		}
	}
}
