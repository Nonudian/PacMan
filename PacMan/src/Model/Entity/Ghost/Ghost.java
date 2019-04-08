package Model.Entity.Ghost;

import Controller.Game;
import Model.Entity.Entity;
import Model.Entity.PacMan;
import Model.Tile.GhostDoor;
import Model.Tile.Lane;
import Model.Tile.Tile;
import Util.Direction;
import java.util.ArrayList;
import java.util.Random;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public abstract class Ghost extends Entity {

    private boolean outside;
    private boolean scared;

    public Ghost(Point2D coords, Direction direction, Color color, Game game, int interval) {
        super(coords, direction, color, game, interval);
        this.outside = false;
        this.scared = false;
    }

    @Override
    public void reset() {
        this.resetOutside();
        this.resetScared();
        super.reset();
    }

    public void scare() {
        this.setTurnBack(true);
        this.setColor(Color.DARKORCHID);
        this.scared = true;
    }
    
    public boolean isScared() {
        return this.scared;
    }
    
    public void resetScared() {
        this.scared = false;
        this.resetColor();
    }

    public boolean isOutside() {
        return this.outside;
    }

    public void resetOutside() {
        this.outside = false;
    }

    public void setOutside(boolean outside) {
        this.outside = outside;
    }

    private ArrayList<Direction> getPossibleDirections() {
        ArrayList<Direction> possibleDirections = new ArrayList();
        for (Direction direction : Direction.values()) {
            if (this.currentDirection.getOpposed() != direction) {
                Point2D adjacentCoords = this.game.getNextCoords(this.coords, direction);
                if (this.game.isReachable(adjacentCoords)) {
                    Tile tile = this.game.getTileByCoords(adjacentCoords);
                    if (tile instanceof Lane) {
                        if (!(tile instanceof GhostDoor) || ((GhostDoor) tile).getPermittedDirection() == direction) {
                            Entity entity = ((Lane) tile).getEntity();
                            if (entity == null || entity instanceof PacMan) {
                                possibleDirections.add(direction);
                            }
                        }
                    }
                }
            }
        }
        if (possibleDirections.isEmpty()) {
            possibleDirections.add(this.currentDirection.getOpposed());
        }
        return possibleDirections;
    }

    private Direction getClosestDirection(Point2D aimedCoords) {
        Direction nextDirection = this.currentDirection;
        ArrayList<Direction> possibleDirections = this.getPossibleDirections();

        if (this.isScared()) {
            //ghosts choose random direction [PACMAN POWERED]
            nextDirection = possibleDirections.get(new Random().nextInt(possibleDirections.size()));
        } else {
            // ghosts reach pacman [PACMAN UNPOWERED]
            double maxDistance = Double.MAX_VALUE;
            for (Direction direction : possibleDirections) {
                Point2D nextCoords = this.game.getNextCoords(this.coords, direction);
                double distance = aimedCoords.distance(nextCoords);
                if (distance < maxDistance) {
                    maxDistance = distance;
                    nextDirection = direction;
                }
            }
        }
        return nextDirection;
    }

    @Override
    public Direction getNextDirection() {
        if(this.turnBack) {
            this.resetTurnBack();
            return this.currentDirection.getOpposed();
        }
        
        Point2D aimedCoords;
        if (this.isOutside()) {
            aimedCoords = this.game.getPacMan().getCoords();
        } else {
            GhostDoor ghostdoor = this.game.getGhostDoor();
            aimedCoords = this.game.getNextCoords(ghostdoor.getCoords(), ghostdoor.getPermittedDirection());
        }
        return this.getClosestDirection(aimedCoords);
    }

    @Override
    public boolean canKill(Entity enemy) {
        return (enemy instanceof PacMan && !this.isScared());
    }
}
