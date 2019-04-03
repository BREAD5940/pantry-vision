package org.team5940.pantry.vision;

import org.opencv.core.Point;

public class BetterPoint extends Point {

    public BetterPoint(double x, double y) {
        super(x, y);
    }

    public BetterPoint(Point other) {
        super(other.x, other.y);
    }

    public BetterPoint plus(Point other) {
        return new BetterPoint(this.x + other.x, this.y + other.y);
    }

    public BetterPoint minus(Point other) {
        return new BetterPoint(this.x - other.x, this.y - other.y);
    }

}