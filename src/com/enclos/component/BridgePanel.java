package com.enclos.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class BridgePanel extends JPanel {

	public BridgePanel() {
		setOpaque(true);
		//setComponentZOrder(this.getParent(), 1);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("BRIDGE");
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		//TODO setSize someplace else
		super.paintComponent(g);
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 30, 30);
		setBackground(Color.red);
		setSize(30, 30);

	}
}
