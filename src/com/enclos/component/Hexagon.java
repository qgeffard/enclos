package com.enclos.component;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.PathIterator;
import java.util.List;

import components.HexagonPanel;

public class Hexagon extends Shape {

	private Polygon polygon = null;
	private static long distanceBetweenHexagons = 0;
	private Point virtualIndex = new Point();
	
	
	public boolean contains(int x, int y) {
		return polygon.contains(new Point(x, y)) ? true:false;
	}
	
	public Polygon getPolygon() {
		return polygon;
	}

	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
	}

	public Point getVirtualIndex() {
		return this.virtualIndex;
	}
	public Hexagon(int virtualX, int virtualY) {
		this.virtualIndex.setLocation(virtualX, virtualY);
	}

	@Override
	public String toString() {
		return super.toString()+" Hexagon  - "+getVirtualIndex().toString();
	}

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
		
		Hexagon.distanceBetweenHexagons = Math.round(totalLength / 6) + Math.round(Math.sqrt(Math.pow((pointList.get(0).x)
				- (pointList.get(2).x), 2)
				+ Math.pow((pointList.get(0).y) - (pointList.get(2).y), 2)));

	}
	
	public static int getDistanceBetweenHexagons(){
		return Math.round(Hexagon.distanceBetweenHexagons);
	}

}
