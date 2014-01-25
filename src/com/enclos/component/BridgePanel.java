package com.enclos.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.enclos.data.BridgeRotation;

public class BridgePanel extends JPanel {

	private int rotation = 0;
	private static int  edge = 0;

	// Default size
	// TODO dynamic from the board's size
	private int width = 100;
	private int height = 100;

	private static final long serialVersionUID = 1L;

	
	public BridgePanel() {
		setOpaque(false);
		// setComponentZOrder(this.getParent(), 1);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("BRIDGE");
			}
		});
	}

	public void setRotation(int rotation){
		this.rotation = rotation;
	}
	public static void setEdge(int edge) {
		BridgePanel.edge = edge;
	}

	// return the new bridge size from the new Board's size
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.rotate(Math.toRadians(rotation), getWidth()/2, getHeight()/2);
		
		g2d.setColor(Color.yellow);
		g2d.fillRect(getWidth()/2-width/2, getHeight()/2-height/2, edge, edge);
		setBackground(Color.red);
	}
}
