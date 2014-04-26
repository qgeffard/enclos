package com.enclos.component;

import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.List;

import com.enclos.data.Player;

public class Sheep {
	private Point virtualIndexHexagon;
	private Player owner;
	private Hexagon hexagon;
	
	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public Point getVirtualIndexHexagon() {
		return virtualIndexHexagon;
	}

	public void setVirtualIndexHexagon(Point virtualIndexHexagon) {
		this.virtualIndexHexagon = virtualIndexHexagon;
	}

	public void setHexagon(Hexagon hex){
		this.hexagon = hex;
	}
	
	public Hexagon getHexagon(){
		return this.hexagon;
	}
	
	@Override
	public String toString() {
		return super.toString() + "Owner:"+ this.owner.toString()  +" Sheep " + virtualIndexHexagon.toString();
	}

}
