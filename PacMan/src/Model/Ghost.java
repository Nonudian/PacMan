package Model;

import Controller.Game;
import Util.Direction;
import java.util.ArrayList;
import java.util.Random;
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
        Point2D pacmanCoords = this.game.getPacMan().getCoords();
        ArrayList<Direction> possibleDirections = this.getPossibleDirections();

        if (this.game.getPacMan().isPowered()) {
            //ghosts choose random direction [PACMAN POWERED]
            nextDirection = possibleDirections.get(new Random().nextInt(possibleDirections.size()));
        } else {
            // ghosts reach pacman [PACMAN UNPOWERED]
            double maxDistance = Double.MAX_VALUE;
            for (Direction direction : possibleDirections) {
                Point2D nextCoords = this.game.getNextCoords(this.coords, direction);
                double distance = pacmanCoords.distance(nextCoords);
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
