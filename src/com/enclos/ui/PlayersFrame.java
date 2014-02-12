package com.enclos.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.enclos.data.Player;

public class PlayersFrame extends JFrame {

	// on met la liste là pour le moment, pour les tests
	private static List<Player> playersList = new ArrayList<Player>();

	JPanel contentPane = null;
	JPanel playersPanel = null;
	CardLayout playersLayout = null;

	public PlayersFrame() {

		this.contentPane = new JPanel();
		this.setContentPane(this.contentPane);

		this.contentPane.setLayout(new BoxLayout(this.contentPane,
				BoxLayout.Y_AXIS));

		this.playersLayout = new CardLayout();
		this.playersPanel = new JPanel();
		this.playersPanel.setLayout(this.playersLayout);
		this.contentPane.add(this.playersPanel);

		generateMenu();
		if (playersList.size() == 0) {
			this.playersPanel.add(new JLabel("No players registered"));
		} else {
			for (Player player : playersList) {
				JPanel panel = new JPanel();
				panel.add(new JLabel(player.getFirstName()));
				panel.add(new JLabel(player.getLastName()));
				panel.add(new JLabel(String.valueOf(player.getAge())));
				this.playersPanel.add(panel);
			}
			generateCommands();
		}

		this.setLocationRelativeTo(null);
		this.setSize(500, 500);
		this.setVisible(true);

	}

	private void generateMenu() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.black);

		JMenu menu = new JMenu("Players");
		menu.setForeground(Color.white);
		JMenuItem newPlayerItem = new JMenuItem("Create profile");
		JMenuItem editPlayerItem = new JMenuItem("Edit shown profile");
		menu.add(newPlayerItem);
		menu.add(editPlayerItem);

		newPlayerItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Player newPlayer = NewPlayerForm.display(PlayersFrame.this);
				if (newPlayer != null) {
					playersList.add(newPlayer);
					System.out.println(newPlayer.toString());
				}
			}
		});

		menuBar.add(menu);
		setJMenuBar(menuBar);
	}

	private void generateCommands() {

		JPanel buttonsPanel = new JPanel();
		JButton next = new JButton("Next player");
		JButton prev = new JButton("Previous player");
		buttonsPanel.add(prev);
		buttonsPanel.add(next);

		next.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PlayersFrame.this.playersLayout
						.next(PlayersFrame.this.playersPanel);
			}
		});

		prev.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PlayersFrame.this.playersLayout
						.previous(PlayersFrame.this.playersPanel);
			}
		});
		this.contentPane.add(buttonsPanel);
	}
}
