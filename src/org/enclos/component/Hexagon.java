package org.enclos.component;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Clement CARREAU
 * @author Quentin GEFFARD
 * @author Julien TELA
 */

public class Hexagon extends Shape {
	
	/**
	 * Polygon of the Hexagon
	 */
	private Polygon polygon = null;
	
	/**
	 * List of close Hexagon who are link by a bridge without barrier and sheep free
	 */
	private List<Hexagon> neighboors = new LinkedList<Hexagon>();
	
	/**
	 * List of close Hexagon who are link by a bridge without barrier
	 */
	private List<Hexagon> neighboorsWithSheep = new LinkedList<Hexagon>();
	private static long distanceBetweenHexagons = 0;
	
	/**
	 * Unique identifier of the hexagon
	 */
	private Point virtualIndex = new Point();
	private Point2D center = null;
	private static long averageLength = 0;
	private Sheep sheep = null;
	private Color color = Color.BLACK;
	
	/**
	 * Getter static of averageLenght attribute
	 * @return long length of hexagon
	 */
	public static long getAverageLength() {
		return averageLength;
	}
	
    /**
     * Check if the point given has an argument is in the polygon 
     *
     * @param  x the x coordinate of the point we want to check
     * @param  y the y coordinate of the point we want to check
     * @return      Boolean if point give in argument is in the polygon
     */
	public boolean contains(int x, int y) {
		return polygon.contains(new Point(x, y)) ? true : false;
	}
	
	
	/**
	 * Getter of the attribute
	 * @return list of neighboor hexagon 
	 */
	public List<Hexagon> getNeighboorsWithSheep() {
		return neighboorsWithSheep;
	}
	
	/**
	 * Setter of the attribute
	 * @param neighboorsWithSheep
	 */
	public void setNeighboorsWithSheep(List<Hexagon> neighboorsWithSheep) {
		this.neighboorsWithSheep = neighboorsWithSheep;
	}

	/**
	 * Getter of the attribute
	 * @return Polygon polygon
	 */
	public Polygon getPolygon() {
		return polygon;
	}

	/**
	 * Setter of the attribute
	 * @param polygon
	 */
	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
	}

	/**
	 * Getter of the attribute
	 * @return Point point
	 */
	public Point getVirtualIndex() {
		return this.virtualIndex;
	}
	
	/**
	 * Constructor of Hexagon class
	 * @param virtualX
	 * @param virtualY
	 */
	public Hexagon(int virtualX, int virtualY) {
		this.virtualIndex.setLocation(virtualX, virtualY);
	}
	
	/**
	 * Feed all calculate attribute of the hexagon averageLength, distanceBetweenHexagons ..
	 * @param hexagon
	 */
	public static void setDistanceBetweenHexagons(Hexagon hexagon) {
		double totalLength = 0;
		Polygon polygon = hexagon.getPolygon();

		List<Point> pointList = hexagon.getPointList();
		for (int i = 0; i < 5; i++) {
			if (i == 5) {
				totalLength += Math.sqrt(Math.pow((pointList.get(i).x)
						- (pointList.get(0).x), 2)
						+ Math.pow((pointList.get(i).y) - (pointList.get(0).y),
								2));
			} else
				totalLength += Math.sqrt(Math.pow((pointList.get(i + 1).x)
						- (pointList.get(i).x), 2)
						+ Math.pow((pointList.get(i + 1).y)
								- (pointList.get(i).y), 2));
		}

		Hexagon.averageLength = Math.round(totalLength / 6);
		Hexagon.distanceBetweenHexagons = Math.round(totalLength / 6)
				+ Math.round(Math.sqrt(Math.pow((pointList.get(0).x)
						- (pointList.get(2).x), 2)
						+ Math.pow((pointList.get(0).y) - (pointList.get(2).y),
								2)));

	}
	
	/**
	 * Getter of the attribute
	 * @return int distance
	 */
	public static int getDistanceBetweenHexagons() {
		return Math.round(Hexagon.distanceBetweenHexagons);
	}

	/**
	 * Add the hexagon given in the neighboor list 
	 * @param hex
	 */
	public void addNeighboor(Hexagon hex) {
		this.neighboors.add(hex);
	}
	
	/**
	 * Add the hexagon given in the neighboor with sheep list
	 * @param hex
	 */
	public void addNeighboorWithSheep(Hexagon hex) {
		this.neighboorsWithSheep.add(hex);
	}

	
	/**
	 * Setter who find the center point of the hexagon and feed its attribute
	 */
	public void setCenterPoint() {
		this.center = new Point((int) this.getPointList().get(3).getX()
				+ Math.round(this.getSize() / 2), (int) this.getPointList()
				.get(3).getY());
	}

	/**
	 * Getter center attribute
	 * @return Point2D center point
	 */
	public Point2D getCenterPoint() {
		return this.center;
	}

	/**
	 * Getter list Hexagon neighbor attribute
	 * @return List<Hexagon> neighbors
	 */
	public List<Hexagon> getNeighboors() {
		return this.neighboors;
	}
	
	/**
	 * Setter sheep attribute
	 * @param sheep
	 */
	public void setSheep(Sheep sheep){
		this.sheep = sheep;
		if(sheep != null)
			sheep.setVirtualIndexHexagon(this.virtualIndex);
	}
	
	/**
	 * Getter of the sheep attribute
	 * @return Sheep sheep
	 */
	public Sheep getSheep(){
		return this.sheep;
	}
	
	/**
	 * Getter color attribute
	 * @return Color the color
	 */
	public Color getColor(){
		return this.color;
	}

	/**
	 * Setter color attribute
	 * @param color
	 */
	public void setColor(Color color){
		this.color = color;
	}

    /**
     * Override toString
     *  @return string
     */
	@Override
	public String toString() {
		return super.toString() + " Hexagon  - " +this.sheep + "Point :"+virtualIndex;// == null ? "je n'ai pas de sheep" : "j'ai un sheep"
	}
}
