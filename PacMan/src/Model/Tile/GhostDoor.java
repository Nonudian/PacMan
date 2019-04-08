package Model.Tile;

import Controller.Game;
import Util.Direction;
import javafx.geometry.Point2D;

public class GhostDoor extends GhostLane {
    
    private final Direction permittedDirection;
    
    public GhostDoor(Point2D coords, Game game, Direction direction) {
        super(coords, game);
        this.permittedDirection = direction;
    }
    
    public Direction getPermittedDirection() {
        return this.permittedDirection;
    }
}
