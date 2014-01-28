package com.enclos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScoreFrame extends JFrame {

	// in case we need it later
	private JFrame parent = null;
	private Dimension size = null;

	public ScoreFrame(JFrame parent) {

		this.parent = parent;
		this.size = parent.getSize();
		setTitle("Scores");
		setSize(this.size);
		setAlwaysOnTop(true);
		setUndecorated(true);
		setOpacity(0.50f);
		setEnabled(false);
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(new JLabel("Scores des joueurs : "),BorderLayout.CENTER);
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
