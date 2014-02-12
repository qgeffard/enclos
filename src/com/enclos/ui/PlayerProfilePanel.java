package com.enclos.ui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.enclos.data.Player;

//public class PlayerProfilePanel extends JPanel{
//
//	private Player player = null;
//	private JPanel profilePicPanel = null;
//	
//	public PlayerProfilePanel(Player player){
//		this.player = player;
//		
//		this.add(new JLabel(player.getFirstName()));
//		this.add(new JLabel(player.getLastName()));
//		this.add(new JLabel(String.valueOf(player.getAge())));
//		profilePicPanel = new JPanel();
//	}
//	
//	@Override
//	public void paintComponent(Graphics g){
//		g.drawImage(this.player.getProfilePicture(), 0, 0, null);
//	//	setBackground(Color.red);
//	}
//}

public class PlayerProfilePanel extends JPanel{

	private Image image = null;
	
	public PlayerProfilePanel(Image image){
		this.image = image;
	}
	
	@Override
	public void paintComponent(Graphics g){
		g.drawImage(this.image, 0, 0, null);
	//	setBackground(Color.red);
	}
}
