package org.enclos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
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
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.enclos.component.Bridge;
import org.enclos.component.Hexagon;
import org.enclos.component.Shape;
import org.enclos.component.Sheep;
import org.enclos.data.Difficulty;
import org.enclos.data.Direction;
import org.enclos.data.Human;
import org.enclos.data.Player;
import org.enclos.data.PlayerAction;
import org.enclos.data.SimpleWriter;
import org.enclos.resources.song.Speaker;
import org.json.simple.JSONArray;

/**
 * @author Clement CARREAU
 * @author Quentin GEFFARD
 * @author Julien TELA
 */

public class Board extends JPanel {

	private static final long serialVersionUID = 1L;
	private final List<Hexagon> hexagons = new LinkedList<Hexagon>();
	private final List<Bridge> bridges = new LinkedList<Bridge>();
	private List<Sheep> sheeps = new LinkedList<Sheep>();
	private final List<Shape> shapes = new LinkedList<Shape>();
	private final List<Bridge> barriers = new LinkedList<Bridge>();
	private List<Human> realPlayersList = new LinkedList<Human>();
	private final List<Player> playerList = new LinkedList<Player>();
	private Hexagon firstHexSelected = null;
	private long size = 3;
	private int nbSheepPerPlayer = 3;
	private final int NB_SHEEP;
	private int nbTurn = 0;
	private Player currentPlayer = null;
	private Sheep lastMovedSheep = null;
	private Hexagon lastHexagonPosition = null;
	private Difficulty difficulty = null;

	private boolean guiIsBeingCreated = true;
	private boolean dataToLoad = false;
	private List<JSONArray> barriersToLoad;
	private Map<Sheep, Point> sheepInfosToLoad;

	private Computer computer = null;

	private final Enclos parent;

	Image background = new ImageIcon("resources/image/grass.jpg").getImage();

	private Hexagon lastCell = null;

	/**
	 * First constructor of Board class, used when board is create in player versus player mode
	 * @param boardSize
	 * @param nbSheep
	 * @param playersSelected
	 * @param enclos
	 */
	public Board(Long boardSize, int nbSheep, List<Human> playersSelected, Enclos enclos) {
		this.parent = enclos;
		this.realPlayersList = playersSelected;

		for (Human player : playersSelected) {
			Human playerCloned = player.clone();
			playerList.add(playerCloned);
		}

		this.nbSheepPerPlayer = nbSheep;
		this.NB_SHEEP = nbSheep * this.playerList.size();
		this.size = boardSize;
		
		parent.getBackgroundMusicSpeaker().playMusic();
		initGame();
	}
	
	/**
	 * Second constructor of Board class, used when board is create in player versus computer mode
	 * @param size
	 * @param nbSheep
	 * @param playersSelected
	 * @param enclos
	 * @param difficulty
	 */
	public Board(Long size, int nbSheep, List<Human> playersSelected, Enclos enclos, Difficulty difficulty) {

		this.parent = enclos;

		this.realPlayersList = playersSelected;

		for (Human player : playersSelected) {
			Human playerCloned = player.clone();
			playerList.add(playerCloned);
		}
		this.difficulty = difficulty;
		computer = new Computer(this, difficulty);
		playerList.add(computer);

		this.nbSheepPerPlayer = nbSheep;
		this.NB_SHEEP = nbSheep * this.playerList.size();
		this.size = size;
		
		parent.getBackgroundMusicSpeaker().playMusic();
		initGame();
	}
	
	/**
	 * Clear sheep
	 */
	private void resetSheep() {
		for (Hexagon hexa : hexagons) {
			if (hexa.getSheep() != null)
				hexa.setSheep(null);
		}
		sheeps.clear();
	}
	
	/**
	 * generate the board object sure as hexagon, bridge..
	 */
	private void generateCells() {
		// HEXAGONS
		this.hexagons.add(new Hexagon(0, 0));
		for (int level = 1; level <= this.size; level++) {
			for (int rang = 0; rang < level * 6; rang++) {
				hexagons.add(new Hexagon(level, rang));
			}
		}

		// SHEEP
		int cout = 1;
		for (int i = 0; i < this.NB_SHEEP; i++) {
			Hexagon currentHexa = this.hexagons.get(cout);
			Player owner = this.playerList.get(i % this.playerList.size());
			Sheep sheep = new Sheep();

			File imgPath = null;
			switch (i % playerList.size()) {
			case 0:
				imgPath = new File("resources/image/white_sheep.png");
				break;
			case 1:
				imgPath = new File("resources/image/green_sheep.png");
				break;
			case 2:
				imgPath = new File("resources/image/rasta_sheep.png");
				break;
			case 3:
				imgPath = new File("resources/image/red_sheep.png");
				break;
			case 4:
				imgPath = new File("resources/image/ultra_sheep.png");
				break;
			case 5:
				imgPath = new File("resources/image/dark_sheep_of_the_death.png");
				break;
			default:
				break;
			}

			sheep.setImgPath(imgPath);
			sheep.setOwner(owner);
			sheep.setHexagon(currentHexa);
			owner.getSheeps().add(sheep);
			this.sheeps.add(sheep);
			currentHexa.setSheep(sheep);
			cout++;
		}

		// BRIDGES
		int nbBridges = calculateNumberOfBridges();
		for (int i = 0; i < nbBridges; i++) {
			this.bridges.add(new Bridge());
		}
	}
	
	/**
	 * Used to calculate the number of bridge contains in the board instance
	 * @return the number of bridge
	 */
	private int calculateNumberOfBridges() {

		int nbBridges = 0;
		for (int i = 1; i < 3 * this.size; i += 3) {
			nbBridges += 6 * i;
		}
		nbBridges += this.size * 6;
		return nbBridges;
	}

	/**
	 * Methods override to draw all object of board
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		if (!guiIsBeingCreated) {
			g2.drawImage(this.background, 0, 0, null);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			drawHexas(g2);
			drawBridges(g2);
			if (dataToLoad) {
				loadData();
				dataToLoad = false;
			}
			drawBarriers(g2);
			drawSheep(g2);
			drawCurrentPlayer(g2);
			generateNeighboors();

			this.shapes.clear();
			this.shapes.addAll(this.hexagons);
			this.shapes.addAll(this.bridges);
		}

		if (guiIsBeingCreated) {
			guiIsBeingCreated = false;
			this.repaint();
		}
	}
	
	/**
	 * Draw hexagon
	 * @param g
	 */
	private void drawHexas(Graphics2D g) {
		int counter;
		Hexagon currentCell = null;
		Direction currentDirection = null;

		// drawing center
		drawCenterCell(g);

		for (int i = 1; i <= this.size; i++) {
			if (lastCell != null) {
				lastCell = getCorrespondingHexagon(i - 1, 0);
			} else {
				lastCell = hexagons.get(0);
			}

			currentCell = getCorrespondingHexagon(i, 0);
			drawCell(g, currentCell, lastCell, Direction.NORTH);
			currentDirection = Direction.SOUTH_EAST;
			counter = 0;

			for (int j = 1; j < i * 6; j++) {
				currentCell = getCorrespondingHexagon(i, j);
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
	
	/**
	 * Draw bridge
	 * @param g
	 */
	private void drawBridges(Graphics2D g) {
		int i = 0;
		g.setColor(Color.YELLOW);
		for (Hexagon hexa : hexagons) {
			Point2D centerOfShape = hexa.getCenterPoint();

			for (Direction direction : Direction.values()) {
				Polygon polygon = new Polygon();
				AffineTransform currentTransform = direction.getDirection(Hexagon.getDistanceBetweenHexagons());

				Point2D targetPoint = new Point();
				targetPoint = currentTransform.transform(centerOfShape, targetPoint);

				for (Hexagon targetHexa : hexagons) {
					if (targetHexa.contains((int) targetPoint.getX(), (int) targetPoint.getY())) {
						Point point1, point2, point3, point4;
						switch (direction.name()) {
						case "SOUTH_EAST":
							point1 = new Point(targetHexa.getPointList().get(3).x, targetHexa.getPointList().get(3).y);
							polygon.addPoint(point1.x, point1.y);
							point2 = new Point(targetHexa.getPointList().get(4).x, targetHexa.getPointList().get(4).y);
							polygon.addPoint(point2.x, point2.y);
							point3 = new Point(hexa.getPointList().get(0).x, hexa.getPointList().get(0).y);
							polygon.addPoint(point3.x, point3.y);
							point4 = new Point(hexa.getPointList().get(1).x, hexa.getPointList().get(1).y);
							polygon.addPoint(point4.x, point4.y);
							break;
						case "SOUTH":
							point1 = new Point(targetHexa.getPointList().get(4).x, targetHexa.getPointList().get(4).y);
							polygon.addPoint(point1.x, point1.y);
							point2 = new Point(targetHexa.getPointList().get(5).x, targetHexa.getPointList().get(5).y);
							polygon.addPoint(point2.x, point2.y);
							point3 = new Point(hexa.getPointList().get(1).x, hexa.getPointList().get(1).y);
							polygon.addPoint(point3.x, point3.y);
							point4 = new Point(hexa.getPointList().get(2).x, hexa.getPointList().get(2).y);
							polygon.addPoint(point4.x, point4.y);
							break;
						case "SOUTH_WEST":
							point1 = new Point(targetHexa.getPointList().get(5).x, targetHexa.getPointList().get(5).y);
							polygon.addPoint(point1.x, point1.y);
							point2 = new Point(targetHexa.getPointList().get(0).x, targetHexa.getPointList().get(0).y);
							polygon.addPoint(point2.x, point2.y);
							point3 = new Point(hexa.getPointList().get(2).x, hexa.getPointList().get(2).y);
							polygon.addPoint(point3.x, point3.y);
							point4 = new Point(hexa.getPointList().get(3).x, hexa.getPointList().get(3).y);
							polygon.addPoint(point4.x, point4.y);
							break;
						case "NORTH_WEST":
							point1 = new Point(targetHexa.getPointList().get(0).x, targetHexa.getPointList().get(0).y);
							polygon.addPoint(point1.x, point1.y);
							point2 = new Point(targetHexa.getPointList().get(1).x, targetHexa.getPointList().get(1).y);
							polygon.addPoint(point2.x, point2.y);
							point3 = new Point(hexa.getPointList().get(3).x, hexa.getPointList().get(3).y);
							polygon.addPoint(point3.x, point3.y);
							point4 = new Point(hexa.getPointList().get(4).x, hexa.getPointList().get(4).y);
							polygon.addPoint(point4.x, point4.y);
							break;
						case "NORTH":
							point1 = new Point(targetHexa.getPointList().get(1).x, targetHexa.getPointList().get(1).y);
							polygon.addPoint(point1.x, point1.y);
							point2 = new Point(targetHexa.getPointList().get(2).x, targetHexa.getPointList().get(2).y);
							polygon.addPoint(point2.x, point2.y);
							point3 = new Point(hexa.getPointList().get(4).x, hexa.getPointList().get(4).y);
							polygon.addPoint(point3.x, point3.y);
							point4 = new Point(hexa.getPointList().get(5).x, hexa.getPointList().get(5).y);
							polygon.addPoint(point4.x, point4.y);
							break;
						case "NORTH_EAST":
							point1 = new Point(targetHexa.getPointList().get(2).x, targetHexa.getPointList().get(2).y);
							polygon.addPoint(point1.x, point1.y);
							point2 = new Point(targetHexa.getPointList().get(3).x, targetHexa.getPointList().get(3).y);
							polygon.addPoint(point2.x, point2.y);
							point3 = new Point(hexa.getPointList().get(5).x, hexa.getPointList().get(5).y);
							polygon.addPoint(point3.x, point3.y);
							point4 = new Point(hexa.getPointList().get(0).x, hexa.getPointList().get(0).y);
							polygon.addPoint(point4.x, point4.y);
							break;
						default:
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
						bridge.setVirtualIndex(hexa.getVirtualIndex(), targetHexa.getVirtualIndex());

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
							bridges.get(i).setVirtualIndex(hexa.getVirtualIndex(), targetHexa.getVirtualIndex());
							i++;
						}
						g.fillPolygon(polygon);
					}
				}
			}
		}

	}

	/**
	 * Draw barriers
	 * @param g2
	 */
	private void drawBarriers(Graphics2D g2) {
		g2.setColor(Color.RED);
		for (Bridge currentBarrier : this.barriers) {
			g2.fillPolygon(currentBarrier.getPolygon());
		}
	}

	/**
	 * Draw sheep
	 * @param g
	 */
	private void drawSheep(Graphics2D g) {
		for (int i = 1; i < this.NB_SHEEP + 1; i++) {
			Sheep sheep = this.sheeps.get(i - 1);
			Hexagon currentHexa = findOwnerOfSheep(sheep);
			if (currentHexa != null) {
				Point2D centerOfHexa = new Point((int) currentHexa.getPointList().get(3).getX() + Math.round(currentHexa.getSize() / 2), (int) currentHexa.getPointList().get(3).getY() - Math.round(currentHexa.getSize() / 2));

				int imageBounds = (int) Hexagon.getAverageLength() + 5;
				if (imageBounds == 0)
					break;
				File img = sheep.getImgPath();
				try {
					BufferedImage originalImage = ImageIO.read(img);
					int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

					BufferedImage resizeImageJpg = resizeImage(originalImage, type, imageBounds, imageBounds);
					g.drawImage(resizeImageJpg, (int) centerOfHexa.getX(), (int) centerOfHexa.getY(), null);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Draw current player sheep
	 * @param g
	 */
	private void drawCurrentPlayer(Graphics2D g) {
		try {
			BufferedImage originalImage = ImageIO.read(this.currentPlayer.getSheeps().get(0).getImgPath());
			int imageBounds = this.getWidth() / 10;

			int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			BufferedImage resizeImageJpg = resizeImage(originalImage, type, imageBounds, imageBounds);

			g.drawImage(resizeImageJpg, resizeImageJpg.getWidth() / 2, resizeImageJpg.getHeight() / 2, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setColor(Color.white);
		if (currentPlayer instanceof Human) {
			if (difficulty != null) {
				g.drawString("Mode : " + difficulty.toString(), 25, 25);
			}
			g.drawString("Turn of : " + ((Human) this.currentPlayer).getFirstName() + " " + ((Human) this.currentPlayer).getLastName() + " !", 25, 50);
		}

	}
	
	/**
	 * Get the owner of the sheep given
	 * @param sheep
	 * @return Player
	 */
	private Hexagon findOwnerOfSheep(Sheep sheep) {
		Hexagon owner = null;
		for (Hexagon hex : hexagons) {
			if (hex.getSheep() == sheep) {
				owner = hex;
			}
		}
		return owner;
	}
	
	/**
	 * Draw Hexagon
	 * @param g
	 */
	private void drawCenterCell(Graphics2D g) {
		Polygon polygon = new Polygon();
		hexagons.get(0).clearPointList();
		g.setColor(hexagons.get(0).getColor());
		for (int i = 0; i < 6; i++) {
			Point point = new Point((int) (this.getWidth() / 2 + hexagons.get(0).getSize() * Math.cos(i * 2 * Math.PI / 6)), (int) (this.getHeight() / 2 + hexagons.get(0).getSize() * Math.sin(i * 2 * Math.PI / 6)));
			polygon.addPoint(point.x, point.y);
			hexagons.get(0).addPointToList(point);

		}
		g.fillPolygon(polygon);
		hexagons.get(0).setPolygon(polygon);
		hexagons.get(0).setCenterPoint();
		Hexagon.setDistanceBetweenHexagons(hexagons.get(0));
	}
	
	/**
	 * Call in the draw hexagon
	 * @param g
	 * @param hexaToDraw
	 * @param lastDrawnHexa
	 * @param direction
	 */
	private void drawCell(Graphics2D g, Hexagon hexaToDraw, Hexagon lastDrawnHexa, Direction direction) {
		g.setColor(hexaToDraw.getColor());
		Polygon polygon = new Polygon();
		Direction currentDirection = direction;

		AffineTransform currentTransform = currentDirection.getDirection(Hexagon.getDistanceBetweenHexagons());

		PathIterator pointsIterator = lastDrawnHexa.getPolygon().getPathIterator(currentTransform);
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

		this.lastCell = hexaToDraw;
	}
	
	
	/**
	 * Getter difficulty
	 * @return Difficulty difficulty
	 */
	public Difficulty getDifficulty() {
		return difficulty;
	}
	
	/**
	 * Get the hexagon corresponding to the virtualIndex given as int i and int j parameters
	 * @param i
	 * @param j
	 * @return hexagon corresponding
	 */
	private Hexagon getCorrespondingHexagon(int i, int j) {
		for (Hexagon hexagon : hexagons) {
			if (hexagon.getVirtualIndex().getX() == i && hexagon.getVirtualIndex().getY() == j)
				return hexagon;
		}
		return null;
	}
	
	/**
	 * Update all hexagon to generate its neighbors list
	 */
	private void generateNeighboors() {
		for (Hexagon hex : hexagons) {
			hex.getNeighboors().clear();
			hex.getNeighboorsWithSheep().clear();
			Point2D center = hex.getCenterPoint();

			for (Direction direction : Direction.values()) {
				AffineTransform currentTransform = direction.getDirection(Hexagon.getDistanceBetweenHexagons());
				Point2D targetPoint = new Point();
				targetPoint = currentTransform.transform(center, targetPoint);

				for (Hexagon hexa : hexagons) {
					if (hexa.getSheep() == null) {
						if (hexa.getPolygon().contains(targetPoint)) {
							Bridge correspondingBridge = this.getBrigeFromIndex(hex, hexa);
							if (!this.barriers.contains(correspondingBridge)) {
								hex.addNeighboor(hexa);
								hex.addNeighboorWithSheep(hexa);
							}
						}
					} else {
						if (hexa.getPolygon().contains(targetPoint)) {
							Bridge correspondingBridge = this.getBrigeFromIndex(hex, hexa);
							if (!this.barriers.contains(correspondingBridge)) {
								hex.addNeighboorWithSheep(hexa);
							}
						}
					}
				}
			}

		}

	}

	/**
	 * Get the dimansion of image according to the frame size
	 * @param imgSize
	 * @param boundary
	 * @return Dimension
	 */
	public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

		int original_width = imgSize.width;
		int original_height = imgSize.height;
		int bound_width = boundary.width;
		int bound_height = boundary.height;
		int new_width = original_width;
		int new_height = original_height;

		if (original_width > bound_width) {
			new_width = bound_width;
			new_height = (new_width * original_height) / original_width;
		}

		if (new_height > bound_height) {
			new_height = bound_height;
			new_width = (new_height * original_width) / original_height;
		}

		return new Dimension(new_width, new_height);
	}
	
	/**
	 * Resized an image with parameter
	 * @param originalImage
	 * @param type
	 * @param img_width
	 * @param img_height
	 * @return image resized
	 */
	private static BufferedImage resizeImage(BufferedImage originalImage, int type, Integer img_width, Integer img_height) {
		BufferedImage resizedImage = new BufferedImage(img_width, img_height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, img_width, img_height, null);
		g.dispose();

		return resizedImage;
	}
	
	/**
	 * Return the bridge corresponding to the virtualIndex given by the pair of hexagon
	 * @param from
	 * @param to
	 * @return Bridge corresponding to the index
	 */
	private Bridge getBrigeFromIndex(Hexagon from, Hexagon to) {
		Bridge bridge = null;

		for (Bridge currentBridge : this.bridges) {
			if (currentBridge.getVirtualIndex().contains(from.getVirtualIndex()) && currentBridge.getVirtualIndex().contains(to.getVirtualIndex())) {
				bridge = currentBridge;
			}
		}
		return bridge;

	}
	
	/**
	 * Getter of number sheep attribute
	 * @return number of sheep 
	 */
	public int getNbSheep() {
		return this.NB_SHEEP;
	}
	
	/**
	 * Getter of number sheep per player attribute
	 * @return number of sheep per player
	 */
	public int getNbSheepPerPlayer() {
		return this.nbSheepPerPlayer;
	}

	/**
	 * Getter board size attribute
	 * @return long - size attribute
	 */
	public long getBoardSize() {
		return this.size;
	}
	
	/**
	 *  Getter of the barriers attribute
	 * @return list of bridge contains in barriers list
	 */
	public List<Bridge> getBarriers() {
		return this.barriers;
	}
	
	/**
	 * Getter of the bridges attribute
	 * @return bridges attribute
	 */
	public List<Bridge> getBridges() {
		return bridges;
	}
	
	/**
	 * Getter of sheeps attribute
	 * @return sheeps attribute
	 */
	public List<Sheep> getSheeps() {
		return sheeps;
	}

	/**
	 * Setter of sheeps attribute
	 * @param sheeps List of Sheep 
	 * @see Sheep
	 */
	public void setSheeps(List<Sheep> sheeps) {
		this.sheeps = sheeps;
	}
	
	/**
	 * Call to init all board
	 */
	public void initGame() {
		generateCells();

		this.firstTurn();

		// on resize les composants
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);

				int min = (getHeight() > getWidth()) ? getWidth() : getHeight();

				for (Shape shape : hexagons) {
					shape.setSize(min / Board.this.size / 6);
					//shape.setSize(min / Board.this.size*3);
				}
				Board.this.repaint();
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				super.mousePressed(event);

				boolean action = false;

				Hexagon selectedHexagon = Board.this.firstHexSelected;

				for (Hexagon hex : Board.this.hexagons) {
					if (hex.contains(event.getX(), event.getY())) {
						if (Board.this.firstHexSelected == null) {
							if (Board.this.currentPlayer.getTurnStatus() != Human.MOVE_SHEEP) {
								if (hex.getSheep() != null && hex.getSheep().getOwner().equals(Board.this.currentPlayer)) {
									action = true;
									Board.this.firstHexSelected = hex;
									colorNeighboors(hex);
									Speaker.playRandomSaySheep();
								}
							}
						} else {
							if (selectedHexagon.getNeighboors().contains(hex)) {
								if (hex.getSheep() == null) {
									action = true;
									Sheep sheepToMove = selectedHexagon.getSheep();
									switchSheep(sheepToMove, hex);
									Board.this.lastHexagonPosition = Board.this.firstHexSelected;
									Board.this.firstHexSelected = null;
									resetHexagonsColor();
									Board.this.currentPlayer.moveSheep();
									Board.this.lastMovedSheep = sheepToMove;
								}
							} else if (hex.getSheep() != null && hex.getSheep() != Board.this.firstHexSelected.getSheep() && hex.getSheep().getOwner().equals(Board.this.currentPlayer)) {
								action = true;
								Board.this.firstHexSelected = hex;
								Speaker.playRandomSaySheep();
								resetHexagonsColor();
								colorNeighboors(hex);
							} else {
								Board.this.firstHexSelected = null;
								resetHexagonsColor();
							}
						}
					}
				}

				if (Board.this.firstHexSelected == null && Board.this.currentPlayer.getTurnStatus() != Human.DROP_BARRIER) {
					for (Bridge bridge : Board.this.bridges) {
						if (bridge.contains(event.getX(), event.getY())) {
							if (!Board.this.barriers.contains(bridge)) {
								action = true;
								Board.this.barriers.add(bridge);
								Board.this.currentPlayer.dropBarrier();
								Speaker.playRandomDropBarrier();

							}

						}
					}
				}

				Board.this.repaint();
				if (action) {
					if (!isGameFinished()) {
						if ((Board.this.currentPlayer.hasLost() && !Board.this.currentPlayer.isEndOfTurn()) || (Board.this.currentPlayer.isEndOfTurn())) {
							Board.this.nextTurn();
						}
					}
				}
			}
		});
	}

	/**
	 * Move Sheep to the target hexagon given
	 * @param sheep
	 * @param target
	 */
	private void switchSheep(Sheep sheep, Hexagon target) {
		sheep.getHexagon().setSheep(null);
		target.setSheep(sheep);
		sheep.setHexagon(target);
	}
	
	/**
	 * All hexagon are repaint with the original color
	 */
	private void resetHexagonsColor() {
		for (Hexagon hexa : Board.this.hexagons) {
			hexa.setColor(Color.BLACK);
		}

	}
	
	/**
	 * Highlight neighbor can be targeted 
	 * @param hex
	 */
	public void colorNeighboors(Hexagon hex) {
		for (Hexagon hexa : hex.getNeighboors()) {
			hexa.setColor(Color.CYAN);
		}
	}
	
	/**
	 * init first turn chosse player
	 */
	private void firstTurn() {
		if (this.currentPlayer == null)
			this.currentPlayer = this.playerList.get((this.nbTurn) % this.playerList.size());

		this.nbTurn++;
		this.currentPlayer.startTurn();
	}
	
	/**
	 * Update all player status 
	 * @return whether the player has lost
	 */
	private boolean updateLoseStatusPlayer() {
		generateNeighboors();
		boolean lost = false;
		for (Player player : this.playerList) {
			lost = true;
			for (Sheep sheep : player.getSheeps()) {
				if (sheep.getHexagon().getNeighboors().size() > 0) {
					lost = false;
				}
			}
			if (lost) {
				if (!player.hasLost()) {
					player.paralyzed();
				}
			} else {
				if (player.hasLost()) {
					player.alive();
				}
			}
		}

		return lost;
	}
	
	/**
	 * Select the next player, and add it in currentPlayer player
	 */
	public void nextTurn() {
		Player nextPlayer = null;
		do {
			// saute le tour du joueur s'il a d�ja perdu
			nextPlayer = this.playerList.get((this.nbTurn) % this.playerList.size());
			this.nbTurn++;
		} while (nextPlayer.hasLost());
		this.currentPlayer = nextPlayer;

		this.currentPlayer.startTurn();
	}
	
	/**
	 * Check if game is terminated, feed score, show message
	 * @return whether the game is finished
	 */
	private boolean isGameFinished() {
		int playersLeft = this.playerList.size();
		updateLoseStatusPlayer();

		for (Player player : this.playerList) {
			if (player.hasLost()) {
				playersLeft--;
			}
		}

		if (playersLeft <= 1) {
			String winner = null;
			
			for (Player player : playerList) {
				if (player instanceof Human) {
					Human realPlayer = getCorrespondingRealPlayer(((Human) player).getFirstName(), ((Human) player).getLastName());
					if (player.hasLost()) {
						realPlayer.lose();
					} else {
						realPlayer.win();
						winner = realPlayer.getLastName()+" "+realPlayer.getFirstName();
					}
				}
			}
			
			if(winner == null){
				winner = "Computer - Difficulty : "+difficulty.toString();
			}

			SimpleWriter.SavePlayer(parent.getPlayers(), "players");
			parent.refreshPlayersInfo();
			
			JOptionPane.showMessageDialog(null, "<html><h1>GAME OVER !</h1> And the winner is "+winner+"</html>");

			for (Human player : realPlayersList) {
				player.alive();
			}

			Board.this.parent.getFrameContentPane().removeDisplayedGame();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Feed all object, call on load
	 * @param barriers
	 * @param sheepInfos
	 * @param currentPlay
	 */
	public void setData(List<JSONArray> barriers, Map<Sheep, Point> sheepInfos, Human currentPlay) {
		dataToLoad = true;
		barriersToLoad = barriers;
		sheepInfosToLoad = sheepInfos;
		currentPlayer = currentPlay;
	}
	
	/**
	 * Read informations given on load 
	 */
	public void loadData() {
		for (JSONArray array : barriersToLoad) {

			String[] firstHexaPosition = ((String) array.get(0)).split(",");
			Hexagon firstHex = getCorrespondingHexagon(Integer.parseInt(firstHexaPosition[0]), Integer.parseInt(firstHexaPosition[1]));
			String[] secondHexaPosition = ((String) array.get(1)).split(",");
			Hexagon secondHex = getCorrespondingHexagon(Integer.parseInt(secondHexaPosition[0]), Integer.parseInt(secondHexaPosition[1]));

			Bridge correspondingBridge = getBrigeFromIndex(firstHex, secondHex);
			this.barriers.add(correspondingBridge);
		}

		// reset board list of sheeps
		resetSheep();

		for (Player player : this.playerList) {
			player.getSheeps().clear();
		}

		for (Sheep sheep : sheepInfosToLoad.keySet()) {
			Hexagon hexa = getCorrespondingHexagon(sheepInfosToLoad.get(sheep).x, sheepInfosToLoad.get(sheep).y);
			hexa.setSheep(sheep);
			sheep.setHexagon(hexa);
			if (sheep.getOwner() != null) {
				sheep.getOwner().getSheeps().add(sheep);
			} else {
				sheep.setOwner(computer);
				computer.getSheeps().add(sheep);
			}

			sheeps.add(sheep);
		}
		repaint();
	}
	
	/**
	 * Getter playerlist attribute
	 * @return player list attribute 
	 */
	public List<Player> getPlayers() {
		return this.playerList;
	}
	
	/**
	 * Getter currentPLayer attribute
	 * @return current Player
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	/**
	 * Get corresponding player cloned find with his firstname and lastname
	 * @param firstName
	 * @param lastName
	 * @return Human the corresponding player
	 */
	public Human getCorrespondingPlayer(String firstName, String lastName) {
		for (Player player : this.playerList) {
			if (player instanceof Human) {
				if (((Human) player).getLastName().equals(lastName) && ((Human) player).getFirstName().equals(firstName)) {
					return (Human) player;
				}
			}
		}
		return null;
	}

	/**
	 * Get corresponding player real find with his firstname and lastname
	 * @param firstName
	 * @param lastName
	 * @return Human the corresponding player
	 */
	public Human getCorrespondingRealPlayer(String firstName, String lastName) {
		for (Human player : this.realPlayersList) {
			if (player.getLastName().equals(lastName) && player.getFirstName().equals(firstName)) {
				return player;
			}
		}
		return null;
	}

	/**
	 * Used to cancel the last action, call on ctrl+z 
	 */
	public void cancelAction() {
		if (!currentPlayer.isBeginOfTurn()) {
			if (currentPlayer.getTurnStatus() == Human.DROP_BARRIER) {
				barriers.remove(barriers.size() - 1);
			} else {
				lastHexagonPosition.setSheep(lastMovedSheep);
				lastMovedSheep.getHexagon().setSheep(null);
				lastMovedSheep.setHexagon(lastHexagonPosition);
				lastMovedSheep = null;
				lastHexagonPosition = null;
			}
			repaint();
			currentPlayer.setTurnStatus(Human.BEGIN_TURN);
		} else {
			JOptionPane.showMessageDialog(null, "No last action to cancel");
		}
	}

	/**
	 * Getter of realPlayerList attribute
	 * @return list of Player 
	 */
	public List<Human> getRealPlayerList() {
		return realPlayersList;
	}
	
	/**
	 * 
	 * Class computer
	 *
	 */
	private class Computer extends Player implements PlayerAction {

		private Difficulty difficulty = Difficulty.RAINBOW;
		private Board board = null;

		private Random random = new Random();
		
		/**
		 * Constructor of computer inner class 
		 * @param board
		 * @param difficulty
		 */
		public Computer(Board board, Difficulty difficulty) {
			this.board = board;

			this.firstName = "add9cf0f98bd686c95909c8c9160fa5463225c10";
			this.lastName = "Dr";

			this.difficulty = difficulty;
		}

		/**
		 * Start turn, call good method according to the difficulty level set
		 */
		@Override
		public void startTurn() {
			boolean isFinished = false;
			switch (difficulty) {
			case RAINBOW:
				isFinished = retardTurn();
				break;
			case NORMAL:
				isFinished = normalTurn();
				break;
			}
			board.repaint();

			if (!isFinished) {
				board.nextTurn();
			}
		}

		private boolean retardTurn() {
			boolean finished = false;
			if (random.nextBoolean()) {
				finished = randomSheepMove();
				if (!finished) {
					finished = randomBarrierDrop();
				}
			} else {
				finished = randomBarrierDrop();
				if (!finished) {
					finished = randomSheepMove();
				}
			}
			return finished;
		}
		
		/**
		 * The normal difficulty turn
		 * @return
		 */
		private boolean normalTurn() {
			boolean finished = false;
			if (random.nextBoolean()) {
				finished = normalSheepMove();
				if (!finished) {
					finished = normalBarrierDrop();
				}
			} else {
				finished = normalBarrierDrop();
				if (!finished) {
					finished = normalSheepMove();
				}
			}
			return finished;
		}
		
		/**
		 * The normal sheep move 
		 * @return
		 */
		private boolean normalSheepMove() {
			Sheep randomSheepToMove = null;

			List<Sheep> sheepsAvailable = new ArrayList<Sheep>();
			for (Sheep currentSheep : sheeps) {
				if (currentSheep.getHexagon().getNeighboors().size() > 0 && !isCloseToEnnemy(currentSheep)) {
					sheepsAvailable.add(currentSheep);
				}
			}
			
			if (sheepsAvailable.size() > 0) {
				int randomSheepIndex = random.nextInt(sheepsAvailable.size());
				randomSheepToMove = sheepsAvailable.get(randomSheepIndex);

				List<ArrayList<Hexagon>> paths = new ArrayList<ArrayList<Hexagon>>();
				Set<Hexagon> listNode = new HashSet<Hexagon>();
				for (Hexagon hexa : board.hexagons) {
					listNode.addAll(hexa.getNeighboorsWithSheep());
				}

				Hexagon startHexa = randomSheepToMove.getHexagon();

				ArrayList<Hexagon> basePath = new ArrayList<Hexagon>();
				basePath.add(startHexa);
				paths.add(basePath);

				Map<Hexagon, Boolean> hexaVisited = new LinkedHashMap<Hexagon, Boolean>();
				for (Hexagon hex : listNode) {
					hexaVisited.put(hex, false);
				}
				hexaVisited.put(randomSheepToMove.getHexagon(), true);
				Stack<Hexagon> stack = new Stack<Hexagon>();
				stack.push(startHexa);

				List<ArrayList<Hexagon>> updatedPaths = new ArrayList<ArrayList<Hexagon>>();
				while (!stack.isEmpty()) {
					Hexagon currentHexa = stack.pop();
					for (Hexagon neighboor : currentHexa.getNeighboorsWithSheep()) {
						if (!hexaVisited.get(neighboor)) {

							hexaVisited.put(neighboor, true);
							stack.add(neighboor);

							for (List<Hexagon> currentBasePath : paths) {
								Hexagon lastHexa = currentBasePath.get(currentBasePath.size() - 1);
								if (lastHexa.getNeighboorsWithSheep().contains(neighboor)) {
									ArrayList<Hexagon> newBasePath = new ArrayList<Hexagon>();
									newBasePath.addAll(currentBasePath);
									newBasePath.add(neighboor);
									updatedPaths.add(newBasePath);
								}
							}

							if (neighboor.getSheep() != null && neighboor.getSheep().getOwner() != this ) {
								stack.clear();
							}
						}
					}
					if (!updatedPaths.isEmpty()) {
						paths.addAll(updatedPaths);
						updatedPaths.clear();
					}
				}

				Set<ArrayList<Hexagon>> viablePaths = new HashSet<ArrayList<Hexagon>>();
				for (ArrayList<Hexagon> currentPath : paths) {
					for (Hexagon hexagon : currentPath) {
						if (hexagon.getSheep() != null && hexagon.getSheep().getOwner() != this && hexagon.getNeighboorsWithSheep().size()>0) {
							viablePaths.add(currentPath);
						}

					}
				}
				ArrayList<Hexagon> bestPath = new ArrayList<Hexagon>();
				int minSize = -1;
				for (ArrayList<Hexagon> currentPath : viablePaths) {
					if (minSize == -1 && currentPath.get(1).getSheep() == null) {
						minSize = currentPath.size();
						bestPath = currentPath;
					} else {
						if (currentPath.size() < minSize && currentPath.size() > 2 && currentPath.get(1).getSheep() == null) {
							minSize = currentPath.size();
							bestPath = currentPath;
						}
					}
				}

				if (bestPath.isEmpty()) {
					return randomSheepMove();
				} else {
					board.switchSheep(randomSheepToMove, bestPath.get(1));
					return board.isGameFinished();
				}
			} else {
				return randomSheepMove();
			}
		}
		
		/**
		 * Check if the sheep given is next to a enemy sheep
		 * @param randomSheepToMove
		 * @return boolean
		 */
		private boolean isCloseToEnnemy(Sheep randomSheepToMove) {
			boolean isCloseToEnnemy = false;
			for (Hexagon hexagon : randomSheepToMove.getHexagon().getNeighboorsWithSheep()) {
				if (hexagon.getSheep() != null && hexagon.getSheep().getOwner() != this) {
					isCloseToEnnemy = true;
				}
			}
			return isCloseToEnnemy;
		}
		
		/**
		 * The normal drop barrier
		 * @return status game  
		 */
		private boolean normalBarrierDrop() {
			Player player = board.playerList.get(0);

			Sheep randomSheep = null;

			List<Sheep> sheepsAvailable = new ArrayList<Sheep>();
			for (Sheep currentSheep : player.getSheeps()) {
				if (currentSheep.getHexagon().getNeighboorsWithSheep().size() > 0) {
					sheepsAvailable.add(currentSheep);
				}
			}
			if (sheepsAvailable.size() > 0) {
				int randomSheepIndex = random.nextInt(sheepsAvailable.size());
				randomSheep = sheepsAvailable.get(randomSheepIndex);

				Hexagon hexagon = randomSheep.getHexagon();
				List<Bridge> bridges = new ArrayList<>();

				for (Hexagon hex : hexagon.getNeighboorsWithSheep()) {
					Bridge bridge = board.getBrigeFromIndex(hexagon, hex);
					if (!board.getBarriers().contains(bridge)) {
						bridges.add(bridge);
					}
				}

				int randomBridgeIndex = random.nextInt(bridges.size());
				barriers.add(bridges.get(randomBridgeIndex));
				Speaker.playRandomDropBarrier();

				return board.isGameFinished();
			} else {
				return randomBarrierDrop();
			}

		}
		
		/**
		 * Drop a barrier on a random bridge, call in rainbow difficulty 
		 * @return game status (terminated or not)
		 */
		private boolean randomBarrierDrop() {

			List<Bridge> barriers = board.getBarriers();
			List<Bridge> bridges = board.getBridges();

			Bridge barrierToDrop = null;
			do {
				int randomBridgeIndex = random.nextInt(bridges.size());
				barrierToDrop = bridges.get(randomBridgeIndex);
			} while (barriers.contains(barrierToDrop));

			barriers.add(barrierToDrop);
			Speaker.playRandomDropBarrier();
			return board.isGameFinished();
		}
		
		/**
		 * Move a sheep randomly 
		 * @return game status
		 */
		private boolean randomSheepMove() {
			Sheep sheepToMove = null;
			do {
				int randomSheepIndex = random.nextInt(sheeps.size());
				sheepToMove = sheeps.get(randomSheepIndex);
			} while (sheepToMove.getHexagon().getNeighboors().size() < 1);

			int randomNeighboorTarget = random.nextInt(sheepToMove.getHexagon().getNeighboors().size());
			Hexagon targetHexagon = sheepToMove.getHexagon().getNeighboors().get(randomNeighboorTarget);

			board.switchSheep(sheepToMove, targetHexagon);
			return board.isGameFinished();

		}
	}

}
