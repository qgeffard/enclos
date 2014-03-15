package com.enclos.component;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

public class Bridge extends Shape{

	private Polygon polygon = null;
	private List<Point> virtualIndex = new ArrayList<Point>();
	private List<Point> virtualIndexReverse = new ArrayList<Point>();
	private Color color = Color.YELLOW;
	
	
//	CONTRUCTS
	public Bridge() {
		this.setVirtualIndex(new Point(0,0), new Point(0,0));
	}
	
	
//	METHODE
	public boolean contains(int x, int y) {
		return polygon.contains(new Point(x, y)) ? true:false;
	}
	
	public Polygon getPolygon() {
		return polygon;
	}

	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
	}
	
	public List<Point> getVirtualIndex(boolean reverse) {
		if(reverse)
			return this.virtualIndexReverse;
		else
			return this.virtualIndex;
	}
	
	public boolean equals(Bridge bridge){
		if(	((this.virtualIndex.get(0).x == bridge.virtualIndex.get(0).x && this.virtualIndex.get(0).y == bridge.virtualIndex.get(0).y)
			&& 
			(this.virtualIndex.get(1).x == bridge.virtualIndex.get(1).x && this.virtualIndex.get(1).y == bridge.virtualIndex.get(1).y))
		||
			((this.virtualIndexReverse.get(0).x == bridge.virtualIndex.get(0).x && this.virtualIndexReverse.get(0).y == bridge.virtualIndex.get(0).y)
			&&
			(this.virtualIndexReverse.get(1).x == bridge.virtualIndex.get(1).x && this.virtualIndexReverse.get(1).y == bridge.virtualIndex.get(1).y))
		)
		{
			for (Point point : this.pointList) {
				if(!bridge.pointList.contains(point))
					return false;
			}
			
			return true;
			
		}
		
		return false;
	}

	public void setVirtualIndex(Point virtualX, Point virtualY) {
		this.virtualIndex.clear();
		this.virtualIndexReverse.clear();
		this.virtualIndex.add(virtualX);
		this.virtualIndex.add(virtualY);
		this.virtualIndexReverse.add(virtualY);
		this.virtualIndexReverse.add(virtualX);
	}
	
	
	public Color getColor(){
		return this.color;
	}
	
	
	public void setColor(Color color){
		this.color = color; 
	}

	@Override
	public String toString(){
		return super.toString()+" Bridge  - "+getVirtualIndex(false).toString();
	}
}
