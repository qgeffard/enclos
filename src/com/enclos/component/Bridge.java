package com.enclos.component;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

public class Bridge extends Shape{

	private Polygon polygon = null;
	
	//attribut de rotation
	private int rotation = 0;

	public Bridge(int x, int y, int size){
		this.x = x;
		this.y = y;
		this.size = size;
		this.rotation = rotation;
	}
	
	public Bridge(int x, int y, int size, int rotation){
		this(x,y,size);
		this.rotation = rotation;
	}
	

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	
	@Override
	public void warn(){
		super.warn();
		System.out.println(" Bridge");
	}
}
