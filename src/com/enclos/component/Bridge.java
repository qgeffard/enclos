package com.enclos.component;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

public class Bridge extends Shape {

    private Polygon polygon = null;
    private final List<Point> virtualIndex = new ArrayList<Point>();
    
    public Bridge() {
        this.setVirtualIndex(new Point(0, 0), new Point(0, 0));
    }

    @Override
    public boolean contains(int x, int y) {
        return polygon.contains(new Point(x, y)) ? true : false;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public List<Point> getVirtualIndex() {
        return this.virtualIndex;
    }

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

    public void setVirtualIndex(Point virtualX, Point virtualY) {
        this.virtualIndex.clear();
        this.virtualIndex.add(virtualX);
        this.virtualIndex.add(virtualY);
    }

    @Override
    public String toString() {
        return super.toString() + " Bridge  - " + getVirtualIndex().toString();
    }
}
