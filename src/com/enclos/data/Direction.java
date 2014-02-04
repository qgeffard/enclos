package com.enclos.data;

import java.awt.geom.AffineTransform;

public enum Direction {

	NORTH(0),
	NORTH_EAST(60),
	SOUTH_EAST(120),
	SOUTH(180),
	SOUTH_WEST(240),
	NORTH_WEST(300);
	
	private AffineTransform affineTransform = null;
	
	private Direction(int rotation){
		affineTransform = AffineTransform.getRotateInstance(rotation);
	}
	
	public AffineTransform getDirection(){
		return this.affineTransform;
	}
	
	public Direction getNext() {
		return values()[(ordinal()+1) % values().length];
		}
	
}
