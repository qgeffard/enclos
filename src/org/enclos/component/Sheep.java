package org.enclos.component;

import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.util.List;

import org.enclos.data.Human;
import org.enclos.data.Player;

public class Sheep {
	private Point virtualIndexHexagon;
	private Player owner;
	private Hexagon hexagon;
	private File imgPath;
	
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
	
	public File getImgPath() {
		return imgPath;
	}

	public void setImgPath(File imgPath) {
		this.imgPath = imgPath;
	}

	@Override
	public String toString() {
		return super.toString() + "Owner:"+ this.owner.toString()  +" Sheep " + virtualIndexHexagon.toString();
	}

}
