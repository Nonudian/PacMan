package Model;

import com.sun.javafx.scene.traversal.Direction;
import javafx.geometry.Point2D;

public abstract class Entity implements Runnable {
    
    protected Direction currentDirection;
    protected Point2D coords;
    
    public Entity(Point2D coords, Direction direction) {
        this.currentDirection = direction;
        this.coords = coords;
    }
    
    public Direction getDirection() {
        return this.currentDirection;
    }
    
    public Point2D getCoords() {
        return this.coords;
    }
    
    public void moveTo(Point2D coords) {
        this.coords = coords;
    }
    
    protected abstract boolean canKill(Entity enemy);
    
    @Override
    public void run() {
        
    }
}
