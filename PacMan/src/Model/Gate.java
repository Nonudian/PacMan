package Model;

import Controller.Game;
import static Util.GumType.EMPTY;
import javafx.geometry.Point2D;

public class Gate extends Lane {
    
    private Gate target;
    
    public Gate(Point2D coords, Game game) {
        super(coords, game, EMPTY);
    }
    
    public void setTarget(Gate target) {
        this.target = target;
    }
    
    public Gate getTarget() {
        return this.target;
    }
    
}
