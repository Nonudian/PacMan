package Model;

import static Model.GumType.*;
import javafx.geometry.Point2D;

public class Lane extends Tile {

    private final Game game;
    private GumType gum;
    private Entity entity;
    
    public Lane(Point2D coords, Game game) {
        super(coords);
        this.game = game;
        this.gum = EMPTY;
        this.entity = null;
    }

    public Lane(Point2D coords, Game game, GumType gum) {
        super(coords);
        this.game = game;
        this.gum = gum;
        this.entity = null;
    }

    public Lane(Point2D coords, Game game, Entity entity) {
        super(coords);
        this.game = game;
        this.gum = EMPTY;
        this.entity = entity;
    }

    public boolean isGummed() {
        return this.gum != EMPTY;
    }

    public GumType getType() {
        return this.gum;
    }

    private boolean eatGum() {
        if (this.entity instanceof PacMan) {
            if (this.gum == NORMAL) {
                this.game.addScore(100);
            } else if (this.gum == SUPER) {
                this.game.addScore(500);
                ((PacMan)this.entity).startPower();
            }
            this.gum = EMPTY;
            return true;
        }
        return false;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public void removeEntity() {
        this.entity = null;
    }

    public void setEntity(Entity newEntity) {
        this.entity = newEntity;
        if (this.isGummed()) {
            this.eatGum();
        }
    }
}
