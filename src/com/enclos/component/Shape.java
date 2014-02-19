package com.enclos.component;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;


public abstract class Shape {
	
	protected int x;
	protected int y;
	protected int size;
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
	
	public abstract boolean contains(int x, int y);
	
	
	@Override 
	public String toString(){
		return "Shape clicked : ";
	}

}
