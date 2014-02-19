package com.enclos.component;

import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.List;

import com.enclos.data.Player;

public class Sheep extends Shape {
	private List<Hexagon> movesAvailable;
	private Player owner;
	private Ellipse2D.Double circle;
	
	
	public Ellipse2D.Double getCircle() {
		return circle;
	}

	public void setCircle(Ellipse2D.Double circle) {
		this.circle = circle;
	}



	@Override
	public String toString(){
		return super.toString()+" Sheep ";
	}

	@Override
	public boolean contains(int x, int y) {
		return this.circle.contains(new Point(x, y)) ? true:false;
	}

}
