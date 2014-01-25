package com.enclos.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Board extends JPanel {

	public Board() {
		final HexagonPanel hex = new HexagonPanel();
		add(hex);
		// BridgePanel bridge = new BridgePanel();
		// add(hex);
		// add(bridge);

		// setComponentZOrder(hex, 1);
		// setComponentZOrder(bridge, 0);
		// System.out.println(this.getParent());

		//when the frame is resized, the board is resized too
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				//we keep the board square
				int min = Math.min(Board.this.getParent().getWidth(), Board.this
						.getParent().getHeight());
				setSize(min, min);

				Dimension size = hex.getPreferredSize();
				
				//first hexagon centered
				hex.setBounds(getWidth() / 2 - size.width / 2, getHeight() / 2
						- size.height / 2, size.width, size.height);
			}
		});
		//absolute positioning, no layout manager
		setLayout(null);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//differentiate the board from the frame
		setBackground(Color.GRAY);
	}

}
