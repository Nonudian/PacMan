package Model;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class PacMan extends Entity {
    
    private boolean powered;
    
    public PacMan(Point2D coords, Direction direction, Color color) {
        super(coords, direction, color);
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
    protected boolean canKill(Entity enemy) {
        return (enemy instanceof Ghost && this.powered);
    }
}
