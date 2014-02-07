package com.enclos.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Box.Filler;

import com.enclos.component.Bridge;
import com.enclos.component.Hexagon;
import com.enclos.component.Shape;
import com.enclos.controller.State;
import com.enclos.data.Direction;

import components.BridgePanel;
import components.HexagonPanel;

//board de test
public class Board extends JPanel {

	private List<Shape> cells = new LinkedList<Shape>();
	private JFrame parent = null;
	private int size = 3;
	private Image background = null;

	// TODO changer cette merde
	private Shape lastCell = null;

	// on met le frame en constructeur juste pour l'exemple
	public Board(JFrame parent, int size) {
		this.parent = parent;
		this.size = size;
		this.background = new ImageIcon("resources/grass.jpg").getImage();

		generateCells();

		// on resize les composants
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);

				for (Shape shape : cells) {
					shape.setSize(getWidth() / 30);
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

	private void generateCells() {

		for (int k = 0; k <= this.size; k++)
			for (int l = 0; l < k * 6; l++) {
				cells.add(new Hexagon(k, l));
				// System.out.println("Niveau : " + k + " rang : " + l);
			}

		//TODO LISTE DES VOISINS
		/*
		 * 
		 * 
		 * Boucle sur la liste des cells, choper le milieu de la shape (point 3 + shape width/2) et avec ce point tu applique chaque direction pour décaler le point jusqu'a la forme du voisin
		 */

	}

	private int calculateNumberOfBridges() {

		int nbBridges = 0; //nothing to 
		for (int i = 1; i < 3 * this.size; i += 3) {
			nbBridges += 6 * i;
		}
		nbBridges += this.size * 6;

		return nbBridges;
	}

	// on récupère toutes les shapes et on les dessine en fonction de la shape
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(this.background, 0, 0, null);

		drawHexagons(g);
		
	}

	private void drawHexagons(Graphics g) {
		int counter;
		Shape currentCell = null;
		Direction currentDirection = null;

		// drawing center
		drawCenterCell(g);

		for (int i = 1; i <= this.size; i++) {
			if (lastCell != null) {
				lastCell = getCorrespondingCell(i - 1, 0);
			} else {
				lastCell = cells.get(0);
			}

			currentCell = getCorrespondingCell(i, 0);
			drawCell(g, currentCell, lastCell, Direction.NORTH);

			currentDirection = Direction.SOUTH_EAST;
			counter = 0;

			for (int j = 1; j < i * 6; j++) {
				currentCell = getCorrespondingCell(i, j);
				if (counter == i) {
					currentDirection = currentDirection.getNext();
					counter = 0;
				}

				drawCell(g, currentCell, lastCell, currentDirection);

				drawBridge(g,lastCell, currentCell);

				counter++;
			}
		}

	}

	private void drawBridge(Graphics g, Shape lastCell, Shape currentCell) {
		
		
	}

	private Shape getCorrespondingCell(int i, int j) {
		for (Shape shape : cells) {
			if (((Hexagon) shape).getVirtualIndex().getX() == i
					&& ((Hexagon) shape).getVirtualIndex().getY() == j)
				return shape;
		}
		return null;
	}

	private void drawCenterCell(Graphics g) {
		Polygon polygon = new Polygon();
		cells.get(0).clearPointList();
		for (int i = 0; i < 6; i++) {
			Point point = new Point((int) (this.getWidth() / 2 + cells.get(0)
					.getSize() * Math.cos(i * 2 * Math.PI / 6)),
					(int) (this.getHeight() / 2 + cells.get(0).getSize()
							* Math.sin(i * 2 * Math.PI / 6)));
			polygon.addPoint(point.x, point.y);
			cells.get(0).addPointToList(point);

		}
		g.fillPolygon(polygon);
		cells.get(0).setPolygon(polygon);

		Hexagon.setDistanceBetweenHexagons(cells.get(0));
		System.out.println(Hexagon.getDistanceBetweenHexagons());
	}

	private void drawCell(Graphics g, Shape shapeToDraw, Shape lastDrawnShape,
			Direction direction) {
		Polygon polygon = new Polygon();
		int ratio = ((Hexagon) shapeToDraw).getVirtualIndex().y;
		Direction currentDirection = direction;

		// TODO AVERAGE LENGTH
		System.out.println(Hexagon.getDistanceBetweenHexagons());
		AffineTransform currentTransform = currentDirection
				.getDirection(Hexagon.getDistanceBetweenHexagons());

		PathIterator pointsIterator = lastDrawnShape.getPolygon()
				.getPathIterator(currentTransform);

		while (!pointsIterator.isDone()) {
			double[] xy = new double[2];
			pointsIterator.currentSegment(xy);
			if (xy[0] == 0 && xy[1] == 0)
				break;
			polygon.addPoint((int) xy[0], (int) xy[1]);
			pointsIterator.next();
		}
		g.fillPolygon(polygon);
		shapeToDraw.setPolygon(polygon);

		// TODO changer cette merde
		this.lastCell = shapeToDraw;

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
