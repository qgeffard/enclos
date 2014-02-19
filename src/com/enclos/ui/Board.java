package com.enclos.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import com.enclos.component.Bridge;
import com.enclos.component.Hexagon;
import com.enclos.component.Shape;
import com.enclos.component.Sheep;
import com.enclos.data.Direction;

//board de test
public class Board extends JPanel {

	private List<Hexagon> hexagons = new LinkedList<Hexagon>();
	private List<Bridge> bridges = new LinkedList<Bridge>();
	private List<Sheep> sheeps = new LinkedList<Sheep>();
	private List<Shape> shapes = new LinkedList<Shape>();
	private int size = 3;
	private int nbSheep = 6;

	// TODO changer cette merde
	private Hexagon lastCell = null;

	// on met le frame en constructeur juste pour l'exemple
	public Board(int size, int nbSheep) {
		this.nbSheep = nbSheep;
		this.size = size;
		generateCells();

		// on resize les composants
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);

				for (Shape shape : hexagons) {
					shape.setSize(getWidth() / 30);
				}
			}
		});

		// le clicklistener (contains non implement�)
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				super.mouseClicked(event);

				for (Shape shape : shapes) {
					if (shape.contains(event.getX(), event.getY()))
						System.out.println(shape);
				}
			}
		});
	}

	@Override
	public Dimension getPreferredSize() {
		Container parent = getParent();
		int numberOfGames = getParent().getComponentCount();
		return new Dimension(parent.getWidth() / numberOfGames,
				parent.getHeight() / numberOfGames);
	}

	private void generateCells() {
		// HEXAGONS
		hexagons.add(new Hexagon(0, 0));
		for (int level = 1; level <= this.size; level++) {
			for (int rang = 0; rang < level * 6; rang++) {
				hexagons.add(new Hexagon(level, rang));
			}
		}

		// BRIDGES
		int nbBridges = calculateNumberOfBridges();
		for(int i=0; i<nbBridges; i++){
			this.bridges.add(new Bridge());
		}
		
		// SHEEPS
		for (int i = 0; i < this.nbSheep; i++) {
			this.sheeps.add(new Sheep());
		}
	}

	private int calculateNumberOfBridges() {

		int nbBridges = 0;
		for (int i = 1; i < 3 * this.size; i += 3) {
			nbBridges += 6 * i;
		}
		nbBridges += this.size * 6;
		return nbBridges;
	}

	@Override
	public void paintComponent(Graphics g) {
		// super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		drawHexas(g2);
		drawBridges(g2);
		drawSheep(g2);

		this.shapes.clear();
		this.shapes.addAll(this.hexagons);
		this.shapes.addAll(this.bridges);
		this.shapes.addAll(this.sheeps);

	}

	private void drawHexas(Graphics2D g) {
		g.setColor(Color.BLACK);
		int counter;
		Hexagon currentCell = null;
		Direction currentDirection = null;

		// drawing center
		drawCenterCell(g);

		for (int i = 1; i <= this.size; i++) {
			if (lastCell != null) {
				lastCell = getCorrespondingCell(i - 1, 0);
			} else {
				lastCell = hexagons.get(0);
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

				if (currentCell != null && lastCell != null)
					drawCell(g, currentCell, lastCell, currentDirection);

				counter++;
			}
		}
	}

	private void drawBridges(Graphics2D g) {
		g.setColor(Color.YELLOW);
		int i = 0;
		// TODO LISTE DES VOISINS
		/*
		 * Boucle sur la liste des cells, choper le milieu de la shape (point 3
		 * + shape width/2) et avec ce point tu applique chaque direction pour
		 * d�caler le point jusqu'a la forme du voisin
		 */
		for (Hexagon hexa : hexagons) {
			Point2D centerOfShape = new Point((int) hexa.getPointList().get(3).getX()+ Math.round(hexa.getSize() / 2), (int) hexa.getPointList().get(3).getY());

			for (Direction direction : Direction.values()) {
				Polygon polygon = new Polygon();
				AffineTransform currentTransform = direction
						.getDirection(Hexagon.getDistanceBetweenHexagons());

				Point2D targetPoint = new Point();
				targetPoint = currentTransform.transform(centerOfShape,
						targetPoint);

				for (Hexagon targetHexa : hexagons) {
					if (targetHexa.contains((int) targetPoint.getX(), (int) targetPoint.getY())) {
						switch (direction.name()) {
						case "SOUTH_EAST":
							polygon.addPoint(targetHexa.getPointList().get(3).x,targetHexa.getPointList().get(3).y);
							polygon.addPoint(targetHexa.getPointList().get(4).x,targetHexa.getPointList().get(4).y);
							polygon.addPoint(hexa.getPointList().get(0).x, hexa.getPointList().get(0).y);
							polygon.addPoint(hexa.getPointList().get(1).x, hexa.getPointList().get(1).y);
							break;
						case "SOUTH":
							polygon.addPoint(
									targetHexa.getPointList().get(4).x,
									targetHexa.getPointList().get(4).y);
							polygon.addPoint(
									targetHexa.getPointList().get(5).x,
									targetHexa.getPointList().get(5).y);
							polygon.addPoint(hexa.getPointList().get(1).x, hexa
									.getPointList().get(1).y);
							polygon.addPoint(hexa.getPointList().get(2).x, hexa
									.getPointList().get(2).y);
							break;
						case "SOUTH_WEST":
							polygon.addPoint(
									targetHexa.getPointList().get(5).x,
									targetHexa.getPointList().get(5).y);
							polygon.addPoint(
									targetHexa.getPointList().get(0).x,
									targetHexa.getPointList().get(0).y);
							polygon.addPoint(hexa.getPointList().get(2).x, hexa
									.getPointList().get(2).y);
							polygon.addPoint(hexa.getPointList().get(3).x, hexa
									.getPointList().get(3).y);
							break;
						case "NORTH_WEST":
							polygon.addPoint(
									targetHexa.getPointList().get(0).x,
									targetHexa.getPointList().get(0).y);
							polygon.addPoint(
									targetHexa.getPointList().get(1).x,
									targetHexa.getPointList().get(1).y);
							polygon.addPoint(hexa.getPointList().get(3).x, hexa
									.getPointList().get(3).y);
							polygon.addPoint(hexa.getPointList().get(4).x, hexa
									.getPointList().get(4).y);
							break;
						case "NORTH":
							polygon.addPoint(
									targetHexa.getPointList().get(1).x,
									targetHexa.getPointList().get(1).y);
							polygon.addPoint(
									targetHexa.getPointList().get(2).x,
									targetHexa.getPointList().get(2).y);
							polygon.addPoint(hexa.getPointList().get(4).x, hexa
									.getPointList().get(4).y);
							polygon.addPoint(hexa.getPointList().get(5).x, hexa
									.getPointList().get(5).y);
							break;
						case "NORTH_EAST":
							polygon.addPoint(
									targetHexa.getPointList().get(2).x,
									targetHexa.getPointList().get(2).y);
							polygon.addPoint(
									targetHexa.getPointList().get(3).x,
									targetHexa.getPointList().get(3).y);
							polygon.addPoint(hexa.getPointList().get(5).x, hexa
									.getPointList().get(5).y);
							polygon.addPoint(hexa.getPointList().get(0).x, hexa
									.getPointList().get(0).y);
							break;
						}
						
						Bridge bridge = new Bridge(); 				
						bridge.setVirtualIndex(hexa.getVirtualIndex(),targetHexa.getVirtualIndex());
						
						// check if it already exist
						boolean bridgeAlreadyExist = false;
						for (Bridge bridgeloop : this.bridges) {
							bridgeAlreadyExist = bridge.equals(bridgeloop);
							if (bridgeAlreadyExist)
								break;
						}

						// add to the bridge list if no
						if (!bridgeAlreadyExist) {
							bridges.get(i).setPolygon(polygon);
							bridges.get(i).setVirtualIndex(hexa.getVirtualIndex(),targetHexa.getVirtualIndex());
							i++;
							g.fillPolygon(polygon);
						}

					}
				}
			}
		}

	}

	private void drawSheep(Graphics2D g) {
		for (int i = 1; i < this.nbSheep + 1; i++) {
			Sheep sheep = this.sheeps.get(i - 1);
			Hexagon currentHexa = this.hexagons.get(i);
			Point2D centerOfHexa = new Point((int) currentHexa.getPointList()
					.get(3).getX()
					+ Math.round(currentHexa.getSize() / 2), (int) currentHexa
					.getPointList().get(3).getY()
					- Math.round(currentHexa.getSize() / 2));

			// if players are two only
			if (i % 2 == 0)
				g.setColor(Color.WHITE);
			else
				g.setColor(Color.GREEN);

			Ellipse2D.Double circle = new Ellipse2D.Double(centerOfHexa.getX(),
					centerOfHexa.getY(), currentHexa.getSize(),
					currentHexa.getSize());
			g.fill(circle);

			sheep.setCircle(circle);
		}
	}

	private void drawCenterCell(Graphics2D g) {
		Polygon polygon = new Polygon();
		hexagons.get(0).clearPointList();
		for (int i = 0; i < 6; i++) {
			Point point = new Point((int) (this.getWidth() / 2 + hexagons
					.get(0).getSize() * Math.cos(i * 2 * Math.PI / 6)),
					(int) (this.getHeight() / 2 + hexagons.get(0).getSize()
							* Math.sin(i * 2 * Math.PI / 6)));
			polygon.addPoint(point.x, point.y);
			hexagons.get(0).addPointToList(point);

		}
		g.fillPolygon(polygon);
		hexagons.get(0).setPolygon(polygon);

		Hexagon.setDistanceBetweenHexagons(hexagons.get(0));
		// System.out.println(Hexagon.getDistanceBetweenHexagons());
	}

	private void drawCell(Graphics2D g, Hexagon hexaToDraw,
			Hexagon lastDrawnHexa, Direction direction) {
		Polygon polygon = new Polygon();
		Direction currentDirection = direction;

		AffineTransform currentTransform = currentDirection
				.getDirection(Hexagon.getDistanceBetweenHexagons());

		PathIterator pointsIterator = lastDrawnHexa.getPolygon()
				.getPathIterator(currentTransform);
		hexaToDraw.clearPointList();
		while (!pointsIterator.isDone()) {
			double[] xy = new double[2];
			pointsIterator.currentSegment(xy);
			if (xy[0] == 0 && xy[1] == 0)
				break;
			polygon.addPoint((int) xy[0], (int) xy[1]);
			hexaToDraw.addPointToList(new Point((int) xy[0], (int) xy[1]));
			pointsIterator.next();
		}
		g.fillPolygon(polygon);
		hexaToDraw.setPolygon(polygon);

		// TODO changer cette merde
		this.lastCell = hexaToDraw;

	}

	private Hexagon getCorrespondingCell(int i, int j) {
		for (Hexagon hexagon : hexagons) {
			if (hexagon.getVirtualIndex().getX() == i
					&& hexagon.getVirtualIndex().getY() == j)
				return hexagon;
		}
		return null;
	}
}
