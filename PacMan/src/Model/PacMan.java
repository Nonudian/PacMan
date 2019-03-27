package Model;

import com.sun.javafx.scene.traversal.Direction;
import javafx.geometry.Point2D;

public class PacMan extends Entity {
    
    private boolean powered;
    
    public PacMan(Point2D coords, Direction direction) {
        super(coords, direction);
        this.powered = false;
    }
    
    public boolean isPowered() {
        return this.powered;
    }
    
    public void startPower() {
        this.powered = true;
    }
    
    public void endPower() {
        this.powered = false;
    }

    @Override
    protected void moveTo(Point2D coords) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean canKill(Entity enemy) {
        return (enemy instanceof Ghost && this.powered);
    }

    @Override
    protected boolean canDie(Entity enemy) {
        return (enemy instanceof Ghost && !this.powered);
    }
}
