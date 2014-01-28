package com.enclos.component;

import java.awt.Point;
import java.awt.Polygon;

public class Hexagon extends Shape{

	private Polygon polygon = null;
	
	public Hexagon(int x, int y, int size){
		this.x = x;
		this.y = y;
		this.size = size;
	}

	
	@Override
	public void warn(){
		super.warn();
		System.out.println(" Hexagon");
	}

}
