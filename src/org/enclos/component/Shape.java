package org.enclos.component;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Clement CARREAU
 * @author Quentin GEFFARD
 * @author Julien TELA
 */

public abstract class Shape {
	
	protected int x;
	protected int y;
	protected long size;
	protected List<Point> pointList = new ArrayList<>();
	
	
	/**
	 * Getter
	 * @return List<Point> 
	 */
	public List<Point> getPointList() {
		return pointList;
	}
	
	/**
	 * clear the point list attribute
	 */
	public void clearPointList(){
		this.pointList.clear();
	}
	
	/**
	 * Add the point pass as argument in the point list attribute
	 * @param point
	 */
	public void addPointToList(Point point){
		this.pointList.add(point);
	}
	
	/**
	 * Getter of x attribute
	 * @return int x
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Setter of x attribute
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Getter of y attribute 
	 * @return int y
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Setter of y attribute
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Getter size attribute
	 * @return long size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * Setter size attribute
	 * @param l
	 */
	public void setSize(long l) {
		this.size = l;
	}
	
	public abstract boolean contains(int x, int y);
	
    /**
     * Override toString
     *  @return string
     */
	@Override 
	public String toString(){
		return "Shape clicked : ";
	}

}
