package com.enclos.ui;

import java.awt.GridLayout;
import java.util.ArrayList;
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

	public static Map<String,String> display() {
		Map<String,String> settings = new HashMap<>();
		
		String[] players = { "Two", "Three", "Four", "Five" };
		JComboBox combo = new JComboBox(players);
		JTextField boardSize = new JTextField("3");
		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(new JLabel("Players :"));
		panel.add(combo);
		panel.add(new JLabel("Board Size:"));
		panel.add(boardSize);

		JCheckBox closeOtherGames = new JCheckBox("Close ongoing games");
		closeOtherGames.setSelected(true);
		panel.add(closeOtherGames);
		
		int result = JOptionPane.showConfirmDialog(null, panel, "New Game",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			settings.put("players",combo.getSelectedItem().toString());
			settings.put("boardSize",boardSize.getText());
			settings.put("close",closeOtherGames.isSelected() == true ? "close":"");
		} else {
			return null;
		}
		return settings;
	}
}
