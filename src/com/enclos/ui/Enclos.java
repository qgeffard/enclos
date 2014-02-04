package com.enclos.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.enclos.component.Hexagon;
import com.enclos.component.Shape;
import com.enclos.controller.State;

public class Enclos extends JFrame {

	private State state = null;
	private ScoreFrame scoreFrame = null;

	public Enclos() {

		this.state = new State(this);
		this.scoreFrame = new ScoreFrame(this);
		Toolkit.getDefaultToolkit().setDynamicLayout(false);
		setTitle("Jeu de l'enclos");

		// à revoir
		setMaximumSize(getScreenMaximumSize());
		//enabled tab listener	
		setFocusTraversalKeysEnabled(false);
		setSize(500, 500);
		setContentPane(new Board(this,3));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		generateMenu();

		setVisible(true);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				state.setSize(getSize(), true);
				scoreFrame.setSize(getSize());
			}
		});

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if (e.getKeyCode() == KeyEvent.VK_TAB) {
					if (!scoreFrame.isVisible()) {
						scoreFrame.setLocation(getLocation());
						scoreFrame.setVisible(true);
						requestFocus();
					}
					System.out.println("pressed");
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				super.keyPressed(e);
				if (e.getKeyCode() == KeyEvent.VK_TAB) {
					scoreFrame.setVisible(false);

					System.out.println("released");
				}
			}
		});

	}

	private void generateMenu() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.black);

		JMenu menu = new JMenu("Jeu");
		menu.setForeground(Color.white);
		JMenuItem scoreItem = new JMenuItem("Scores");
		JMenuItem playerItem = new JMenuItem("Players");
		menu.add(scoreItem);
		menu.add(playerItem);
		menuBar.add(menu);
		setJMenuBar(menuBar);
		
//		scoreItem.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				scoreFrame.setVisible(true);
//				
//			}
//		});

	}

	private Dimension getScreenMaximumSize() {
		Dimension maxDimension = null;
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		if (screenDimension.getHeight() > screenDimension.getWidth())
			return new Dimension(screenDimension.width, screenDimension.width);
		else
			return new Dimension(screenDimension.height, screenDimension.height);
	}

}