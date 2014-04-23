package com.enclos.ui;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NewGameForm extends JOptionPane {

	public static Map<String, String> display(int nbPlayers) {
		Map<String, String> settings = new HashMap<>();

		List<String> nbSheepsPerPlayer = Arrays.asList("1", "2", "3", "4", "5");
		JComboBox<String> sheepComboBox = new JComboBox(
				nbSheepsPerPlayer.toArray());

		JTextField boardSize = new JTextField("3");
		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(new JLabel("Players :"));
		panel.add(new JLabel(String.valueOf(nbPlayers)));
		panel.add(new JLabel("Sheeps per player :"));
		panel.add(sheepComboBox);
		panel.add(new JLabel("Board Size :"));
		panel.add(boardSize);

		JCheckBox closeOtherGames = new JCheckBox("Close ongoing games");
		closeOtherGames.setSelected(true);
		panel.add(closeOtherGames);

		int result = JOptionPane.showConfirmDialog(null, panel, "New Game",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			settings.put("nbSheepPerPlayer", sheepComboBox.getSelectedItem().toString());
			settings.put("boardSize", boardSize.getText());
			settings.put("close", closeOtherGames.isSelected() == true
					? "close"
					: "");
		} else {
			return null;
		}
		return settings;
	}
}
