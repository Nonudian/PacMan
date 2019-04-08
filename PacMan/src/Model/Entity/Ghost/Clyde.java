package Model.Entity.Ghost;

import Controller.Game;
import Util.Direction;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Clyde extends Ghost {
    
    public Clyde(Point2D coords, Direction direction, Game game, int interval) {
        super(coords, direction, Color.ORANGE, game, interval);
    }
    
}
