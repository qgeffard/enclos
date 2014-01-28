package com.enclos.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Box.Filler;

import com.enclos.component.Bridge;
import com.enclos.component.Hexagon;
import com.enclos.component.Shape;
import com.enclos.controller.State;

//board de test
public class Board extends JPanel {

	private List<Shape> cells = new ArrayList<Shape>();
	private JFrame parent = null;

	// on met le frame en constructeur juste pour l'exemple
	public Board(JFrame parent) {
		this.parent = parent;

		generateCells();

		// on resize les composants
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				for (Shape shape : cells) {
					shape.setSize(getWidth() / 20);
				}
			}
		});

		// le clicklistener (contains non implementé)
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);

				Shape clickedShape = null;

				for (Shape shape : cells) {
					if (shape.contains(e.getX(), e.getY()))
						clickedShape = shape;
				}
				if (clickedShape != null)
					clickedShape.warn();

			}
		});
	}

	// on génère des cells bidon, le rotate est à 0 sinon ca fait du bullshit, à
	// revoir la connerie de translation avant/après rotate
	private void generateCells() {
		for (int i = 10; i < 50; i += 10) {
			cells.add(new Hexagon(i, i, parent.getWidth() / 20));
		}
		for (int i = 100; i < 500; i += 100) {
			cells.add(new Bridge(i, i, parent.getWidth() / 20, 48));
		}

	}

	// on récupère toutes les shapes et on les dessine en fonction de la shape
	@Override
	public void paintComponent(Graphics g) {
		for (Shape shape : cells) {
			if (shape instanceof Hexagon) {
				drawHexagon(g, shape);
			} else {
				drawBridge((Graphics2D) g, shape);

			}
		}
	}

	private void drawHexagon(Graphics g, Shape shape) {
		Polygon polygon = new Polygon();

		for (int i = 0; i < 6; i++) {
			Point point = new Point((int) (shape.getSize() + shape.getSize()
					* Math.cos(i * 2 * Math.PI / 6)),
					(int) (shape.getSize() + shape.getSize()
							* Math.sin(i * 2 * Math.PI / 6)));
			polygon.addPoint(point.x, point.y);
		}
		shape.setPolygon(polygon);

		g.fillPolygon(polygon);
	}

	private void drawBridge(Graphics2D g, Shape shape) {
		// We create a rectangle from the bridge's attributes
		Rectangle rectangle = new Rectangle(shape.getX(), shape.getY(),
				shape.getSize(), shape.getSize());
		// we create a polygon by rotating the rectangle
		Polygon polygon = rotateRectangle(shape, rectangle);
		shape.setPolygon(polygon);

		// we draw the polygon
		g.fillPolygon(polygon);

	}

	private Polygon rotateRectangle(Shape shape, Rectangle rectangle) {
		// we create a rotation
		AffineTransform at = AffineTransform.getRotateInstance(
				((Bridge) shape).getRotation(), rectangle.getCenterX(),
				rectangle.getCenterY());

		Polygon polygon = new Polygon();

		// for each point of the rectangle we create another point corresponding
		// to the new rotated point
		PathIterator pointsIterator = rectangle.getPathIterator(at);
		while (!pointsIterator.isDone()) {
			double[] xy = new double[2];
			pointsIterator.currentSegment(xy);
			polygon.addPoint((int) xy[0], (int) xy[1]);

			pointsIterator.next();
		}
		return polygon;
	}

}
