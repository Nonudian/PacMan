package Model;

import Controller.Game;
import static Util.GumType.EMPTY;
import javafx.geometry.Point2D;

public class Portal extends Lane {
    
    private Portal target;
    
    public Portal(Point2D coords, Game game) {
        super(coords, game, EMPTY);
    }
    
    public void setTarget(Portal target) {
        this.target = target;
    }
    
    public Portal getTarget() {
        return this.target;
    }
    
}
