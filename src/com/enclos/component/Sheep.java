package com.enclos.component;

import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.List;

import com.enclos.data.Player;

public class Sheep {
	private List<Hexagon> movesAvailable;
	private Player owner;

	
	
	@Override
	public String toString() {
		return super.toString() + " Sheep ";
	}

}
