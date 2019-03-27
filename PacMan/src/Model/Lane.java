package Model;

import javafx.geometry.Point2D;

public class Lane extends Tile {
    
    private Game game;
    private GumType gum;
    private Entity entity;
    
    public Lane(Point2D coords, Game game, GumType gum) {
        super(coords);
        this.game = game;
        this.gum = gum;
        this.entity = null;
    }
    
    public GumType getType() {
        return this.gum;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
}
