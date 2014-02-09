package com.enclos.component;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

//classe m�re des cellules
public abstract class Shape {
	
	protected int x;
	protected int y;
	protected int size;
	protected Polygon polygon = null;
	protected List<Point> pointList = new ArrayList<>();
	
	public List<Point> getPointList() {
		return pointList;
	}

	public void clearPointList(){
		this.pointList.clear();
	}
	
	public void addPointToList(Point point){
		this.pointList.add(point);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public void warn(){
		System.out.print("Shape clicked : ");
	}
	
	public void setPolygon(Polygon polygon){
		this.polygon = polygon;
	}
	
	public Polygon getPolygon() throws NullPointerException{
		return this.polygon;
	}

	public boolean contains(int x, int y) {
		return polygon.contains(new Point(x, y)) ? true:false;
	}

}
