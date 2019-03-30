package Model;

import Util.Direction;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public abstract class Entity {
    
    protected Direction currentDirection;
    protected Point2D coords;
    protected final Point2D start;
    protected final Color color;
    
    public Entity(Point2D coords, Direction direction, Color color) {
        this.currentDirection = direction;
        this.start = coords;
        this.coords = coords;
        this.color = color;
    }
    
    public Direction getDirection() {
        return this.currentDirection;
    }
    
    public void setDirection(Direction direction) {
        this.currentDirection = direction;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public Point2D getCoords() {
        return this.coords;
    }
    
    public Point2D getStart() {
        return this.start;
    }
    
    public void moveTo(Point2D coords) {
        this.coords = coords;
    }
    
    public void moveToStart() {
        this.coords = this.start;
    }
    
    public abstract boolean canKill(Entity enemy);
}
