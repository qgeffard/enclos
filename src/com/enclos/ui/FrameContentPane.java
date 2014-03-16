package com.enclos.ui;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class FrameContentPane extends JPanel{
	private Image background = null;
	
	public FrameContentPane(){
		this.background = new ImageIcon("resources/grass.jpg").getImage();
		this.setLayout(new FlowLayout());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		//g.drawImage(this.background, 0, 0, null);			
	}
}
