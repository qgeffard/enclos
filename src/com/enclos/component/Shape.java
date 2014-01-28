package com.enclos.component;

import java.awt.Point;
import java.awt.Polygon;

//classe mère des cellules
public abstract class Shape {
	
	protected int x;
	protected int y;
	protected int size;
	protected Polygon polygon = null;
	
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
		System.out.println("Shape clicked : ");
	}
	
	public void setPolygon(Polygon polygon){
		this.polygon = polygon;
	}

	public boolean contains(int x, int y) {
		return polygon.contains(new Point(x, y)) ? true:false;
	}

}
