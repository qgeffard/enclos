package com.enclos.data;

import java.awt.geom.AffineTransform;

public enum Direction {

    SOUTH_EAST(0.5235987755983), SOUTH(1.5707963267949), SOUTH_WEST(2.6179938779915), NORTH_WEST(3.6651914291881), NORTH(4.7123889803847), NORTH_EAST(
            5.7595865315813);

    private double angle;

    private Direction(double angle) {
        this.angle = angle;
    }

    public AffineTransform getDirection(int distance) {
        int dx = (int) (Math.cos(this.angle) * distance);
        int dy = (int) (Math.sin(this.angle) * distance);
        AffineTransform affineTransform = AffineTransform.getTranslateInstance(dx, dy);
        return affineTransform;
    }

    public Direction getNext() {
        return values()[(ordinal() + 1) % values().length];
    }

}
