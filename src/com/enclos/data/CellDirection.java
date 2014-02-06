package com.enclos.data;

import java.awt.geom.AffineTransform;

public class CellDirection {

	private int x;
	private int y;
	private AffineTransform direction = null;

	public CellDirection(int x, int y, String orient) {
		switch (orient) {
		case "NORTH":
			direction = AffineTransform.getTranslateInstance(0, y);
			break;
		case "NORTH_EAST":
			direction = AffineTransform.getTranslateInstance(-x, y);
			break;
		case "EAST":
			direction = AffineTransform.getTranslateInstance(x, y);
			break;
		case "SOUTH_EAST":
			direction = AffineTransform.getTranslateInstance(x, y);
			break;
		case "SOUTH":
			direction = AffineTransform.getTranslateInstance(x, y);
			break;
		case "SOUTH_WEST":
			direction = AffineTransform.getTranslateInstance(x, y);
			break;
		case "WEST":
			direction = AffineTransform.getTranslateInstance(x, y);
			break;
		case "NORH_WEST":
			direction = AffineTransform.getTranslateInstance(x, y);
			break;

		default:
			break;
		}

	}

	public CellDirection(int x, int y) {
		direction = AffineTransform.getTranslateInstance(x, y);
	}

}
