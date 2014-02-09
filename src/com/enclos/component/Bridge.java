package com.enclos.component;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

public class Bridge extends Shape{

	private Polygon polygon = null;
	
	public Bridge(int virtualX, int virtualY) {
		this.virtualIndex.setLocation(virtualX, virtualY);
	}

	@Override
	public void warn(){
		super.warn();
		System.out.println(" Bridge");
	}
}
