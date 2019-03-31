package Model;

import Controller.Game;
import Util.Direction;
import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Ghost extends Entity {

    public Ghost(Point2D coords, Direction direction, Color color, Game game, int interval) {
        super(coords, direction, color, game, interval);
    }

    private ArrayList<Direction> getPossibleDirections() {
        ArrayList<Direction> possibleDirections = new ArrayList();
        for (Direction direction : Direction.values()) {
            if (this.currentDirection.getOpposed() != direction) {
                Point2D adjacentCoords = this.game.getNextCoords(this.coords, direction);
                if (this.game.isReachable(adjacentCoords)) {
                    if (this.game.getTileByCoords(adjacentCoords) instanceof Lane) {
                        possibleDirections.add(direction);
                    }
                }
            }
        }
        return possibleDirections;
    }
    
    @Override
    protected Direction getNextDirection() {
        Direction nextDirection = this.currentDirection;
        PacMan pacman = this.game.getPacMan();
        Point2D pacmanCoords = pacman.getCoords();
        
        double maxDistance = Double.MAX_VALUE;
        double minDistance = 0;
        
        for (Direction direction : getPossibleDirections()) {
            Point2D nextCoords = this.game.getNextCoords(this.coords, direction);
            double distance = pacmanCoords.distance(nextCoords);
            if (pacman.isPowered()) {
                // ghosts want to get away from pacman
                if (distance > minDistance) {
                    minDistance = distance;
                    nextDirection = direction;
                }
            } else {
                // ghosts want to reach pacman
                if (distance < maxDistance) {
                    maxDistance = distance;
                    nextDirection = direction;
                }
            }
        }
        return nextDirection;
    }

    @Override
    public boolean canKill(Entity enemy) {
        return (enemy instanceof PacMan && !((PacMan) enemy).isPowered());
    }
}
