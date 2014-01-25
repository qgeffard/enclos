package com.enclos.component;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import javax.swing.JPanel;

import com.enclos.controller.HexagonListener;

public class HexagonPanel extends JPanel {

	// Default size
	// TODO dynamic from the board's size
	private int width = 50;
	private int height = 50;

	public HexagonPanel() {
		setOpaque(false);
		// setComponentZOrder(this.getParent(), 0);
		addMouseListener(new HexagonListener());

	}

	// return the new hexagon size from the new Board's size
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// hexagon's size depending of the board's size
		int frameWidth = (getParent().getBounds().width) / 20;
		int frameHeight = (getParent().getBounds().height) / 20;

		Polygon tile = new Polygon();

		for (int i = 0; i < 6; i++) {
			Point point = new Point((int) (frameWidth + frameWidth
					* Math.cos(i * 2 * Math.PI / 6)),
					(int) (frameHeight + frameHeight
							* Math.sin(i * 2 * Math.PI / 6)));
			tile.addPoint(point.x, point.y);

		}

		g.drawPolygon(tile);
		g.fillPolygon(tile);

		// since the jpanel is a square, we need to keep the max(width,height)
		// of the hexagon to surround it, otherwise it would be cut
		int maxLength = Math.max(tile.getBounds().width,
				tile.getBounds().height);
		this.width = maxLength;
		this.height = maxLength;

	}
}
