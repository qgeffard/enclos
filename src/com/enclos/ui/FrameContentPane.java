package com.enclos.ui;

import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.enclos.data.Player;

public class FrameContentPane extends JPanel {
	private CardLayout cardLayout;
	private JPanel gamePanel;
	private JPanel playersPanel;
	private Enclos parent;
	
	public FrameContentPane(Enclos parent) {
		this.parent = parent;
		
		cardLayout = new CardLayout();
		this.setLayout(cardLayout);

		gamePanel = new JPanel();
		gamePanel.setLayout(new FlowLayout());

		playersPanel = new JPanel();
		playersPanel.setLayout(new FlowLayout());

		JButton b1 = new JButton("Add player");
		b1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Player newPlayer = createNewPlayer();
				PlayerProfilePanel playerProfile = new PlayerProfilePanel(newPlayer);
				FrameContentPane.this.parent.GetPlayers().add(newPlayer);
				FrameContentPane.this.playersPanel.add(playerProfile);
				
				revalidate();
			}

			private Player createNewPlayer() {
				Player newPlayer = null;
				JTextField firstName = new JTextField();
				JTextField lastName = new JTextField();
				JTextField age = new JTextField();
				final JComponent[] inputs = new JComponent[] {
						new JLabel("First Name"),
						firstName,
						new JLabel("Last Name"),
						lastName,
						new JLabel("Age"),
						age
					};
				JOptionPane.showMessageDialog(null, inputs, "Add a new player", JOptionPane.PLAIN_MESSAGE);
			try{
				 newPlayer = new Player(firstName.getText(), lastName.getText(), Integer.parseInt(age.getText()));
			}catch(Exception e){
				newPlayer = new Player(firstName.getText(), lastName.getText(), 0);
			}
				return newPlayer;
			}
		});

		playersPanel.add(b1);
		this.add(gamePanel);
		this.add(playersPanel);
	}

	@Override
	public void paintComponent(Graphics g) {
		// g.drawImage(this.background, 0, 0, null);
	}

	public void addToGamePanel(Board board) {
		gamePanel.add(board);
	}

	public void switchPanel() {
		cardLayout.next(this);
	}
}
