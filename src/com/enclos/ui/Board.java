package com.enclos.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
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
	private Hexagon firstHexSelected = null;
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

				Hexagon selectedHexagon = Board.this.firstHexSelected;
				for (Hexagon hex : Board.this.hexagons) {
					if (hex.contains(event.getX(), event.getY())) {
						if (Board.this.firstHexSelected == null) {
							if (hex.getSheep() != null) {
								Board.this.firstHexSelected = hex;
							}
						}else{
							if(selectedHexagon.getNeighboors().contains(hex))
							if (hex.getSheep() == null) {
								hex.setSheep(selectedHexagon.getSheep());
								selectedHexagon.setSheep(null);
								Board.this.firstHexSelected = null;
							}							
						}
					}
				}

				Board.this.repaint();
				// for (Shape shape : Board.this.shapes) {
				// if (shape.contains(event.getX(), event.getY()))
				// System.out.println(shape);
				// }
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

		for (int i = 1; i <= nbSheep; i++) {
			Hexagon currentHexa = hexagons.get(i);
			Sheep sheep = new Sheep();
			this.sheeps.add(sheep);
			currentHexa.setSheep(sheep);
		}

		// BRIDGES
		int nbBridges = calculateNumberOfBridges();
		for (int i = 0; i < nbBridges; i++) {
			this.bridges.add(new Bridge());
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
		generateNeighboors();

		this.shapes.clear();
		this.shapes.addAll(this.hexagons);
		this.shapes.addAll(this.bridges);
		// this.shapes.addAll(this.sheeps);
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

		for (Hexagon hexa : hexagons) {
			Point2D centerOfShape = hexa.getCenterPoint();

			for (Direction direction : Direction.values()) {
				Polygon polygon = new Polygon();
				AffineTransform currentTransform = direction
						.getDirection(Hexagon.getDistanceBetweenHexagons());

				Point2D targetPoint = new Point();
				targetPoint = currentTransform.transform(centerOfShape,
						targetPoint);

				for (Hexagon targetHexa : hexagons) {
					if (targetHexa.contains((int) targetPoint.getX(),
							(int) targetPoint.getY())) {
						Point point1, point2, point3, point4;
						switch (direction.name()) {
							case "SOUTH_EAST" :
								point1 = new Point(targetHexa.getPointList()
										.get(3).x, targetHexa.getPointList()
										.get(3).y);
								polygon.addPoint(point1.x, point1.y);
								point2 = new Point(targetHexa.getPointList()
										.get(4).x, targetHexa.getPointList()
										.get(4).y);
								polygon.addPoint(point2.x, point2.y);
								point3 = new Point(
										hexa.getPointList().get(0).x, hexa
												.getPointList().get(0).y);
								polygon.addPoint(point3.x, point3.y);
								point4 = new Point(
										hexa.getPointList().get(1).x, hexa
												.getPointList().get(1).y);
								polygon.addPoint(point4.x, point4.y);
								break;
							case "SOUTH" :
								point1 = new Point(targetHexa.getPointList()
										.get(4).x, targetHexa.getPointList()
										.get(4).y);
								polygon.addPoint(point1.x, point1.y);
								point2 = new Point(targetHexa.getPointList()
										.get(5).x, targetHexa.getPointList()
										.get(5).y);
								polygon.addPoint(point2.x, point2.y);
								point3 = new Point(
										hexa.getPointList().get(1).x, hexa
												.getPointList().get(1).y);
								polygon.addPoint(point3.x, point3.y);
								point4 = new Point(
										hexa.getPointList().get(2).x, hexa
												.getPointList().get(2).y);
								polygon.addPoint(point4.x, point4.y);
								break;
							case "SOUTH_WEST" :
								point1 = new Point(targetHexa.getPointList()
										.get(5).x, targetHexa.getPointList()
										.get(5).y);
								polygon.addPoint(point1.x, point1.y);
								point2 = new Point(targetHexa.getPointList()
										.get(0).x, targetHexa.getPointList()
										.get(0).y);
								polygon.addPoint(point2.x, point2.y);
								point3 = new Point(
										hexa.getPointList().get(2).x, hexa
												.getPointList().get(2).y);
								polygon.addPoint(point3.x, point3.y);
								point4 = new Point(
										hexa.getPointList().get(3).x, hexa
												.getPointList().get(3).y);
								polygon.addPoint(point4.x, point4.y);
								break;
							case "NORTH_WEST" :
								point1 = new Point(targetHexa.getPointList()
										.get(0).x, targetHexa.getPointList()
										.get(0).y);
								polygon.addPoint(point1.x, point1.y);
								point2 = new Point(targetHexa.getPointList()
										.get(1).x, targetHexa.getPointList()
										.get(1).y);
								polygon.addPoint(point2.x, point2.y);
								point3 = new Point(
										hexa.getPointList().get(3).x, hexa
												.getPointList().get(3).y);
								polygon.addPoint(point3.x, point3.y);
								point4 = new Point(
										hexa.getPointList().get(4).x, hexa
												.getPointList().get(4).y);
								polygon.addPoint(point4.x, point4.y);
								break;
							case "NORTH" :
								point1 = new Point(targetHexa.getPointList()
										.get(1).x, targetHexa.getPointList()
										.get(1).y);
								polygon.addPoint(point1.x, point1.y);
								point2 = new Point(targetHexa.getPointList()
										.get(2).x, targetHexa.getPointList()
										.get(2).y);
								polygon.addPoint(point2.x, point2.y);
								point3 = new Point(
										hexa.getPointList().get(4).x, hexa
												.getPointList().get(4).y);
								polygon.addPoint(point3.x, point3.y);
								point4 = new Point(
										hexa.getPointList().get(5).x, hexa
												.getPointList().get(5).y);
								polygon.addPoint(point4.x, point4.y);
								break;
							case "NORTH_EAST" :
								point1 = new Point(targetHexa.getPointList()
										.get(2).x, targetHexa.getPointList()
										.get(2).y);
								polygon.addPoint(point1.x, point1.y);
								point2 = new Point(targetHexa.getPointList()
										.get(3).x, targetHexa.getPointList()
										.get(3).y);
								polygon.addPoint(point2.x, point2.y);
								point3 = new Point(
										hexa.getPointList().get(5).x, hexa
												.getPointList().get(5).y);
								polygon.addPoint(point3.x, point3.y);
								point4 = new Point(
										hexa.getPointList().get(0).x, hexa
												.getPointList().get(0).y);
								polygon.addPoint(point4.x, point4.y);
								break;
							default :
								point1 = null;
								point2 = null;
								point3 = null;
								point4 = null;
								break;
						}

						Bridge bridge = new Bridge();
						bridge.addPointToList(point1);
						bridge.addPointToList(point2);
						bridge.addPointToList(point3);
						bridge.addPointToList(point4);
						bridge.setVirtualIndex(hexa.getVirtualIndex(),
								targetHexa.getVirtualIndex());

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
							bridges.get(i).clearPointList();
							bridges.get(i).addPointToList(point1);
							bridges.get(i).addPointToList(point2);
							bridges.get(i).addPointToList(point3);
							bridges.get(i).addPointToList(point4);
							bridges.get(i).setVirtualIndex(
									hexa.getVirtualIndex(),
									targetHexa.getVirtualIndex());
							i++;
						}
						g.fillPolygon(polygon);

					}
				}
			}
		}

	}

	private void drawSheep(Graphics2D g) {
		for (int i = 1; i < this.nbSheep + 1; i++) {
			BufferedImage sheepImage = null;
			Sheep sheep = this.sheeps.get(i - 1);
			Hexagon currentHexa = findOwnerOfSheep(sheep);
			Point2D centerOfHexa = new Point((int) currentHexa.getPointList()
					.get(3).getX()
					+ Math.round(currentHexa.getSize() / 2), (int) currentHexa
					.getPointList().get(3).getY()
					- Math.round(currentHexa.getSize() / 2));

			// TODO changer cette merde
			int imageBounds = (int) currentHexa.getAverageLength();
			if (imageBounds == 0)
				break;
			// if players are two only
			if (i % 2 == 0) {
				File img = new File("resources/white_sheep.png");
				try {
					BufferedImage originalImage = ImageIO.read(img);
					int type = originalImage.getType() == 0
							? BufferedImage.TYPE_INT_ARGB
							: originalImage.getType();

					BufferedImage resizeImageJpg = resizeImage(originalImage,
							type, imageBounds, imageBounds);
					g.drawImage(resizeImageJpg, (int) centerOfHexa.getX(),
							(int) centerOfHexa.getY(), null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				File img = new File("resources/green_sheep.png");
				try {
					BufferedImage originalImage = ImageIO.read(img);
					int type = originalImage.getType() == 0
							? BufferedImage.TYPE_INT_ARGB
							: originalImage.getType();

					BufferedImage resizeImageJpg = resizeImage(originalImage,
							type, imageBounds, imageBounds);
					g.drawImage(resizeImageJpg, (int) centerOfHexa.getX(),
							(int) centerOfHexa.getY(), null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}

	private Hexagon findOwnerOfSheep(Sheep sheep) {
		Hexagon owner = null;
		for (Hexagon hex : hexagons) {
			if (hex.getSheep() == sheep) {
				owner = hex;
			}
		}
		return owner;
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
		hexagons.get(0).setCenterPoint();
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
		hexaToDraw.setCenterPoint();
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

	private void generateNeighboors() {
		for (Hexagon hex : hexagons) {
			hex.getNeighboors().clear();
			Point2D center = hex.getCenterPoint();

			for (Direction direction : Direction.values()) {
				AffineTransform currentTransform = direction
						.getDirection(Hexagon.getDistanceBetweenHexagons());
				Point2D targetPoint = new Point();
				targetPoint = currentTransform.transform(center, targetPoint);

				for (Hexagon hexa : hexagons) {
					if (hexa.getPolygon().contains(targetPoint)) {
						hex.addNeighboor(hexa);
					}
				}
			}

		}

	}

	public static Dimension getScaledDimension(Dimension imgSize,
			Dimension boundary) {

		int original_width = imgSize.width;
		int original_height = imgSize.height;
		int bound_width = boundary.width;
		int bound_height = boundary.height;
		int new_width = original_width;
		int new_height = original_height;

		// first check if we need to scale width
		if (original_width > bound_width) {
			// scale width to fit
			new_width = bound_width;
			// scale height to maintain aspect ratio
			new_height = (new_width * original_height) / original_width;
		}

		// then check if we need to scale even with the new height
		if (new_height > bound_height) {
			// scale height to fit instead
			new_height = bound_height;
			// scale width to maintain aspect ratio
			new_width = (new_height * original_width) / original_height;
		}

		return new Dimension(new_width, new_height);
	}

	private static BufferedImage resizeImage(BufferedImage originalImage,
			int type, Integer img_width, Integer img_height) {
		BufferedImage resizedImage = new BufferedImage(img_width, img_height,
				type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, img_width, img_height, null);
		g.dispose();

		return resizedImage;
	}
}
