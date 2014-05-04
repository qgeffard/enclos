package org.enclos.component;

import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.util.List;

import org.enclos.data.Human;
import org.enclos.data.Player;

/**
 * @author Clement CARREAU
 * @author Quentin GEFFARD
 * @author Julien TELA
 */

public class Sheep {
	
	private Point virtualIndexHexagon;
	private Player owner;
	private Hexagon hexagon;
	private File imgPath;
	
	/**
	 * Getter owner attribute (Player owner of the sheep)
	 * @return Player
	 */
	public Player getOwner() {
		return owner;
	}

	/**
	 * Setter owner attribute
	 * @param owner
	 */
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	/**
	 * Getter virtualIndexHexagon
	 * @return Point
	 * @see Point
	 */
	public Point getVirtualIndexHexagon() {
		return virtualIndexHexagon;
	}
	
	/**
	 * Setter virtualIndexHexagon
	 * @param virtualIndexHexagon
	 * @see Point
	 */
	public void setVirtualIndexHexagon(Point virtualIndexHexagon) {
		this.virtualIndexHexagon = virtualIndexHexagon;
	}

	/**
	 * Setter Hexagon attribute
	 */
	public void setHexagon(Hexagon hex){
		this.hexagon = hex;
	}
	
	/**
	 * Getter Hexagon attribute
	 * @return Hexagon
	 * @see Hexagon
	 */
	public Hexagon getHexagon(){
		return this.hexagon;
	}
	
	/**
	 * Get the File of image attribute
	 * @return File the file
	 */
	public File getImgPath() {
		return imgPath;
	}
	
	/**
	 * Setter imgPath attribute 
	 * @param imgPath
	 */
	public void setImgPath(File imgPath) {
		this.imgPath = imgPath;
	}
	
    /**
     * Override toString
     *  @return string
     */
	@Override
	public String toString() {
		return super.toString() + "Owner:"+ this.owner.toString()  +" Sheep " + virtualIndexHexagon.toString();
	}

}
