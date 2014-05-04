package org.enclos.ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.enclos.data.Human;
import org.enclos.data.PlayerProfilePanelState;

/**
 * @author Clement CARREAU
 * @author Quentin GEFFARD
 * @author Julien TELA
 */

public class PlayersGridPanel extends JPanel {

	private List<PlayerProfilePanel> profiles = null;
	
	/**
	 * Constructor of PlayersGridPanel class
	 */
	public PlayersGridPanel() {
        
		profiles = new ArrayList<PlayerProfilePanel>();
		
		this.setLayout(new GridLayout((int) Math.ceil(this.profiles.size()/2),4));
	}
	
	/**
	 * add profile panel to the player profile
	 * @param playerProfilePanel
	 */
	public void addPlayerProfile(PlayerProfilePanel playerProfilePanel) {
		add(playerProfilePanel);
		profiles.add(playerProfilePanel);
	}
	
	/**
	 * remove profile panel from the player profile
	 * @param playerProfilePanel
	 */
	public void removePlayerProfile(PlayerProfilePanel playerProfilePanel) {
		remove(playerProfilePanel);
		profiles.remove(playerProfilePanel);
	}
	
	/**
	 * Getter of profiles attributes
	 * @return list of profiles
	 */
	public List<PlayerProfilePanel> getProfiles() {
		return profiles;
	}
	
	/**
	 * Get the number of player selected 
	 * @return number of player selected 
	 */
	public int getPlayerSelectedCount() {
		int count = 0;

		for (PlayerProfilePanel profile : profiles) {
			if (profile.getState() == PlayerProfilePanelState.SELECTED) {
				count++;
			}
		}

		return count;
	}

	/**
	 * get list of player was selected
	 * @return player list
	 */
	public List<Human> getPlayersSelected() {

		List<Human> selectedPlayers = new ArrayList<Human>();

		for (PlayerProfilePanel profile : profiles) {
			if (profile.getState() == PlayerProfilePanelState.SELECTED) {
				selectedPlayers.add(profile.getPlayer());
			}
		}

		return selectedPlayers;
	}
	
	/**
	 * reset
	 */
	public void reset() {
		for (PlayerProfilePanel profile : profiles)
		{
			profile.unSelect();
		}
	}
}
