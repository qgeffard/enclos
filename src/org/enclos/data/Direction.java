package org.enclos.data;

import java.awt.geom.AffineTransform;

/**
 * @author Clement CARREAU
 * @author Quentin GEFFARD
 * @author Julien TELA
 */

public enum Direction {

    SOUTH_EAST(0.5235987755983), SOUTH(1.5707963267949), SOUTH_WEST(2.6179938779915), NORTH_WEST(3.6651914291881), NORTH(4.7123889803847), NORTH_EAST(
            5.7595865315813);

    private double angle;
    
    /**
     * Constructor enum Direction
     * @param angle
     */
    private Direction(double angle) {
        this.angle = angle;
    }

    /**
     * Get direction with the weel angle
     * @param distance
     * @return AffineTransform 
     * @see AffineTransform 
     */
    public AffineTransform getDirection(int distance) {
        int dx = (int) (Math.cos(this.angle) * distance);
        int dy = (int) (Math.sin(this.angle) * distance);
        AffineTransform affineTransform = AffineTransform.getTranslateInstance(dx, dy);
        return affineTransform;
    }

    /**
     * Get the next Direction field
     * @return Direction field
     */
    public Direction getNext() {
        return values()[(ordinal() + 1) % values().length];
    }

}
