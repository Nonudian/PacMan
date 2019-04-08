package Model.Tile;

import Model.Entity.*;
import Model.Entity.Ghost.Ghost;
import Controller.Game;
import Util.GumType;
import static Util.GumType.*;
import javafx.geometry.Point2D;

public class Lane extends Tile {

    private final Game game;
    private GumType gum;
    private final GumType startGum;
    private Entity entity;

    public Lane(Point2D coords, Game game) {
        super(coords);
        this.game = game;
        this.gum = EMPTY;
        this.startGum = EMPTY;
        this.entity = null;
    }

    public Lane(Point2D coords, Game game, GumType gum) {
        super(coords);
        this.game = game;
        this.gum = gum;
        this.startGum = gum;
        this.entity = null;
    }

    public Lane(Point2D coords, Game game, Entity entity) {
        super(coords);
        this.game = game;
        this.gum = EMPTY;
        this.startGum = EMPTY;
        this.entity = entity;
    }

    public GumType getType() {
        return this.startGum;
    }

    public boolean isGummed() {
        return this.gum != EMPTY;
    }

    public GumType getState() {
        return this.gum;
    }

    private boolean eatGum() {
        if (this.entity instanceof PacMan) {
            switch (this.gum) {
                case NORMAL:
                    this.game.addScore(100);
                    break;
                case SUPER:
                    this.game.addScore(500);
                    ((PacMan) this.entity).startPower();
                    break;
                case INVERTED:
                    if (!((PacMan) this.entity).isPowered()) {
                        this.game.addScore(500);
                        ((PacMan) this.entity).startSick();
                    }
                    break;
            }
            this.game.removeGumLane(this);
            this.gum = EMPTY;
            return true;
        }
        return false;
    }

    public boolean hasEntity() {
        return this.entity != null;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public void removeEntity() {
        if (this instanceof GhostDoor) {
            ((Ghost) this.entity).setOutside(true);
        }
        this.entity = null;
    }

    public void resetGum() {
        this.gum = this.startGum;
        if (this.gum != EMPTY) {
            this.game.addGumLane(this);
        }
    }

    public void setEntity(Entity newEntity) {
        this.entity = newEntity;
        if (this.isGummed()) {
            this.eatGum();
        }
    }
}
