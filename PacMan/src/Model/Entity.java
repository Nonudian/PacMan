package Model;

import com.sun.javafx.scene.traversal.Direction;

public abstract class Entity implements Runnable {
    
    protected Direction currentDirection;
    
    public Entity(Direction direction) {
        this.currentDirection = direction;
    }
    
    @Override
    public void run() {
        
    }
    
    protected abstract void move();
}
