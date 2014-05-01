package com.enclos.ui;

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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.simple.JSONArray;

import com.enclos.component.Bridge;
import com.enclos.component.Hexagon;
import com.enclos.component.Shape;
import com.enclos.component.Sheep;
import com.enclos.data.Direction;
import com.enclos.data.Player;
import com.enclos.data.SimpleWriter;
import com.enclos.resources.song.Speaker;

//board de test
public class Board extends JPanel {

	private static final long serialVersionUID = 1L;
	private final List<Hexagon> hexagons = new LinkedList<Hexagon>();
	private final List<Bridge> bridges = new LinkedList<Bridge>();
	private List<Sheep> sheeps = new LinkedList<Sheep>();
	private final List<Shape> shapes = new LinkedList<Shape>();
	private final List<Bridge> barriers = new LinkedList<Bridge>();
	private List<Player> realPlayersList = new LinkedList<Player>();
	private final List<Player> playerList = new LinkedList<Player>();
	private Hexagon firstHexSelected = null;
	private long size = 3;
	private int nbSheepPerPlayer = 3;
	private final int NB_SHEEP;
	private int nbTurn = 0;
	private Player currentPlayer = null;
	private Sheep lastMovedSheep = null;
	private Hexagon lastHexagonPosition = null;

	private boolean guiIsBeingCreated = true;
	private boolean dataToLoad = false;
	private List<JSONArray> barriersToLoad;
	private Map<Sheep, Point> sheepInfosToLoad;

	private final Enclos parent;

	Image background = new ImageIcon("resources/image/grass.jpg").getImage();

	private Hexagon lastCell = null;

	public Board(long size, int nbSheepPerPlayer, List<Player> players, Enclos parent) {
		this.parent = parent;
		
		this.realPlayersList = players;
		
		for (Player player : players) {
			Player playerCloned = player.clone();
			this.playerList.add(playerCloned);
		}
		this.nbSheepPerPlayer = nbSheepPerPlayer;
		this.NB_SHEEP = nbSheepPerPlayer * this.playerList.size();
		this.size = size;
		initGame();
	}

	private void resetSheep() {
		for (Hexagon hexa : hexagons) {
			if (hexa.getSheep() != null)
				hexa.setSheep(null);
		}
		sheeps.clear();
	}

	@Override
	public Dimension getPreferredSize() {
		Container parent = getParent();
		int numberOfGames = getParent().getComponentCount();
		return new Dimension(parent.getWidth() / numberOfGames, parent.getHeight() / numberOfGames);
	}

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

	private void drawBarriers(Graphics2D g2) {
		g2.setColor(Color.RED);
		for (Bridge currentBarrier : this.barriers) {
			g2.fillPolygon(currentBarrier.getPolygon());
		}
	}

	// TODO trouver taille ad�quate au mouton
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

	private void drawCurrentPlayer(Graphics2D g) {
		try {
			BufferedImage originalImage = ImageIO.read(this.currentPlayer.getSheeps().get(0).getImgPath());
			int imageBounds = this.getWidth() / 10;

			int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			BufferedImage resizeImageJpg = resizeImage(originalImage, type, imageBounds, imageBounds);

			g.drawImage(resizeImageJpg, resizeImageJpg.getWidth() / 2, resizeImageJpg.getHeight() / 2, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setColor(Color.white);
		g.drawString("Turn of : " + this.currentPlayer.getFirstName() + " " + this.currentPlayer.getLastName() + " !", 25, 25);

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

	private Hexagon getCorrespondingHexagon(int i, int j) {
		for (Hexagon hexagon : hexagons) {
			if (hexagon.getVirtualIndex().getX() == i && hexagon.getVirtualIndex().getY() == j)
				return hexagon;
		}
		return null;
	}

	private void generateNeighboors() {
		for (Hexagon hex : hexagons) {
			hex.getNeighboors().clear();
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
							}
						}
					}
				}
			}

		}

	}

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

	private static BufferedImage resizeImage(BufferedImage originalImage, int type, Integer img_width, Integer img_height) {
		BufferedImage resizedImage = new BufferedImage(img_width, img_height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, img_width, img_height, null);
		g.dispose();

		return resizedImage;
	}

	private Bridge getBrigeFromIndex(Hexagon from, Hexagon to) {
		Bridge bridge = null;

		for (Bridge currentBridge : this.bridges) {
			if (currentBridge.getVirtualIndex().contains(from.getVirtualIndex()) && currentBridge.getVirtualIndex().contains(to.getVirtualIndex())) {
				bridge = currentBridge;
			}
		}
		return bridge;

	}

	public int getNbSheep() {
		return this.NB_SHEEP;
	}

	public int getNbSheepPerPlayer() {
		return this.nbSheepPerPlayer;
	}

	public long getBoardSize() {
		return this.size;
	}

	public List<Bridge> getBarriers() {
		return this.barriers;
	}

	public List<Sheep> getSheeps() {
		return sheeps;
	}

	public void setSheeps(List<Sheep> sheeps) {
		this.sheeps = sheeps;
	}

	public void initGame() {
		generateCells();

		this.firstTurn();

		// on resize les composants
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);

				for (Shape shape : hexagons) {
					shape.setSize(getHeight() / Board.this.size / 6);
				}
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
							if (Board.this.currentPlayer.getTurnStatus() != Player.MOVE_SHEEP) {
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
									hex.setSheep(sheepToMove);
									sheepToMove.setHexagon(hex);
									selectedHexagon.setSheep(null);
									Board.this.lastHexagonPosition = Board.this.firstHexSelected;
									Board.this.firstHexSelected = null;
									resetHexagonsColor();
									Board.this.currentPlayer.moveSheep();
									Board.this.lastMovedSheep = sheepToMove;
									if (Board.this.currentPlayer.isEndOfTurn()) {
										Board.this.nextTurn();
									}
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

				if (Board.this.firstHexSelected == null && Board.this.currentPlayer.getTurnStatus() != Player.DROP_BARRIER) {
					for (Bridge bridge : Board.this.bridges) {
						if (bridge.contains(event.getX(), event.getY())) {
							if (!Board.this.barriers.contains(bridge)) {
								action = true;
								Board.this.barriers.add(bridge);
								Board.this.currentPlayer.dropBarrier();
								Speaker.playRandomDropBarrier();
								if (Board.this.currentPlayer.isEndOfTurn()) {
									Board.this.nextTurn();
								} else {

								}
							}
						}
					}
				}

				Board.this.repaint();
				if (action) {
					if (isGameFinished()) {
						JOptionPane.showMessageDialog(null, "GAME OVER !!!!");
						Board.this.parent.getFrameContentPane().removeDisplayedGame();
					} else if (Board.this.currentPlayer.hasLost() && !Board.this.currentPlayer.isEndOfTurn()) {
						Board.this.nextTurn();
					}
				}
			}

			private void resetHexagonsColor() {
				for (Hexagon hexa : Board.this.hexagons) {
					hexa.setColor(Color.BLACK);
				}

			}

			private void colorNeighboors(Hexagon hex) {
				for (Hexagon hexa : hex.getNeighboors()) {
					hexa.setColor(Color.CYAN);
				}
			}
		});
	}

	private void firstTurn() {
		if (this.currentPlayer == null)
			this.currentPlayer = this.playerList.get((this.nbTurn) % this.playerList.size());

		this.nbTurn++;
		this.currentPlayer.startTurn();
	}

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

	private void nextTurn() {
		Player nextPlayer = null;
		do {
			// saute le tour du joueur s'il a d�ja perdu
			nextPlayer = this.playerList.get((this.nbTurn) % this.playerList.size());
			this.nbTurn++;
		} while (nextPlayer.hasLost());
		this.currentPlayer = nextPlayer;

		this.currentPlayer.startTurn();
	}

	private boolean isGameFinished() {
		int playersLeft = this.playerList.size();
		updateLoseStatusPlayer();

		for (Player player : this.playerList) {
			if (player.hasLost()) {
				playersLeft--;
			} 
		}
		
		if (playersLeft <= 1) {
			for(Player player : playerList){
				Player realPlayer = getCorrespondingRealPlayer(player.getFirstName(), player.getLastName());
				if(player.hasLost()){
					realPlayer.lose();
				}else{
					realPlayer.win();
				}
			}
			
			SimpleWriter.SavePlayer(parent.getPlayers(), "players");
			parent.refreshPlayersInfo();

			for(Player player : realPlayersList){
				player.resetLoseStatus();
			}
			
			return true;
		} else {
			return false;
		}
	}

	public void setData(List<JSONArray> barriers, Map<Sheep, Point> sheepInfos, Player currentPlay) {
		dataToLoad = true;
		barriersToLoad = barriers;
		sheepInfosToLoad = sheepInfos;
		currentPlayer = currentPlay;
	}

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
			// add the new sheeps with their new position to the player's
			// sheep's list
			sheep.getOwner().getSheeps().add(sheep);
			sheeps.add(sheep);
		}

		repaint();
	}

	public List<Player> getPlayers() {
		return this.playerList;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public Player getCorrespondingPlayer(String firstName, String lastName) {
		for (Player player : this.playerList) {
			if (player.getLastName().equals(lastName) && player.getFirstName().equals(firstName)) {
				return player;
			}
		}
		return null;
	}

	public Player getCorrespondingRealPlayer(String firstName, String lastName) {
		for (Player player : this.realPlayersList) {
			if (player.getLastName().equals(lastName) && player.getFirstName().equals(firstName)) {
				return player;
			}
		}
		return null;
	}

	public void cancelAction() {
		if (!currentPlayer.isBeginOfTurn()) {
			if (currentPlayer.getTurnStatus() == Player.DROP_BARRIER) {
				barriers.remove(barriers.size() - 1);
			} else {
				lastHexagonPosition.setSheep(lastMovedSheep);
				lastMovedSheep.getHexagon().setSheep(null);
				lastMovedSheep.setHexagon(lastHexagonPosition);
				lastMovedSheep = null;
				lastHexagonPosition = null;
			}
			repaint();
			currentPlayer.setTurnStatus(Player.BEGIN_TURN);
		} else {
			JOptionPane.showMessageDialog(null, "No last action to cancel");
		}
	}
	
	public List<Player> getRealPlayerList(){
		return realPlayersList;
	}
}
