package Model.Entity.Ghost;

import Controller.Game;
import Util.Direction;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Pinky extends Ghost {
    
    public Pinky(Point2D coords, Direction direction, Game game, int interval) {
        super(coords, direction, Color.PINK, game, interval);
    }
    
}
