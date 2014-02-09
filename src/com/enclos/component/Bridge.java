package com.enclos.component;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Bridge extends Shape{

	private Polygon polygon = null;
	private List<Point> virtualIndex = new ArrayList<Point>();
	private List<Point> virtualIndexReverse = new ArrayList<Point>();
	
	public List<Point> getVirtualIndex(boolean reverse) {
		if(reverse)
			return this.virtualIndexReverse;
		else
			return this.virtualIndex;
	}
	
	public Bridge(Point virtualX, Point virtualY) {
		this.virtualIndex.add(virtualX);
		this.virtualIndex.add(virtualY);
		this.virtualIndexReverse.add(virtualY);
		this.virtualIndexReverse.add(virtualX);
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
			return true;
			
		}
		
		return false;
	}

	@Override
	public void warn(){
		super.warn();
		System.out.print(": Bridge");
	}
}
