package Model;

import Util.Direction;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Ghost extends Entity {
    
    public Ghost(Point2D coords, Direction direction, Color color) {
        super(coords, direction, color);
    }

    @Override
    public boolean canKill(Entity enemy) {
        return (enemy instanceof PacMan && !((PacMan)enemy).isPowered());
    }
}
