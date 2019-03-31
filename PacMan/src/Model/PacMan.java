package Model;

import Controller.Game;
import Util.Direction;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class PacMan extends Entity {
    
    private boolean powered;
    
    public PacMan(Point2D coords, Direction direction, Color color, Game game, int interval) {
        super(coords, direction, color, game, interval);
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
    public boolean canKill(Entity enemy) {
        return (enemy instanceof Ghost && this.powered);
    }

    @Override
    protected Direction getNextDirection() {
        return this.getDirection();
    }
}
