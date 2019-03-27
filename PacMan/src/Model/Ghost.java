package Model;

import com.sun.javafx.scene.traversal.Direction;

public class Ghost extends Entity {
    
    public Ghost(Direction direction) {
        super(direction);
    }

    @Override
    protected void move() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
