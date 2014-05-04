package org.enclos.component;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Clement CARREAU
 * @author Quentin GEFFARD
 * @author Julien TELA
 */

public class Bridge extends Shape {

    private Polygon polygon = null;
    
    /**
     * Point List used as the unique identifier of the Bridge
     */
    private final List<Point> virtualIndex = new ArrayList<Point>();
    
    /**
     * Constructor of  Bridge Class, set its virtualIndex 
     */
    public Bridge() {
        this.setVirtualIndex(new Point(0, 0), new Point(0, 0));
    }
    
    /**
     * Check if the point given has an argument is in the polygon 
     *
     * @param  x the x coordinate of the point we want to check
     * @param  y the y coordinate of the point we want to check
     * @return      Boolean if point give in argument is in the polygon
     */
    @Override
    public boolean contains(int x, int y) {
        return polygon.contains(new Point(x, y)) ? true : false;
    }
    
    /**
     * Getter on the polygon attribute class
     *
     * @return Polygon of the bridge   
     */
    public Polygon getPolygon() {
        return polygon;
    }
    
    /**
     * Setter on the polygon attribute class 
     * 
     * @param polygon to affect to the attribute
     */
    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }
    
    /**
     * Getter of the virtualIndex, the virtualIndex is used like the unique identifier of bridge instance 
     *
     * @return      Boolean if point give in argument is in the polygon
     */
    public List<Point> getVirtualIndex() {
        return this.virtualIndex;
    }

    
    /**
     * Override of the equals method
     * 
     * @param bridge we want compare
     * @return boolean true if Bridge given is the same of the current Bridge
     */
    public boolean equals(Bridge bridge) {

        Point p1 = this.getVirtualIndex().get(0);
        Point p2 = this.getVirtualIndex().get(1);

        if (bridge.getVirtualIndex().contains(p1) && bridge.getVirtualIndex().contains(p2)) {
            for (Point point : this.pointList) {
                if (!bridge.pointList.contains(point))
                    return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Construct the unique identifier of the bridge
     * 
     * @param virtualX
     * @param virtualY
     */
    public void setVirtualIndex(Point virtualX, Point virtualY) {
        this.virtualIndex.clear();
        this.virtualIndex.add(virtualX);
        this.virtualIndex.add(virtualY);
    }
    
    /**
     * Override toString
     *  @return string
     */
    @Override
    public String toString() {
        return super.toString() + " Bridge  - " + getVirtualIndex().toString();
    }
}
