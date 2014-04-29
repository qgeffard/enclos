package com.enclos.ui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class IntroFrame extends JFrame {

	// in case we need it later
	private JFrame parent = null;
	private Dimension size = null;

	public IntroFrame(JFrame parent) {

		this.parent = parent;
		this.size = parent.getSize();
		setSize(this.size);
		setAlwaysOnTop(true);
		setUndecorated(true);
		

		Image image =Toolkit.getDefaultToolkit().createImage("resources/image/intro.gif");
        ImageIcon icon = new ImageIcon(image);
		JLabel label = new JLabel(icon);
		this.getContentPane().add(label);
        
        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);

	}

}
