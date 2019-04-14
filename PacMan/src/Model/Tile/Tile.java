package Model.Tile;

import javafx.geometry.Point2D;

public abstract class Tile {
    
    protected final Point2D coords;
    
    public Tile(Point2D coords) {
        this.coords = coords;
    }
    
    public Point2D getCoords() {
        return this.coords;
    }
    
}
