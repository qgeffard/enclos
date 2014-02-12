package com.enclos.ui;

import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.enclos.data.Player;

public class NewPlayerForm extends JOptionPane {

	private String path = "";

	public static Player display(PlayersFrame playersFrame) {

		JTextField playerFirstName = new JTextField("Jean-Michel");
		JTextField playerLastName = new JTextField("De la betteraviere");
		JTextField playerAge = new JTextField("21");
		JButton pictureButton = new JButton("Choose a profil picture");
		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(new JLabel("First Name :"));
		panel.add(playerFirstName);
		panel.add(new JLabel("Last Name :"));
		panel.add(playerLastName);
		panel.add(new JLabel("Age :"));
		panel.add(playerAge);
		panel.add(pictureButton);

		final FileDialog fileDialog = new FileDialog(new JFrame(),
				"Choose picture", FileDialog.LOAD);
		pictureButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fileDialog.setVisible(true);
			}
		});

		int result = JOptionPane.showConfirmDialog(null, panel, "New Game",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {

			if (fileDialog.getFile() != null) {
				File file = new File(fileDialog.getFile());
				return new Player(playerFirstName.getText(),
						playerLastName.getText(), Integer.valueOf(playerAge
								.getText()), file.getAbsolutePath());
			} else {
				return new Player(playerFirstName.getText(),
						playerLastName.getText(), Integer.valueOf(playerAge
								.getText()));
			}
		}else {
			return null;
		}
	}
}