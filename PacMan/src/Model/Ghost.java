package Model;

import com.sun.javafx.scene.traversal.Direction;
import javafx.geometry.Point2D;

public class Ghost extends Entity {
    
    public Ghost(Point2D coords, Direction direction) {
        super(coords, direction);
    }

    @Override
    protected boolean canKill(Entity enemy) {
        return (enemy instanceof PacMan && !((PacMan)enemy).isPowered());
    }
}
