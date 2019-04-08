package Model.Entity.Ghost;

import Controller.Game;
import Util.Direction;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Inky extends Ghost {
    
    public Inky(Point2D coords, Direction direction, Game game, int interval) {
        super(coords, direction, Color.CYAN, game, interval);
    }
    
}
